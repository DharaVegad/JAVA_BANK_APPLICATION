/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is transaction service class
*/

package com.bank.lambton.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.bank.lambton.dao.TransactionDao;
import com.bank.lambton.vo.Account;
import com.bank.lambton.vo.CategoryOption;
import com.bank.lambton.vo.Login;
import com.bank.lambton.vo.Transaction;
import com.bank.lambton.vo.Transaction.TransactionValue;
import com.bank.lambton.vo.LambtonBankTransaction;

interface TransactionServiceInterface {
	public Transaction validateTransaction(HttpServletRequest request, Login l, Model m, String categoryName);

	public Transaction createPendingTransaction(Transaction t);

	public boolean finaliseTransaction(Transaction t);
}

public class TransactionService implements TransactionServiceInterface {

	@Autowired
	UtilityService utilityService;
	@Autowired
	AccountService accountService;
	@Autowired
	TransactionDao transactionDao;
	@Autowired
	CustomerService customerService;

	public List<Transaction> getTransactionListByCustomerId(int customerId, List<Account> accountList) {
		return transactionDao.getTransactionListByCustomerId(customerId, accountList);
	}

	public List<Transaction> getTransactionListByAccountId(int accountId) {
		return transactionDao.getTransactionListByAccountId(accountId);
	}

	public Transaction validateTransactionData(HttpServletRequest request, Login l, Model m, String categoryName) {

		try {
			float amount = Float.parseFloat((String) request.getParameter("amount"));
			amount = (amount < 0) ? -amount:amount; // absolue amount
			amount = (categoryName.contains("eposit")) ? amount : -amount;
			int accountId = Integer.parseInt((String) request.getParameter("accountId"));

			if (-amount > accountService.getAccountBalance(l, accountId)) {
				m.addAttribute("TerrorMessage", "Insufficient balance!");
				return null; // insufficient balance
			}
			// create transaction object
			Transaction transaction = new Transaction();
			transaction.setFromAccountId(accountId);
			transaction.setAmount(amount);
			transaction.setCustomerId(l.getCustomerId());
			transaction.setRemark((String) request.getParameter("remark"));

			transaction.setStatus("pending");
			return transaction;
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("errorMessage", "Incorrect input!");
		}
		return null;
	}

	public Transaction validateTransaction(HttpServletRequest request, Login l, Model m, String categoryName) {
		try {
			Transaction validatedTransaction = validateTransactionData(request, l, m, categoryName);
			if (validatedTransaction == null)
				return null;
			List<CategoryOption> options = utilityService.getCategoryOptionsList(categoryName);
			List<TransactionValue> valueList = new ArrayList<TransactionValue>();

			// get only those request attributes which belong to this transaction category
			for (CategoryOption o : options) {

				if (!requestContains(o.getInputName(), request)) {
					m.addAttribute("errorMessage", "Please fill all the input fields!");
					return null; // empty required field
				}
				TransactionValue tValue = new TransactionValue();
				// set transaction option values
				tValue.setOptionId(o.getId());
				tValue.setOptionValue((String) request.getParameter(o.getInputName()));
				valueList.add(tValue); // add to value list
			}
			validatedTransaction.setTransactionValues(valueList);
			// add pending transaction to database
			validatedTransaction.setStatus("pending");
			return createPendingTransaction(validatedTransaction);
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("errorMessage", " Incorrect input!");
			return null;
		}
	}

	public boolean finaliseTransaction(Transaction t) {
		return transactionDao.finaliseTransaction(t);
	}

	public Transaction createPendingTransaction(Transaction t) {
		return transactionDao.createPendingTransaction(t);
	}

	public LambtonBankTransaction validateWithinBankTransfer(HttpServletRequest request, Login l, Model m,
			int toAccountId) {
// verfy fromAccount balance and toAccount account
		System.out.println("Idsssssss"+request.getParameter("Id"));
		Account fromAccount = accountService.getSelfAccount(l,
				Integer.parseInt((String) request.getParameter("accountId")));
		Account toAccount = accountService.getAccount(toAccountId);

		if(fromAccount == null || toAccount == null) {
			m.addAttribute("errorMessage", "Invalid account!");
			return null;
		}
		Transaction vt = validateTransactionData(request, l, m, " ");
		if (vt == null)
			return null;
		// create pending transaction
		LambtonBankTransaction t = new LambtonBankTransaction();
		float amount = vt.getAmount();
		amount = (amount < 0) ? -amount : amount; // absolute amount
		t.setAmount(amount);
		t.setRemark(vt.getRemark());
		t.setFromAccountId(vt.getFromAccountId());
		t.setCustomerId(vt.getCustomerId());
		t.setToAccountId(toAccount.getId());

		return (LambtonBankTransaction) createPendingTransaction(t);
	}

	public Transaction validateSelfTransfer(HttpServletRequest request, Login l, Model m, float amountSign) {
		int toAccountId = Integer.parseInt(request.getParameter("toAccountId"));
		return validateWithinBankTransfer(request, l, m, toAccountId);
	}

	public boolean finaliseWithinBankTransaction(HttpServletRequest request) {
		LambtonBankTransaction withinBankT = (LambtonBankTransaction) request.getSession().getAttribute("transaction");
		return transactionDao.finaliseWithinBankTransaction(withinBankT);
	}

// check if the request has specified attribute
	private boolean requestContains(String attribute, HttpServletRequest request) {
		Enumeration attrs = request.getParameterNames();
		while (attrs.hasMoreElements()) {
			if (attrs.nextElement().equals(attribute)) {
				if (!((String) request.getParameter(attribute)).trim().equals(""))
					return true;
				else
					return false;
			}
		}
		return false;
	}

}