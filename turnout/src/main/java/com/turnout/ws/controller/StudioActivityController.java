package com.turnout.ws.controller;

import java.text.DateFormat;
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
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.service.StudioActivityService;
import com.turnout.ws.service.StudioService;


/**
*
* StudioActivityController class is front face for accessing and manipulating studios activity(challenges and events) related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* Basic CRUD operation is done like update, retrieve and add. 
*/
@RestController
@Path("/")
public class StudioActivityController {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudioActivityController.class);
	
	private final StudioActivityService studiosActivityService;
	private final StudioService studioService;
	
	/**
	 * An injectable constructor with two dependencies as argument.
	 * 
	 * @param studiosActivityService An Object of StudioActivityService as an injectable member.
	 * @param studioService An Object of StudioService as an injectable member.
	 * @see StudioActivityService
	 * @see StudioService
	 */
	@Autowired
	public StudioActivityController(final StudioActivityService studiosActivityService,final StudioService studioService)
	{
		this.studiosActivityService=studiosActivityService;
		this.studioService=studioService;
	}
	/**
	 * This method used to get all the active events from the database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize Total number of records shown in current page.
	 * @return Returns JSONArray contains all the events details.
	 */
	@RolesAllowed("USER")
	@GET
	@Path("/activity/events/{pageno}/{pagesize}")
	@Produces("application/json")
	public JSONArray getListedActivity(@PathParam("pageno") int pageno,@PathParam("pagesize") int pagesize)
	{		
		LOGGER.debug("testing:Reach Controller Class");
		return studiosActivityService.getListedActivity(pageno,pagesize);
		
	}
	/**
	 * This method used to get event details from database based on the activity id passing.
	 * 
	 * @param activityid An unique studio activity id.
	 * @param partyid An unique id of party.
	 * @return JSONObject It will contains specific event details. 
	 */
	@RolesAllowed("USER")
	@GET
	@Path("getActivityDetail/activityid/{activityid}/{partyid}")
	@Produces("application/json")
	public JSONObject getEventDetails(@PathParam("activityid") int activityid,@PathParam("partyid") int partyid)
	{
		if(activityid==0) {
			throw new IllegalArgumentException("activityid should not be empty");
 		} else if(!(studiosActivityService.isStudiosActivityExist(activityid))) {
 			throw new IllegalArgumentException("studio "+activityid+" is not available");
 		}
		return studiosActivityService.getActivityDetails(activityid,partyid);
	}
	
/*	@RolesAllowed("ADMIN")
	@POST
	@Path("/addStudioActivity")
	@Produces("application/json")
	public JSONObject addStudioActivity(@Valid JSONObject studio) throws ParseException
	{
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		
		
		String staname = studio.get("STA_NAME").toString();
		int stdid=Integer.parseInt(studio.get("STD_ID").toString());
		String stadesc = studio.get("STA_DESCRIPTION").toString();
		String staShotDesc = studio.get("STA_SHORT_DESCRIPTION").toString();
		
		Date stastartdate = dateformat.parse(studio.get("STA_START_DATE").toString());
		Date staenddate = dateformat.parse(studio.get("STA_END_DATE").toString());
		Date stacredate = dateformat.parse(studio.get("STA_CREATED_DATE").toString());
		Date stamoddate = dateformat.parse(studio.get("STA_MODIFIED_DATE").toString());
		int staupdby = Integer.parseInt(studio.get("STA_UPDATED_BY").toString());
		
		Date staExpiryDate = dateformat.parse(studio.get("STA_EXPIRY_DATE").toString());
		
		int stagoalpoints = Integer.parseInt(studio.get("STA_GOAL_POINTS").toString());
		String stabatchid = studio.get("STA_BADGE_ID").toString();
		int statypeid = Integer.parseInt(studio.get("STA_TYPE_ID").toString());
		
		String staimage = studio.get("STA_IMAGE_URL").toString();
		String stainfluen = studio.get("STA_INFLUENCER").toString();
		
		int stalikes = 0;
		double staprice = Double.parseDouble(studio.get("STA_PRICE").toString());
		
		String staaddr1 = studio.get("STA_ADDRESS_LINE1").toString();
		String staaddr2 = studio.get("STA_ADDRESS_LINE2").toString();
		String staaddr3 = studio.get("STA_ADDRESS_LINE3").toString();
		String statown = studio.get("STA_TOWN").toString();
		String stacounty = studio.get("STA_COUNTY").toString();
		String stacountry = studio.get("STA_COUNTRY").toString();
		String stapostcode = studio.get("STA_POST_CODE").toString();
		
		float stalet = Float.parseFloat(studio.get("STA_LATITUDE").toString());
		float stalong = Float.parseFloat(studio.get("STA_LONGITUDE").toString());		
		
		Date stastarttime = timeformat.parse(studio.get("STA_START_TIME").toString());
		Date staendtime = timeformat.parse(studio.get("STA_END_TIME").toString());
				
		String staextlink = studio.get("STA_EXTERNAL_LINK").toString();
		String stalocation = studio.get("STA_LOCATION").toString();
		
		String stastatus = studio.get("STA_STATUS").toString();
		
		int staNoDays = Integer.parseInt(studio.get("STA_NO_OF_DAYS").toString());
		
		if(stdid==0 || staname==null ||statypeid==0){
			throw new IllegalArgumentException("studio id,studio name and activity id  should not be empty");
 		}else if(!(studioService.isStudioExist(stdid))){
 			throw new IllegalArgumentException("studio "+stdid+" is not available");
 		}
		return studiosActivityService.addStudioActivity(staname,stdid,stadesc,staShotDesc,stastartdate,staenddate,stacredate,stamoddate,staupdby,
				stagoalpoints,stabatchid,statypeid,staimage,stainfluen,stalikes,staprice,staaddr1,staaddr2,staaddr3,statown,stacounty,stacountry,stapostcode,
				stalet,stalong,stastarttime,staendtime,staextlink,stalocation,staExpiryDate,stastatus,staNoDays);
	}
	
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/updateStudioActivity")
	@Produces("application/json")
	public JSONObject updateStudioActivity(@Valid JSONObject studio) throws ParseException {
		
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		
		int staid          = Integer.parseInt(studio.get("STA_ID").toString());
		String staname     = studio.get("STA_NAME").toString();
		int stdid          = Integer.parseInt(studio.get("STD_ID").toString());
		String stadesc     = studio.get("STA_DESCRIPTION").toString();
		String staShotDesc = studio.get("STA_SHORT_DESCRIPTION").toString();		
		Date stastartdate  = dateformat.parse(studio.get("STA_START_DATE").toString());
		Date staenddate    = dateformat.parse(studio.get("STA_END_DATE").toString());
		Date stacredate    = dateformat.parse(studio.get("STA_CREATED_DATE").toString());
		Date stamoddate    = dateformat.parse(studio.get("STA_MODIFIED_DATE").toString());
		int staupdby       = Integer.parseInt(studio.get("STA_UPDATED_BY").toString());
		Date staExpiryDate = dateformat.parse(studio.get("STA_EXPIRY_DATE").toString());
		int stagoalpoints  = Integer.parseInt(studio.get("STA_GOAL_POINTS").toString());
		String stabatchid  = studio.get("STA_BADGE_ID").toString();
		int statypeid      = Integer.parseInt(studio.get("STA_TYPE_ID").toString());
		String staimage    = studio.get("STA_IMAGE_URL").toString();
		String stainfluen  = studio.get("STA_INFLUENCER").toString();
		int stalikes = 0;
		double staprice    = Double.parseDouble(studio.get("STA_PRICE").toString());
		String staaddr1    = studio.get("STA_ADDRESS_LINE1").toString();
		String staaddr2    = studio.get("STA_ADDRESS_LINE2").toString();
		String staaddr3    = studio.get("STA_ADDRESS_LINE3").toString();
		String statown     = studio.get("STA_TOWN").toString();
		String stacounty   = studio.get("STA_COUNTY").toString();
		String stacountry  = studio.get("STA_COUNTRY").toString();
		String stapostcode = studio.get("STA_POST_CODE").toString();
		float stalet       = Float.parseFloat(studio.get("STA_LATITUDE").toString());
		float stalong      = Float.parseFloat(studio.get("STA_LONGITUDE").toString());
		Date stastarttime  = timeformat.parse(studio.get("STA_START_TIME").toString());
		Date staendtime    = timeformat.parse(studio.get("STA_END_TIME").toString());
		LOGGER.debug("end time is "+staendtime);
		
		String staextlink  = studio.get("STA_EXTERNAL_LINK").toString();
		String stalocation = studio.get("STA_LOCATION").toString();
		
		String stastatus = studio.get("STA_STATUS").toString();
		int staNoDays = Integer.parseInt(studio.get("STA_NO_OF_DAYS").toString());
		
		
		if(stdid == 0 || staname == null ||statypeid == 0){
			throw new IllegalArgumentException("studio id,studio name and activity id  should not be empty");
 		} else if(!(studioService.isStudioExist(stdid))){
 			throw new IllegalArgumentException("studio "+stdid+" is not available");
 		}
		return studiosActivityService.updateStudioActivity(staid,staname,stdid,stadesc,staShotDesc,stastartdate,staenddate,stacredate,stamoddate,staupdby,
				stagoalpoints,stabatchid,statypeid,staimage,stainfluen,stalikes,staprice,staaddr1,staaddr2,staaddr3,statown,stacounty,stacountry,stapostcode,
				stalet,stalong,stastarttime,staendtime,staextlink,stalocation,staExpiryDate,stastatus,staNoDays);
	}	*/
	
	/**
	 * This function used to save events and challenges into database. It will return just added or updated studio activity id.
	 *
	 * @param studio Its unique id of studio.
	 * @return Returns inserted or updated row id. 
	 * @throws ParseException It throws when unexpected error happened while parsing text.
	 */
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/saveStudioActivity")
	@Produces("application/json")
	public JSONObject saveStudioActivity(@Valid JSONObject studio) throws ParseException {
		
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		
		int staid          = Integer.parseInt(studio.get("STA_ID").toString());
		String staname     = studio.get("STA_NAME").toString();		
		int stdid          = Integer.parseInt(studio.get("STD_ID").toString());
		String stadesc     = studio.get("STA_DESCRIPTION").toString();
		String staShotDesc = studio.get("STA_SHORT_DESCRIPTION").toString();		
		Date stastartdate  = dateformat.parse(studio.get("STA_START_DATE").toString());
		Date staenddate    = dateformat.parse(studio.get("STA_END_DATE").toString());
		Date stacredate    = dateformat.parse(studio.get("STA_CREATED_DATE").toString());
		Date stamoddate    = dateformat.parse(studio.get("STA_MODIFIED_DATE").toString());
		int staupdby       = Integer.parseInt(studio.get("STA_UPDATED_BY").toString());
		Date staExpiryDate = dateformat.parse(studio.get("STA_EXPIRY_DATE").toString());
		int stagoalpoints  = Integer.parseInt(studio.get("STA_GOAL_POINTS").toString());
		String stabatchid  = studio.get("STA_BADGE_ID").toString();
		int statypeid      = Integer.parseInt(studio.get("STA_TYPE_ID").toString());
		String staimage    = studio.get("STA_IMAGE_URL").toString();
		String stainfluen  = studio.get("STA_INFLUENCER").toString();
		int stalikes = 0;
		double staprice    = Double.parseDouble(studio.get("STA_PRICE").toString());
		String staaddr1    = studio.get("STA_ADDRESS_LINE1").toString();
		String staaddr2    = studio.get("STA_ADDRESS_LINE2").toString();
		String staaddr3    = studio.get("STA_ADDRESS_LINE3").toString();
		String statown     = studio.get("STA_TOWN").toString();
		String stacounty   = studio.get("STA_COUNTY").toString();
		String stacountry  = studio.get("STA_COUNTRY").toString();
		String stapostcode = studio.get("STA_POST_CODE").toString();
		float stalet       = Float.parseFloat(studio.get("STA_LATITUDE").toString());
		float stalong      = Float.parseFloat(studio.get("STA_LONGITUDE").toString());
		Date stastarttime  = timeformat.parse(studio.get("STA_START_TIME").toString());
		Date staendtime    = timeformat.parse(studio.get("STA_END_TIME").toString());
		LOGGER.debug("end time is "+staendtime);
		
		
		String staextlink  = studio.get("STA_EXTERNAL_LINK").toString();
		String stalocation = studio.get("STA_LOCATION").toString();
		
		String stastatus = studio.get("STA_STATUS").toString().toUpperCase();
		int staNoDays = Integer.parseInt(studio.get("STA_NO_OF_DAYS").toString());
		
		
		if(stdid == 0 || (staname == null || staname.isEmpty()) ||statypeid == 0){
			throw new IllegalArgumentException("studio id,studio name and activity id  should not be empty");
 		} else if(!(studioService.isStudioExist(stdid))){
 			throw new IllegalArgumentException("studio id  "+stdid+" is not available");
 		}
		
		return studiosActivityService.saveStudioActivity(staid,staname,stdid,stadesc,staShotDesc,stastartdate,staenddate,stacredate,stamoddate,staupdby,
				stagoalpoints,stabatchid,statypeid,staimage,stainfluen,stalikes,staprice,staaddr1,staaddr2,staaddr3,statown,stacounty,stacountry,stapostcode,
				stalet,stalong,stastarttime,staendtime,staextlink,stalocation,staExpiryDate,stastatus,staNoDays);
	}
	/**
	 * In this method we will get the challenge details from the database based on activity id passing.
	 * 
	 * @param activityid An unique id of studio activity
	 * @param partyid An unique id of party.
	 * @return Return JSONObject contains challenge details.
	 */
	@RolesAllowed("USER")
	@GET
	@Path("/challengeWall/activityid/{activityid}/{partyid}")
	@Produces("application/json")
	public JSONObject getChallengeWall(@PathParam("activityid") int activityid,@PathParam("partyid") int partyid)
	{			
		return studiosActivityService.getChallengeWall(activityid,partyid);
	}
	/**
	 * This method used to get challenge details from database based on the activity id passing.
	 * 
	 * @param pageno current page number
	 * @param pagesize Total number of records shown in current page.
	 * @return Returns JSONArray contains all the challenge details.
	 */
	@RolesAllowed("USER")
	@GET
	@Path("/activity/challenges/{pageno}/{pagesize}")
	@Produces("application/json")
	public JSONArray getAllChallenges(@PathParam("pageno") int pageno,@PathParam("pagesize") int pagesize)
	{	
		return studiosActivityService.getAllChallenges(pageno,pagesize);
	}
	/**
	 * This method used to get all the activity from database and helps to search the activities based on passed string.
	 * 
	 * @param name Name of the events/challenge.
	 * @param status Activity status
	 * @param type which studio activity need to show
	 * @param stdid An unique id of studio.
	 * @return Returns JSONArray contains all the studio activity details.
	 */
	
	
	@RolesAllowed("ADMIN")
	@GET
	@Path("/activitySearch/name/{name}/status/{status}/type/{type}/stdid/{stdid}")
	@Produces("application/json")	
	public JSONArray activitySearch(@PathParam("name") String name,@PathParam("status") String status,@PathParam("type") int type, @PathParam("stdid") int stdid) {
		
		if(name == "" || status == null ) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(type == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}

		return studiosActivityService.activitySearch(name, status, type, stdid);
	}
	/**
	 * In this method we will get the activity detail from the database based on activity id passing.
	 * 
	 * @param activityid An unique id of studio activity.
	 * @return Return JSONObject contains challenge details.
	 */
	@RolesAllowed("ADMIN")
	@GET
	@Path("/activityDetail/activityid/{activityid}")
	@Produces("application/json")
	public JSONObject getEventDetails(@PathParam("activityid") int activityid)
	{
		if(activityid == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
 		}
		return studiosActivityService.activityDetails(activityid);
		
	}	

}
