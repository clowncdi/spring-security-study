package com.example.springsecuritystudy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/user")
	public String user() {
		return "user";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@GetMapping("/example")
	public String example(Model model) {
		model.addAttribute("name", "정우성");
		model.addAttribute("age", 51);
		return "example";
	}

}
