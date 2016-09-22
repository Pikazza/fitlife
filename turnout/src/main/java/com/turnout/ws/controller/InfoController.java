package com.turnout.ws.controller;



import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.domain.Info;
import com.turnout.ws.exception.CommentsNotEmptyException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.service.InfoService;

/**
*
* InfoController class is front face for accessing and manipulating notifications(infos) related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
*/
@RestController
@Path("/")
public class InfoController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InfoController.class);
	private final InfoService infoService;
	
	/**
	 * An injectable constructor with a dependency as argument.
	 * 
	 * @param infoService An Object of infoService as an injectable member.
	 * @see infoService
	 */
	@Autowired
	public InfoController(final InfoService infoService) {
		this.infoService = infoService;
	}


	/**
	 * This method used to get notification info from database based on the type.
	 * 
	 * @param type It is used to specify info type.
	 * @return Returns JSONObject contains value of info.
	 */

	@RolesAllowed({"ADMIN","USER"})
	@GET
	@Path("/infoDetail/type/{type}")
	@Produces("application/json")
	public JSONObject getListInfo(@PathParam("type") String type)
	{
		return infoService.getListInfo(type);		
	}
	/**
	 * This method used to get all info from the database.
	 * 
	 * @return Returns JSONArray contains all the info.
	 */
	@RolesAllowed("ADMIN")
	@GET
	@Path("/listinfo")
	@Produces("application/json")
	public JSONArray getAllListInfo()
	{
		return infoService.getAllListInfo();		
	}
	
	/**
	 * This method used to save a info details into database. It will return info id of just inserted or updated.
	 * 
	 * @param info A JSONOject contains information of info.
	 * @return Returns JSONObject that has a value of just inserted info id.
	 */
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/saveInfo")
	@Produces("application/json")
	public JSONObject saveInfo(@Valid JSONObject info)
	{
		int id      = Integer.parseInt(info.get("ID").toString());
		String desc = info.get("DESCRIPTION").toString();
		String type = info.get("TYPE").toString();
		String title = info.get("TITLE").toString();
		
		return infoService.saveInfo(id,desc,type,title);
	}	
	
}
