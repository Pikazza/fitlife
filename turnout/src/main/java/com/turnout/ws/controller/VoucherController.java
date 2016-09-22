package com.turnout.ws.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.service.VoucherService;

/**
*
* VoucherController class is front face for accessing and manipulating voucher codes related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* Logic for adding new voucher codes to a reward is done here.
*/
@RestController
@Path("/")
public class VoucherController {
	private static final Logger LOGGER=LoggerFactory.getLogger(VoucherController.class);
	private final VoucherService voucherService;
	
	/**
	 * An injectable constructor with a dependency of VoucherService as argument.
	 * 
	 * @param voucherService An Object of VoucherService as an injectable member.
	 * @see VoucherService
	 */
	@Autowired
	public VoucherController(final VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	
	/**
	 * This method used to save voucher code into database. It will return inserted or updated voucher id.
	 * 
	 * @param voucher A JSONObject that holds details of voucher to be inserted.
	 * @return Returns JSONObject with inserted voucher id.
	 */
	@RolesAllowed("ADMIN")
	@POST
	@Path("/addVoucher")
	@Produces("application/json")
	public JSONObject addVoucher(@Valid JSONObject voucher) {
		String voc_code = voucher.get("VOC_CODE").toString();
		String voc_percentage = voucher.get("VOC_DISC_PERCENTAGE").toString();
		String voc_status = voucher.get("VOC_STATUS").toString();
		return voucherService.addVoucher(voc_code, Integer.parseInt(voc_percentage), voc_status);
	}		
	
}
