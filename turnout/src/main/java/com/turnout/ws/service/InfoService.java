package com.turnout.ws.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * InfoService is an interface that contains collection of methods that can be accessed for manipulating notifications. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in InfoServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface InfoService {
	/**
	 * Return notification and error messge title and description from database based on message type pass.
	 * 
	 * @param type message type.
	 * @return return JSONObject contains value of messages.
	 */
	public JSONObject getListInfo(String type);
	
	/**
	 * Return all the error and notification from the database.
	 * 
	 * @return return jsonarray contains all the details.
	 */
	public JSONArray getAllListInfo();
	
	/**
	 * Used to save message details into database and it will return updated message id.
	 * 
	 * @param id the primary key of info.
	 * @param desc notification or error message description.
	 * @param type message type.
	 * @param title message title.
	 * @return return just updated message id.
	 */
	public JSONObject saveInfo(int id,String desc,String type,String title);	
}