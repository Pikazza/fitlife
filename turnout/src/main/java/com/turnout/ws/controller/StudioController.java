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
import com.turnout.ws.service.StudioService;
import com.turnout.ws.service.VoucherService;


/**
*
* StudioController class is front face for accessing and manipulating studios related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* Basic CRUD operation is done like update, retrieve and add. 
*/
@RestController
@Path("/")
public class StudioController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudioController.class);
	
	private final StudioService studioService;
	
	/**
	 * An injectable constructor with a dependency of StudioService as argument.
	 * 
	 * @param studioService An Object of StudioService as an injectable member.
	 * @see StudioService
	 */
	@Autowired
	public StudioController(final StudioService studioService) {
		this.studioService = studioService;
	}
	
	/**
	 * It will get all the studios from database based on the given status.
	 * For example, it will list all the active studios if status is passed as active.
	 * 
	 * 
	 * @param status  A string may have the value of active or inactive.
	 * @return It returns list if studios.
	 */
	@RolesAllowed({"ADMIN","USER"})
	@GET
	@Path("/studios/status/{status}")
	@Produces("application/json")
	public JSONArray getStudiosList(@PathParam("status") String status)
	{
		return studioService.getStudiosList(status.toUpperCase());
	}
	
	/**
	 * Returns details of studio based on passed studio id.
	 * 
	 * @param stdid An id of studio
	 * @return It returns details of given studio.
	 * @throws UnirestException a runtime exception happens while accessing studio's instagram account 
	 */
	@RolesAllowed({"ADMIN","USER"})
	@GET
	@Path("/studioDetails/{stdid}")
	@Produces("application/json")
	public JSONObject getStudioProfile(@PathParam("stdid") int stdid) throws UnirestException
	{
		return studioService.getStudioProfile(stdid);
		
	}
	
	
	/**
	 * This method used to add new studio or update existing studio into database.
	 * It will return a studio id (primary key) of just inserted studio.
	 * 
	 * @param crstdReq A JSONObject that holds details of studio to be inserted.
	 * @return Returns JSONObject with inserted voucher id.
	 * @throws NumberFormatException 
	 * @throws ParseException 
	 */
	@RolesAllowed("ADMIN")
	@Path("/saveStudio")
	@POST
	@Produces("application/json")
	public JSONObject saveStudio(@Valid JSONObject  crstdReq) throws NumberFormatException, ParseException {
		LOGGER.debug("Save studio controller");
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject jsonResult = new JSONObject();

		int stdId             = Integer.parseInt(crstdReq.get("STD_ID").toString());
		String stdName        = crstdReq.get("STD_NAME").toString();
		String stdTradeName   = crstdReq.get("STD_TRADING_NAME").toString();
		String stdAddr1       = crstdReq.get("STD_ADDRESS_LINE1").toString();
		String stdAddr2       = crstdReq.get("STD_ADDRESS_LINE2").toString();
		String stdAddr3       = crstdReq.get("STD_ADDRESS_LINE3").toString();
		String stdTown        = crstdReq.get("STD_TOWN").toString();
		String stdCounty      = crstdReq.get("STD_COUNTY").toString();
		String stdCountry     = crstdReq.get("STD_COUNTRY").toString();
		String stdPostcode    = crstdReq.get("STD_POST_CODE").toString();
		String stdBAddr1      = crstdReq.get("STD_BILLING_ADDRS_LINE1").toString();
		String stdBAddr2      = crstdReq.get("STD_BILLING_ADDRS_LINE2").toString();
		String stdBAddr3      = crstdReq.get("STD_BILLING_ADDRS_LINE3").toString();
		String stdBTown       = crstdReq.get("STD_BILLING_ADDRS_TOWN").toString();
		String stdBCounty     = crstdReq.get("STD_BILLING_ADDRS_COUNTY").toString();
		String stdBCountry    = crstdReq.get("STD_BILLING_ADDRS_COUNTRY").toString();
		String stdBPostcode   = crstdReq.get("STD_BILLING_ADDRS_POSTCODE").toString();
		String stdCompHouseNo = crstdReq.get("STD_COMPANY_HOUSE_NUMBER").toString();
		String stdVatno     = crstdReq.get("STD_VAT_NUMBER").toString();
		String stdCompLogo  = crstdReq.get("STD_COMPANY_LOGO").toString();
		String stdImgurl    = crstdReq.get("STD_IMAGE_URL").toString();
		String stdLatitude  = crstdReq.get("STD_LATITUDE").toString();
		String stdLongitude = crstdReq.get("STD_LONGITUDE").toString();	    
		
		String stdsiteurl = crstdReq.get("STD_SITE_URL").toString();	    
		String creby = crstdReq.get("STD_CREATED_BY").toString();	    
		String creon = crstdReq.get("STD_CREATED_ON").toString();	    
		String modby = crstdReq.get("STD_MODIFIED_BY").toString();	    
		String modon = crstdReq.get("STD_MODIFIED_ON").toString();	    
		String desc  = crstdReq.get("STD_DESCRIPTION").toString();
		String phone = crstdReq.get("STD_PHONE_NO").toString();
		String mail  = crstdReq.get("STD_MAIL_ID").toString();
		
		String streamDesc  = crstdReq.get("STD_STREAM_DESC").toString();
		
		String status       = crstdReq.get("STD_STATUS").toString().toUpperCase();
		String type         = crstdReq.get("STD_TYPE").toString().toUpperCase();
		String pamAuthId    = crstdReq.get("STD_PAM_AUTH_ID").toString();
		String pamAuthToken = crstdReq.get("STD_PAM_AUTH_TOKEN").toString();
		int points          = Integer.parseInt(crstdReq.get("STD_POINTS").toString());
		int timeSpent       = Integer.parseInt(crstdReq.get("STD_TIME_SPENT").toString());
		
		
		if(stdName == null || stdTradeName == null || stdName =="" || stdTradeName ==""){
			throw new IllegalArgumentException("studio name or studio trade name should not be empty");
		}
		
		if(stdId != 0) {
			if(!studioService.isStudioExist(stdId)) {
				throw new IllegalArgumentException("studio id "+stdId+ " Not found" );
			}
		}
		System.out.println("call service method");
		return studioService.saveStudio(stdId,stdName,stdTradeName,stdAddr1,stdAddr2,stdAddr3,stdTown,stdCounty,stdCountry,stdPostcode,
                stdBAddr1,stdBAddr2,stdBAddr3,stdBTown,stdBCounty,stdBCountry,stdBPostcode,
                stdCompHouseNo,stdVatno,stdCompLogo,stdImgurl,Float.parseFloat(stdLatitude),Float.parseFloat(stdLongitude),
                stdsiteurl,creby,dateformat.parse(creon),modby,dateformat.parse(modon),desc,phone,mail,status,type,pamAuthId,
                pamAuthToken,points,timeSpent,streamDesc);		
	}
	
/* @RolesAllowed("ADMIN")
	@Path("/addStudio")
	@POST
	@Produces("application/json")
	public JSONObject createStudio(@Valid JSONObject  crstdReq) throws NumberFormatException, ParseException {
		
		LOGGER.debug("entered into controller in create studio");
		DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
		JSONObject jsonResult = new JSONObject();
		String stdName = crstdReq.get("STD_NAME").toString();
		String stdTradeName = crstdReq.get("STD_TRADING_NAME").toString();
		String stdAddr1 = crstdReq.get("STD_ADDRESS_LINE1").toString();
		String stdAddr2 = crstdReq.get("STD_ADDRESS_LINE2").toString();
		String stdAddr3 = crstdReq.get("STD_ADDRESS_LINE3").toString();
		String stdTown = crstdReq.get("STD_TOWN").toString();
		String stdCounty = crstdReq.get("STD_COUNTY").toString();
		String stdCountry = crstdReq.get("STD_COUNTRY").toString();
		String stdPostcode = crstdReq.get("STD_POST_CODE").toString();
		String stdBAddr1 = crstdReq.get("STD_BILLING_ADDRS_LINE1").toString();
		String stdBAddr2 = crstdReq.get("STD_BILLING_ADDRS_LINE2").toString();
		String stdBAddr3 = crstdReq.get("STD_BILLING_ADDRS_LINE3").toString();
		String stdBTown = crstdReq.get("STD_BILLING_ADDRS_TOWN").toString();
		String stdBCounty = crstdReq.get("STD_BILLING_ADDRS_COUNTY").toString();
		String stdBCountry    = crstdReq.get("STD_BILLING_ADDRS_COUNTRY").toString();
		String stdBPostcode   = crstdReq.get("STD_BILLING_ADDRS_POSTCODE").toString();
		String stdCompHouseNo = crstdReq.get("STD_COMPANY_HOUSE_NUMBER").toString();
		String stdVatno     = crstdReq.get("STD_VAT_NUMBER").toString();
		String stdCompLogo  = crstdReq.get("STD_COMPANY_LOGO").toString();
		String stdImgurl    = crstdReq.get("STD_IMAGE_URL").toString();
		String stdLatitude  = crstdReq.get("STD_LATITUDE").toString();
		String stdLongitude = crstdReq.get("STD_LONGITUDE").toString();	    
		
		String stdsiteurl = crstdReq.get("STD_SITE_URL").toString();	    
		String creby = crstdReq.get("STD_CREATED_BY").toString();	    
		String creon = crstdReq.get("STD_CREATED_ON").toString();	    
		String modby = crstdReq.get("STD_MODIFIED_BY").toString();	    
		String modon = crstdReq.get("STD_MODIFIED_ON").toString();	    
		String desc  = crstdReq.get("STD_DESCRIPTION").toString();	 
		String phone = crstdReq.get("STD_PHONE_NO").toString();
		String mail  = crstdReq.get("STD_MAIL_ID").toString();
		
		String status    = crstdReq.get("STD_STATUS").toString().toUpperCase();
		String type      = crstdReq.get("STD_TYPE").toString().toUpperCase();
		String pamAuthId = crstdReq.get("STD_PAM_AUTH_ID").toString();
		String pamAuthToken = crstdReq.get("STD_PAM_AUTH_TOKEN").toString();
		int points       = Integer.parseInt(crstdReq.get("STD_POINTS").toString());
		int timeSpent    = Integer.parseInt(crstdReq.get("STD_TIME_SPENT").toString());
		
		
		if(stdName==null || stdTradeName==null || stdName=="" || stdTradeName==""){
			throw new IllegalArgumentException("studio name or studio trade name should not be empty");
		}

		return studioService.createStudio(stdName,stdTradeName,stdAddr1,stdAddr2,stdAddr3,stdTown,stdCounty,stdCountry,stdPostcode,
                stdBAddr1,stdBAddr2,stdBAddr3,stdBTown,stdBCounty,stdBCountry,stdBPostcode,
                stdCompHouseNo,stdVatno,stdCompLogo,stdImgurl,Float.parseFloat(stdLatitude),Float.parseFloat(stdLongitude),
                stdsiteurl,creby,dateformat.parse(creon),modby,dateformat.parse(modon),desc,phone,mail,status,type,pamAuthId,
                pamAuthToken,points,timeSpent);
	}
	
	@RolesAllowed("ADMIN")
	@Path("/updateStudio")
	@POST
	@Produces("application/json")
	public JSONObject UpdateStudio(@Valid JSONObject upStdReq) throws NumberFormatException, ParseException {
		
		DateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
		JSONObject jsonResult = new JSONObject();
		int stdId           = Integer.parseInt(upStdReq.get("STD_ID").toString());
		String stdName      = upStdReq.get("STD_NAME").toString();
		String stdTradeName = upStdReq.get("STD_TRADING_NAME").toString();
		String stdAddr1     = upStdReq.get("STD_ADDRESS_LINE1").toString();
		String stdAddr2     = upStdReq.get("STD_ADDRESS_LINE2").toString();
		String stdAddr3     = upStdReq.get("STD_ADDRESS_LINE3").toString();
		String stdTown      = upStdReq.get("STD_TOWN").toString();
		String stdCounty    = upStdReq.get("STD_COUNTY").toString();
		String stdCountry   = upStdReq.get("STD_COUNTRY").toString();
		String stdPostcode  = upStdReq.get("STD_POST_CODE").toString();
		String stdBAddr1    = upStdReq.get("STD_BILLING_ADDRS_LINE1").toString();
		String stdBAddr2    = upStdReq.get("STD_BILLING_ADDRS_LINE2").toString();
		String stdBAddr3    = upStdReq.get("STD_BILLING_ADDRS_LINE3").toString();
		String stdBTown     = upStdReq.get("STD_BILLING_ADDRS_TOWN").toString();
		String stdBCounty   = upStdReq.get("STD_BILLING_ADDRS_COUNTY").toString();
		String stdBCountry  = upStdReq.get("STD_BILLING_ADDRS_COUNTRY").toString();
		String stdBPostcode = upStdReq.get("STD_BILLING_ADDRS_POSTCODE").toString();
		String stdCompHouseNo = upStdReq.get("STD_COMPANY_HOUSE_NUMBER").toString();
		String stdVatno     = upStdReq.get("STD_VAT_NUMBER").toString();
		String stdCompLogo  = upStdReq.get("STD_COMPANY_LOGO").toString();
		String stdImgurl    = upStdReq.get("STD_IMAGE_URL").toString();
		String stdLatitude  = upStdReq.get("STD_LATITUDE").toString();
		String stdLongitude = upStdReq.get("STD_LONGITUDE").toString();
		String stdsiteurl = upStdReq.get("STD_SITE_URL").toString();
		String creby      = upStdReq.get("STD_CREATED_BY").toString();
		String creon      = upStdReq.get("STD_CREATED_ON").toString();
		String modby      = upStdReq.get("STD_MODIFIED_BY").toString();
		String modon      = upStdReq.get("STD_MODIFIED_ON").toString();
		String desc       = upStdReq.get("STD_DESCRIPTION").toString();
		String phone      = upStdReq.get("STD_PHONE_NO").toString();
		String mail       = upStdReq.get("STD_MAIL_ID").toString();
		
		String status    = upStdReq.get("STD_STATUS").toString().toUpperCase();
		String type      = upStdReq.get("STD_TYPE").toString().toUpperCase();
		String pamAuthId = upStdReq.get("STD_PAM_AUTH_ID").toString();
		String pamAuthToken = upStdReq.get("STD_PAM_AUTH_TOKEN").toString();
		int points       = Integer.parseInt(upStdReq.get("STD_POINTS").toString());
		int timeSpent    = Integer.parseInt(upStdReq.get("STD_TIME_SPENT").toString());		
		
		if(stdId == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!studioService.isStudioExist(stdId)) {
			throw new IllegalArgumentException("studio id "+stdId+ "Not found" );
		}
		
		return studioService.updateStudio(stdId,stdName,stdTradeName,stdAddr1,stdAddr2,stdAddr3,stdTown,stdCounty,stdCountry,stdPostcode,
                stdBAddr1,stdBAddr2,stdBAddr3,stdBTown,stdBCounty,stdBCountry,stdBPostcode,
                stdCompHouseNo,stdVatno,stdCompLogo,stdImgurl,Float.parseFloat(stdLatitude),Float.parseFloat(stdLongitude),
                stdsiteurl,creby,dateformat.parse(creon),modby,dateformat.parse(modon),desc,phone,mail,status,type,pamAuthId,
                pamAuthToken,points,timeSpent);

	}
	
	@RolesAllowed("ADMIN")
	@GET
	@Path("/changeStatus/{stdid}/{status}")
	@Produces("application/json")
	public JSONObject doChangeStatus(@PathParam("stdid") int stdId, @PathParam("status") String status)
	{
		if(stdId == 0 || status == null) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!studioService.isStudioExist(stdId)) {
			throw new IllegalArgumentException("studio id "+stdId+ "Not found" );
		}
		return studioService.doChangeStatus(stdId,status.toUpperCase());		
	}
*/
	
	/**
	 * It will search and get list of all studios from database when it found the matched string.
	 * 
	 * 
	 * @param search a search string
	 * @return array of studios
	 */
	@RolesAllowed("ADMIN")
	@GET
	@Path("/studioSearch/{search}")
	@Produces("application/json")	
	public JSONArray doStdSearch(@PathParam("search") String search) {
		if(search == "" || search == null) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}
		return studioService.doStudioSearch(search);
	}
	

}
