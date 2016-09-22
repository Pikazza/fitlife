package com.turnout.ws.controller;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.turnout.ws.domain.Party;
import com.turnout.ws.helper.EncryptDecrypt;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.SchedulerNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.PartyRepository;



@Component
@Path("/home")
public class Home {
	
	private final SchedulerNotification schedulerNotification;
	private final SmtpMailSender smtpMailSender;	
	private final PartyRepository partyRepository;
	private final EncryptDecrypt encryptDecrypt;
	
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param schedulerNotification An Object of schedulerNotification as an injectable member.
	 * @param smtpMailSender An Object of smtpMailSender as an injectable member.
	 * @param partyRepository An Object of partyRepository as an injectable member.
	 * @param encryptDecrypt An Object of encryptDecrypt as an injectable member.
	 * @see SchedulerNotification
	 * @see smtpMailSender
	 * @see partyRepository
	 * @see encryptDecrypt
	 */
	@Autowired	
	public Home(final SchedulerNotification schedulerNotification,
			final SmtpMailSender smtpMailSender,final PartyRepository partyRepository,final EncryptDecrypt encryptDecrypt) {
		
		// TODO Auto-generated constructor stub
		this.schedulerNotification = schedulerNotification;
		this.smtpMailSender = smtpMailSender;
		this.partyRepository = partyRepository;
		this.encryptDecrypt = encryptDecrypt;

	}
	/**
	 * This method used to run scheduler manually.
	 * 
	 * @return Returns JSONObject has a value of success message.
	 */
	@RolesAllowed("ADMIN")
    @GET
	@Produces("application/json")
    @Consumes("application/json")
	public JSONObject getWelcomemessage()
	{
    	JSONObject res = new JSONObject();
		//String test = propsControl.getCheckedIn();
	/*	schedulerNotification.evntRemainder();
		schedulerNotification.reedemEligible();
		Party ptyios = partyRepository.findOne(2);		
		smtpMailSender.sendPushMsgToIOS(ptyios.getPtyDeviceToken());
		Party ptyand = partyRepository.findOne(3);
		smtpMailSender.sendPushMsgToAndroid(ptyand.getPtyDeviceToken());*/
    	schedulerNotification.evntRemainder();
    	res.put("test", "welcome to Fiternity");
		return res;
	}
	
	/**
	 * This method used to check the web service running or not.
	 * 
	 * @return Returns a string has a value of welcome message.
	 */
    
    @GET
    @RolesAllowed("ADMIN")
    @Path("/test")
	@Produces("text/html")  
	public String gethello()
	{	
    	return "test welcome to Fiternity";
	}
}
