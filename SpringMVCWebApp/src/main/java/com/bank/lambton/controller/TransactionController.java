/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is transaction controller class
*/

package com.bank.lambton.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bank.lambton.service.AccountService;
import com.bank.lambton.service.CustomerService;
import com.bank.lambton.service.TransactionService;
import com.bank.lambton.service.UtilityService;
import com.bank.lambton.service.ViewService;
import com.bank.lambton.vo.Account;
import com.bank.lambton.vo.Login;
import com.bank.lambton.vo.Register;
import com.bank.lambton.vo.Transaction;

@Controller
public class TransactionController {

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

	@RequestMapping("transactionProcess")
	public String processTransaction(Model m, HttpServletRequest request) {

		// show home if the user is logged in
		Login l = customerService.isLoggedIn(request);
		if (l == null) {
			// else show login page
			m.addAttribute("login", new Login());
			m.addAttribute("register", new Register());
			return viewService.model(m).views(Arrays.asList("login", "signup"));
		}

		// create pending Transaction
		Transaction transaction = transactionService.validateTransaction(request, l, m,
				request.getParameter("categoryName"));

		if (transaction == null) {

			return "redirect:/categories/" + String.join(" ", request.getParameter("categoryName").split("-"));
		}
		// store data in session
		request.getSession().setAttribute("transaction", transaction);

		// show confirm transaction page
		m.addAttribute("redirect", "./transaction-status");
		m.addAttribute("utilityServiceObject", utilityService);
		return viewService.model(m).view("confirmTransaction");

//		return "redirect:/transaction-status";
	}

	@RequestMapping("/selfTransferProcess")
	public String processSelfTransfer(Model m, HttpServletRequest request) {

		// show home if the user is logged in
		Login l = customerService.isLoggedIn(request);
		if (l == null) {
			// else show login page
			m.addAttribute("login", new Login());
			m.addAttribute("register", new Register());
			return viewService.model(m).views(Arrays.asList("login", "signup"));
		}

		Transaction transaction = transactionService.validateSelfTransfer(request, l, m, 1);

		if (transaction == null) {
			// m.addAttribute("status","transfer fail");
			return "redirect:/transfer/self";
			// return viewService.model(m).view("transactionStatus");

		}
		// store data in session
		request.getSession().setAttribute("transaction", transaction);

		// show confirm transaction page
		return "redirect:/transfer-transaction-status";
	}

	@RequestMapping("/emailTransferProcess")
	public String processEmailTransfer(Model m, HttpServletRequest request) {

		// show home if the user is logged in
		Login l = customerService.isLoggedIn(request);
		if (l == null) {
			// else show login page
			m.addAttribute("login", new Login());
			m.addAttribute("register", new Register());
			return viewService.model(m).views(Arrays.asList("login", "signup"));
		}
		String emailOrAccountId = request.getParameter("emailOrAccountId").trim();

		Account account = accountService.getCustomerAccount(emailOrAccountId, "Savings");
		if (account == null) {
			m.addAttribute("errorMessage", "Invalid email or account number");
			return "redirect:/transfer/by-email";

		}
		// create pending transactionService
		Transaction transaction = transactionService.validateWithinBankTransfer(request, l, m, account.getId()); // -1
																													// for
																													// negative
																													// transactions
		if (transaction == null) {
			m.addAttribute("status", "transfer fail");
			return viewService.model(m).view("transactionStatus");

		}

		// store data in session
		request.getSession().setAttribute("transaction", transaction);

//		m.addAttribute("redirect", "./transfer-transaction-status");
//		m.addAttribute("customerServiceObject", customerService);
//		return viewService.model(m).view("confirmEmailTransaction");
		// show success or fail
		m.addAttribute("status", "success");

		return viewService.model(m).view("transactionStatus");

	}

	@RequestMapping("/transfer-transaction-status")
	public String confirmTransferTransaction(Model m, HttpServletRequest request) {

		// show home if the user is logged in
		Login l = customerService.isLoggedIn(request);
		if (l == null) {
			// else show login page
			m.addAttribute("login", new Login());
			m.addAttribute("register", new Register());
			return viewService.model(m).views(Arrays.asList("login", "signup"));
		}

		// show success or fail
		if (!transactionService.finaliseWithinBankTransaction(request)) {
			m.addAttribute("status", "transfer fail");
			return viewService.model(m).view("transactionStatus");

		}
		m.addAttribute("status", "transfer success");
		return viewService.model(m).view("transactionStatus");
//		return "redirect:/transaction-status";
	}

	@RequestMapping("/transaction-status")
	public String confirmTransaction(Model m, HttpServletRequest request) {

		// show home if the user is logged in
		Login l = customerService.isLoggedIn(request);
		if (l == null) {
			// else show login page
			m.addAttribute("login", new Login());
			m.addAttribute("register", new Register());
			return viewService.model(m).views(Arrays.asList("login", "signup"));
		}

		boolean isSuccessful = transactionService
				.finaliseTransaction((Transaction) request.getSession().getAttribute("transaction"));

		// show success or fail
		if (isSuccessful)
			m.addAttribute("status", "success");
		else
			m.addAttribute("status", "fail");

		return viewService.model(m).view("transactionStatus");
//		return "redirect:/transaction-status";

	}

}