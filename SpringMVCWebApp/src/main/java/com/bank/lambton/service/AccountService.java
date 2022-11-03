/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is account service class
*/

package com.bank.lambton.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.bank.lambton.dao.AccountDao;
import com.bank.lambton.vo.Account;
import com.bank.lambton.vo.Login;


public class AccountService {
	@Autowired
	public AccountDao accountDao;
	@Autowired
	public TransactionService tService;

	public List<Account> getAccountsList(Login l) {
		System.out.println();
		return accountDao.getAccountsList(l);
	}

	public boolean isAccount(int accountId) {
		return (getAccount(accountId) == null) ? false : true;
	}

	public boolean isSelfAccount(Login l, int accountId) {
		return (getSelfAccount(l, accountId) == null) ? false : true;
	}

	public Account getCustomerAccount(String emailOrAccountId, String type) {
		return accountDao.getCustomerAccount(emailOrAccountId, type);
	}

	public Account getAccount(int accountId) {
		return accountDao.getAccount(accountId);
	}

	public Account getSelfAccount(Login l, int accountId) {
		return accountDao.getAccount(l, accountId);
	}

	public float getAccountBalance(Login l, int accountId) {
		return accountDao.getAccount(l, accountId).getBalance();
	}

	public boolean transferBalance(int fromAccountId, int toAccountId, float amount) {
		return accountDao.transferBalance(fromAccountId, toAccountId, amount);
	}

}
