package com.kh.spring.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.spring.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckLoginInterceptor implements HandlerInterceptor{

	// url을 가기★전에★(selectBoard.bo) 처리 되어있는지(로그인 되어있는지) 아닌지 확인하고 url을 select해도(selectBoard.bo로 가도) 늦지않음(preHandle)
	// postHandle을 사용하면 이미 다 select 해놓은 상태(selectBoard.bo 들어간 상태)에서 안해도 되는 처리작업(로그인확인)을 또 해야함
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		if(loginUser == null) {
			String url = request.getRequestURI();	// 현재 url
			String msg = null;
			if(url.contains("selectBoard.bo") || url.contains("selectAttm.at")) {
				msg = "로그인 후 이용하세요";
			} else {
				msg = "로그인 세션이 만료되어 로그인 화면으로 넘어갑니다.";
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('" + msg + "'); location.href='loginView.me';</script>");
			
			return false;	// 못 넘어가게 만듦, loginView.me로 못가는거지 selectBoard.bo나 selectAttm.at으로 못갈건 아니니까
		}
		
		
		
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
		// 무조건 true 반환(controller로 넘어갈 수 있게)
	}

}
