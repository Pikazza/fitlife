package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when entered reward is not found in database.
 *
 */
public class RewardNotFoundException extends Exception implements Serializable {
	/**
	 * Constructs a RewardNotFoundException with no detail message.
	 */
	public RewardNotFoundException()
	{
		super();
	}
	/**
	 * Constructs a RewardNotFoundException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public RewardNotFoundException(String msg)
	{
		super(msg);
	}
	/**
	 * Constructs a RewardNotFoundException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public RewardNotFoundException(String msg,Exception exp)
	{
		super(msg,exp);
	}

}
