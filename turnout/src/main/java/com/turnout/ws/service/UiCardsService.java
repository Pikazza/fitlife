package com.turnout.ws.service;

import org.json.simple.JSONObject;

/**
 * UiCardsService is an interface that contains collection of methods that can be accessed for manipulating ui cards. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in UiCardsServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface UiCardsService {
	/**
	 * Stream method used to bring the premium and normal studio activities and user activities details from the database.
	 *  
	 * @param ptyid the primary key of the party.
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONObject contains all the records.
	 */
	public JSONObject getStream(int ptyid, int pageno, int pagesize);
	

}
