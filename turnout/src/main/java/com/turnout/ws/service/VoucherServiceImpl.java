package com.turnout.ws.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.Voucher;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.VoucherRepository;

/**
 * VoucherServiceImpl is class that contains collection of methods that can be accessed for manipulating voucher codes. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
@Validated
public class VoucherServiceImpl implements VoucherService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherServiceImpl.class);

	private final VoucherRepository voucherRepository;
	
	/**
	 * An injectable constructor with a dependency of voucherRepository as argument.
	 * 
	 * @param voucherRepository An Object of beaconMasterRepository as an injectable member.
	 * @see voucherRepository.
	 */
	@Autowired
	public VoucherServiceImpl(final VoucherRepository voucherRepository) {
		this.voucherRepository = voucherRepository;
	}
	/**
	 * This method used to save voucher code into database. It will return inserted or updated voucher id.
	 * 
	 * @param voc_code  voucher code.
	 * @param voc_percentage discount percentage for that particular voucher code.
	 * @param voc_status curent status about the voucher code.
	 * @return return JSONObject with inserted voucher id.
	 */
	@Transactional
	public JSONObject addVoucher(String voc_code, int voc_percentage, String voc_status) {
		JSONObject jsonResult = new JSONObject();
		if(!(voc_code == null ||  voc_status == null )) {
			Voucher voc = new Voucher();
			voc.setVocCode(voc_code);
			voc.setVocDiscPercentage(voc_percentage);
			voc.setVocStatus(voc_status);
			voucherRepository.saveAndFlush(voc);
			jsonResult.put("RESULT", TurnOutConstant.SUCCESS);
			jsonResult.put("VOC_ID", voc.getVocId());
		} else {
			jsonResult.put("RESULT", TurnOutConstant.FAILED);
		}
		return jsonResult;
	}	
}