package com.turnout.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyRepository;

/**
 * PartyServiceImpl is class that contains collection of methods that can be accessed for manipulating parties. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to add or edit an ineligible object throws an CurrentPasswordNotMatchedException, typically PartyNotFoundException or PartyExistException.
 * 
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
@Service
@Validated
public class PartyServiceImpl implements PartyService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyServiceImpl.class);
	

	private final PartyRepository partyRepository;
	private final CustomPartyRepository customPartyRepository;
	private final PartyAuthMechRepository partyAuthMechRepository;
	private final InstagramPostReceiver instagramPostReceiver;
	private final InfoRepository infoRepository;
	
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param partyRepository         An Object of partyRepository as an injectable member.
	 * @param customPartyRepository   An Object of customPartyRepository as an injectable member.
	 * @param partyAuthMechRepository An Object of partyAuthMechRepository as an injectable member.
	 * @param instagramPostReceiver   An Object of instagramPostReceiver as an injectable member.
	 * @param infoRepository          An Object of infoRepository as an injectable member.
	 * @see partyRepository
	 * @see customPartyRepository
	 * @see partyAuthMechRepository
	 * @see instagramPostReceiver
	 * @see infoRepository 
	 */
	@Autowired
	public PartyServiceImpl(final PartyRepository partyRepository,
			final CustomPartyRepository customPartyRepository,
			final PartyAuthMechRepository partyAuthMechRepository,
			final InstagramPostReceiver instagramPostReceiver,
			final InfoRepository infoRepository) {
		this.partyRepository = partyRepository;
		this.customPartyRepository = customPartyRepository;
		this.partyAuthMechRepository = partyAuthMechRepository;
		this.instagramPostReceiver = instagramPostReceiver;
		this.infoRepository = infoRepository;
	}
	/**
	 * It is used to check whether the given party id exists in database or not. If already exist return false otherwise return true.
	 * 
	 * @param ptyid an primary key of party element.
	 * @return It will return boolean message.
	 */
	
	public boolean isParty(int ptyid) {
		Party party=partyRepository.findOne(ptyid);
		if(party == null){
			return false;
		}
		return true;
	}

	/**
	 * This method used to get party details from database based on party id.
	 * 
	 * @param partyid A integer value holds party id.
	 * @return returns JSONObject contains party details.
	 * @throws PartyNotFoundException  Exception thrown when entered party is not found.
	 */
	@Transactional
	public JSONObject retrievePartyDetailsById(int partyid ) throws PartyNotFoundException 
	{
		Party party = partyRepository.findOne(partyid);		
		
		if (party == null ) {
			throw new PartyNotFoundException("No party Found");
		}
		LOGGER.debug("Retrieving  party details for paty id "+partyid);
		PartyAuthMech pauthmech = partyAuthMechRepository.findByPtyId(partyid);
		JSONObject 	ptyobj = new JSONObject();
		
		ptyobj.put("PTY_ID", party.getPtyId());
		ptyobj.put("PTY_NAME", party.getPtyName());
		ptyobj.put("PTY_LAST_NAME", party.getPtyLastName());
		ptyobj.put("PTY_ADDRESS_LINE_1", party.getPtyAddressLine1());
		ptyobj.put("PTY_ADDRESS_LINE_2", party.getPtyAddressLine2());
		ptyobj.put("PTY_ADDRESS_LINE_3", party.getPtyAddressLine3());
		ptyobj.put("PTY_TOWN", party.getPtyTown());
		ptyobj.put("PTY_POST_CODE", party.getPtyPostCode());
		ptyobj.put("PTY_COUNTRY", party.getPtyCountry());;
		ptyobj.put("PTY_TEL", party.getPtyTel());
		ptyobj.put("PTY_MOBILE", party.getPtyMobile());
		ptyobj.put("PTY_EMAIL", party.getPtyEmail());
		ptyobj.put("PTY_PHOTO", party.getPtyPhoto());
		ptyobj.put("PTY_GAINED_POINTS", party.getPtyGainedPoints());		
		ptyobj.put("PTY_DESCRIPTION", party.getPtyDescription());
		ptyobj.put("PTY_SHOWUP_PREFERENCE", party.getPtyShowupPreference());
		ptyobj.put("PTY_ACTIVITY_PREFERENCE", party.getPtyActivityPreference());
		ptyobj.put("LOGIN",pauthmech.getAmhId().toString());		
		ptyobj.put("PTY_STATUS", party.getPtyStatus());
		ptyobj.put("PTY_PWD", pauthmech.getPamAuthToken());	
		

		if(pauthmech.getAmhId().equalsIgnoreCase("IN"))
		{
			ptyobj.put("PAM_AUTH_ID",pauthmech.getPamAuthId().toString());
			
		}		
		
		LOGGER.debug("Retrieving  party details is done and details is "+ptyobj);
		LOGGER.debug("Retrieving  party "+party.getPtyId());		
		return ptyobj;	
		
	}
	/**
	 * This method used to update the users information into database. It will return updated party id.
	 * 
	 * @param ptyId the primary key of party element.
	 * @param ptyemail party email address.
	 * @param ptyPhoto party profile photo.
	 * @param ptyDesc Party short description.
	 * @param ptyShowupPreference Based on this setting notification will send to other my friends.
	 * @param ptyActivityPreference Party can set activity preference, Based on this preference party activity shown in the stream page.
	 * @param login
	 * @param newPwd Party can able to change the password using this new password.
	 * @param curPwd Current login password.
	 * @param ptyname Party first name.
	 * @param ptyLastname Party last name.
	 * @return  Returns JSONObject that has a value of party id.
	 * @throws PartyNotFoundException Exception thrown when entered party is not found.
	 * @throws CurrentPasswordNotMatchedException Exception thrown when current pass word not match with user's entered password.
	 * @throws PartyExistException Exception thrown when entered party is already exist in database.
	 */
	@Override
	@Transactional
	public JSONObject updatePartyDetailsById(int ptyId,String ptyemail,String ptyPhoto,String ptyDesc,
			String ptyShowupPreference,String ptyActivityPreference,String login,String newPwd,String curPwd,
			String ptyname,String ptyLastname) throws PartyNotFoundException, CurrentPasswordNotMatchedException, PartyExistException {
	
		JSONObject result = new JSONObject();
		LOGGER.debug("updating party details of party id "+ptyId );		
		Party party = partyRepository.findOne(ptyId);
		if(login.equals("SL")) {
			//LOGGER.debug("CURPWD:::"+curPwd);		
			System.out.println("CURPWD:::"+curPwd);
			if(!curPwd.isEmpty() && !newPwd.isEmpty()) {				
				PartyAuthMech pauthmech = partyAuthMechRepository.findByPtyId(ptyId);
				if(pauthmech.getPamAuthToken().equals(curPwd)) {
					System.out.println("newPwd:::"+newPwd);
					pauthmech.setPamAuthToken(newPwd);
					partyAuthMechRepository.save(pauthmech);
				} else {
					System.out.println("newPwd:::WRONG PASSWORD");
					Info msgObj = infoRepository.findByType("pwd-mismatch");					
					throw new CurrentPasswordNotMatchedException(msgObj.getTitle()+"~"+msgObj.getDescription()+"~UpdatePartydetails");				
				}
			}
		}		
		
		System.out.println(ptyemail+" "+ptyPhoto+" "+ptyname+" "+ptyLastname+" "+ptyDesc+" "+ptyShowupPreference+" "+ptyActivityPreference);
		party.setPtyEmail(ptyemail);
		party.setPtyPhoto(ptyPhoto);				
		party.setPtyName(ptyname);
		party.setPtyLastName(ptyLastname);
		party.setPtyDescription(ptyDesc);
		party.setPtyShowupPreference(ptyShowupPreference);
		party.setPtyActivityPreference(ptyActivityPreference);
		partyRepository.saveAndFlush(party);
		//LOGGER.debug("updating party details is done");		
		System.out.println("updating party details is done");
		result.put("PTY_ID", party.getPtyId());
		return result;
	}
	/**
	 * This method used to get party profile,interested event, accepted challenge and friends lists details from database based on party id passing.
	 * 
	 * @param partyid an integer value holds party id.
	 * @return returns JSONObject contains party details.
	 * @throws UnirestException Exception thrown when the http connection fails.
	 */
	@Override
	public JSONObject getProfileById(int partyid) throws UnirestException {		
		JSONObject profile = new JSONObject();
		Map<String, Object>  mapprofile  = customPartyRepository.getProfileById(partyid);
		
		profile.put("PTY_ID", mapprofile.get("PTY_ID"));
		profile.put("PTY_NAME", mapprofile.get("PTY_NAME").toString());	
		profile.put("PTY_LAST_NAME", mapprofile.get("PTY_LAST_NAME").toString());
		profile.put("PTY_EMAIL", mapprofile.get("PTY_EMAIL").toString());		
		profile.put("PTY_DESCRIPTION", mapprofile.get("PTY_DESCRIPTION").toString());
		profile.put("PTY_GAINED_POINTS", mapprofile.get("PTY_GAINED_POINTS"));		
		profile.put("PTY_STATUS", mapprofile.get("PTY_STATUS"));		
		String imageName = mapprofile.get("PTY_PHOTO").toString();
		profile.put("PTY_PHOTO", imageName);
		
		profile.put("CNTGYM", mapprofile.get("CNTGYM"));
		profile.put("CNTBADGE", mapprofile.get("CNTBADGE"));		
		profile.put("CNTSHOWUPS", mapprofile.get("CNTSHOWUPS"));
		profile.put("EVENTS", getPartyEvents(partyid));
		profile.put("CHALLENGES", getPartyChallenges(partyid));
		profile.put("FRIENDS_LIST", getFriendsList(partyid));		
		
		PartyAuthMech pauthmech = partyAuthMechRepository.findByPtyId(partyid);
		if(pauthmech.getAmhId().equalsIgnoreCase("IN"))
		{
			if((pauthmech.getPamAuthId() != null && !pauthmech.getPamAuthId().isEmpty()) && (pauthmech.getPamAuthToken() != null && !pauthmech.getPamAuthToken().isEmpty())) {
				profile.put("INSTAGRAMPOST", instagramPostReceiver.getInstagramPost(pauthmech.getPamAuthId(),pauthmech.getPamAuthToken()));
			} else {
				profile.put("INSTAGRAMPOST", "");
			}
		}
		return profile;
	}

	/**
	 * It is used to list all the friends based on party id passing.
	 * 
	 * @param partyid A integer value holds party id.
	 * @return Returns JSONObject contains friends details.
	 */
	private JSONArray getFriendsList(int partyid) {
		List friendsList = customPartyRepository.getFriendsList(partyid);
		JSONArray friendsjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(friendsList != null)
		{
			for(Iterator itr=friendsList.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
				object.put(entry.getKey(), entry.getValue());	
				}
				friendsjson.add(object);
			}
		}	
		return friendsjson;
	}

	/**
	 * It is used to list all my accepted challenges list.
	 * 
	 * @param partyid A integer value holds party id.
	 * @return Returns JSONArray contains challenges details.
	 */
	private JSONArray getPartyChallenges(int partyid) {
		List partyChallenges = customPartyRepository.getPartyChallenges(partyid);
		JSONArray activityjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(partyChallenges != null)
		{
			for(Iterator itr=partyChallenges.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
				object.put(entry.getKey(), entry.getValue());	
				}
				activityjson.add(object);
			}
		}	
		
		return activityjson;
	}

	/**
	 * It is used to list all my interested events list.
	 * 
	 * @param partyid partyid A integer value holds party id.
	 * @return Returns JSONArray contains interested events list.
	 */
	private JSONArray getPartyEvents(int partyid) {
		List partyEvents = customPartyRepository.getPartyEvents(partyid);
		JSONArray activityjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(partyEvents != null)
		{
			for(Iterator itr=partyEvents.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
				object.put(entry.getKey(), entry.getValue());	
				}
				activityjson.add(object);
			}
		}
		return activityjson;
	}
	
	/**
	 * This function used to change the status of party.
	 * 
	 * @param ptyId the primary key of party element.
	 * @param ptyStatus party status (Active / Inactive).
	 * @return it will return updated party id.
	 */
	@Override
	@Transactional
	public JSONObject changeUserStatus(int ptyId, String ptyStatus) throws PartyNotFoundException {
			
		JSONObject result = new JSONObject();		    
		LOGGER.debug("changing party status of party id "+ptyId );
		Party party = partyRepository.findOne(ptyId);
		party.setPtyStatus(ptyStatus);
		
		partyRepository.saveAndFlush(party);
		LOGGER.debug("changing party status is done");
		result.put("PTY_ID", party.getPtyId());
		return result;
		
	}	
	/**
	 * This method used to get all the party from database and helps to search the party based on passed string.
	 * The string may have the value of first name,last name,email,mobile no.
	 * 
	 * @param search a string holds a text that has to be searched.
	 * @return returns JSONArray contains all the results after searching is done.
	 */
	@Transactional
	public JSONArray partySearch(String search) {

		JSONArray listarr = new JSONArray();
		JSONObject listobj;
		Map<String, Object> record;
		
		List rs = customPartyRepository.partySearch(search);
		
		if( rs.size() != 0) {
			for (Iterator itr = rs.iterator(); itr.hasNext();) {
				record = (Map) itr.next();
				listobj = new JSONObject();
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					listobj.put(entry.getKey(), entry.getValue());
				}
				listarr.add(listobj);		
		    }
		} else {
			listobj = new JSONObject();
			listobj.put("Records", TurnOutConstant.NOT_EXIST);
			listarr.add(listobj);
		}
		return listarr;
	}	
}