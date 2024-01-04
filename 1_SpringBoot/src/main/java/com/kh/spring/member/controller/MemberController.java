package com.kh.spring.member.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class MemberController {
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);

	@GetMapping("loginView.me")
	public String moveToLogin() {	// 로그인 페이지로 이동
		return "login";				// member/login이라고 적어야 views안에 login
	}
	
	@PostMapping("login.me")
	public String login(@ModelAttribute Member m, Model model, @RequestParam("beforeURL") String beforeURL) {
		
		Member loginUser = mService.login(m);
		if(bcrypt.matches(m.getPwd(), loginUser.getPwd())) {
			model.addAttribute("loginUser", loginUser);
			if(loginUser.getIsAdmin().equals("N")) {
				// 로그 추가
				logger.info(loginUser.getId());
				return "redirect:" + beforeURL;
			} else {
				return "admin";
			} 
		} else {
		//	throw new MemberException("로그인 실패");
			model.addAttribute("msg", "실패");
			return "";
		}
	}
	
	@GetMapping("logout.me")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:home.do";
	}
	
	@GetMapping("enroll.me")
	public String enroll() {
		return "enroll";
	}
	
	@PostMapping("insertMember.me")
	public String insertMember(@ModelAttribute Member m, @RequestParam("emailId") String emailId,
														 @RequestParam("emailDomain")String emailDomain) {
			
		String email = null;
		if(emailId != null) {
			email = emailId + "@" + emailDomain;
		}
		m.setEmail(email);
		String encPwd = bcrypt.encode(m.getPwd());		// 내 비밀번호 암호화해서 > encPwd
		m.setPwd(encPwd);								// encPwd > m에 넣음
		
		int result = mService.insertMember(m);
		if(result > 0) {
			return "login";
		} else { 
			throw new MemberException("회원가입 실패");
			
		}
	}
	
	@GetMapping("myInfo.me")
	public String myInfo(HttpSession session, Model model) {
		String id = ((Member)model.getAttribute("loginUser")).getId();	// 세션에서 아이디를 가져와
		ArrayList<HashMap<String, Object>> list = mService.selectMylist(id);
		System.out.println(list);
		
		model.addAttribute("list", list);
		return "myInfo";
	}
	
	@GetMapping("checkId.me")
	   public void checkId(@RequestParam("id")String id, PrintWriter out) {
	      
	      int count = mService.checkId(id);
	      String result = count == 0? "yes":"No";
	      
	      out.print(result);
	   }
	   
	@GetMapping("checkNickName.me")
	@ResponseBody
    public String checknickName(@RequestParam("nickName")String nickName) {
	      
		int count = mService.checknickName(nickName);
		String result = count == 0? "yes":"No";      
		return result;
	}
	
	@GetMapping("editMyInfo.me")
	public String editMyInfo() {
		return "edit";
	}
	
	@PostMapping("updateMember.me")
	public String updateMember(@ModelAttribute Member m, @RequestParam("emailId") String emailId,
							   @RequestParam("emailDomain") String emailDomain, Model model) {
		String email = null;
		if(!emailId.equals("")) {
			email = emailId + "@" + emailDomain;
		} 
		m.setEmail(email);		// m에 email(emailId + emailDomain)을 넣어라
		
		int result = mService.updateMember(m);
		if(result > 0) {
			model.addAttribute("loginUser", m);
			return "redirect:myInfo.me";
		} else {
			throw new MemberException("내 정보 수정 실패");
		}
	}
	
	@GetMapping("deleteMember.me")
	public String deleteMember(Model model) {
		Member m = (Member)model.getAttribute("loginUser");
		
		String id = m.getId();
		
		int result = mService.deleteMember(id);
		if(result > 0) {
			return "redirect:logout.me";
		} else {
			throw new MemberException("회원 탈퇴 실패");
		}
	}
	
	@PostMapping("updatePassword.me")
	public String updatePassword(@RequestParam("currentPwd") String pwd,
			@RequestParam("newPwd") String newPwd, Model model) {
		Member m = (Member)model.getAttribute("loginUser");
		
		if(bcrypt.matches(pwd, m.getPwd())){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", m.getId());
			map.put("newPwd", bcrypt.encode(newPwd));
			
			int result = mService.updatePassword(map);
			
			if(result > 0) {
				model.addAttribute("loginUser", mService.login(m));
				return "redirect:home.do";
			} else { 
				throw new MemberException("비밀번호 수정 실패");
			}
		} else {
			throw new MemberException("비밀번호 수정 실패");
		}
		
	}
	
	@PostMapping("updateMemImg")
	@ResponseBody
	public int updateMemImg() {
		
	}
	
	
	
}

