package com.turnout.ws.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 *
 */
@Component
public class EncryptDecrypt {
	private static final Logger LOGGER=LoggerFactory.getLogger(InstagramPostReceiver.class);
	
	/**
	 * This method used to encrypt the parties password instead of storing plain text in database.
	 * It will call a static method makeSecurePassword for encryption logic.
	 * 
	 * @param password A password of a party.
	 * @return Returns encrypted password.
	 */
	public String encrypt(String password)
	{
		
		String securePassword = makeSecurePassword(password);
		
		return securePassword;
		
	}
	
	/**
	 * This method used to encrypt the parties password instead of storing plain text in database.
	 * 
	 * @param password A password of a party.
	 * @return Returns encrypted password.
	 */
	private static String makeSecurePassword(String password)
    {
        String generatedPassword = null;
        try {
            byte[] salt = "f!#@r^!#ysfujarczuk$#@pha^!@".getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(password.getBytes());
            md.update(salt);               
            generatedPassword = new BigInteger(1,md.digest()).toString(16);
            System.out.println("________________________________________________--");
            System.out.println(generatedPassword);
            System.out.println("________________________________________________--");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
