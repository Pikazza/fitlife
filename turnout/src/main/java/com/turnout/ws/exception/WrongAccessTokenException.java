package com.turnout.ws.exception;

/**
 * Exception thrown when entered access token of instagram is wrong or not a valid one.
 *
 */
public class WrongAccessTokenException extends Exception {
	/**
	 * Constructs a WrongAccessTokenException with no detail message.
	 */
	public WrongAccessTokenException() {
		super();
	}
	/**
	 * Constructs a WrongAccessTokenException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public WrongAccessTokenException(String msg) {
		super(msg);
	}
	/**
	 * Constructs a WrongAccessTokenException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public WrongAccessTokenException(String msg,Exception exp) {
		super(msg,exp);
	}
	

}
