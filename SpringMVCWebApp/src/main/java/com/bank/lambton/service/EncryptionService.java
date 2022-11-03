/* Date -@07-08-2021
Author - @Dhara vegad, Priyal patel
Description - this is encryption service class
*/

package com.bank.lambton.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;












public class EncryptionService {

	public String encrypt(String input) {
		try {
			//getInstance() method is called with algorithm SH-512
			MessageDigest md  = MessageDigest.getInstance("SHA-512");
			
			
			
			byte[] messageDigest = md.digest(input.getBytes());
			
			
			BigInteger no = new BigInteger(1, messageDigest);
			
			
			String hashtext = no.toString(16);
			
			
			while (hashtext.length()<32) {
				hashtext = "0" + hashtext;
			}
			
			
			return hashtext;
		}
		
		
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
			}
		}