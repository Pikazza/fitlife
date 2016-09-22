package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when temporary verification code does not match with user's entered code.
 *
 */
public class WrongVerifyCodeException extends Exception implements Serializable{
	/**
	 * Constructs a WrongVerifyCodeException with no detail message.
	 */
	public WrongVerifyCodeException() {
		super();
	}
	/**
	 * Constructs a WrongVerifyCodeException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public WrongVerifyCodeException(String msg) {
		super(msg);
	}
	/**
	 * Constructs a WrongVerifyCodeException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public WrongVerifyCodeException(String msg,Exception exp) {
		super(msg,exp);
	}

}
