/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is utility dao class
*/

package com.bank.lambton.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.bank.lambton.vo.CategoryOption;
import com.bank.lambton.vo.Utility;



public class UtilityDao {
	
	private JdbcTemplate template;
	
	// get category list
	
	public List<Utility> getCategoryList(){
		
		return template.query("select * from transactioncategories", new RowMapper<Utility>(){
			
			public Utility mapRow(ResultSet rs, int rownumber) throws SQLException {
				
				Utility uc = new Utility();
				uc.setId(rs.getInt("ID"));
				uc.setName(rs.getString("CategoryName"));
				return uc;
			}
		});
	
	}
	
	public CategoryOption getCategoryOption(int optionId) {
		String sql = "select * from transactioncategoryoptions where ID="+optionId;
		return template.query(sql,new ResultSetExtractor<CategoryOption>(){
			public CategoryOption extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				if(rs.next()) {
					
					CategoryOption co = new CategoryOption();
					co.setId(rs.getInt("ID"));
					co.setCategoryId(rs.getInt("categoryID"));
					co.setTitle(rs.getString("optionTitle"));
					co.setInputName(rs.getString("inputName"));
					co.setInputType(rs.getString("inputType"));
					return co;
				}
				return null;
			
				
				}
			
		});
	}
	
	//get category options list
	
	public List<CategoryOption> getCategoryOptionsList(Utility uc){
		
		String sql = "select * from transactioncategoryoptions tco inner join TransactionCategories tc on tc.ID = tco.categoryID where tc.CategoryName='"+uc.getName()+"';";
		
		return template.query(sql, new RowMapper<CategoryOption>() {
			
			public CategoryOption mapRow(ResultSet rs, int rownumber) throws SQLException {
			
			CategoryOption co = new CategoryOption();
			co.setId(rs.getInt("ID"));
			co.setCategoryId(rs.getInt("categoryID"));
			co.setTitle(rs.getString("optionTitle"));
			co.setInputName(rs.getString("inputName"));
			co.setInputType(rs.getString("inputType"));
			System.out.println(co);
			return co;
			}
		});
	}
	// add category
	
	public JdbcTemplate getTemplate() {
		return template;
	}
	
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

}