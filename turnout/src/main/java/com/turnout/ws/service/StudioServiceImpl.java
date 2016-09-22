package com.turnout.ws.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.StudioActivityType;
import com.turnout.ws.domain.StudiosActivity;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CustomRewardRepository;
import com.turnout.ws.repository.CustomStudioActivityRepository;
import com.turnout.ws.repository.CustomStudioPartyActivityRepository;
import com.turnout.ws.repository.RewardRepository;
import com.turnout.ws.repository.StudioActivityRepository;
import com.turnout.ws.repository.StudioRepository;

/**
 * StudioServiceImpl is class that contains collection of methods that can be accessed for manipulating studios. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 * 
 * Attempting to add or edit an ineligible object throws an UnirestException.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
public class StudioServiceImpl implements StudioService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudioServiceImpl.class);
	
	private final StudioRepository studioRepository;
	private final CustomRewardRepository customRewardRepository;
	private final CustomStudioActivityRepository customStudioActivityRepository;
	private final CustomStudioPartyActivityRepository customStudioPartyActivityRepository;  

	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param studioRepository                    An Object of studioRepository as an injectable member.
	 * @param customRewardRepository              An Object of customRewardRepository as an injectable member.
	 * @param customStudioActivityRepository      An Object of customStudioActivityRepository as an injectable member.
	 * @param customStudioPartyActivityRepository An Object of customStudioPartyActivityRepository as an injectable member.
	 * @see studioRepository
	 * @see customRewardRepository
	 * @see customStudioActivityRepository
	 * @see customStudioPartyActivityRepository
	 */
	
	@Autowired
	public StudioServiceImpl(final StudioRepository studioRepository,
			 final CustomRewardRepository customRewardRepository,
			 final CustomStudioActivityRepository customStudioActivityRepository,
			 final CustomStudioPartyActivityRepository customStudioPartyActivityRepository) {

		this.studioRepository = studioRepository;
		this.customRewardRepository = customRewardRepository;
		this.customStudioActivityRepository = customStudioActivityRepository;
		this.customStudioPartyActivityRepository = customStudioPartyActivityRepository;
	}

	private Sort orderBy() {
	    return new Sort(Sort.Direction.ASC, "stdName");
	}
	
	/**
	 * It will get all the studios from database based on the given status.
	 * For example, it will list all the active studios if status is passed as active.
	 * 
	 * @param status  a string may have the value of active or inactive.
	 * @return It will return all active studios list. 
	 */
	public JSONArray getStudiosList(String status) {
		
		List<Studio> studioList ;
		if(status.equals("ALL")) {
			studioList = studioRepository.findAll(orderBy());	
		} else {
			studioList = studioRepository.findByStdStatus(status, orderBy());
		}		
		LOGGER.debug("studioList"+studioList);
		JSONArray stdjsonArr = new JSONArray();
		JSONObject obj;
		for(Studio std:studioList)
		{
			obj = new JSONObject();
			obj.put("STD_ID", std.getStdId());
			obj.put("STD_NAME", std.getStdName());
			obj.put("STD_TRADING_NAME", std.getStdTradingName());
			obj.put("STD_ADDRESS_LINE1", std.getStdAddressLine1());
			obj.put("STD_ADDRESS_LINE2", std.getStdAddressLine2());
			obj.put("STD_ADDRESS_LINE3", std.getStdAddressLine3());
			obj.put("STD_TOWN", std.getStdTown());
			obj.put("STD_COUNTY", std.getStdCounty());
			obj.put("STD_COUNTRY", std.getStdCountry());
			obj.put("STD_POST_CODE", std.getStdPostCode());
			obj.put("STD_IMAGE_URL", std.getStdImageUrl());
			obj.put("STD_BILLING_ADDRS_LINE1", std.getStdBillingAddrsLine1());
			obj.put("STD_BILLING_ADDRS_LINE2", std.getStdBillingAddrsLine2());
			obj.put("STD_BILLING_ADDRS_LINE3", std.getStdBillingAddrsLine3());
			obj.put("STD_BILLING_ADDRS_TOWN", std.getStdBillingAddrsTown());
			obj.put("STD_BILLING_ADDRS_COUNTY", std.getStdBillingAddrsCounty());
			obj.put("STD_BILLING_ADDRS_COUNTRY", std.getStdBillingAddrsCountry());
			obj.put("STD_BILLING_ADDRS_POSTCODE", std.getStdBillingAddrsPostcode());
			obj.put("STD_COMPANY_HOUSE_NUMBER", std.getStdCompanyHouseNumber());
			obj.put("STD_VAT_NUMBER", std.getStdVatNumber());
			obj.put("STD_COMPANY_LOGO", std.getStdCompanyLogo());
			obj.put("STD_LATITUDE",std.getStdLatitude() );
			obj.put("STD_LONGITUDE",std.getStdLongitude() );
			obj.put("STD_DESCRIPTION", std.getStdDescription());	
			obj.put("STD_PHONENO", std.getStdPhoneNo());	
			obj.put("STD_MAILID", std.getStdMailId());	
			
			obj.put("STD_STATUS", std.getStdStatus());
			obj.put("STD_TYPE", std.getStdType());
			obj.put("STD_PAM_AUTH_ID", std.getStdPamAuthId());
			obj.put("STD_PAM_AUTH_TOKEN", std.getStdPamAuthToken());
			obj.put("STD_POINTS", std.getStdPoints());
			obj.put("STD_TIME_SPENT", std.getStdTimeSpent());
			
			obj.put("STD_STREAM_DESC", std.getStdStreamDescription());
			
			stdjsonArr.add(obj);					
		}		
		return stdjsonArr;
	}
	/**
	 * Returns client response that holds details of studio based on passed studio id.
	 * 
	 * @param stdid An id of studio
	 * @return It returns details of given studio.
	 * @throws UnirestException a runtime exception happens while accessing studio's instagram account 
	 */
	public JSONObject getStudioProfile(int stdid) throws UnirestException {
		
		Studio stdprofile = studioRepository.findOne(stdid);
		
		JSONObject stdprofjson = new JSONObject();
		stdprofjson.put("STD_ID", stdprofile.getStdId());
		stdprofjson.put("STD_NAME", stdprofile.getStdName());
		stdprofjson.put("STD_TRADING_NAME", stdprofile.getStdTradingName());
		stdprofjson.put("STD_ADDRESS_LINE1", stdprofile.getStdAddressLine1());
		stdprofjson.put("STD_ADDRESS_LINE2", stdprofile.getStdAddressLine2());
		stdprofjson.put("STD_ADDRESS_LINE3", stdprofile.getStdAddressLine3());
		stdprofjson.put("STD_TOWN", stdprofile.getStdTown());
		stdprofjson.put("STD_COUNTY", stdprofile.getStdCounty());
		stdprofjson.put("STD_COUNTRY", stdprofile.getStdCountry());
		stdprofjson.put("STD_POST_CODE", stdprofile.getStdPostCode());
		stdprofjson.put("STD_IMAGE_URL", stdprofile.getStdImageUrl());
		stdprofjson.put("STD_BILLING_ADDRS_LINE1", stdprofile.getStdBillingAddrsLine1());
		stdprofjson.put("STD_BILLING_ADDRS_LINE2", stdprofile.getStdBillingAddrsLine2());
		stdprofjson.put("STD_BILLING_ADDRS_LINE3", stdprofile.getStdBillingAddrsLine3());
		stdprofjson.put("STD_BILLING_ADDRS_TOWN", stdprofile.getStdBillingAddrsTown());
		stdprofjson.put("STD_BILLING_ADDRS_COUNTY", stdprofile.getStdBillingAddrsCounty());
		stdprofjson.put("STD_BILLING_ADDRS_COUNTRY", stdprofile.getStdBillingAddrsCountry());
		stdprofjson.put("STD_BILLING_ADDRS_POSTCODE", stdprofile.getStdBillingAddrsPostcode());
		stdprofjson.put("STD_COMPANY_HOUSE_NUMBER", stdprofile.getStdCompanyHouseNumber());
		stdprofjson.put("STD_VAT_NUMBER", stdprofile.getStdVatNumber());
		stdprofjson.put("STD_COMPANY_LOGO", stdprofile.getStdCompanyLogo());
		stdprofjson.put("STD_LATITUDE", stdprofile.getStdLatitude());
		stdprofjson.put("STD_LONGITUDE", stdprofile.getStdLongitude());	
		stdprofjson.put("STD_DESCRIPTION", stdprofile.getStdDescription());	
		stdprofjson.put("STD_PHONENO", stdprofile.getStdPhoneNo());	
		stdprofjson.put("STD_MAILID", stdprofile.getStdMailId());	
		stdprofjson.put("STD_SITE_URL", stdprofile.getStdSiteUrl());
		
		stdprofjson.put("STD_STATUS", stdprofile.getStdStatus());
		stdprofjson.put("STD_TYPE", stdprofile.getStdType());
		stdprofjson.put("STD_PAM_AUTH_ID", stdprofile.getStdPamAuthId());
		stdprofjson.put("STD_PAM_AUTH_TOKEN", stdprofile.getStdPamAuthToken());
		stdprofjson.put("STD_POINTS", stdprofile.getStdPoints());
		stdprofjson.put("STD_TIME_SPENT", stdprofile.getStdTimeSpent());
		
		stdprofjson.put("STD_STREAM_DESC", stdprofile.getStdStreamDescription());
		
		List stdActList = customStudioActivityRepository.getStudioActivityByStdid(stdid);
		
		JSONArray stdActivity = new JSONArray();
		JSONObject activity;
		Map<String, Object> activityrecord;
		if(stdActList != null)
		{
			for(Iterator itr=stdActList.iterator();itr.hasNext();)
			{
				activity=new JSONObject();
				activityrecord = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : activityrecord.entrySet())
				{
					activity.put(entry.getKey(), entry.getValue());	
				}
				stdActivity.add(activity);
			}
			
		}
		stdprofjson.put("STD_EVENT",stdActivity );
		
		List stdchallengelist = customStudioActivityRepository.getStudioChallengesByStdid(stdid);
		
		JSONArray stdChallenge = new JSONArray();
		JSONObject challenge;
		Map<String, Object> challengerecord;
		if(stdchallengelist != null)
		{
			for(Iterator itr=stdchallengelist.iterator();itr.hasNext();)
			{
				challenge=new JSONObject();
				challengerecord = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : challengerecord.entrySet())
				{
					challenge.put(entry.getKey(), entry.getValue());	
				}
				stdChallenge.add(challenge);
			}
			
		}		
		stdprofjson.put("STD_CHALLENGE",stdChallenge );
		
		List lstRwd = customRewardRepository.getRewardByStdId(stdid);
		
		JSONArray rewardarr = new JSONArray();
		JSONObject rewardjson;
		Map<String, Object> recrod;
		if(lstRwd != null)
		{
			for(Iterator itr=lstRwd.iterator();itr.hasNext();)
			{
				rewardjson=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
					rewardjson.put(entry.getKey(), entry.getValue());	
				}
				rewardarr.add(rewardjson);
			}
		}		
		stdprofjson.put("REWARDS",rewardarr );
		
		List lstcheckin = customStudioPartyActivityRepository.getCheckinUser(stdid);
		
		JSONArray checkinarr = new JSONArray();
		JSONObject checkinjson;
		Map<String, Object> record;
		if(lstcheckin != null)
		{
			for(Iterator itr=lstcheckin.iterator();itr.hasNext();)
			{
				checkinjson=new JSONObject();
				record = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : record.entrySet())
				{
					checkinjson.put(entry.getKey(), entry.getValue());	
				}
				checkinarr.add(checkinjson);
			}
		}		
		stdprofjson.put("CHECKEDIN",checkinarr );
		
		if((stdprofile.getStdPamAuthId().isEmpty() || stdprofile.getStdPamAuthId().equals("null") ) && (stdprofile.getStdPamAuthToken().isEmpty() || stdprofile.getStdPamAuthToken().equals("null"))) {
			stdprofjson.put("INSTAGRAM_POST", "");
		} else {			
			stdprofjson.put("INSTAGRAM_POST", getInstagramPost(stdprofile.getStdPamAuthId(),stdprofile.getStdPamAuthToken()));
		}
		return stdprofjson;
	}
	/**
	 * This function will get recent instagram posts.
	 * 
	 * @param stdPamAuthId Instagram user authentication id.
	 * @param stdPamAuthToken Instagram user authentication token.
	 * @return it will all recent post.
	 * @throws UnirestException a runtime exception happens while accessing studio's instagram account.
	 */
	private JSONArray getInstagramPost(String stdPamAuthId, String stdPamAuthToken) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get("https://api.instagram.com/v1/users/"+stdPamAuthId+"/media/recent/?access_token="+stdPamAuthToken)
				  .header("cache-control", "no-cache")
				  .header("postman-token", "961b18f0-806c-bd44-0e8c-bc3ac0eca130")
				  .asJson();
		String type,link;
		String image;
		String video;
		JSONArray jsonopdata = new JSONArray();
		JSONObject jsonopobj ;
		
		if(response.getStatus()==200)
		{
			org.json.JSONObject responseBody = response.getBody().getObject();
			//accessToken = responseBody.getString("access_token");
			
			org.json.JSONArray jsondata =  responseBody.getJSONArray("data");
			
			for (int i = 0; i < jsondata.length(); i++)
			{
				jsonopobj = new JSONObject();				
				
				org.json.JSONObject jsonobj = jsondata.getJSONObject(i);
				type = jsonobj.getString("type");
				link = jsonobj.getString("link");
				
				jsonopobj.put("type", type);
				jsonopobj.put("link", link);
				 
				if(type.equalsIgnoreCase("video"))
				{
					org.json.JSONObject varray = jsonobj.getJSONObject("videos");
					
					org.json.JSONObject vstdres = varray.getJSONObject("standard_resolution");
					video = vstdres.getString("url");
					
					jsonopobj.put("videos", video);
					
				}
				else
				{
					jsonopobj.put("videos", "");
				}
				
				org.json.JSONObject imgarray = jsonobj.getJSONObject("images");				    
			    org.json.JSONObject imgstdres = imgarray.getJSONObject("standard_resolution");			    
			    image = imgstdres.getString("url");	
			    jsonopobj.put("images", image);				
				
				org.json.JSONObject usrarray = jsonobj.getJSONObject("user");			    
			    String username = usrarray.getString("username");
				jsonopobj.put("username", username);
				jsonopdata.add(jsonopobj);
			}			 
			LOGGER.debug("Instagram........"+jsonopdata);
		}		
		return jsonopdata;
	}

/*	@Transactional
	public JSONObject createStudio(String stdName, String stdTradeName, String stdAddr1, String stdAddr2, String stdAddr3,
			String stdTown, String stdCounty, String stdCountry, String stdPostcode, String stdBAddr1, String stdBAddr2,
			String stdBAddr3, String stdBTown, String stdBCounty, String stdBCountry, String stdBPostcode,
			String stdCompHouseNo, String stdVatno, String stdCompLogo, String stdImgurl, float f, float g,
			String stdsiteurl, String creby, Date credate, String modby, Date moddate, String desc, String phone,
			String mail,String status,String type,String pamAuthId, String pamAuthToken,int points,int timeSpent) {

		
		JSONObject result = new JSONObject();
		if(stdName!=null || stdTradeName!=null){
		Studio std = new Studio();
		std.setStdName(stdName);
		std.setStdTradingName(stdTradeName);
		std.setStdAddressLine1(stdAddr1);
		std.setStdAddressLine2(stdAddr2);
		std.setStdAddressLine3(stdAddr3);
		std.setStdTown(stdTown);
		std.setStdCounty(stdCounty);
		std.setStdCountry(stdCountry);
		std.setStdPostCode(stdPostcode);
		std.setStdBillingAddrsLine1(stdBAddr1);
		std.setStdBillingAddrsLine2(stdBAddr2);
		std.setStdBillingAddrsLine3(stdBAddr3);
		std.setStdBillingAddrsTown(stdBTown);
		std.setStdBillingAddrsCounty(stdCounty);
		std.setStdBillingAddrsCountry(stdBCountry);
		std.setStdBillingAddrsPostcode(stdBPostcode);
		std.setStdCompanyHouseNumber(stdCompHouseNo);
		std.setStdVatNumber(stdVatno);
		std.setStdCompanyLogo(stdCompLogo);
		std.setStdImageUrl(stdImgurl);
		std.setStdLatitude(f);
		std.setStdLongitude(g);
		std.setStdSiteUrl(stdsiteurl);
		std.setStdCreatedBy(creby);
		std.setStdCreatedOn(credate);
		std.setStdModifiedBy(modby);
		std.setStdModifiedOn(moddate);
		std.setStdMailId(mail);
		std.setStdPhoneNo(phone);
		std.setStdDescription(desc);
		
		std.setStdStatus(status);
		std.setStdType(type);
		std.setStdPamAuthId(pamAuthId);
		std.setStdPamAuthToken(pamAuthToken);
		std.setStdPoints(points);
		std.setStdTimeSpent(timeSpent);

		studioRepository.saveAndFlush(std);
		result.put("STD_ID", std.getStdId());
		}else{
			result.put("RESULT", TurnOutConstant.FAILED);
		}
		
		return result;		
}
	
	@Transactional
	public JSONObject updateStudio(int stdId, String stdName, String stdTradeName, String stdAddr1, String stdAddr2, String stdAddr3,
			String stdTown, String stdCounty, String stdCountry, String stdPostcode, String stdBAddr1, String stdBAddr2,
			String stdBAddr3, String stdBTown, String stdBCounty, String stdBCountry, String stdBPostcode,
			String stdCompHouseNo, String stdVatno, String stdCompLogo, String stdImgurl, float f, float g,
			String stdsiteurl, String creby, Date credate, String modby, Date moddate, String desc, String phone,
			String mail,String status,String type,String pamAuthId, String pamAuthToken,int points,int timeSpent) {
		
			JSONObject result = new JSONObject();
			if(stdId!= 0) {
				//Studio std = new Studio();
				Studio std = studioRepository.findOne(stdId);
				std.setStdName(stdName);
				std.setStdTradingName(stdTradeName);
				std.setStdAddressLine1(stdAddr1);
				std.setStdAddressLine2(stdAddr2);
				std.setStdAddressLine3(stdAddr3);
				std.setStdTown(stdTown);
				std.setStdCounty(stdCounty);
				std.setStdCountry(stdCountry);
				std.setStdPostCode(stdPostcode);
				std.setStdBillingAddrsLine1(stdBAddr1);
				std.setStdBillingAddrsLine2(stdBAddr2);
				std.setStdBillingAddrsLine3(stdBAddr3);
				std.setStdBillingAddrsTown(stdBTown);
				std.setStdBillingAddrsCounty(stdCounty);
				std.setStdBillingAddrsCountry(stdBCountry);
				std.setStdBillingAddrsPostcode(stdBPostcode);
				std.setStdCompanyHouseNumber(stdCompHouseNo);
				std.setStdVatNumber(stdVatno);
				std.setStdCompanyLogo(stdCompLogo);
				std.setStdImageUrl(stdImgurl);
				std.setStdLatitude(f);
				std.setStdLongitude(g);
				std.setStdSiteUrl(stdsiteurl);
				std.setStdCreatedBy(creby);
				std.setStdCreatedOn(credate);
				std.setStdModifiedBy(modby);
				std.setStdModifiedOn(moddate);
				std.setStdMailId(mail);
				std.setStdPhoneNo(phone);
				std.setStdDescription(desc);
				
				std.setStdStatus(status);
				std.setStdType(type);
				std.setStdPamAuthId(pamAuthId);
				std.setStdPamAuthToken(pamAuthToken);
				std.setStdPoints(points);
				std.setStdTimeSpent(timeSpent);				
				
				studioRepository.saveAndFlush(std);
				result.put("RESULT", TurnOutConstant.SUCCESS);
			} else {
				result.put("RESULT", TurnOutConstant.FAILED);
			}
		
		return result;		
	}	*/
	/**
	 * This method used to add new studio or update existing studio into database.
	 * It will return a studio id (primary key) of just inserted studio.
	 * 
	 * @param stdId An unique id of studio.
	 * @param stdName Studio name.
	 * @param stdTradeName Studio trade name.
	 * @param stdAddr1 Studio address line1.
	 * @param stdAddr2 Studio address line2.
	 * @param stdAddr3 Studio address line3.
	 * @param stdTown Studio town.
	 * @param stdCounty Studio country.
	 * @param stdCountry Studio country.
	 * @param stdPostcode Studio postcode.
	 * @param stdBAddr1 Studio business address line1.
	 * @param stdBAddr2 Studio business address line 2.
	 * @param stdBAddr3 Studio business address line 3.
	 * @param stdBTown  studio business town.
	 * @param stdBCounty studio business county.
	 * @param stdBCountry Studio business country.
	 * @param stdBPostcode Studio business post code.
	 * @param stdCompHouseNo Studio business door no.
	 * @param stdVatno Studio VAT no.
	 * @param stdCompLogo Studio logo
	 * @param stdImgurl Studio banner image.
	 * @param f Studio latitude.
	 * @param g Studio longtitude.
	 * @param stdsiteurl Studio website url.
	 * @param creby Studio creator name.
	 * @param credate Studio created date and time.
	 * @param modby Studio info modifier name.
	 * @param moddate Studio modify date and time.
	 * @param desc Studio description
	 * @param phone Studio contact phone no.
	 * @param mail Studio contact email address.
	 * @param  status Studio status (active / inactive).
	 * @param type Type of the studio (Normal / premium).
	 * @param pamAuthId Studio instagram auth id.
	 * @param pamAuthToken Studio instagram auth token.
	 * @param points No of points user can get after redeem.
	 * @param timeSpent  Minimum time spent in studio.
	 * @param streamDesc Premium studio description,this info will show Premium card description. 
	 * @return It will return added or updated studio id. 
	 */
	
	@Transactional
	public JSONObject saveStudio(int stdId, String stdName, String stdTradeName, String stdAddr1, String stdAddr2, String stdAddr3,
			String stdTown, String stdCounty, String stdCountry, String stdPostcode, String stdBAddr1, String stdBAddr2,
			String stdBAddr3, String stdBTown, String stdBCounty, String stdBCountry, String stdBPostcode,
			String stdCompHouseNo, String stdVatno, String stdCompLogo, String stdImgurl, float f, float g,
			String stdsiteurl, String creby, Date credate, String modby, Date moddate, String desc, String phone,
			String mail,String status,String type,String pamAuthId, String pamAuthToken,int points,int timeSpent,String streamDesc) {
		
			JSONObject result = new JSONObject();
			if(stdName != null || stdTradeName != null) {
				Studio std;
				if(stdId!= 0) {
					std = studioRepository.findOne(stdId);
					System.out.println("studio object "+std);
				} else {
					System.out.println("create studio object");
					std = new Studio();
				}
				std.setStdName(stdName);
				std.setStdTradingName(stdTradeName);
				std.setStdAddressLine1(stdAddr1);
				std.setStdAddressLine2(stdAddr2);
				std.setStdAddressLine3(stdAddr3);
				std.setStdTown(stdTown);
				std.setStdCounty(stdCounty);
				std.setStdCountry(stdCountry);
				std.setStdPostCode(stdPostcode);
				std.setStdBillingAddrsLine1(stdBAddr1);
				std.setStdBillingAddrsLine2(stdBAddr2);
				std.setStdBillingAddrsLine3(stdBAddr3);
				std.setStdBillingAddrsTown(stdBTown);
				std.setStdBillingAddrsCounty(stdCounty);
				std.setStdBillingAddrsCountry(stdBCountry);
				std.setStdBillingAddrsPostcode(stdBPostcode);
				std.setStdCompanyHouseNumber(stdCompHouseNo);
				std.setStdVatNumber(stdVatno);
				std.setStdCompanyLogo(stdCompLogo);
				std.setStdImageUrl(stdImgurl);
				std.setStdLatitude(f);
				std.setStdLongitude(g);
				std.setStdSiteUrl(stdsiteurl);
				std.setStdCreatedBy(creby);
				std.setStdCreatedOn(credate);
				std.setStdModifiedBy(modby);
				std.setStdModifiedOn(moddate);
				std.setStdMailId(mail);
				std.setStdPhoneNo(phone);
				std.setStdDescription(desc);
				
				std.setStdStatus(status);
				std.setStdType(type);
				std.setStdPamAuthId(pamAuthId);
				std.setStdPamAuthToken(pamAuthToken);
				std.setStdPoints(points);
				std.setStdTimeSpent(timeSpent);
				
				std.setStdStreamDescription(streamDesc);
				
				studioRepository.saveAndFlush(std);
				result.put("STD_ID", std.getStdId());
			} else {
				result.put("RESULT", TurnOutConstant.FAILED);
			}
		
		return result;		
	}	
	/**
	 * This method used to check whether the studio exist in database.It will return true or false message.
	 * 
	 * @param stdid an unique id of studio element.
	 * @return boolean true or false.
	 */
	@Override
	public boolean isStudioExist(int stdid) {		
		Studio studio = studioRepository.findOne(stdid);		
		if(studio!= null)
			return true;
			else
		return false;
	}
	
	/**
	 * It will search and get list of all studios from database when it found the matched string.
	 * 
	 * @param search
	 * @return Returns JSONArray contains information about studios.
	 */
	public JSONArray doStudioSearch(String stdSearch) {
		LOGGER.debug("implementation section "+stdSearch);
		//List<Studio> studioList = studioRepository.findBystdNameIgnoreCaseContaining(stdSearch);
		List<Studio> studioList = studioRepository.searchByText("%" +stdSearch+ "%");
		LOGGER.debug("studio search list "+studioList);
		JSONArray stdjsonArr = new JSONArray();
		JSONObject obj;
		for(Studio std:studioList)
		{
			obj = new JSONObject();
			obj.put("STD_ID", std.getStdId());
			obj.put("STD_NAME", std.getStdName());
			obj.put("STD_TRADING_NAME", std.getStdTradingName());
			obj.put("STD_ADDRESS_LINE1", std.getStdAddressLine1());
			obj.put("STD_ADDRESS_LINE2", std.getStdAddressLine2());
			obj.put("STD_ADDRESS_LINE3", std.getStdAddressLine3());
			obj.put("STD_TOWN", std.getStdTown());
			obj.put("STD_COUNTY", std.getStdCounty());
			obj.put("STD_COUNTRY", std.getStdCountry());
			obj.put("STD_POST_CODE", std.getStdPostCode());
			obj.put("STD_IMAGE_URL", std.getStdImageUrl());
			obj.put("STD_BILLING_ADDRS_LINE1", std.getStdBillingAddrsLine1());
			obj.put("STD_BILLING_ADDRS_LINE2", std.getStdBillingAddrsLine2());
			obj.put("STD_BILLING_ADDRS_LINE3", std.getStdBillingAddrsLine3());
			obj.put("STD_BILLING_ADDRS_TOWN", std.getStdBillingAddrsTown());
			obj.put("STD_BILLING_ADDRS_COUNTY", std.getStdBillingAddrsCounty());
			obj.put("STD_BILLING_ADDRS_COUNTRY", std.getStdBillingAddrsCountry());
			obj.put("STD_BILLING_ADDRS_POSTCODE", std.getStdBillingAddrsPostcode());
			obj.put("STD_COMPANY_HOUSE_NUMBER", std.getStdCompanyHouseNumber());
			obj.put("STD_VAT_NUMBER", std.getStdVatNumber());
			obj.put("STD_COMPANY_LOGO", std.getStdCompanyLogo());
			obj.put("STD_LATITUDE",std.getStdLatitude() );
			obj.put("STD_LONGITUDE",std.getStdLongitude() );
			obj.put("STD_DESCRIPTION", std.getStdDescription());	
			obj.put("STD_PHONENO", std.getStdPhoneNo());	
			obj.put("STD_MAILID", std.getStdMailId());
			
			obj.put("STD_STREAM_DESC", std.getStdStreamDescription());
			stdjsonArr.add(obj);					
		}		
		return stdjsonArr;
	}	

}
