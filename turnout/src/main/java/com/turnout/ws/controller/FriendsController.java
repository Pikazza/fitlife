package com.turnout.ws.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.service.FriendsService;

/**
*
* FriendsController class is front face for accessing, finding and manipulating friends related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
*/
@RestController
@Path("/")
public class FriendsController {
	private static final Logger LOGGER=LoggerFactory.getLogger(FriendsController.class);
	
	private final FriendsService friendsService;
	/**
	 * An injectable constructor with a dependency of friendsService as argument.
	 * 
	 * @param friendsService An Object of friendsService as an injectable member.
	 * @see friendsService
	 */
	@Autowired
	public FriendsController(final FriendsService friendsService) {
		// TODO Auto-generated constructor stub
		this.friendsService = friendsService;
	}
	/**
	 * This method used to save friend request data into database.
	 *  
	 * @param friendReq A JSONObject contains information about friend request.
	 * @return Returns JSONObject that has a value of success or error message.
	 */
	@RolesAllowed("USER")
	@POST
	@Path("/followUnfollow")
	@Produces("application/json")
	public JSONObject addfollowUnfollow(@Valid JSONObject friendReq)
	{
		int frndId1 = Integer.parseInt(friendReq.get("FRND_ID1").toString());
		int frndId2 = Integer.parseInt(friendReq.get("FRND_ID2").toString());
		String frndStatus = friendReq.get("FRND_STATUS").toString();		
		
		return friendsService.addfollower(frndId1,frndId2,frndStatus);		
		
	}
	/**
	 * This method will get all the friends information from database based on given party id.
	 * 
	 * @param ptyid A integer has a value of party id.
	 * @return Returns JSONObject contains all the friends that the party accepted.
	 */
	
	@RolesAllowed("USER")
	@GET
	@Path("/friends/partyid/{ptyid}")
	@Produces("application/json")
	public JSONObject getFriendsList(@PathParam("ptyid") int ptyid)
	{		
		return friendsService.getFriendsList(ptyid);		
	}
	
	/**
	 * This method used to get all the friends from database and helps to search the friends based on passed string.
	 * 
	 * @param userid A integer has a value of party id.
	 * @return Returns JSONArray contains all the friends that the party accepted. 
	 */
	
	@RolesAllowed("USER")
	@GET
	@Path("/findFriends/userid/{userid}")
	@Produces("application/json")
	public JSONArray findFriends(@PathParam("userid") int userid) {
		if(userid == 0) {
			throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
		}
		return friendsService.findFriends(userid);
	}

}
