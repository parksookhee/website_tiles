package com.site.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ThreeController {
	@RequestMapping(value="/three", method=RequestMethod.GET)
	public String homeIndex(){
		return "three/index";
	}
}
