package com.turnout.ws.service;

import org.json.simple.JSONObject;

import com.turnout.ws.exception.RewardNotFoundException;
import com.turnout.ws.exception.VoucherNotFoundException;

/**
 * RewardsHasVoucherService is an interface that contains collection of methods that can be accessed for manipulating reward and voucher mapping resource. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in RewardsHasVoucherServiceImpl class.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to add or edit an ineligible object throws an RewardNotFoundException, typically VoucherNotFoundException.
 * 
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface RewardsHasVoucherService {
	/**
	 * This method used to add reward voucher. In this method voucher id mapped with reward id
	 * 
	 * @param rwdid an unique id of reward element.
	 * @param vouid an unique id of voucher element.
	 * @return returns JSONObject that has a value of success message.
	 * @throws RewardNotFoundException Exception thrown when entered reward is not found in database.
	 * @throws VoucherNotFoundException Exception thrown when entered voucher is not found in database.
	 */
	public JSONObject addRewardVoucher(int rwdid, int vouid) throws RewardNotFoundException, VoucherNotFoundException;

}
