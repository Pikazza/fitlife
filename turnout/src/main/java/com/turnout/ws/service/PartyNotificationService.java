package com.turnout.ws.service;

import java.util.Date;

import javax.mail.MessagingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;

/**
 * PartyNotificationService is an interface that contains collection of methods that can be accessed for manipulating applications notification settings. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in PartyNotificationServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface PartyNotificationService {
	/**
	 * This method used to save a party notification details into database.It will return a notification id of just updated notification.
	 * 
	 * @param ptyId an unique id of party.
	 * @param othLikes other likes notification enable or disable.
	 * @param othCmts other comments notification enable or disable.
	 * @param othIntrstEvnt other event interested notification enable or disable.
	 * @param OthAcptChlng other challenge accepting notification enable or disable.
	 * @param pointsCrdt points credited notification enable or disable.
	 * @param EvntRemainder event remainder notification enable or disable.
	 * @param readyReedem redeem reward notification enable or disable.
	 * @param modifyDate party notification setting changed date and time.
	 * @return notification updated id.
	 */
	public JSONObject addPartyNotification(int ptyId, String othLikes,String othCmts,String othIntrstEvnt,String OthAcptChlng,String pointsCrdt,String EvntRemainder,String readyReedem,Date modifyDate);
	
	/**
	 * This method used to get party notification from database based on party id.
	 * 
	 * @param ptyId an unique id of party
	 * @return returns JSONObject contains party notification details.
	 */
	public JSONObject getPartyNotification(int ptyId);
}