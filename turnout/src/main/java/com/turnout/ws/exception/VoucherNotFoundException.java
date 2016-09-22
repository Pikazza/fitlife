package com.turnout.ws.exception;

import java.io.Serializable;

/**
 * Exception thrown when entered voucher is not found in database.
 *
 */
public class VoucherNotFoundException extends Exception implements Serializable {
	/**
	 * Constructs a VoucherNotFoundException with no detail message.
	 */
	public VoucherNotFoundException() {
	super();
	}
	/**
	 * Constructs a VoucherNotFoundException with the specified detail message.
	 * 
	 * @param msg the detail message.
	 */
	public VoucherNotFoundException(String msg) {
	super(msg);
	}
	/**
	 * Constructs a VoucherNotFoundException with the specified Exception and detail message.
	 * 
	 * @param msg the detail message.
	 * @param exp the embedded exception.
	 */
	public VoucherNotFoundException(String msg,Exception exp) {
	super(msg,exp);
	}

}
