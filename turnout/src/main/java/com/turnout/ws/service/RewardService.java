package com.turnout.ws.service;

import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.turnout.ws.exception.NoSufficientGainedPointsException;

/**
 * RewardService is an interface that contains collection of methods that can be accessed for manipulating rewards. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in RewardServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to add or edit an ineligible object throws an NoSufficientGainedPointsException, typically ParseException.
 * 
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface RewardService {

	/**
	 * This method used to validate the voucher code.
	 * 
	 * @param ptyid an unique id of party id.
	 * @param rwdid an unique id of reward id.
	 * @return returns JSONObject that has a value of success message.
	 * @throws NoSufficientGainedPointsException Exception thrown when there is no sufficient gained point while redeeming reward.
	 */
	public JSONObject reedemReward(int ptyid, int rwdid) throws NoSufficientGainedPointsException;
	
	/**
	 * This method used to get all reward listing from database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONArray contains all the reward listing.
	 */
	
	public JSONArray getRewardListing(int pageno, int pagesize);
	
	/**
	 * This method used to get reward detail from database based on reward id passed.
	 * 
	 * @param rewardid the primary key of reward element.
	 * @return returns JSONObject which contains whole details of reward element.
	 */
	public JSONObject getRewardDetail(int rewardid);

	/**
	 * It is used to check whether reward already exist in database or not. It will return true or false message.
	 * 
	 * @param rewardid an unique id of reward element.
	 * @return return boolean true or false.
	 */
	public boolean isRewardExist(int rewardid);

/*	public JSONObject addReward(String rwdName, String rwdDesc, String rwdImg, int rwdPoints, String rwdExpDate,
			int rwdStdid, String rwdStatus) throws ParseException;

	public JSONObject updateReward(int rwdId,String rwdName, String rwdDesc, String rwdImg, int rwdPoints, String rwdExpDate,
			int rwdStdid, String rwdStatus) throws ParseException;*/
	/**
	 * 
	 * @param rwdId the primary key of reward.
	 * @param rwdName name of the reward.
	 * @param rwdDesc reward description.
	 * @param rwdImg reward image.
	 * @param rwdPoints reward points.
	 * @param rwdExpDate reward expiry date.
	 * @param rwdStdid an unique id of studio.
	 * @param rwdStatus reward status.
	 * @return returns JSONObject that has a value of reward id.
	 * @throws ParseException Exception thrown when we try to parsing.
	 */
	public JSONObject saveReward(int rwdId,String rwdName, String rwdDesc, String rwdImg, int rwdPoints, String rwdExpDate,
			int rwdStdid, String rwdStatus) throws ParseException;	
	
	/**
	 * This method used to get all the rewards from database and helps to search the rewards based on reward name, studio id, type of reward.
	 * 
	 * @param rwdname name of the reward.
	 * @param stdid an unique id of the studio.
	 * @param type type of reward current / expired reward.
	 * @return returns JSONArray contains all the results after searching is done.
	 */
	public JSONArray rewardSearch(String rwdname, int stdid, String type);
}
