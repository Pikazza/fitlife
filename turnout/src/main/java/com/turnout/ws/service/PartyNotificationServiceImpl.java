package com.turnout.ws.service;



import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.hibernate.jpa.criteria.expression.function.AggregationFunction.COUNT;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;

import ch.qos.logback.core.net.SyslogOutputStream;

/**
 * PartyNotificationServiceImpl is class that contains collection of methods that can be accessed for manipulating applications notification settings. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
@Validated
public class PartyNotificationServiceImpl implements PartyNotificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyNotificationServiceImpl.class);
	
	private final PartyNotificationRepository partyNotificationRepository;
	
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param partyNotificationRepository An Object of partyNotificationRepository as an injectable member.
	 * @see partyNotificationRepository.
	 */
	@Autowired
	public PartyNotificationServiceImpl(final PartyNotificationRepository partyNotificationRepository) {
		this.partyNotificationRepository = partyNotificationRepository;
	}
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
	@Transactional
	public JSONObject addPartyNotification(int ptyId, String othLikes,String othCmts,String othIntrstEvnt,String OthAcptChlng,String pointsCrdt,String EvntRemainder,String readyReedem,Date modifyDate) {
		JSONObject jsonResult = new JSONObject();
		PartyNotification pty = partyNotificationRepository.findByNotifyPtyId(ptyId);
		PartyNotification party;
		if(pty == null) {
			party = new PartyNotification();
		} else {
			party = pty;
		}
		party.setNotifyPtyId(ptyId);
		party.setNotifyOthLikes(othLikes);
		party.setNotifyOthCmts(othCmts);
		party.setNotifyOthIntrstEvnt(othIntrstEvnt);
		party.setNotifyOthAcptChlng(OthAcptChlng);
		party.setNotifyPersonalPointsCrdt(pointsCrdt);
		party.setNotifyPersonalEvntRemainder(EvntRemainder);
		party.setNotifyPersonalReadyReedem(readyReedem);
		party.setNotifyModifyDate(modifyDate);
		partyNotificationRepository.saveAndFlush(party);
		if(party.getNotifyId() != 0) {
			jsonResult.put("RESULT",party.getNotifyId());
		} else {
			jsonResult.put("RESULT",TurnOutConstant.FAILED);
		}
		return jsonResult;
	}
	/**
	 * This method used to get party notification from database based on party id.
	 * 
	 * @param ptyId A integer value holds party id.
	 * @return returns JSONObject contains party notification details.
	 */
	@Transactional
	public JSONObject getPartyNotification(int ptyId) {
		JSONObject jsonResult = new JSONObject();
		PartyNotification party = partyNotificationRepository.findByNotifyPtyId(ptyId);
		jsonResult.put("NOTIFY_PTY_ID", party.getNotifyPtyId());
		jsonResult.put("NOTIFY_OTH_LIKES", party.getNotifyOthLikes());		
		jsonResult.put("NOTIFY_OTH_CMTS", party.getNotifyOthCmts());
		jsonResult.put("NOTIFY_OTH_INTRST_EVNT", party.getNotifyOthIntrstEvnt());
		jsonResult.put("NOTIFY_OTH_ACPT_CHLNG", party.getNotifyOthAcptChlng());
		jsonResult.put("NOTIFY_PERSONAL_POINTS_CRDT", party.getNotifyPersonalPointsCrdt());
		jsonResult.put("NOTIFY_PERSONAL_EVNT_REMAINDER", party.getNotifyPersonalEvntRemainder());
		jsonResult.put("NOTIFY_PERSONAL_READY_REEDEM", party.getNotifyPersonalReadyReedem());
		return jsonResult;
	}	

}