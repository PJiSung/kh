package com.kh.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
//	@RequestMapping("home.do)")
//	public String home() {
//		return null;
//	}
	
	@GetMapping("home.do") 		// get방식 - GetMapping
	public String home() {		// post - PostMapping
		return "home";
	}
}
