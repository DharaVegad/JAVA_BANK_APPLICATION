/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is register controller class
*/

package com.bank.lambton.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.lambton.dao.CustomerDao;
import com.bank.lambton.service.CustomerService;
import com.bank.lambton.service.ViewService;
import com.bank.lambton.vo.Login;
import com.bank.lambton.vo.Register;



@Controller
public class RegisterController {
	@Autowired
	ViewService viewService;
	@Autowired
	CustomerService customerService;

	@RequestMapping("/signup")
	public String showSignup(Model m) {

		m.addAttribute("register", new Register());
		return viewService.model(m).view("signup");
	}

	@RequestMapping(value = "/registerProcess", method = RequestMethod.POST)
	public String processRegisteration(@Valid @ModelAttribute Register register, BindingResult br, Model m) {
		String view = "signup";
		String errorMessage = "";

		if (br.hasErrors())
			return viewService.model(m).view(view);

		int registerResult = customerService.register(register);

		if (registerResult == CustomerDao.EMAIL_ALREADY_EXISTS)
			errorMessage = "This email has been already taken!";

		if (registerResult == CustomerDao.USERNAME_ALREADY_EXISTS) {
			errorMessage = "Username already taken!";
		
		}

		if (registerResult == CustomerDao.USER_ALREADY_EXISTS) {
			errorMessage = "Customer already exists,please check your input!";
		}
			
		if (registerResult == CustomerDao.NO_ROW_EFFECTED)
			errorMessage = "An error has occurred!Please try again";

		if (registerResult == 1) {
			m.addAttribute("message", "Registration successfull,You can login now!");
			m.addAttribute("login", new Login());
			view = "login";

		}
		m.addAttribute("errorMessage", errorMessage);

		return viewService.model(m).view(view);
	}
}