package com.turnout.ws.service;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * StudioPartyActivityService is an interface that contains collection of methods that can be accessed for manipulating party and studio activity relations. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in StudioPartyActivityServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface StudioPartyActivityService {
	/**
	 * This method allows to show a party who are interested in given event.
	 * So he can get latest notification about that event.
	 * 
	 * @param stdid the primary key of studio.
	 * @param stdactid the primary key of studio activity id.
	 * @param ptyid the primary key of party.
	 * @param ptystatus It has constant text "ACCEPTED".
	 * @return returns success message with inserted row id.
	 */
	public	JSONObject addInterestedPartyToEvent(int stdid, int stdactid, int ptyid, String ptystatus);
	
	/**
	 * This method used to check whether the party already accepted or not.
	 * If already accepted, It will return true message otherwise return false.
	 * 	
	 * @param ptyid an unique id of party element.
	 * @param stdid an unique id of studio element.
	 * @param stdactid an unique id of studio activity id element.
	 * @param status it has constant text "ACCEPTED".
	 * @return return boolean true or false message.
	 */
	public boolean isInterested(int ptyid, int stdid, int stdactid,String status);
	
	/**
	 * Beacons will call this method when party check in any studio to register their entry.
	 * 
	 * @param bconId beacon id it has mapped with the studio.
	 * @param bconrange beacon detecting range.
	 * @param ptyId an unique id of party element.
	 * @param checkintime party entering time into studio.
	 * @return returns success message.
	 */
	public JSONObject userCheckin(String bconSite, double bconrange, int ptyId, Date checkintime);
	
	/**
	 * Beacons will call this method when party check out any studio to register their entry.
	 * 
	 * @param ptystaId an unique id of studio party activity element.
	 * @param checkouttime party exist time from the studio.
	 * @return returns success message.
	 */
	public JSONObject userCheckout(int ptystaId, Date checkouttime);
	
	/**
	 * This method used to bring activities and history from the database.
	 * 
	 * @param partyid the primary key of the id.
	 * @return it will return points history.
	 */
	public JSONObject myProfile(int partyid);
	
	/**
	 * All of accepted challenge will be listed here based on given party id. 
	 * @param partyid an unique id of party.
	 * @return returns list of accepted challenge as a JSONArray.
	 */
	public JSONArray myChallenge(int partyid);
	
	/**
	 * It will get a list of badges a party has already won.
	 * 
	 * @param partyid an unique id of party.
	 * @return returns list of badges a party has won.
	 */
	public JSONArray myBadges(int partyid);
	
	/**
	 * It will get one or more participants who are actively accepted or attended given challenge based on their name.
	 * 
	 * @param name the name of the participants.
	 * @param staid the primary key of studio activity id.
	 * @return return jsonarray contains all the participant information.
	 */
	public JSONArray participantsSearch(String name, int staid);
	
	/**
	 * This function used for selecting winners from list of participants who are all finished their challenge.
	 * 
	 * @param ptyId the primary key of party.
	 * @param staId an unique id of studio activity id.
	 * @param staStdId an unique id of studio.
	 * @param checkintime winner selected date and time.
	 * @param gainedTaskBage 
	 * @return returns challenge's id on which new winner has been selected.
	 */
	
	public JSONObject selectWinner(int ptyId,int staId,int staStdId,Date checkintime,String gainedTaskBage);

}