package com.site.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Two1Controller {
	@RequestMapping(value="/two1", method=RequestMethod.GET)
	public String homeIndex(){
		return "two1/index";
	}
}
