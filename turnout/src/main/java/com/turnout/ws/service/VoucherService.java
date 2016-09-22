package com.turnout.ws.service;

import org.json.simple.JSONObject;

/**
 * RewardService is an interface that contains collection of methods that can be accessed for manipulating voucher codes. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in VoucherServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface VoucherService {
	/**
	 * This method used to store voucher details into database and it will return (primary) key voucher id.
	 * 
	 * @param voc_code vocuher code
	 * @param rwdpoints discount percentage for that particular voucher code.
	 * @param voc_status a string may have the value of active or inactive.
	 * @return return inserted voucher id.
	 */
	public JSONObject addVoucher(String voc_code, int rwdpoints, String voc_status);
}