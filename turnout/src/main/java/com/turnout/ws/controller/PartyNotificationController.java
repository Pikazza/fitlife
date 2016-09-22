package com.turnout.ws.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.service.InfoService;
import com.turnout.ws.service.PartyNotificationService;
import com.turnout.ws.service.PartyService;

/**
*
* PartyNotificationController class is front face for accessing and manipulating parties notification related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* Basic CRUD operation is done like update, retrieve and add.
*/
@RestController
@Path("/")
public class PartyNotificationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyNotificationController.class);
	
	private final PartyNotificationService partyNotificationService;
	private final PartyService partyService;
	private final InfoService infoService;
	
	/**
	 * An injectable constructor with dependencies as argument.
	 * 
	 * @param partyNotificationService An Object of partyNotificationService as an injectable member.
	 * @param partyService An Object of partyService as an injectable member.
	 * @param infoService An Object of infoService as an injectable member.
	 * @see partyNotificationService
	 * @see partyService
	 * @see infoService
	 */
	@Autowired
	public PartyNotificationController(final PartyNotificationService partyNotificationService, 
			final PartyService partyService,final InfoService infoService) {
		this.partyNotificationService = partyNotificationService;
		this.partyService = partyService;
		this.infoService = infoService;
	}
	
	/**
	 * This method used to save a party notification details into database.It will return a notification id of just updated notification.
	 * 
	 * @param postVal A JSONOject contains information of party notification.
	 * @return Returns JSONObject that has a value of notification id.
	 * @throws Exception Exception thrown when the required input parameters are empty.
	 */
	
	@RolesAllowed("USER")
	@POST
	@Path("/addPartyNotification")
	@Produces("application/json")
	public JSONObject addPartyNotification(@Valid JSONObject postVal) throws Exception
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	
		
		int ptyId            = Integer.parseInt(postVal.get("NOTIFY_PTY_ID").toString());
		String othLikes      = postVal.get("NOTIFY_OTH_LIKES").toString();
		String othCmts       = postVal.get("NOTIFY_OTH_CMTS").toString();
		String othIntrstEvnt = postVal.get("NOTIFY_OTH_INTRST_EVNT").toString();	
		String OthAcptChlng  = postVal.get("NOTIFY_OTH_ACPT_CHLNG").toString();
		String pointsCrdt    = postVal.get("NOTIFY_PERSONAL_POINTS_CRDT").toString();
		String EvntRemainder = postVal.get("NOTIFY_PERSONAL_EVNT_REMAINDER").toString();
		String readyReedem   = postVal.get("NOTIFY_PERSONAL_READY_REEDEM").toString();
		Date modifyDate    = dateFormat.parse(postVal.get("NOTIFY_MODIFY_DATE").toString());
		
		if(ptyId == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!partyService.isParty(ptyId)) {
			  JSONObject msgObj = infoService.getListInfo("party-not-found");
			  throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~AddPartyNotification");
			//throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~AddPartyNotification");		
		}		
		return partyNotificationService.addPartyNotification(ptyId, othLikes, othCmts, othIntrstEvnt, OthAcptChlng, pointsCrdt, EvntRemainder, readyReedem, modifyDate);
	}
	
	/**
	 * This method used to get party notification from database based on party id.
	 * 
	 * @param ptyId A integer value holds party id.
	 * @return Returns JSONObject contains party notification details.
	 * @throws Exception Exception thrown when the required input parameters are empty.
	 */
	
	@RolesAllowed("USER")
	@GET
	@Path("/getPartyNotification/partyid/{ptyid}")
	@Produces("application/json")
	public JSONObject getPartyNotification(@PathParam("ptyid") int ptyId) throws Exception
	{
		if(ptyId == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!partyService.isParty(ptyId)) {
			  JSONObject msgObj = infoService.getListInfo("party-not-found");
			  throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~GetPartyNotification");
			//throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~GetPartyNotification");		
		}		
		return partyNotificationService.getPartyNotification(ptyId);
	}	
}