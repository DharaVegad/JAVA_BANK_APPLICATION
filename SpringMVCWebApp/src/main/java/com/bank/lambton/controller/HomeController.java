/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is home controller class
*/

package com.bank.lambton.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.lambton.service.AccountService;
import com.bank.lambton.service.CustomerService;
import com.bank.lambton.service.TransactionService;
import com.bank.lambton.service.UtilityService;
import com.bank.lambton.service.ViewService;
import com.bank.lambton.vo.Account;
import com.bank.lambton.vo.Login;
import com.bank.lambton.vo.Transaction;

@Controller
public class HomeController {

	@Autowired
	ViewService viewService;
	@Autowired
	UtilityService utilityService;
	@Autowired
	CustomerService customerService;
	@Autowired
	AccountService accountService;
	@Autowired
	TransactionService transactionService;

	@RequestMapping()
	public String showHome(Model m, HttpServletRequest request) {
		List<String> viewList = new ArrayList<String>(Arrays.asList("home"));

//show home if the user is logged in
		Login l = customerService.isLoggedIn((javax.servlet.http.HttpServletRequest) request, m);
		if (l == null)
			return viewService.model(m).views(Arrays.asList("login", "signup"));

		List<Account> accountList = accountService.getAccountsList(l);

// get details for the logged in user m.addAttribute("customer",customerService.getCustomer(l));
		m.addAttribute("categoriesList", utilityService.getCategoryList());
		m.addAttribute("accountsList", accountList);

		List<Transaction> transactionList = transactionService.getTransactionListByCustomerId(l.getCustomerId(),
				accountList);

		if (transactionList.size() > 0) {
			m.addAttribute("transactionsList", transactionList);
			viewList.add("showTransaction");

		}
		return viewService.model(m).views(viewList);

	}
	
	@RequestMapping("/about")
	public String showAbout(Model m, HttpServletRequest request) {
		List<String> viewList = new ArrayList<String>(Arrays.asList("about"));

//show home if the user is logged in
		Login l = customerService.isLoggedIn((javax.servlet.http.HttpServletRequest) request, m);
		if (l == null)
			return viewService.model(m).views(Arrays.asList("login", "signup"));

		List<Account> accountList = accountService.getAccountsList(l);

// get details for the logged in user m.addAttribute("customer",customerService.getCustomer(l));
		return viewService.model(m).views(viewList);

	}
	
	@RequestMapping("/contactus")
	public String showContactus(Model m, HttpServletRequest request) {
		List<String> viewList = new ArrayList<String>(Arrays.asList("contactus"));

//show home if the user is logged in
		Login l = customerService.isLoggedIn((javax.servlet.http.HttpServletRequest) request, m);
		if (l == null)
			return viewService.model(m).views(Arrays.asList("login", "signup"));

		List<Account> accountList = accountService.getAccountsList(l);

// get details for the logged in user m.addAttribute("customer",customerService.getCustomer(l));
		return viewService.model(m).views(viewList);

	}


	@RequestMapping("/categories/{categoryName}")
	public String showHome(Model m, HttpServletRequest request, @PathVariable String categoryName) {

//show home if the user is logged in
		Login l = customerService.isLoggedIn((javax.servlet.http.HttpServletRequest) request, m);

		if (l == null)
			return viewService.model(m).views(Arrays.asList("login", "signup"));

		String category = String.join(" ", categoryName.split("-"));

		m.addAttribute("customer", customerService.getCustomer(l));
		m.addAttribute("categoryName", category);
		m.addAttribute("optionList", utilityService.getCategoryOptionsList(category));
		m.addAttribute("accountList", accountService.getAccountsList(l));

		return viewService.model(m).view("transaction");
	}

	@RequestMapping("/transfer/self")
	public String showTransferSelf(Model m, HttpServletRequest request) {

//show home if the user is logged in
		Login l = customerService.isLoggedIn((javax.servlet.http.HttpServletRequest) request, m);

		if (l == null)
			return viewService.model(m).views(Arrays.asList("login", "signup"));

		m.addAttribute("customer", customerService.getCustomer(l));
		m.addAttribute("accountList", accountService.getAccountsList(l));

		return viewService.model(m).view("selfTransferForm");
	}

	@RequestMapping("/transfer/by-email")
	public String showTransferByEmail(Model m, HttpServletRequest request) {

//show home if the user is logged in
		Login l = customerService.isLoggedIn((javax.servlet.http.HttpServletRequest) request, m);

		if (l == null)
			return viewService.model(m).views(Arrays.asList("login", "signup"));

		m.addAttribute("customer", customerService.getCustomer(l));
		m.addAttribute("accountList", accountService.getAccountsList(l));

		return viewService.model(m).view("emailTransferForm");
	}

}