/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is customer dao class
*/

package com.bank.lambton.dao;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.bank.lambton.service.EncryptionService;
import com.bank.lambton.vo.Customer;
import com.bank.lambton.vo.Login;
import com.bank.lambton.vo.Register;


public class CustomerDao {
private JdbcTemplate template;

public static final int USER_ALREADY_EXISTS = -4;
public static final int EMAIL_ALREADY_EXISTS = -5;
public static final int USERNAME_ALREADY_EXISTS = -6;
public static final int NO_ROW_EFFECTED = -1;

@Autowired
public EncryptionService enService;
@Autowired
public AccountDao accountDao;

public void setTemplate(JdbcTemplate template) {
	this.template = template;
	
}
public int register(Register r) {
	Customer c1 = null;
	
	//return if the email or username already exists 
	if(usernameExists(r.getUsername())!=null)
		return USERNAME_ALREADY_EXISTS;
	
	if(emailExists(r.getEmail())!=null)
		return EMAIL_ALREADY_EXISTS;
	
	String sql = "INSERT INTO customers (firstName, lastName, email) values ('"+r.getFirstName()+"','"+r.getLastName()+"', '"+r.getEmail()+"');";
	try {
		
		//insert into Customers
		if(template.update(sql)==1) {
			
			sql = "select * from customers where email = '"+r.getEmail()+"'";
			c1 = getCustomer(sql);
			sql = "INSERT INTO login (CustomerID, username, password) values ("+c1.getId()+", '"+r.getUsername()+"', '"+r.getPassword()+"');";
			
			//insert into Login
			if(template.update(sql)!=1) {
				//roll back if there is any error
				registerRollBack(r);
				return NO_ROW_EFFECTED;
				
			}
			//create saving & credit account with zero initial balance
			if(!accountDao.createAccounts(c1, new String[]{"Chequing", "Savings", "Credit"}, new float[] {0, 0, (float) 1000.00})) {
				registerRollBack(r);
				return NO_ROW_EFFECTED;
			}
			return 1;
		}
		
	}catch (DuplicateKeyException e) {
		// roll  back if there is any error
		registerRollBack(r);
		e.printStackTrace();
		return USER_ALREADY_EXISTS;
		
	}catch (Exception e) {
		e.printStackTrace();
		// roll back if there is any error
		
	}
	registerRollBack(r);
	return NO_ROW_EFFECTED;
	
}
public void registerRollBack(Register r) {
	try {
		template.execute("delete from customers where email='"+r.getEmail()+"'");
	}catch(Exception e) {e.printStackTrace();}
	}
public Customer emailExists(String email) {
	String sql = "select * from customers where email = '"+email+"';";
	return getCustomer(sql);
}
	
public Login usernameExists(String username) {
	String sql = "select * from login where username = '"+username+"';";
	return getLogin(sql);
}
	

public Customer getCustomerFromAccountId(int accountId) {
	return getCustomer("select * from Customers c inner join Accounts a on c.ID=a.CustomerID where a.ID = "+accountId);
	
}

public Customer getCustomer(Login l) {
	return getCustomer("select * from Customers c inner join Login l on l.CustomerID=c.ID where username = '"+l.getUsername()+"' and password='"+l.getPassword()+"';");
	}

public Login validateLogin(Login l) {
	String sql = "select * from Login where username='" + l.getUsername() + "' and password='"
			+ l.getPassword() + "';";
	return getLogin(sql);
	
}

public Customer getCustomer(String sql) {
	return template.query(sql, new ResultSetExtractor<Customer>(){
		public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
			
			Customer c=null;
			if(rs.next()) {
				c=new Customer();
				c.setId(rs.getInt("ID"));
				c.setFirstName(rs.getString("firstname"));
				c.setLastName(rs.getString("lastname"));
				c.setEmail(rs.getString("email"));
				
				
			}
			return c;
		}
	});
}

public Login getLogin(String sql) {
	try {
		return template.query(sql, new ResultSetExtractor<Login>() {
			public Login extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					Login l = new Login();
					l.setCustomerId(rs.getInt("CustomerID"));
					l.setUsername(rs.getString("username"));
					l.setPassword(rs.getString("password"));
					return l;
				}
				return null;
			}
			
		});
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


}