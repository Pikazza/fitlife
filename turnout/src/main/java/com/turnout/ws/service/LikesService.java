package com.turnout.ws.service;

import org.json.simple.JSONObject;

/**
 * LikesService is an interface that contains collection of methods that can be accessed for manipulating likes related resource. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in LikesServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface LikesService {

	/**
	 * Used to add and remove likes for challenges post. It will return primary key like id.
	 * 
	 * @param likeyn it has a constant value "Y /N ".
	 * @param likeType type of the like "COMMENTS / STUDIOS_ACTIVITY".
	 * @param likeTypeId the primary key of studio activity type. 
	 * @param likePtyId the primary key of party.
	 * @return return JSONObject that has a value of like id.
	 */
	
	public JSONObject addLikes(String likeyn, String likeType, int likeTypeId, int likePtyId);

}
