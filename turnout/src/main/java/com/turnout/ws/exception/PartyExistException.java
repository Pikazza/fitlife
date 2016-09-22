package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when entered party is already exist in database.
 *
 */
public class PartyExistException extends Exception implements Serializable{
	/**
	 * Constructs a PartyExistException with no detail message.
	 */
	public PartyExistException() {
		super();
	}
	/**
	 * Constructs a PartyExistException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public PartyExistException(String msg) {
		super(msg);
	}
	/**
	 * Constructs a PartyExistException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public PartyExistException(String msg,Exception exp) {
		super(msg,exp);
	}

}
