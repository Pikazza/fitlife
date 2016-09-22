package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when comments field are empty because comments can not store an empty value in database.
 *
 */
public class CommentsNotEmptyException  extends Exception implements Serializable{
	/**
	 * Constructs a CommentsNotEmptyException with no detail message.
	 */
	public CommentsNotEmptyException()
	{
		super();
	}
	/**
	 * Constructs a CommentsNotEmptyException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public CommentsNotEmptyException(String msg)
	{
		super(msg);
	}
	/**
	 * Constructs a CommentsNotEmptyException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public CommentsNotEmptyException(String msg,Exception exp)
	{
		super(msg,exp);
	}
}
