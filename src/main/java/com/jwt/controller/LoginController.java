package com.jwt.controller;

import java.io.IOException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jwt.model.Employee;

@Controller
public class LoginController {
	private static final Logger logger = Logger
			.getLogger(LoginController.class);
	@RequestMapping(value = "/")
	public ModelAndView login(ModelAndView model) throws IOException {
		
		model.setViewName("Login");
		return model;
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("Login");

		return model;

	}
	
	@RequestMapping(value = "/logout")
	public ModelAndView logout(ModelAndView model) throws IOException {
		
		model.setViewName("Login");
		return model;
	}
	@RequestMapping(value = "/Dashboard")
	public ModelAndView Dashboard(ModelAndView model) throws IOException {
		
		model.setViewName("Dashboard");
		return model;
	}

	@RequestMapping(value = "/Reporting")
	public ModelAndView Reporting(ModelAndView model) throws IOException {
		model.addObject("srcValue", "http://192.168.0.8:8080/ReportPortal/report.html?Module=8&lang=en");
		model.setViewName("Reporting");
		return model;
	}	
	@RequestMapping(value = "/ETL")
	public ModelAndView ETL(ModelAndView model) throws IOException {
		//model.addObject("srcValue", "http://192.168.0.8:8080/ReportPortal/report.html?Module=8&lang=en");
		model.setViewName("ETL");
		return model;
	}	
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();
		
		//check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);
		
			model.addObject("username", userDetail.getUsername());
		}
		
		model.setViewName("403");
		return model;

	}
}
