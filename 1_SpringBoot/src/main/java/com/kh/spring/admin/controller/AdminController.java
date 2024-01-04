package com.kh.spring.admin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.spring.admin.model.service.AdminService;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.common.Pagination;
import com.kh.spring.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
	
	@Autowired
	   private AdminService aService;
	   
	   @GetMapping("admin.adm")
	   public String moveToMainAdmin(Model model) {
	      File f = new File("D:/logs/member/");
	      File[] files = f.listFiles();
	      
	      // 키를 기준으로 정렬(키에 들어가 있는 객체가 정렬 기준이 있어야함)
	      // 중복 방지하기 위해 키에 String 많이 씀
	      TreeMap<String, Integer> dateCount = new TreeMap<String, Integer>();
	      BufferedReader br = null;
	      try {
	      // 날짜별로 로그인이 얼마나 들어왔는지
	      for(File file: files) {
	         System.out.println(file);
	            br = new BufferedReader(new FileReader(file));
	            String data;
	            while((data = br.readLine()) != null) { //readLine() : 마지막에 null 반환
	               System.out.println(data);
	               String date = data.split(" ")[0]; // 띄어쓰기로 잘라내서 날짜 가져오기
	               if(!dateCount.containsKey(date)) { //dateCount 안에 date가 있지 않다면 == 처음보는 날짜면
	                  dateCount.put(date, 1);
	               } else {
	                  dateCount.put(date, dateCount.get(date) + 1); // .get() 해당 키의 value값 가져오기
	               }
	            }
	         }
	      } catch (FileNotFoundException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            br.close();
	         } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	      
	      ArrayList<HashMap<String, Object>> list = aService.newestBoardList();
	      
	      
	      if(list != null) {
	         model.addAttribute("dateData", dateCount);
	         model.addAttribute("list", list);
	         return "admin";
	      }
	      return "admin";
	   }
	
	@GetMapping("members.adm")
	public String selectMembers(Model model) {
		ArrayList<Member> list = aService.selectMembers();
		model.addAttribute("list", list);
		return "members";
	}
	
	@GetMapping("updateInfo.adm")
	@ResponseBody
	public String updateInfo(@RequestParam("column") String column, @RequestParam("data") String data, @RequestParam("id") String id) {
		Properties prop = new Properties(); //map형식 key와 value 둘다 String
		column = column.equals("ADMIN") ? "is_admin" : (column.equals("STATUS") ? "member_status" : column);
		prop.setProperty("column", column);
		prop.setProperty("data", data);
		prop.setProperty("id", id);
		int result = aService.updateInfo(prop);
		return result == 1 ? "success" : "fail";
	}
	
	@GetMapping("boards.adm")
	public String slectBoards(@RequestParam(value="page", defaultValue="1") int currentPage, Model model, HttpServletRequest request) {
		int listCount = aService.getListCount(1);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> list = aService.selectBoardList(pi, 1);
		if(list != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			model.addAttribute("loc", request.getRequestURI());
			return "boards";
		}else {
			throw new BoardException("게시글 조회 실패");
		}
	}
	
	@GetMapping("updateBoardStatus.adm")
	@ResponseBody
	public String updateBoardStatus(@RequestParam("bId") int bId, @RequestParam("value") String value, @RequestParam(value = "type", required = false) Integer type) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("bId", bId);
		map.put("value", value);
		int result = aService.updateBoardStatus(map);
		if(type != null) {
			aService.updateAttmStatus(map);
		}
		return result == 1 ? "success" : "fail"; 
	}
	
	@GetMapping("attms.adm")
	public String attms(@RequestParam(value="page", defaultValue="1") int currentPage, Model model, HttpServletRequest request) {
		int bListCount = aService.getListCount(2);
		PageInfo pi = Pagination.getPageInfo(currentPage, bListCount, 10);
		ArrayList<Board> bList = aService.selectBoardList(pi, 2);
		
		ArrayList<Attachment> aList = aService.selectAttmList();
		model.addAttribute("aList", aList);
		model.addAttribute("bList", bList);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("pi", pi);
		return "attms";
	}
}
