/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is customer service class
*/

package com.bank.lambton.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.bank.lambton.dao.CustomerDao;
import com.bank.lambton.vo.Customer;
import com.bank.lambton.vo.Login;
import com.bank.lambton.vo.Register;


/**
 * 
 * @author rahulchhabra
 *
 */


interface UserServiceInterface {

	int register(Register customer);

	com.bank.lambton.vo.Login validateLogin(Login login);

	Customer getCustomer(Login login);

	Customer getCustomerFromAccountId(int id);
}

public class CustomerService implements UserServiceInterface {
	@Autowired
	public CustomerDao customerDao;
	@Autowired
	public EncryptionService enService;

	public int register(Register register) {

		// Encrypting password
		String encrypted = enService.encrypt(register.getPassword().trim());

		// return 0 row effected if could not encrypt password
		if (encrypted == null)
			return 0;
		register.setPassword(encrypted);
		return customerDao.register(register);
	}

	public Login validateLogin(Login login) {
		// encrypting to match with the encrypted password in database
		login.setPassword(enService.encrypt(login.getPassword()));
		return validateLoginToken(login);
	}

	public Login validateLoginToken(Login login) {
		return customerDao.validateLogin(login);
	}

	public Login isLoggedIn(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();
		String username = null, password = null;

		if (cookies != null) {

			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("username"))
					username = cookie.getValue();
				if (cookie.getName().equals("password"))
					password = cookie.getValue();
			}
		}
		

		if (null == username || null == password || username.equals("") || password.equals(""))
			return null;

		Login login = new Login();
		login.setUsername(username);
		login.setPassword(password);
		Login l = validateLoginToken(login);
		if (l != null)
			return l;
		return null;
	}

	public Login isLoggedIn(HttpServletRequest request, Model m) {
		Login l = isLoggedIn(request);
		if (l != null)
			return l;
		m.addAttribute("login", new Login());
		m.addAttribute("register", new Register());
		return null;
	}

	public Customer getCustomer(Login login) {
		return customerDao.getCustomer(login);
	}

	public Customer getCustomerFromAccountId(int id) {
		return customerDao.getCustomerFromAccountId(id);
	}

}