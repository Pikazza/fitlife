package com.turnout.ws.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * BeaconMasterService is an interface that contains collection of methods that can be accessed for manipulating beacons. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in BeaconMasterServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface BeaconMasterService {
	
	/**
	 * This method get all the beacons based on the search string.
	 * 
	 * @param search string contains search text.
	 * @return Returns JSONArray contains all the results after searching is done.
	 */
	
	public JSONArray beaconsList(String search);
	
	/**
	 * This method will get beacon details from database based on studio id and beacon id.
	 * @param bconId A primary key of beacon element.
	 * @param stdId A primary key of studio element
	 * @return Returns JSONObject which contains whole details of beacon element.
	 */
	
	public JSONObject beaconInfo(String bconId, int stdId);
	
	/**
	 * This method used to save a beacon details into database.It will return a beacon id (primary key) of just inserted or updated beacon.
	 * 
	 * @param bconId This string holds beacon unique website address.
	 * @param staType This integer holds studio activity type (event / challenge).
	 * @param stdId This integer holds the unique studio id.
	 * @param detectType This string holds beacon type (Own / Fiternity). 
	 * @param bconStatus This string holds beacon status (Active / Inactive).
	 * @return it will return just inserted beacon id.
	 */
	
	public JSONObject saveBeacon(String bconId,String bconSite, int staType, int stdId, String detectType, String bconStatus);
}