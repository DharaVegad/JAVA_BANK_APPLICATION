/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is bank transaction class
*/

package com.bank.lambton.vo;

public class LambtonBankTransaction extends Transaction {

	private int transactionId;
	private int toAccountId;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(int toAccountId) {
		this.toAccountId = toAccountId;
	}

}