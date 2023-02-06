package com.example.springsecuritystudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

	@GetMapping({"/", "/home"})
	public String index() {
		return "index";
	}

	@GetMapping("/example")
	public String example(Model model) {
		model.addAttribute("name", "정우성");
		model.addAttribute("age", 51);
		return "example";
	}

}
