package com.turnout.ws.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.service.OffersService;
import com.turnout.ws.service.StudioService;


/**
*
* OfferController class is front face for accessing and manipulating offers(promos and upsells) related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
*/
@RestController
@Path("/")
public class OfferController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OfferController.class);
	
	private final OffersService ofrService;
	private final StudioService studioService;
	
	/**
	 * An injectable constructor with dependencies of ofrService,studioService as argument.
	 * 
	 * @param ofrService An Object of ofrService as an injectable member.
	 * @param studioService An Object of studioService as an injectable member.
	 * @see ofrService
	 * @see studioService
	 */
	@Autowired
	public OfferController(final OffersService ofrService,final StudioService studioService) {
		this.ofrService    = ofrService;
		this.studioService = studioService;
	}
	/**
	 * This method used to get active offers from database based on the beacon id and offer type.
	 * 
	 * @param beaconId An id of beacon mapped with specific studio.
	 * @param type It is used to specify the notification type like welcome or exit.
	 * @return Returns JSONObject which contains whole details of welcome / exit notification.
	 */
	
	@RolesAllowed("USER")
	@GET
	@Path("/offers/{beaconId}/{type}")
	@Produces("application/json")
	public JSONObject getOffers(@PathParam("beaconId") String beaconId , @PathParam("type") String type)
	{
		if(beaconId == "" || beaconId == null) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}
		return ofrService.getActiveOffers(beaconId , type);
		
	}
	
/*	@RolesAllowed("ADMIN")
	@POST
	@Path("/addOffers")
	@Produces("application/json")
	public JSONObject addOffers(@Valid JSONObject  ofrReq) throws ParseException  {
		DateFormat dateformat  = new SimpleDateFormat("MM/dd/yyyy");
		JSONObject jsonResult  = new JSONObject();
		int ofrStdId           = Integer.parseInt(ofrReq.get("OFR_STD_ID").toString());
		String ofrType         = ofrReq.get("OFR_TYPE").toString().toUpperCase();
		String ofrStatus       = ofrReq.get("OFR_STATUS").toString().toUpperCase();
		String ofrImg          = ofrReq.get("OFR_IMG").toString();
		String ofrDescription  = ofrReq.get("OFR_DESCRIPTION").toString();
		String ofrExternalLink = ofrReq.get("OFR_EXTERNAL_LINK").toString();
		String ofrCreatedDate  = ofrReq.get("OFR_CREATED_DATE").toString(); 
		
		
		if(ofrStdId == 0 || ofrType == "" || ofrStatus == "" ){
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!studioService.isStudioExist(ofrStdId)) {
			throw new IllegalArgumentException("studio id "+ofrStdId+ " Not found" );
		}

		return ofrService.addOffers(ofrStdId,ofrType,ofrStatus,ofrImg,ofrDescription,ofrExternalLink,dateformat.parse(ofrCreatedDate));
	}
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/updateOffers")
	@Produces("application/json")
	public JSONObject updateOffers(@Valid JSONObject  ofrReq) throws ParseException  {
		DateFormat dateformat  = new SimpleDateFormat("MM/dd/yyyy");
		JSONObject jsonResult  = new JSONObject();
		int ofrId              = Integer.parseInt(ofrReq.get("OFR_ID").toString());
		int ofrStdId           = Integer.parseInt(ofrReq.get("OFR_STD_ID").toString());
		String ofrType         = ofrReq.get("OFR_TYPE").toString().toUpperCase();
		String ofrStatus       = ofrReq.get("OFR_STATUS").toString().toUpperCase();
		String ofrImg          = ofrReq.get("OFR_IMG").toString();
		String ofrDescription  = ofrReq.get("OFR_DESCRIPTION").toString();
		String ofrExternalLink = ofrReq.get("OFR_EXTERNAL_LINK").toString();
		String ofrCreatedDate  = ofrReq.get("OFR_CREATED_DATE").toString(); 
		
		
		if(ofrStdId == 0 || ofrId == 0){
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!studioService.isStudioExist(ofrStdId)) {
			throw new IllegalArgumentException("studio id "+ofrStdId+ " Not found" );
		}

		return ofrService.updateOffers(ofrId,ofrStdId,ofrType,ofrStatus,ofrImg,ofrDescription,ofrExternalLink,dateformat.parse(ofrCreatedDate));
	}*/
	
	/**
	 * This method is used to save welcome/ exit notification offers into database. It will return a offer id of just inserted or updated.
	 * 
	 * @param ofrReq A JSONObject contain information of offers.
	 * @return Returns JSONObject that has a value of offer id.
	 * @throws ParseException Exception throw when parsing value empty. 
	 */
	
	@RolesAllowed("ADMIN")
	@POST
	@Path("/saveOffers")
	@Produces("application/json")
	public JSONObject saveOffers(@Valid JSONObject  ofrReq) throws ParseException  {
		DateFormat dateformat  = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject jsonResult  = new JSONObject();
		int ofrId              = Integer.parseInt(ofrReq.get("OFR_ID").toString());
		int ofrStdId           = Integer.parseInt(ofrReq.get("OFR_STD_ID").toString());
		String ofrType         = ofrReq.get("OFR_TYPE").toString().toUpperCase();
		String ofrStatus       = ofrReq.get("OFR_STATUS").toString().toUpperCase();
		String ofrImg          = ofrReq.get("OFR_IMG").toString();
		String ofrDescription  = ofrReq.get("OFR_DESCRIPTION").toString();
		String ofrExternalLink = ofrReq.get("OFR_EXTERNAL_LINK").toString();
		String ofrCreatedDate  = ofrReq.get("OFR_CREATED_DATE").toString(); 
		
		if(ofrStdId == 0 || ofrType == "" || ofrStatus == "" ) {			
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!studioService.isStudioExist(ofrStdId)) {
			throw new IllegalArgumentException("studio id "+ofrStdId+ " Not found" );
		}

		return ofrService.saveOffers(ofrId,ofrStdId,ofrType,ofrStatus,ofrImg,ofrDescription,ofrExternalLink,dateformat.parse(ofrCreatedDate));
	}
	
	/**
	 * This method used to get all the offers from database and helps to search the offers based on offer name.
	 * 
	 * @param ofrname A string holds a text that has to be searched.
	 * @return Returns JSONArray contains all the results after searching is done.
	 */
	
	@RolesAllowed("ADMIN")
	@GET
	@Path("/offersSearch/ofrname/{ofrname}")
	@Produces("application/json")	
	public JSONArray offerSearch(@PathParam("ofrname") String ofrname) {
		
		if(ofrname == "" || ofrname == null ) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}
		return ofrService.offerSearch(ofrname);
	}	
	
	/**
	 * This method will get offer details from database based on offer id. 
	 * 
	 * @param ofrId A unique id of an offer.
	 * @return Returns JSONObject which contains whole details of offers entity.
	 */
	
	@RolesAllowed("ADMIN")
	@GET
	@Path("/offersDetails/ofrId/{ofrId}")
	@Produces("application/json")	
	public JSONObject offerDetails(@PathParam("ofrId") int ofrId) {
		
		if(ofrId == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}
		return ofrService.offerDetails(ofrId);
	}	
}