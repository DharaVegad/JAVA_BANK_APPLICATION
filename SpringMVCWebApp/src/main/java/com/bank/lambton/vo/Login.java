/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is login class
*/

package com.bank.lambton.vo;

import javax.validation.constraints.Size;

public class Login {
	private int customerId;
	@Size(min=4,message="username must be greater than 4 characters")
	private String username;
	@Size(min=3,message="password must be greater or equal to 8 characters")
	private String password;

	public int getCustomerId(){
	return customerId;
	}

	public void setCustomerId(int customerId){
	this.customerId=customerId;
	}

	public String getUsername(){
	return username;
	}

	public void setUsername(String username)
	{
	this.username=username;
	}

	public String getPassword(){
	return password;
	}

	public void setPassword(String password){
	this.password=password;
	}

	}

