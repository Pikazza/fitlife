package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when entered party name is not valid or it may contain alpha numeric value.
 *
 */
public class PartyNameValidateException extends Exception implements Serializable{
	/**
	 * Constructs a PartyNameValidateException with no detail message.
	 */
	public PartyNameValidateException() {
		super();
	}
	
	/**
	 * Constructs a PartyNameValidateException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public PartyNameValidateException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a PartyNameValidateException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public PartyNameValidateException(String msg,Exception exp) {
		super(msg,exp);
	}
	
	

}
