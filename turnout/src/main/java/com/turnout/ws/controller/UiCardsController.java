package com.turnout.ws.controller;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.service.UiCardsService;

/**
*
* UiCardsController class is front face for accessing and manipulating the cards that is displaying on Fiternity application. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* All logic related to stream page is associated with this class.
* 
*/
@RestController
@Path("/")
public class UiCardsController {
	private static final Logger LOGGER=LoggerFactory.getLogger(UiCardsController.class);

	private final UiCardsService uiCardsService;
	
	/**
	 * An injectable constructor with a dependency of UiCardsService as argument.
	 * 
	 * @param uiCardsService An Object of UiCardsService as an injectable member.
	 * @see UiCardsService
	 */
	@Autowired
	public UiCardsController(final UiCardsService uiCardsService) {
		this.uiCardsService = uiCardsService;
	}
	
	/**
	 * This method used to get all the user and studio activities from the database. 
	 * 
	 * @param ptyid An unique id of party.
	 * @param pageno current page number.
	 * @param pagesize Total number of records shown in current page.
	 * @return Returns JSONObject contains all the records.
	 */
	
	@RolesAllowed("USER")
	@GET
	@Path("/stream/partyid/{ptyid}/{pageno}/{pagesize}")
	@Produces("application/json")
	public JSONObject getStream(@PathParam("ptyid") int ptyid,@PathParam("pageno") int pageno,@PathParam("pagesize") int pagesize)
	{		
		return uiCardsService.getStream(ptyid,pageno,pagesize);
		
	}
}
