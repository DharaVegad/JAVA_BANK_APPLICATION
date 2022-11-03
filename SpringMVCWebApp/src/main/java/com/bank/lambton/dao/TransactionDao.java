/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is transaction dao class
*/

package com.bank.lambton.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bank.lambton.vo.Account;
import com.bank.lambton.vo.Transaction;
import com.bank.lambton.vo.Transaction.TransactionValue;
import com.bank.lambton.vo.LambtonBankTransaction;

public class TransactionDao {

	@Autowired
	AccountDao accountDao;

	private JdbcTemplate template;

	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;

	}

	public List<Transaction> getTransactionListByCustomerId(int customerId, List<Account> accountList) {
		return getTransactionList(
				"Select * from Transaction t left join withinbanktransactions wt on t.ID=wt.transtioniD where t.CustomerID="
						+ customerId + " or wt.toAccountID=" + accountList.get(0).getId());
	}

	public List<Transaction> getTransactionListByAccountId(int accountId) {
		return getTransactionList(
				"Select * from Transaction t left join withinbanktransactions wt on t.ID=wt.transtioniD where fromAccountID="
						+ accountId + " or wt.toAccountID= " + accountId + " order by commitDate");
	}

	public Transaction createPendingTransaction(Transaction t) {
		int tId = (int) Math.floor(Math.random() * (10000000 - 100000 + 1) + 100000);
		t.setId(tId); // generate random number between 10000000-100000

		String sql = "INSERT INTO Transaction (ID, CustomerID, fromAccountId, amount, remark) Values (" + t.getId()
				+ "," + t.getCustomerId() + "," + t.getFromAccountId() + "," + t.getAmount() + ",'" + t.getRemark()
				+ "');";
		System.out.println(sql);
		try {
			// insert into Customers
			if (template.update(sql) == 1)
				return t;
		} catch (DuplicateKeyException e) {
			// try again with different transaction id
			createPendingTransaction(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public boolean finaliseTransaction(Transaction t) {
		List<TransactionValue> valuesList = t.getTransactionValues();
		try {
			// if value list size is 0...only deduct amount
			if (valuesList == null || valuesList.size() == 0)
				return updateBalanceAndSetStatus(t); // deduct amount and set transaction status

			// prepare SQL Statement
			String sql = "INSERT INTO transactionvalues (optionID, optionValue, transtioniD) Values";
			for (TransactionValue tv : valuesList)
				sql += "(" + tv.getOptionId() + ", '" + tv.getOptionValue() + "', " + t.getId() + "),";
			sql = sql.substring(0, sql.length() - 1); // remove comma from last

			if (template.update(sql) == valuesList.size()) // if all the option fields are inserted
				return updateBalanceAndSetStatus(t); // deduct amount & set transaction status

		} catch (Exception e) {
			e.printStackTrace();

		}
		setTransactionStatus(t.getId(), "failed");
		return false;
	}

	public boolean finaliseWithinBankTransaction(LambtonBankTransaction t) {
		if (!accountDao.transferBalance(t.getFromAccountId(), t.getToAccountId(), t.getAmount()))
			return !setTransactionStatus(t.getId(), "failed");
		template.update("insert into WithinBankTransactions (transtioniD, toAccountID) values (" + t.getId() + ", "
				+ t.getToAccountId() + ")");
		return setTransactionStatus(t.getId(), "completed");
	}

	public boolean updateBalanceAndSetStatus(Transaction t) {
		if (!accountDao.updateBalanceBy(t.getAmount(), t.getFromAccountId()))
			return !setTransactionStatus(t.getId(), "failed");
		return setTransactionStatus(t.getId(), "completed");

	}

	public boolean setTransactionStatus(int transactionId, String status) {
		String sql = "update Transaction set status='" + status + "' where ID = " + transactionId + ";";
		if (template.update(sql) == 1)
			return true;
		return false;
	}

	public List<Transaction> getTransactionList(String sql) {
		return template.query(sql, new RowMapper<Transaction>() {

			public Transaction mapRow(ResultSet rs, int rownumber) throws SQLException {

				Transaction t = new Transaction();
				t.setId(rs.getInt("ID"));
				t.setAmount(rs.getFloat("amount"));
				t.setRemark(rs.getString("remark"));
				t.setFromAccountId(rs.getInt("fromAccountId"));
				t.setCustomerId(rs.getInt("CustomerID"));
				t.setStatus(rs.getString("status"));
				t.setCommitDate(rs.getDate("commitDate"));
				return t;

			}

		});
	}

}