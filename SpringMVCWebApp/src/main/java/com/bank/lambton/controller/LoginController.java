/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is login controller class
*/

package com.bank.lambton.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.lambton.service.CustomerService;
import com.bank.lambton.service.ViewService;
import com.bank.lambton.vo.Login;

@Controller
public class LoginController {

	@Autowired
	ViewService viewService;
	@Autowired
	CustomerService customerService;

	@RequestMapping("/login")
	public String showLogIn(Model m, HttpServletRequest request) {

		Login l = customerService.isLoggedIn(request);
		if (l == null)
			// redirect to home if already logged in
			return "redirect:/";

		m.addAttribute("login", new Login());
		return viewService.model(m).view("login");
	}

	@RequestMapping("/logout")
	public String logout(Model m, HttpServletRequest response) {
		//log out and redirect to home
		((HttpServletResponse) response).addCookie(new Cookie("username", ""));
		((HttpServletResponse) response).addCookie(new Cookie("customerId", ""));
		((HttpServletResponse) response).addCookie(new Cookie("password", ""));
		return "redirect:/";
	}

	@RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
	public String processLogin(@Valid @ModelAttribute Login l, BindingResult br, Model m, HttpServletResponse response,
			HttpServletRequest request) {

		String view = "login";

		if (br.hasErrors())
			return viewService.model(m).view(view);

		//validate user name and password
		Login login = customerService.validateLogin(l);

		if (login == null) {
			m.addAttribute("errorMessage", "Invalid username or password");
			return viewService.model(m).view(view);

		}

		//set cookies
		response.addCookie(new Cookie("username", login.getUsername()));
		response.addCookie(new Cookie("firstname", customerService.getCustomer(l).getFirstName()));
		response.addCookie(new Cookie("customerId", String.valueOf(login.getCustomerId())));
		response.addCookie(new Cookie("password", login.getPassword()));

		// redirect to home if login is successful
		return "redirect:/";
	}

}