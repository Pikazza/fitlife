package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when entered email id is not an usual format or when it may not be an valid email id.
 *
 */
public class PartyEmailValidateException extends Exception implements Serializable{
	/**
	 * Constructs a PartyEmailValidateException with no detail message.
	 */
	public PartyEmailValidateException() {
		super();
	}
	
	/**
	 * Constructs a PartyEmailValidateException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public PartyEmailValidateException(String msg) {
		super(msg);
	}
	/**
	 * Constructs a PartyEmailValidateException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public PartyEmailValidateException(String msg,Exception exp) {
		super(msg,exp);
	}

}
