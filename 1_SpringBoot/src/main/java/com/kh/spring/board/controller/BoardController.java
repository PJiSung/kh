package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Pagination;
import com.kh.spring.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

	@Autowired
	private BoardService bService;
	
	// 게시글 목록조회
	@GetMapping("list.bo")
	public String selectBoardList(@RequestParam(value="page", defaultValue="1")int page, Model model, HttpServletRequest request) {
		
		int listCount = bService.getListCount(1);
		
		int currentPage = page;
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		ArrayList<Board> list = bService.selectBoardList(pi, 1);
		
		if(list != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI());
//			System.out.println(request.getRequestURI());	// list.bo
//			System.out.println(request.getRequestURL());	// 다 가져옴(http://!@!@!@!@!)
			
			return "boardList";
		} else {
			throw new BoardException("게시글 조회 실패");
		}
	}
	
	// 게시글 작성
	@GetMapping("writeBoard.bo")
	public String writeBoard() {
		return "writeBoard";
	}
	
	@PostMapping("insertBoard.bo")
	public String insertBoard(@ModelAttribute Board b, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getId();	// 세션에 로그인 된 유저의 아이디를 가져와라
		
		b.setBoardWriter(id); 												// 그 아이디를 b에 집어넣어
		b.setBoardType(1);
		
		int result = bService.insertBoard(b);
		if(result > 0) {
		//	return "boardList";												// 이렇게 가면 입력한 정보를 저장하지 않고 그냥 냅다 가서 에러뜸						
			return "redirect:list.bo";										
			// 내가 등록한 글을 db에서 같이 불러오는 거니까 list.bo가 다시 실행되도록 "redirect:list.bo"가 맞음
		} else {
			throw new BoardException("게시글 등록 실패");
		}
	}
	
	// 게시글 상세조회
	@GetMapping("selectBoard.bo")
	public String selectBoard(@RequestParam("boardId")int boardId,
							  @RequestParam("page") int page,
							  HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		String id = null;
		if(loginUser != null) {
			id = loginUser.getId();	// 로그인 유저가 있으면 아이디는 로그인유저에서 가져와
		} 
		
		Board b = bService.selectBoard(boardId, id);
		ArrayList<Reply> list = bService.selectReply(boardId);
		
		if(b != null) {
			model.addAttribute("b", b);
			model.addAttribute("page", page);
			model.addAttribute("list", list);
			return "boardDetail";
		} else {
			throw new BoardException("게시글 상세보기 실패");
		}
	}
	
	// 댓글 추가
	@RequestMapping(value="insertReply.bo", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String insertReply(@ModelAttribute Reply r, HttpSession session) {
		String user = ((Member)session.getAttribute("loginUser")).getId();
		int result = bService.insertReply(r);
		
		ArrayList<Reply> list = bService.selectReply(r.getRefBoardId()); // 가져와서 ajax로 바로 뿌릴거임
		// ajax : Stirng 아닌 다른 타입 전달시 JSON포맷 사용
		// JSON 내장 객체
		JSONArray jArr = new JSONArray();	// list 방식 - reply List
		for(Reply reply : list) {
			JSONObject json = new JSONObject(); // map(key-value) 방식 - 각 reply 담기
			json.put("replyId", reply.getReplyId());
			json.put("replyContent", reply.getReplyContent());
			json.put("refBoardId", reply.getRefBoardId());
			json.put("replyWriter", reply.getReplyWriter());
			json.put("nickName", reply.getNickName());
			json.put("replyCreateDate", reply.getReplyCreateDate());
			json.put("replyModifyDate", reply.getReplyModifyDate());
			json.put("replyStatus", reply.getReplyStatus());
			
			jArr.put(json);
		}
		return jArr.toString();
	}
	
	// 게시글 수정
	@PostMapping("updateForm.bo")
	public String updateForm(@RequestParam("boardId") int boardId, @RequestParam("page") int page, Model model) {
		
		Board b = bService.selectBoard(boardId, null);
		model.addAttribute("b", b);
		model.addAttribute("page", page);
		return "boardEdit";
	}	
	
	@PostMapping("updateBoard.bo")
	public String updateBoard(@RequestParam("page") int page, @ModelAttribute Board b, RedirectAttributes redirectAttributes) {
		b.setBoardType(1);
		
		System.out.println(b);
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			redirectAttributes.addAttribute("boardId", b.getBoardId());
			redirectAttributes.addAttribute("page", page);
			return "redirect:selectBoard.bo";
		} else {
			throw new BoardException("게시글 수정 실패");
		}
	}
	
	// 게시글 삭제
	@PostMapping("delete.bo")
	public String deleleBoard(@RequestParam("boardId")int boardId) {
		int result = bService.deleteBoard(boardId);
		
		if(result > 0) {
			return "redirect:list.bo";
		}else {
			throw new BoardException("게시글 삭제 실패");
		}
		
	}
	
	// 첨부파일 목록 조회
	@GetMapping("list.at")
	public String selectAttmList(@RequestParam(value="page", defaultValue="1") int page, Model model, 
			HttpServletRequest request) {
		int listCount = bService.getListCount(2);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 9);	 // 한 페이지에 9개 보이기, 해당 페이지 info 반환
		ArrayList<Board> bList = bService.selectBoardList(pi, 2);
		ArrayList<Attachment> aList = bService.selectAttmBoardList(null);	
		 //지금은 썸네일 보내지만 나중에는 한 게시글에 대한 첨부파일들을 보내야해서
		
		if(bList != null) {
			model.addAttribute("bList", bList);
			model.addAttribute("pi", pi);
			model.addAttribute("aList", aList);
			model.addAttribute("loc", request.getRequestURI());

			return "attmList";
		} else {
			throw new BoardException("첨부파일 게시글 조회 실패");
		}
	}
	
	// 첨부파일 게시글 작성
	@GetMapping("writeAttm.at")
	public String writeAttm() {
		return "writeAttm";
	}
	
	@PostMapping("insertAttm.at")
	public String insertAttm(@ModelAttribute Board b,
	                      @RequestParam("file") ArrayList<MultipartFile> files, HttpServletRequest request) {
		String id = ((Member)request.getSession().getAttribute("loginUser")).getId();
	    b.setBoardWriter(id);
	    ArrayList<Attachment> list = new ArrayList<>();
	    for(int i = 0; i < files.size(); i++) {
	    	MultipartFile upload = files.get(i);
	        if(!upload.getOriginalFilename().equals("")) {
	        	String[] returnArr = saveFile(upload);
	            if(returnArr[1] != null) {
	            	Attachment a = new Attachment();
	                a.setOriginalName(upload.getOriginalFilename());
	                a.setRenameName(returnArr[1]);
	                a.setAttmPath(returnArr[0]);
	               
	                list.add(a);
	            }
	        }
	    }
	      
	    for(int i = 0; i < list.size(); i++) {
	    	Attachment a = list.get(i);
	        if(i == 0) {
	        	a.setAttmLevel(0);
	        } else {
	        	a.setAttmLevel(1);
	        }
	    }
	      
	    int result1 = 0;
	    int result2 = 0;
	      
	    if(list.isEmpty()) {
	    	b.setBoardType(1);
	        result1 = bService.insertBoard(b);
	    } else {
	    	b.setBoardType(2);
	        result1 = bService.insertBoard(b);
	        for(Attachment a : list) {
	        	System.out.println(b);
	        	System.out.println("dddddddddddddd="+b.getBoardId()); 
	        	a.setRefBoardId(b.getBoardId());
	        }
	        result2 = bService.insertAttm(list);
	    }
	    
	    if(result1 + result2  == list.size()+1) {
	    	if(result2 == 0) {
	    		return "redirect:list.bo";
	        } else {
	        	return "redirect:list.at";
	        }
	    } else {
	    	for(Attachment a : list) {
	    		deleteFile(a.getRenameName(),request);
	        }
	    	throw new BoardException("첨부파일 게시글 삽입을 실패하였습니다.");
	    }  
	}

	private void deleteFile(String fileName, HttpServletRequest request) {
		String savePath = request.getSession().getServletContext().getRealPath("resiurces") + "\\uploadFiles";
	    File f = new File(savePath + "\\" + fileName);
	    if(f.exists()) {
	    	f.delete();
	    }
	}

	private String[] saveFile(MultipartFile upload) {
	   String root = "C:\\";
	   String savePath = root + "\\uploadFiles";
	      
	   File folder = new File(savePath);
	   if(!folder.exists()) {
		   folder.mkdirs();
	       // 폴더가 존재하지 않는다면 만들겠다!
	   }
	      
	   // 2. 저장될 파일 rename
	   Date time = new Date(System.currentTimeMillis());
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	   int ranNum = (int)(Math.random()*100000);
	   String originFileName = upload.getOriginalFilename();
	   String renameFileName  = sdf.format(time)+ ranNum + originFileName.substring(originFileName.lastIndexOf("."));
	   //            날짜            랜덤숫자          확장자      (학생랜덤 ". 에 대한 위치를 subString에 담아주는것"jpg)
	      
	   // 3. rename된 파일을 저장소에 저장
	   String renamePath = folder + "\\" + renameFileName;
	   try {
		   upload.transferTo(new File(renamePath));
	   } catch (IllegalStateException e) {
		   e.printStackTrace();
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	      
	   String[]returnArr = new String[2];
	   returnArr[0] = savePath; // 파일 저장소에 대한 경로
	   returnArr[1] = renameFileName; // rename한 이름
	      
	   return returnArr;
   }
   
	// 게시글 상세보기
	@GetMapping("selectAttm.at")
	public String selectAttm(@RequestParam("boardId") int boardId, @RequestParam("page") int page,
		   HttpSession session, Model model) {
	   Member loginUser = (Member)session.getAttribute("loginUser");
	   String id = null;
	   if(loginUser != null) {
		   id = loginUser.getId();
	   }
	   Board b = bService.selectBoard(boardId, id);
	   ArrayList<Attachment> list = bService.selectAttmBoardList((Integer)boardId);
	   
	   if(b != null) {
		   model.addAttribute("b", b);
		   model.addAttribute("list", list);
		   model.addAttribute("page", page);
		   
		   return "attmDetail";
	   } else {
		   throw new BoardException("게시글 상세조회 실패");
	   }
	}	
   
	// 게시글 수정
	@PostMapping("updateForm.at")
	public String updateAttmForm(@RequestParam("boardId") int boardId,
		   					@RequestParam("page") int page, Model model) {
	   
	   Board b = bService.selectBoard(boardId, null);
	   ArrayList<Attachment> list = bService.selectAttmBoardList(boardId);
	   model.addAttribute("b", b);
	   model.addAttribute("page", page);
	   model.addAttribute("list", list);
	   return "attmEdit";
	} 

	@PostMapping("updateAttm.at")
	public String updateAttm(@ModelAttribute Board b, @RequestParam("page") int page, 
							 @RequestParam("deleteAttm") String[] deleteAttm,
							 @RequestParam("file") ArrayList<MultipartFile> files,
							 HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		
		
		b.setBoardType(2);
		
		// 파일 새로 추가
		ArrayList<Attachment> list = new ArrayList<>(); // list : 새로추가할 파일들
		for(int i = 0; i < files.size(); i++) {
			MultipartFile upload = files.get(i);
			
			if(!upload.getOriginalFilename().equals("")) {
				String[] returnArr = saveFile(upload);
				if(returnArr[1] != null) {
					Attachment a = new Attachment();
					a.setOriginalName(upload.getOriginalFilename());
					a.setRenameName(returnArr[1]); // 파일 저장소에 새로 저장할 이름
					a.setAttmPath(returnArr[0]); // 파일 저장소
					
					list.add(a);
				}
			}
		}
		// 파일 삭제
		ArrayList<String> delRename = new ArrayList<>();
		ArrayList<Integer> delLevel = new ArrayList<>();
		
		for(int i = 0; i < deleteAttm.length; i++) {
			String rename = deleteAttm[i];
			if(!rename.equals("none")) {
				String[] split = rename.split("/");
				delRename.add(split[0]);
				delLevel.add(Integer.parseInt(split[1]));
			}
		}
		
		int deleteAttmResult = 0;
		int updateBoardResult = 0;
		boolean existBeforeAttm = true; // 이전의 첨부파일이 존재하는지
		
		if(!delRename.isEmpty()) { // 삭제할 파일이 있으면
			deleteAttmResult = bService.deleteAttm(delRename);
			if(deleteAttmResult > 0) {
				for(String rename : delRename) {
					deleteFile(rename, request);
				}
			}
			
			if(delRename.size() == deleteAttm.length) { // 전체 파일 삭제
				existBeforeAttm = false; // 기존 파일 존재 X
				if(list.isEmpty()) {
					b.setBoardType(1); // 일반 게시판으로 넘기기
				}
			} else {	// 기존 파일이 남아있다면
				for(int level : delLevel) {
					if(level == 0) { // 썸네일 설정
						bService.updateAttmLevel(b.getBoardId());
						break;
					}
				}
			}
		}
		
		for(int i = 0; i < list.size(); i++) {
			Attachment a = list.get(i);
			a.setRefBoardId(b.getBoardId());
			
			if(existBeforeAttm) {
				a.setAttmLevel(1);
			} else {
				if(i == 0) {
					a.setAttmLevel(0);
				} else {
					a.setAttmLevel(1);
				}
			}
		}
		
		updateBoardResult = bService.updateBoard(b);
		
		int updateAttmResult = 0;
		if(!list.isEmpty()) {
			updateAttmResult = bService.insertAttm(list);
		}
		
		if(updateBoardResult + updateAttmResult == list.size()+1) {
			if(delRename.size() == deleteAttm.length && updateAttmResult == 0) { // 보드만 삭제할때
				return "redirect:list.bo";
			} else {
				redirectAttributes.addAttribute("boardId", b.getBoardId());
				redirectAttributes.addAttribute("page", page);
				return "redirect:selectAttm.at";
			}
		} else {
			throw new BoardException("첨부파일 게시글 수정을 실패하였습니다.");
		}
	}
	
	// 게시글 삭제
	@PostMapping("delete.at")
	public String deleteAttm(@RequestParam("boardId") int boardId) {
		int result1 = bService.deleteBoard(boardId);
		int result2 = bService.statusNAttm(boardId);
		
		if(result1 > 0 && result2 > 0) {
			return "redirect:list.at";
		}else {
			throw new BoardException("게시글 삭제 실패");
		}
	}
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
}
	