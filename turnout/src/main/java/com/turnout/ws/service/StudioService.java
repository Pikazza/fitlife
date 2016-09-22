package com.turnout.ws.service;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * StudioService is an interface that contains collection of methods that can be accessed for manipulating studios. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in StudioServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to add or edit an ineligible object throws an UnirestException.
 * 
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface StudioService {

	/**
	 * It will get all the studios from database based on the given status.
	 * 
	 * @param status a string may have the value of active or inactive.
	 * @return It will return all active studios list. 
	 */
	public JSONArray getStudiosList(String status);

	/**
	 * Returns client response that holds details of studio based on passed studio id.
	 * 
	 * @param stdid the primary key of studio.
	 * @return it will return details of studio.
	 * @throws UnirestException a runtime exception happens while accessing studio's instagram account.
	 */
	public JSONObject getStudioProfile(int stdid) throws UnirestException;

	/*public JSONObject createStudio(String stdName, String stdTradeName, String stdAddr1, String stdAddr2, String stdAddr3,
			String stdTown, String stdCounty, String stdCountry, String stdPostcode, String stdBAddr1, String stdBAddr2,
			String stdBAddr3, String stdBTown, String stdBCounty, String stdBCountry, String stdBPostcode, 
			String stdCompHouseNo, String stdVatno, String stdCompLogo, String stdImgurl, float f, float g, 
			String stdsiteurl, String creby, Date date, String modby, Date date2, String desc, String phone,
			String mail,String status,String type,String pamAuthId, String pamAuthToken,int points,int timeSpent);
*/
	/**
	 * Used to check whether studio exist or not. It will return true if studio exist otherwise return false.
	 * 
	 * @param stdid the primary key od studio.
	 * @return boolean true or false.
	 */
	public boolean isStudioExist(int stdid);

/*	public JSONObject updateStudio(int stdId, String stdName, String stdTradeName, String stdAddr1, String stdAddr2, String stdAddr3,
			String stdTown, String stdCounty, String stdCountry, String stdPostcode, String stdBAddr1, String stdBAddr2,
			String stdBAddr3, String stdBTown, String stdBCounty, String stdBCountry, String stdBPostcode,
			String stdCompHouseNo, String stdVatno, String stdCompLogo, String stdImgurl, float f, float g, 
			String stdsiteurl, String creby, Date date, String modby, Date date2, String desc, String phone,
			String mail,String status,String type,String pamAuthId, String pamAuthToken,int points,int timeSpent);
*/
	/**
	 * This method used to add new studio or update existing studio into database.
	 * It will return a studio id (primary key) of just inserted or updated studio.
	 * 
	 * @param stdId the primary key of studio.
	 * @param stdName name of studio.
	 * @param stdTradeName  trade name of studio.
	 * @param stdAddr1 studio address line 1.
	 * @param stdAddr2 studio address line 2
	 * @param stdAddr3 studio address line 3
	 * @param stdTown studio town.
	 * @param stdCounty studio county.
	 * @param stdCountry studio country.
	 * @param stdPostcode studio post code.
	 * @param stdBAddr1 studio business address line 1.
	 * @param stdBAddr2 studio business address line 2.
	 * @param stdBAddr3 studio business address line 3.
	 * @param stdBTown studio business town.
	 * @param stdBCounty studio business county.
	 * @param stdBCountry studio business country.
	 * @param stdBPostcode studio post code.
	 * @param stdCompHouseNo studio company house number
	 * @param stdVatno studio VAT number
	 * @param stdCompLogo studios logo
	 * @param stdImgurl studios image location
	 * @param f studios latitude
	 * @param g studios longitude
	 * @param stdsiteurl studio site url
	 * @param creby created party
	 * @param date created date
	 * @param modby modified party
	 * @param date2 modified date
	 * @param desc description about studio
	 * @param phone studios phone number
	 * @param mail studios mail id
	 * @param status status of a studio
	 * @param type studio type
	 * @param pamAuthId instagrams authentication id.
	 * @param pamAuthToken instagrams authetications token
	 * @param points number of pointer credited to user on each checkin 
	 * @param timeSpent minimum time to be spent in studio
	 * @param streamDesc descriptions appears on stream page.
	 * @return Returns JSONObject contains recently inserted record id.
	 */
	public JSONObject saveStudio(int stdId, String stdName, String stdTradeName, String stdAddr1, String stdAddr2, String stdAddr3,
			String stdTown, String stdCounty, String stdCountry, String stdPostcode, String stdBAddr1, String stdBAddr2,
			String stdBAddr3, String stdBTown, String stdBCounty, String stdBCountry, String stdBPostcode,
			String stdCompHouseNo, String stdVatno, String stdCompLogo, String stdImgurl, float f, float g, 
			String stdsiteurl, String creby, Date date, String modby, Date date2, String desc, String phone,
			String mail,String status,String type,String pamAuthId, String pamAuthToken,int points,int timeSpent,String streamDesc);
	
	/**
	 * It will search and get list of all studios from database when it found the matched string.
	 * 
	 * @param search search string.
	 * @return return JSONArray contains information about studios.
	 */
	public JSONArray doStudioSearch(String search);
}
