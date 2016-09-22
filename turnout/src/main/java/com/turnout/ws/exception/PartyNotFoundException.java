package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when entered party is not found.
 *
 */
public class PartyNotFoundException  extends Exception implements Serializable{
	/**
	 * Constructs a PartyNotFoundException with no detail message.
	 */
	public PartyNotFoundException()
	{
		super();
	}
	/**
	 * Constructs a PartyNotFoundException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public PartyNotFoundException(String msg)
	{
		super(msg);
	}
	/**
	 * Constructs a PartyNotFoundException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public PartyNotFoundException(String msg,Exception exp)
	{
		super(msg,exp);
	}
}
