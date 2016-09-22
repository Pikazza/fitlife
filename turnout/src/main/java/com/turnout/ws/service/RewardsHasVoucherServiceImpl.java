package com.turnout.ws.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.RewardsHasVoucher;
import com.turnout.ws.domain.Voucher;
import com.turnout.ws.exception.RewardNotFoundException;
import com.turnout.ws.exception.VoucherNotFoundException;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.RewardRepository;
import com.turnout.ws.repository.RewardsHasVoucherRepository;
import com.turnout.ws.repository.VoucherRepository;

/**
 * RewardsHasVoucherServiceImpl is class that contains collection of methods that can be accessed for manipulating reward and voucher mapping resource. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 * 
 * Attempting to add or edit an ineligible object throws an VoucherNotFoundException, typically RewardNotFoundException.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
@Validated
public class RewardsHasVoucherServiceImpl implements RewardsHasVoucherService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RewardsHasVoucherServiceImpl.class);

	private final RewardsHasVoucherRepository rewardsHasVoucherRepository;
	private final RewardRepository rewardRepository;
	private final VoucherRepository voucherRepository;
	
	/**
	 * An injectable constructor with two dependencies as argument.
	 * 
	 * @param rewardsHasVoucherRepository An Object of rewardsHasVoucherRepository as an injectable member.
	 * @param rewardRepository An Object of rewardRepository as an injectable member.
	 * @param voucherRepository An Object of voucherRepository as an injectable member.
	 * @see rewardsHasVoucherRepository
	 * @see rewardRepository
	 * @see voucherRepository
	 */
	@Autowired
	public RewardsHasVoucherServiceImpl(final RewardsHasVoucherRepository rewardsHasVoucherRepository,
			final RewardRepository rewardRepository,
			final VoucherRepository voucherRepository) {
		this.rewardsHasVoucherRepository = rewardsHasVoucherRepository;
		this.rewardRepository = rewardRepository;
		this.voucherRepository = voucherRepository;
	}
	/**
	 * This method used to add reward voucher. In this method voucher id mapped with reward id
	 * 
	 * @param rwdid an unique id of reward element.
	 * @param vouid an unique id of voucher element.
	 * @return returns JSONObject that has a value of success message.
	 * @throws RewardNotFoundException Exception thrown when entered reward is not found in database.
	 * @throws VoucherNotFoundException Exception thrown when entered voucher is not found in database.
	 */
	@Override
	public JSONObject addRewardVoucher(int rwdid, int vouid) throws RewardNotFoundException, VoucherNotFoundException {
		JSONObject jsonresult=new JSONObject();
		Reward rwd = rewardRepository.findOne(rwdid);		
		if(rwd == null)
		{
			throw new RewardNotFoundException("Reward not Exist");
		}
		Voucher vou = voucherRepository.findByVocId(vouid);
		if(vou == null)
		{
			throw new VoucherNotFoundException("Voucher Not Exist");
		}		
		RewardsHasVoucher chkrv = rewardsHasVoucherRepository.findByVocVocId(vouid);
		if(chkrv != null)
		{
			jsonresult.put("RESULT", "ALREADY VOUCHER MAPPED");
		}
		else
		{
			RewardsHasVoucher rv = new RewardsHasVoucher();
			rv.setRwdRwdId(rwdid);
			rv.setVocVocId(vouid);
			rewardsHasVoucherRepository.saveAndFlush(rv);
			jsonresult.put("RESULT", TurnOutConstant.SUCCESS);
		}	
		
		return jsonresult;
	}
	
	
}
