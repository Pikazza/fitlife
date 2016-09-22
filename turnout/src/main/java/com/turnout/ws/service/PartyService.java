package com.turnout.ws.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;

/**
 * PartyService is an interface that contains collection of methods that can be accessed for manipulating parties. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in PartyServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to add or edit an ineligible object throws an CurrentPasswordNotMatchedException, typically PartyNotFoundException or PartyExistException.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface PartyService {
	/**
	 * Used to check whether the party exist or not. If exist return true else return false.
	 * 
	 * @param partyid the primary key of party element.
	 * @return it will return true or false message.
	 */
	public boolean isParty(int partyid);
	
	/**
	 * This method used to get party details from database based on party id.
	 * 
	 * @param partyid the primary key of party element.
	 * @return returns JSONObject contains party details.
	 * @throws PartyNotFoundException  Exception thrown when entered party is not found.
	 */
	public JSONObject retrievePartyDetailsById(int partyid) throws PartyNotFoundException;
	
	/**
	 * 
	 * @param ptyId the primary key of party element.
	 * @param ptyemail party email address.
	 * @param ptyPhoto party profile photo.
	 * @param ptyDesc
	 * @param ptyShowupPreference
	 * @param ptyActivityPreference
	 * @param login login id.
	 * @param newPwd new password.
	 * @param curPwd current password.
	 * @param ptyname party firstname.
	 * @param ptyLastname party lastname.
	 * @return it will return just updated or inserted party id.
	 * @throws PartyNotFoundException Exception thrown when entered party is not found.
	 * @throws CurrentPasswordNotMatchedException Exception thrown when current pass word not match with user's entered password.
	 * @throws PartyExistException Exception thrown when entered party is already exist in database.
	 */
	public JSONObject updatePartyDetailsById(int ptyId,  String ptyemail,String ptyPhoto, String ptyDesc, String ptyShowupPreference, String ptyActivityPreference,String login, String newPwd,
			String curPwd, String ptyname, String ptyLastname) throws PartyNotFoundException, CurrentPasswordNotMatchedException, PartyExistException;
	
	/**
	 * This method used to get party profile,interested event, accepted challenge and friends lists details from database based on party id passing.
	 * 
	 * @param partyid an integer value holds party id.
	 * @return returns JSONObject contains party details.
	 * @throws UnirestException Exception thrown when the http connection fails.
	 */
	public JSONObject getProfileById(int partyid) throws UnirestException;
	
	/**
	 *  This function used to change party status.
	 *  
	 * @param ptyId the primary key of party element.
	 * @param ptyStatus party status (Active / Inactive).
	 * @return it will return updated party id.
	 * @throws PartyNotFoundException
	 */
	public JSONObject changeUserStatus(int ptyId,  String ptyStatus) throws PartyNotFoundException;
	
	/**
	 * This method used to get all the party from database and helps to search the party based on passed string.
	 * The string may have the value of first name,last name,email,mobile no.
	 * 
	 * @param search a string holds a text that has to be searched.
	 * @return returns JSONArray contains all the results after searching is done.
	 */
	
	public JSONArray partySearch(String search);

}
