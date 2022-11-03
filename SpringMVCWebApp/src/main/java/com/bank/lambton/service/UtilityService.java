/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is utility service class
*/

package com.bank.lambton.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bank.lambton.dao.UtilityDao;
import com.bank.lambton.vo.CategoryOption;
import com.bank.lambton.vo.Utility;


/**
 * 
 * @author rahulchhabra
 *
 */
interface UtilityServiceInterface {
	
	public List<Utility> getCategoryList();
	public List<CategoryOption> getCategoryOptionsList(Utility uc);
	public List<CategoryOption> getCategoryOptionsList(String categoryName);
	public CategoryOption getCategoryOption(int optionId);
}

public class UtilityService implements UtilityServiceInterface {
	@Autowired
	public UtilityDao utilityDao;
	
public List<Utility> getCategoryList(){
	
	return utilityDao.getCategoryList();
}

// get option list from category object
public List<CategoryOption> getCategoryOptionsList(Utility uc){
	return utilityDao.getCategoryOptionsList(uc);
}

// get option list from category name
public List<CategoryOption> getCategoryOptionsList(String categoryName){
	Utility utilityCategory = new Utility();
	utilityCategory.setName(categoryName);
	return utilityDao.getCategoryOptionsList(utilityCategory);
}

public CategoryOption getCategoryOption(int optionId) {
	return utilityDao.getCategoryOption(optionId);
}

}