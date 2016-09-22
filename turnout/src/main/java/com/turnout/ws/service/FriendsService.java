package com.turnout.ws.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * FriendsService is an interface that contains collection of methods that can be accessed for manipulating friends related informations. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in FriendsServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface FriendsService {
	
	/**
	 * Its used for follow and unfollow the friend request.
	 * 
	 * @param  frndId1 Friend request send party id.
	 * @param  frndId2 Follower id.
	 * @param  frndStatus requesting status.
	 * @return Returns JSONObject that has a value of success or error message.
	 */
	
	public JSONObject addfollower(int frndId1, int frndId2, String frndStatus);
	
	/**
	 * Returns jsonobject contains all the friendslist.
	 * 
	 * @param  ptyid the primary key of party.
	 * @return  friendlist.
	 */

	public JSONObject getFriendsList(int ptyid);
	
	/**
	 * This method used to get all the friends from database and helps to search the friends based on passed string.
	 * 
	 * @param ptyid the primary key party.
	 * @return returns JSONArray contains all the friends. 
	 */
	
	public JSONArray findFriends(int ptyid);

}
