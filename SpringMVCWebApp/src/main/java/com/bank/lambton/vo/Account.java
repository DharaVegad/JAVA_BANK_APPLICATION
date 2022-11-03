/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is account class
*/

package com.bank.lambton.vo;

public class Account {
	private int id;
	private int customerId;
	private String type;
	private float balance;
	private boolean isActive;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", customerId=" + customerId + ", type=" + type + ", balance=" + balance
				+ ", isActive=" + isActive + "]";
	}

}
