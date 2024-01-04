package com.kh.spring.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration // 설정 파일 역할을 하는 클래스를 빈으로 등록
public class TemplateResolverConfig {
	
	@Bean
	public ClassLoaderTemplateResolver dotDoResolver() {
		ClassLoaderTemplateResolver dotDo = new ClassLoaderTemplateResolver();
		dotDo.setPrefix("templates/views/");
		dotDo.setSuffix(".html");
		dotDo.setTemplateMode(TemplateMode.HTML);
		dotDo.setCharacterEncoding("UTF-8");
		dotDo.setOrder(1);
		dotDo.setCacheable(false);		
		dotDo.setCheckExistence(true);	// resolver가 연쇄적으로 작동할 수 있게끔 해줌
		
		
		return dotDo;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotMeResolver() {
		ClassLoaderTemplateResolver dotMe = new ClassLoaderTemplateResolver();
		dotMe.setPrefix("templates/views/member/");	// 완벽하게 찾아주지 않고 돌아가면서 실행하되 걸리면 됨
		dotMe.setSuffix(".html");
		dotMe.setTemplateMode(TemplateMode.HTML);
		dotMe.setCharacterEncoding("UTF-8");
		dotMe.setOrder(2);
		dotMe.setCacheable(false);		
		dotMe.setCheckExistence(true);	
		
		
		return dotMe;
	}
	@Bean
	public ClassLoaderTemplateResolver dotBoResolver() {			// alt + shift + r : 한번에 다 바꿔줌
		ClassLoaderTemplateResolver dotBo = new ClassLoaderTemplateResolver();
		dotBo.setPrefix("templates/views/board/");	// 완벽하게 찾아주지 않고 돌아가면서 실행하되 걸리면 됨
		dotBo.setSuffix(".html");
		dotBo.setTemplateMode(TemplateMode.HTML);
		dotBo.setCharacterEncoding("UTF-8");
		dotBo.setOrder(3);
		dotBo.setCacheable(false);		
		dotBo.setCheckExistence(true);	
		
		return dotBo;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotAtResolver() {			// alt + shift + r : 한번에 다 바꿔줌
		ClassLoaderTemplateResolver dotAt = new ClassLoaderTemplateResolver();
		dotAt.setPrefix("templates/views/attm/");	
		dotAt.setSuffix(".html");
		dotAt.setTemplateMode(TemplateMode.HTML);
		dotAt.setCharacterEncoding("UTF-8");
		dotAt.setOrder(4);
		dotAt.setCacheable(false);		
		dotAt.setCheckExistence(true);	
		
		return dotAt;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotAdResolver() {			// alt + shift + r : 한번에 다 바꿔줌
		ClassLoaderTemplateResolver dotAd = new ClassLoaderTemplateResolver();
		dotAd.setPrefix("templates/views/admin/");	
		dotAd.setSuffix(".html");
		dotAd.setTemplateMode(TemplateMode.HTML);
		dotAd.setCharacterEncoding("UTF-8");
		dotAd.setOrder(4);
		dotAd.setCacheable(false);		
		dotAd.setCheckExistence(true);	
		
		return dotAd;
	}

}