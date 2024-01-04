package com.kh.spring.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckAdminInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Member loginUser = (Member)request.getSession().getAttribute("loginUser");
		if(loginUser == null || loginUser.getIsAdmin().equals("N")) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('접근 권한이 없습니다.'); location.href='home.do';</script>");
			
			return false;
		} else {
			
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
