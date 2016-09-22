package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when there is no such a device.
 *
 */
public class PartyDeviceNotFoundException  extends Exception implements Serializable{
	/**
	 * Constructs a PartyDeviceNotFoundException with no detail message.
	 */
	public PartyDeviceNotFoundException()
	{
		super();
	}
	/**
	 * Constructs a PartyDeviceNotFoundException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public PartyDeviceNotFoundException(String msg)
	{
		super(msg);
	}
	/**
	 * Constructs a PartyDeviceNotFoundException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public PartyDeviceNotFoundException(String msg,Exception exp)
	{
		super(msg,exp);
	}
}
