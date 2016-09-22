package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;

/**
 * This is a testing class for PartyNotificationServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking PartyNotificationServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyNotificationServiceImplTest {
	
	@Mock
	private PartyNotificationRepository partyNotificationRepository;
	

	private PartyNotificationServiceImpl partyNotificationServiceImpl;
	
	/**
	 * This method to be run before the Test method.So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		partyNotificationServiceImpl = new PartyNotificationServiceImpl(partyNotificationRepository);
	}
	
	/**
	 * This test case will test addPartyNotification method.
	 * It will return a notification id of just updated notification when party notification details saved into database.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldAddPartyNotification() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYY");
		PartyNotification pn = new PartyNotification(); //mock(PartyNotification.class);
		pn.setNotifyId(10);
		pn.setNotifyOthLikes("Y");
		pn.setNotifyOthCmts("Y");
		pn.setNotifyOthIntrstEvnt("Y");
		pn.setNotifyOthAcptChlng("Y");
		pn.setNotifyPersonalEvntRemainder("Y");
		pn.setNotifyPersonalPointsCrdt("Y");
		pn.setNotifyPersonalReadyReedem("Y");
		pn.setNotifyModifyDate(dateFormat.parse("27/04/2016 11:50:00"));
		
		when(partyNotificationRepository.findByNotifyPtyId(10)).thenReturn(pn);
		when(partyNotificationRepository.saveAndFlush(pn)).thenReturn(pn);
		JSONObject res = partyNotificationServiceImpl.addPartyNotification(10, "Y", "Y", "Y", "Y", "Y", "Y", "Y", dateFormat.parse("27/04/2016 11:50:00"));
		System.out.println("SAVE NOTIFICATION "+res);
		assertNotNull(res);
	}
	
	/**
	 * /**
	 * This test case will test addPartyNotification method.
	 * this method will get party's all notification settings from database when based on given party id.
	 * 
	 */
	@Test
	public void testShouldGetPartyNotification() {
		PartyNotification pn = new PartyNotification(); //mock(PartyNotification.class);
		pn.setNotifyId(10);
		pn.setNotifyOthLikes("Y");
		pn.setNotifyOthCmts("Y");
		pn.setNotifyOthIntrstEvnt("Y");
		pn.setNotifyOthAcptChlng("Y");
		pn.setNotifyPersonalEvntRemainder("Y");
		pn.setNotifyPersonalPointsCrdt("Y");
		pn.setNotifyPersonalReadyReedem("Y");
		when(partyNotificationRepository.findByNotifyPtyId(10)).thenReturn(pn);
		JSONObject res = partyNotificationServiceImpl.getPartyNotification(10);
		System.out.println("GET PARTY NOTIFICATION "+res);
		assertNotNull(res);
	}
	

}
