package com.turnout.ws.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.service.LikesService;

/**
*
* LikesController class is front face for accessing and manipulating likes related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
*/
@RestController
@Path("/")
public class LikesController {

	private static final Logger Logger = LoggerFactory.getLogger(LikesController.class);
	private final LikesService likesService;
	/**
	 * An injectable constructor with a dependency of likesService as argument.
	 * 
	 * @param likesService An Object of likesService as an injectable member.
	 * @see likesService
	 */
	@Autowired
	public LikesController(final LikesService likesService) {
		this.likesService = likesService;
	}
	/**
	 * This method used to save like count of challenge post. It will return a like id of just inserted or updated likes.
	 * 
	 * @param likeReq A JSONObject contain information of likes.
	 * @return Returns JSONObject that has a value of like id.
	 */
	
	@RolesAllowed("USER")
	@POST
	@Path("/addLikes")
	@Produces("application/json")
	public JSONObject addLikes(@Valid JSONObject likeReq)
	{
		String Likeyn = likeReq.get("LIKE_YN").toString();
		String likeType = likeReq.get("LIKE_TYPE").toString();
		int likeTypeId = Integer.parseInt(likeReq.get("LIKE_TYPE_ID").toString());
		int likePtyId = Integer.parseInt(likeReq.get("LIKE_PTY_ID").toString());
		
		return likesService.addLikes(Likeyn,likeType,likeTypeId,likePtyId);
	}
}