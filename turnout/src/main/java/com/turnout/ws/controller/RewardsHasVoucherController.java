package com.turnout.ws.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.exception.RewardNotFoundException;
import com.turnout.ws.exception.VoucherNotFoundException;
import com.turnout.ws.service.RewardsHasVoucherService;

/**
*
* RewardsHasVoucherController class is front face for accessing and manipulating reward and voucher related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* It is acting like mapping class for reward and voucher and it allows to add voucher code into database map it to specific reward. 
*/
@RestController
@Path("/")
public class RewardsHasVoucherController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RewardsHasVoucherController.class);
	
	private final RewardsHasVoucherService rewardsHasVoucherService;
	
	/**
	 * An injectable constructor with a dependency of rewardsHasVoucherService as argument.
	 * 
	 * @param rewardsHasVoucherService An Object of rewardsHasVoucherService as an injectable member.
	 * @see rewardsHasVoucherService
	 */
	@Autowired
	public RewardsHasVoucherController(final RewardsHasVoucherService rewardsHasVoucherService) {
		this.rewardsHasVoucherService = rewardsHasVoucherService;
	}
	
	/**
	 * This method used to add reward voucher. In this method voucher id mapped with reward id
	 * 
	 * @param rwdVou A JSONOject contains information of voucher.
	 * @return Returns JSONObject that has a value of success message.
	 * @throws NumberFormatException Exception thrown when you try to convert string to number.
	 * @throws RewardNotFoundException Exception thrown when entered reward is not found in database.
	 * @throws VoucherNotFoundException Exception thrown when entered voucher is not found in database.
	 */
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/addRewardVoucher")
	@Produces("application/json")
	public JSONObject addRewardVoucher(@Valid JSONObject rwdVou) throws NumberFormatException, RewardNotFoundException, VoucherNotFoundException{
		String rwdid = rwdVou.get("RWD_ID").toString();
		String vouid = rwdVou.get("VOC_ID").toString();
		return rewardsHasVoucherService.addRewardVoucher(Integer.parseInt(rwdid),Integer.parseInt(vouid));		
	}

}
