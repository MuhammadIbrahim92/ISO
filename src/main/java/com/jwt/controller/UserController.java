package com.jwt.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.jwt.model.Employee;
import com.jwt.model.ScheduleAudit;
import com.jwt.model.User;
import com.jwt.service.EmployeeService;
import com.jwt.service.ScheduleAuditServiceImpl;
import com.jwt.service.UserServiceImpl;

@Controller
public class UserController {

	private static final Logger logger = Logger
			.getLogger(UserController.class);

	public UserController() {
		System.out.println("UserController()");
	}

	@Autowired
	private UserServiceImpl userService;

	@RequestMapping(value = "/newUser", method = RequestMethod.GET)
	public ModelAndView newUser(ModelAndView model) {
		// get all users by model and it's Dao , then model.add object w a7oot el all users ,,
		// ta2dar t add aktr mn object 3la nafs elmodel
		User user = new User();
		model.addObject("user", user);
		model.setViewName("AuditPlan");
		return model;
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public ModelAndView saveUser(@ModelAttribute User user) {
		if (user.getuSER_ID() == 0) { // if employee id is 0 then creating the
			// employee other updating the employee
			userService.addUser(user);
		} else {
			userService.updateUser(user);
		}
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public ModelAndView deleteUSer(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("id"));
		userService.deleteUser(userId);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	public ModelAndView editUser(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("id"));
		User user = userService.getUser(userId);
		ModelAndView model = new ModelAndView("AuditPlan");
		model.addObject("schedule", user);

		return model;
	}
	@RequestMapping(value = "/allUsers", method = RequestMethod.GET)
	public ModelAndView allUsers(ModelAndView model) throws IOException {
		List<User> listUsers = userService.getAllUsers();
		System.out.println("SIZeeeee : "+listUsers.size());
		model.addObject("listUsers", listUsers);
		// MAGED: create new page for all schedules like home page 
		model.setViewName("allSchedules");
		return model;
	}

}