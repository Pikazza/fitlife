package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when current pass word not match with user's entered password.
 *
 */
public class CurrentPasswordNotMatchedException extends Exception implements Serializable{

	/**
	 * Constructs a CurrentPasswordNotMatchedException with no detail message.
	 */
	public CurrentPasswordNotMatchedException() {
		super();
	}
	
	/**
	 * Constructs a CurrentPasswordNotMatchedException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public CurrentPasswordNotMatchedException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a CurrentPasswordNotMatchedException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public CurrentPasswordNotMatchedException(String msg,Exception exp) {
		super(msg,exp);
	}
}
