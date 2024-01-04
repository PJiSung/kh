package com.kh.spring.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kh.spring.common.interceptor.CheckAdminInterceptor;
import com.kh.spring.common.interceptor.CheckLoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("file:///C:/uploadFiles/", "classpath:/static/");
		// image라고 붙이면 외부자원에 접근한다는 패턴, 어디 외부 자원인지 모르니까 .addResourceLocations 붙임
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 인터셉터 등록해주는 메소드
		registry.addInterceptor(new CheckLoginInterceptor())
					.excludePathPatterns("/", "/home.do", "/logout.me", "/enroll.me", "/checkId.me",
							"/checkNickName.me", "/insertMember.me", "/loginView.me", "/list.bo", "/list.at")
					.addPathPatterns("/myInfo.me", "/editMyInfo.me", "/updateMember.me", "/deleteMember.me")
					.addPathPatterns("/*.bo", "/*.at")
					.excludePathPatterns("/list.bo", "/list.at", "/getWeather.do");
		
		// 관리자 계정용 interceptor
		registry.addInterceptor(new CheckAdminInterceptor())
				.addPathPatterns("/*.adm");
		
	}
}
