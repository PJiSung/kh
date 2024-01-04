package com.kh.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})	
// 현재 클래스가 Main클래스임을 SpringBoot에게 알려주는 어노테이션
public class Application {

	public static void main(String[] args) {
		// System.setProperty("server.servlet.context-path", "/springboot/");
		SpringApplication.run(Application.class, args);
	}

}
