package com.turnout.ws.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.springframework.stereotype.Component;

import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.service.PartyService;
import com.turnout.ws.service.StudioActivityService;
import com.turnout.ws.service.StudioPartyActivityService;
import com.turnout.ws.service.VoucherService;

/**
*
* StudioPartyActivityController class is front face for accessing and manipulating resources in combination with studios activity and party. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* All operations related to person and activities(events and challenges) are implemented here like adding party into interested event list and accepting challenges and list of badges a party has won. . 
*/
@Component
@Path("/")
public class StudioPartyActivityController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudioPartyActivityController.class);
	private final StudioPartyActivityService studiosPartyActivityService;
	private final PartyService partyService;
	private final StudioActivityService studioActivityService;
	/**
	 * /**
	 * An injectable constructor with three dependencies as argument.
	 *  
	 * @param partyService An Object of PartyService as an injectable member
	 * @param studioActivityService An Object of StudioActivityService as an injectable member
	 * @param studiosPartyActivityService An Object of StudioPartyActivityService as an injectable member
	 */
	@Autowired
	public StudioPartyActivityController(final PartyService partyService,final StudioActivityService studioActivityService,final StudioPartyActivityService studiosPartyActivityService)
	{
		this.studiosPartyActivityService = studiosPartyActivityService;
		this.partyService = partyService;
		this.studioActivityService = studioActivityService;
	}
	
	/**
	 * This method allows to show a party who are interested in given event.
	 * So he can get latest notification about that event.
	 * 
	 * @param createreq A JSONObject contains value of party, studio id and it's event.
	 * @return Returns success message with inserted row id.
	 * @throws PartyNotFoundException It throws when entered party id is not found in the database.
	 */
	@RolesAllowed("USER")
	@POST
	@Path("/addInterestedPartyToEvent")
	@Produces("application/json")
	public JSONObject addInterestedPartyToEvent(@Valid JSONObject createreq) throws PartyNotFoundException
	{		
		int stdid       = Integer.parseInt(createreq.get("STA_STD_ID").toString());
		int stdactid    = Integer.parseInt(createreq.get("STA_ID").toString());
		int ptyid       = Integer.parseInt(createreq.get("PTY_ID").toString());
		String ptystatus = createreq.get("PTY_STA_STATUS").toString();
		if(ptyid == 0 || stdactid==0 || ptyid==0 || ptystatus==null ||  ptystatus=="" ){
			throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
		} else if(partyService.isParty(ptyid) == false){
			throw new PartyNotFoundException("Party id "+ptyid+" is not active user ");
		}	else if(studioActivityService.isStudiosActivityExist(stdid, stdactid) == false){
			throw new IllegalArgumentException("Studio and Studio Activity not matched" );
		}
		return studiosPartyActivityService.addInterestedPartyToEvent(stdid,stdactid,ptyid,ptystatus);
	
	}
	
	/**
	 * Beacons will call this method when party check in any studio to register their entry.
	 * 
	 * @param checkinreq A JSONObject contains value of party id, beacon id and it's time.
	 * @return Returns success message of insertion.
	 * @throws ParseException It throws when unexpected error happened while parsing text.
	 */
	@RolesAllowed("USER")
	@POST
	@Path("/partyCheckin")
	@Produces("application/json")
	public JSONObject userCheckin(@Valid JSONObject checkinreq) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String bconId = checkinreq.get("BCON_ID").toString();
		double bconrange = Double.parseDouble(checkinreq.get("BCON_RANGE").toString());
		int ptyId = Integer.parseInt(checkinreq.get("PTY_ID").toString());
		LOGGER.debug("Checkin::"+checkinreq.get("CHECKIN_TIME").toString());
		Date checkintime = dateFormat.parse(checkinreq.get("CHECKIN_TIME").toString());
		
		return studiosPartyActivityService.userCheckin(bconId,bconrange,ptyId,checkintime);
	}
	
	/**
	 * Beacons will call this method when party check out any studio to register their entry.
	 * 
	 * @param checkoutreq A JSONObject contains value of activity id and it's time.
	 * @return Returns success message of insertion.
	 * @throws ParseException It throws when unexpected error happened while parsing text.
	 */
	@RolesAllowed("USER")
	@POST
	@Path("/partyCheckout")
	@Produces("application/json")
	public JSONObject userCheckout(@Valid JSONObject checkoutreq) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		int ptystaId = Integer.parseInt(checkoutreq.get("PTY_STA_ID").toString());	
		LOGGER.debug("checkout:::::"+checkoutreq.get("CHECKOUT_TIME").toString());
		Date checkouttime = dateFormat.parse(checkoutreq.get("CHECKOUT_TIME").toString());
		
		return studiosPartyActivityService.userCheckout(ptystaId,checkouttime);
		
	}

	/**
	 * This method will return total points the party has earned so far. 
	 * 
	 * @param partyid An unique id of party.
	 * @return Returns total gained points of given party.
	 */
	@RolesAllowed("USER")
	@GET
	@Path("/mypoints/partyid/{partyid}")
	@Produces("application/json")
	public JSONObject myProfile(@PathParam("partyid")int partyid) 
	{
		return studiosPartyActivityService.myProfile(partyid);
	}
	
	/**
	 * All of accepted challenge will be listed here based on given party id.
	 * 
	 * @param partyid An unique id of party.
	 * @return Returns list of accepted challenge as a JSONArray.
	 */
	@RolesAllowed("USER")
	@GET
	@Path("/mychallenge/partyid/{partyid}")
	@Produces("application/json")
	public JSONArray myChallenge(@PathParam("partyid")int partyid) 
	{
		return studiosPartyActivityService.myChallenge(partyid);
	}
	
	/**
	 * It will get a list of badges a party has already won.
	 *  
	 * @param partyid An unique id of party.
	 * @return Returns list of badges a party has won.
	 */
	@RolesAllowed("USER")
	@GET
	@Path("/mybadges/partyid/{partyid}")
	@Produces("application/json")
	public JSONArray myBadges(@PathParam("partyid")int partyid) 
	{
		return studiosPartyActivityService.myBadges(partyid);
	}
	
	/**
	 * It will get one or more participants who are actively accepted or attended given challenge based on their name.
	 * 
	 * @param name A name of the participants to be searched in database.
	 * @param staid A challenge's id on which participants search will take place.
	 * @return
	 */
	@RolesAllowed("ADMIN")
	@GET
	@Path("/participants/name/{name}/staid/{staid}")
	@Produces("application/json")	
	public JSONArray participantsSearch(@PathParam("name") String name, @PathParam("staid") int staid) {
		
		if(name == "" || name == null) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}
		return studiosPartyActivityService.participantsSearch(name, staid);
	}

	/**
	 * This function used for selecting winners from list of participants who are all finished their challenge.
	 *  
	 * @param checkinreq It contains all the details of winner. So it can be updated into database.
	 * @return Returns Challenge's id on which new winner has been selected.
	 * @throws ParseException It throws when unexpected error happened while parsing text
	 * @throws PartyNotFoundException It throws when entered party id is not found in the database.
	 */
	@RolesAllowed("ADMIN")
	@POST
	@Path("/selectWinner")
	@Produces("application/json")
	public JSONObject selectWinner(@Valid JSONObject checkinreq) throws ParseException, PartyNotFoundException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		int ptyId             = Integer.parseInt(checkinreq.get("PTY_ID").toString());
		int staId             = Integer.parseInt(checkinreq.get("STA_ID").toString());
		int staStdId          = Integer.parseInt(checkinreq.get("STA_STD_ID").toString());
		Date checkintime      = dateFormat.parse(checkinreq.get("CHECKIN_TIME").toString());
		String gainedTaskBage = checkinreq.get("GAINED_TASK_BADGE").toString();
		
		if(ptyId == 0 || staStdId == 0 || staId ==0  ) {
			throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
		} else if(partyService.isParty(ptyId) == false) {
			throw new PartyNotFoundException("Party id "+ptyId+" is not active user ");
		}	else if(studioActivityService.isStudiosActivityExist(staStdId, staId) == false) {
			throw new IllegalArgumentException("Studio and Studio Activity not matched" );
		}
		return studiosPartyActivityService.selectWinner(ptyId,staId,staStdId,checkintime,gainedTaskBage);
	}
}
