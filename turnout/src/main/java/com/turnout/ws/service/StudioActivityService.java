package com.turnout.ws.service;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * StudioActivityService is an interface that contains collection of methods that can be accessed for manipulating activities. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in StudioActivityServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface StudioActivityService {	

	/**
	 * This method used to get all the active events from the database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize  total number of records shown in current page.
	 * @return returns JSONArray contains all the events details.
	 */
	public JSONArray getListedActivity(int pageno, int pagesize);
	
	/**
	 * This method used to get event details from database based on the activity id passing.
	 * 
	 * @param eventid an unique id of studio activity element.
	 * @param partyid an unique id party element.
	 * @return JSONObject it will contains specific event details.
	 */
	public JSONObject getActivityDetails(int eventid, int partyid);
	
	/**
	 * Used to check whether the studio based activity exist or not.
	 * If already data exist it will return true otherwise return false.
	 * 
	 * @param stdid an unique id of studio id.
	 * @param stdactid an unique id of studio activity id.
	 * @return it will return boolean value true or false.
	 */
	public boolean isStudiosActivityExist(int stdid , int stdactid);
/*	public JSONObject addStudioActivity(String staname, int stdid, String stadesc, String staShotDesc, Date stastartdate, Date staenddate,
			Date stacredate, Date stamoddate, int staupdby, int stagoalpoints, String stabatchid, int statypeid,
			String staimage, String stainfluen, int stalikes, double staprice, String staaddr1, String staaddr2, String staaddr3, String statown,
			String stacounty, String stacountry, String stapostcode, float stalet, float stalong, Date stastarttime, Date staendtime,
			String staextlink, String stalocation, Date staExpiryDate, String stastatus,int staNoDays);
	
	public JSONObject updateStudioActivity(int staid, String staname, int stdid, String stadesc, String staShotDesc, Date stastartdate, Date staenddate,
			Date stacredate, Date stamoddate, int staupdby, int stagoalpoints, String stabatchid, int statypeid,
			String staimage, String stainfluen, int stalikes, double staprice, String staaddr1, String staaddr2, String staaddr3, String statown,
			String stacounty, String stacountry, String stapostcode, float stalet, float stalong, Date stastarttime, Date staendtime, String staextlink, 
			String stalocation, Date staExpiryDate, String stastatus,int staNoDays);*/
	/**
	 * 
	 * @param staid
	 * @param staname
	 * @param stdid
	 * @param stadesc
	 * @param staShotDesc
	 * @param stastartdate
	 * @param staenddate
	 * @param stacredate
	 * @param stamoddate
	 * @param staupdby
	 * @param stagoalpoints
	 * @param stabatchid
	 * @param statypeid
	 * @param staimage
	 * @param stainfluen
	 * @param stalikes
	 * @param staprice
	 * @param staaddr1
	 * @param staaddr2
	 * @param staaddr3
	 * @param statown
	 * @param stacounty
	 * @param stacountry
	 * @param stapostcode
	 * @param stalet
	 * @param stalong
	 * @param stastarttime
	 * @param staendtime
	 * @param staextlink
	 * @param stalocation
	 * @param staExpiryDate
	 * @param stastatus
	 * @param staNoDays
	 * @return
	 */
	public JSONObject saveStudioActivity(int staid, String staname, int stdid, String stadesc, String staShotDesc, Date stastartdate, Date staenddate,
			Date stacredate, Date stamoddate, int staupdby, int stagoalpoints, String stabatchid, int statypeid,
			String staimage, String stainfluen, int stalikes, double staprice, String staaddr1, String staaddr2, String staaddr3, String statown,
			String stacounty, String stacountry, String stapostcode, float stalet, float stalong, Date stastarttime, Date staendtime, String staextlink, 
			String stalocation, Date staExpiryDate, String stastatus,int staNoDays);
	
	/**
	 * Used to check whether activity exist or not.If exist return true otherwise return false.
	 * @param activityid the primary key of activity id.
	 * @return return boolean true or false.
	 */
	public boolean isStudiosActivityExist(int activityid);
	
	public boolean updateLikeCount(int staId, int staLikeCnt);
	
	/**
	 * This method used to get challenge details from database based on the activity id passing.
	 * 
	 * @param pageno current page number
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONArray contains all the challenge details.
	 */
	public JSONArray getAllChallenges(int pageno, int pagesize);
	
	/**
	 * In this method we will get the challenge details from the database based on activity id passing.
	 * 
	 * @param activityid an unique id of studio activity
	 * @param partyid an unique id of party.
	 * @return return JSONObject contains challenge details.
	 */
	public JSONObject getChallengeWall(int activityid, int partyid);
	
	/**
	 * This method used to get all the activity from database and helps to search the activities based on passed string.
	 * 
	 * @param name name of the activity.
	 * @param status status of the activity.
	 * @param type activity type(event / challenge).
	 * @param stdid an unique id of studio.
	 * @return returns JSONArray contains all the studio activity details.
	 */
	public JSONArray activitySearch(String name, String status, int type, int stdid);
	
	/**
	 * In this method we will get the activity detail from the database based on activity id passing.
	 * 
	 * @param eventid an unique id of studio activity.
	 * @return return JSONObject contains challenge details.
	 */
	public JSONObject activityDetails(int eventid);
}
