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

import com.turnout.ws.service.BeaconMasterService;

/**

 * BeaconMasterController class is front face for accessing and manipulating beacon related resources. Class and all of its method is mapped with URI path template to which the resource responds.
 * This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
 * 
 * It has its own listing,add,edit and get details method.All of it's logic are implemented in IMPLclass.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
@RestController
@Path("/")
public class BeaconMasterController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeaconMasterController.class);
	private final BeaconMasterService beaconMasterService;
	
	/**
	 * An injectable constructor with a dependency of beaconMasterService as argument.
	 * 
	 * @param beaconMasterService An Object of beaconMasterService as an injectable member.
	 * @see beaconMasterService
	 */
	@Autowired
	public BeaconMasterController(final BeaconMasterService beaconMasterService) {		
		this.beaconMasterService = beaconMasterService;
	}	
	/**
	 * This method used to get all the beacons from database and helps to search the beacons based on given string.
	 * The string may have the value of beacon id or studio name. 
	 * 
	 * @param search A string holds a text that has to be searched.
	 * @return Returns JSONArray contains all the results after searching is done.
	 */
	
	@RolesAllowed("ADMIN")
	@GET
	@Path("/beacons/search/{search}")
	@Produces("application/json")
	public JSONArray beaconsList(@PathParam("search") String search)
	{
		return beaconMasterService.beaconsList(search);		
	}
	/**
	 * This method will get beacon details from database based on studio id and beacon id.
	 * 
	 * @param bconId A unique id of beacon.
	 * @param stdId A unique id of studio.
	 * @return Returns JSONObject which contains whole details of beacon element.
	 */
	
	@RolesAllowed("ADMIN")
	@GET
	@Path("/beaconDetail/bconid/{bconId}/stdid/{stdId}")
	@Produces("application/json")
	public JSONObject getBeaconDetails(@PathParam("bconId") String bconId,
			@PathParam("stdId") int stdId) {		
		return beaconMasterService.beaconInfo(bconId, stdId);		
	}
	
	/**
	 * This method used to save a beacon details into database. It will return a beacon id (primary key) of just inserted or updated beacon.
	 * 
	 * @param beacon A JSONObject contains whole information of beacon.
	 * @return Returns JSONObject that has a value of inserted beacon's id.
	 */
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/saveBeacon")
	@Produces("application/json")
	public JSONObject saveBeacon(@Valid JSONObject beacon) 
	{
		String bconId     = beacon.get("BCON_ID").toString();
		String bconSite   = beacon.get("BCON_SITE").toString();
		int staType       = Integer.parseInt(beacon.get("BCON_STA_TYPE_ID").toString());
		int stdId         = Integer.parseInt(beacon.get("BCON_STD_ID").toString());
		String detectType = beacon.get("BCON_DETECT_TYPE").toString().toUpperCase();
		//String type       = beacon.get("TYPE").toString().toUpperCase();
		String bconStatus = beacon.get("BCON_STATUS").toString().toUpperCase();
		return beaconMasterService.saveBeacon(bconId,bconSite,staType,stdId,detectType,bconStatus);
	}	
	
}
