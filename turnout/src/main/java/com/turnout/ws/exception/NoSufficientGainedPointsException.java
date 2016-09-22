package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when there is no sufficient gained point while redeeming reward.
 *
 */
public class NoSufficientGainedPointsException extends Exception implements Serializable{

	/**
	 * Constructs a NoSufficientGainedPointsException with no detail message.
	 */
	public NoSufficientGainedPointsException() {
		super();
	}
	
	/**
	 * Constructs a NoSufficientGainedPointsException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public NoSufficientGainedPointsException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a NoSufficientGainedPointsException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public NoSufficientGainedPointsException(String msg,Exception exp) {
		super(msg,exp);
	}
	
}
