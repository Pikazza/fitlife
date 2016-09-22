package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.OrderBy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.format.DataFormatDetector;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.StudioActivityType;
import com.turnout.ws.domain.StudioPartyActivity;
import com.turnout.ws.domain.StudiosActivity;
import com.turnout.ws.domain.Voucher;
import com.turnout.ws.domain.BeaconMaster;

import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.NoSufficientGainedPointsException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.repository.BeaconMasterRepository;
import com.turnout.ws.repository.CustomStudioPartyActivityRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.StudioActivityRepository;
import com.turnout.ws.repository.StudioPartyActivityRepository;
import com.turnout.ws.repository.StudioRepository;
/**
 * This is a testing class for StudioPartyActivityServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking StudioPartyActivityServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class StudioPartyActivityServiceImplTest {
	
	@Mock
	private StudioPartyActivityRepository studioPartyActivityRepository;
	
	@Mock
	private CustomStudioPartyActivityRepository customStudioPartyActivityRepository;
	
	@Mock
	private StudioRepository studioRepository;
	
	@Mock
	private PartyRepository partyRepository;
	
	@Mock
	private BeaconMasterRepository beaconMasterRepository;
	
	@Mock
	private PushNotification pushNotification;
	
	@Mock
	private StudioActivityRepository studioActivityRepository;
	
	@Mock
	private InfoRepository infoRepository;
	
	@Mock
	private PartyNotificationRepository partyNotificationRepository;

	
	private StudioPartyActivityServiceImpl studioPartyActivityServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		studioPartyActivityServiceImpl = new StudioPartyActivityServiceImpl(studioPartyActivityRepository,customStudioPartyActivityRepository,studioRepository,partyRepository,beaconMasterRepository,pushNotification,studioActivityRepository,infoRepository,partyNotificationRepository);
	}
	/**
	 * This test case should test addInterestedPartyToEvent method.
	 * This method should add a party into a interested even list.
	 * So he can get latest notification about that event.
	 */
	@Test	
	public void testShouldAddInterestedPartyToEvent() {		
		StudioPartyActivity stdPartyActivity = new StudioPartyActivity(); //mock(StudioPartyActivity.class);
		Party pty = new Party(); //mock(Party.class);
		Studio std = new Studio(); //mock(Studio.class);

		pty.setPtyId(10);
		std.setStdId(10);
		
		stdPartyActivity.setParty(pty);
		stdPartyActivity.setStaId(10);
		stdPartyActivity.setStudio(std);
		Date dat = new Date();
		Calendar cal = Calendar.getInstance();
		stdPartyActivity.setChechinTime(cal.getTime());
		stdPartyActivity.setPtaStaStatus("ACCEPTED");
	
		Map<String, Object> mapObj;
		mapObj = new HashMap<String, Object>();
		mapObj.put("TOTAL", 0);
		
		when(customStudioPartyActivityRepository.getInterested(10, 10, 10, "ACCEPTED")).thenReturn(mapObj);
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(studioRepository.findOne(10)).thenReturn(std);
		when(studioPartyActivityRepository.saveAndFlush(stdPartyActivity)).thenReturn(stdPartyActivity);
		JSONObject res = studioPartyActivityServiceImpl.addInterestedPartyToEvent(10, 10, 10, "ACCEPTED");
		System.out.println("Add Interested to party event "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case should test myProfile method.
	 * This method should return total points the party has earned so far. 
	 */
	@Test
	public void testShouldMyProfile() {
		Party pty = new Party();
		List lstObj = new ArrayList();		
		Map<String, Object> mapObj;
		
		mapObj = new HashMap<String, Object>();
		pty.setPtyId(10);
		pty.setPtyName("Uthirapathi");
		pty.setPtyPhoto("");
		pty.setPtyGainedPoints(0);
		
		mapObj.put("STD_NAME", "HeartcoreFitness");
		mapObj.put("STD_ID", 1);
		mapObj.put("STD_COMPANY_LOGO", "");
		mapObj.put("ID",10);
		mapObj.put("POINTS", 50);
		
		lstObj.add(mapObj);
		
		when(customStudioPartyActivityRepository.myProfile(10)).thenReturn(lstObj);
		when(partyRepository.findOne(10)).thenReturn(pty);
		JSONObject res = studioPartyActivityServiceImpl.myProfile(10);
		System.out.println("My profile "+res);
		assertNotNull(res);		
	}
	
	/**
	 * This test case should test myChallenge method.
	 * This method should return all of accepted challenge listed here based on given party id.
	 */
	@Test
	public void testShouldMyChallenge() {
		List lstObj = new ArrayList();		
		Map<String, Object> mapObj;	
		mapObj = new HashMap<String, Object>();
		
		mapObj.put("STD_NAME", "HeartcoreFitness");
		mapObj.put("STD_ID", 1);
		mapObj.put("STD_COMPANY_LOGO", "");
		mapObj.put("ID",10);
		mapObj.put("STA_IMAGE_URL", 50);
		mapObj.put("STA_NO_OF_DAYS", "");
		mapObj.put("ATTEND", 0);
		mapObj.put("STA_START_DATE", "2016-05-02");
		mapObj.put("ACCEPTED", 2);
		mapObj.put("STA_EXPIRY_DATE", "2016-05-02");
		mapObj.put("STA_END_DATE", "2016-05-02");
		mapObj.put("STA_NAME", "Winter Challenge");
		mapObj.put("STA_SHORT_DESCRIPTION", "Attend any 16 Classes in February ");
		lstObj.add(mapObj);	
		
		when(customStudioPartyActivityRepository.myChallenge(10)).thenReturn(lstObj);
		JSONArray res = studioPartyActivityServiceImpl.myChallenge(10);
		System.out.println("My Challenges "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case should test myBadges method.
	 * It will return a list of badges a party has already won.
	 */
	@Test
	public void testShouldmyBadges() {
		List lstObj = new ArrayList();		
		Map<String, Object> mapObj;	
		mapObj = new HashMap<String, Object>();
		
		mapObj.put("STD_NAME", "HeartcoreFitness");
		mapObj.put("STD_ID", 1);
		mapObj.put("STA_STD_ID", "2");
		mapObj.put("PTY_STA_ID",10);
		mapObj.put("STA_ID", 50);
		mapObj.put("STA_NAME", "Winter Challenge");
		
		lstObj.add(mapObj);	
		
		when(customStudioPartyActivityRepository.myBadges(10)).thenReturn(lstObj);
		JSONArray res = studioPartyActivityServiceImpl.myBadges(10);
		System.out.println("My Badges "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case should test participantsSearch method.
	 * It should return one or more participants who are actively accepted or attended given challenge based on their name.
	 * 
	 */
	@Test
	public void testShouldparticipantsSearch() {
		List lstObj = new ArrayList();		
		Map<String, Object> mapObj;	
		mapObj = new HashMap<String, Object>();
		
		mapObj.put("STD_NAME", "HeartcoreFitness");
		mapObj.put("STD_ID", 1);
		mapObj.put("STA_STD_ID", "2");
		mapObj.put("PTY_STA_ID",10);
		mapObj.put("STA_ID", 50);
		mapObj.put("STA_NAME", "Winter Challenge");
		
		lstObj.add(mapObj);	
		
		when(customStudioPartyActivityRepository.participantsSearch("ALL",10)).thenReturn(lstObj);
		JSONArray res = studioPartyActivityServiceImpl.participantsSearch("ALL",10);
		System.out.println("Participant search "+res);
		assertNotNull(res);
	}	

	/**
	 * This test case should test selectWinner method.
	 * This function should select winners from list of participants who are all finished their challenge.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldSelectWinner() throws ParseException {
		StudioPartyActivity insparty = new StudioPartyActivity(); //mock(StudioPartyActivity.class);
		Party pty = new Party(); //mock(Party.class);
		Studio std = new Studio(); //mock(Studio.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		insparty.setStaId(10);
		insparty.setParty(pty);
		insparty.setStudio(std);
		insparty.setPtaStaStatus("WONBADGE");
		insparty.setChechinTime(dateFormat.parse("13/04/2016 02:39:00"));
		insparty.setGainedTaskBadge("gained task badge");
		
		insparty.setBconId("0");
		insparty.setBconRange(0);
		
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(studioRepository.findOne(10)).thenReturn(std);
		when(studioPartyActivityRepository.saveAndFlush(insparty)).thenReturn(insparty);
		JSONObject res = studioPartyActivityServiceImpl.selectWinner(10, 10, 10, dateFormat.parse("13/04/2016 02:39:00"), "gained task badge");
		System.out.println("Select Winner "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case should test userCheckin method.
	 * This function should register parties entry when party check in any studio.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldUserCheckin() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		List bmlist = new ArrayList();
		 BeaconMaster bn = new BeaconMaster();
		 Party pty = new Party(); //mock(Party.class);
		 Studio std = new Studio(); //mock(Studio.class);
		 
		 bn.setBconId("B0005134");
		 
		 StudioPartyActivity spa = new StudioPartyActivity(); //mock(StudioPartyActivity.class);
		 
		 spa.setStaId(10);
		 spa.setParty(pty);
		 spa.setStudio(std);
		 spa.setPtaStaStatus("ATTEND");
		 spa.setBconId("B0005134");
		 spa.setBconRange(1);
		 spa.setChechinTime(dateFormat.parse("30/04/2016 16:30:30"));
		 
		Sort srt = new Sort(Sort.Direction.DESC, "bconDetectType");
		 
		when(studioPartyActivityRepository.saveAndFlush(spa)).thenReturn(spa);
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(studioRepository.findOne(10)).thenReturn(std);
		
		when(beaconMasterRepository.findByBconId("B0005134", srt)).thenReturn(bmlist);
		
		JSONObject res = studioPartyActivityServiceImpl.userCheckin("B0005134", 1, 10, dateFormat.parse("30/04/2016 16:30:30"));
		System.out.println("PARTY CHECK IN "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case should test userCheckin method.
	 * This function should register parties entry when party check out any studio.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldUserCheckout() throws ParseException {
		StudioPartyActivity spa = new StudioPartyActivity(); //mock(StudioPartyActivity.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		 BeaconMaster bn = new BeaconMaster();
		 Party pty = new Party(); //mock(Party.class);
		 Studio std = new Studio(); //mock(Studio.class);
		 
		 spa.setStaId(10);
		 spa.setParty(pty);
		 spa.setStudio(std);
		 spa.setPtaStaStatus("ATTEND");
		 spa.setBconId("B0005134");
		 spa.setBconRange(1);
		 spa.setCheckoutTime(dateFormat.parse("30/04/2016 16:30:30"));
		 
		 when(studioPartyActivityRepository.saveAndFlush(spa)).thenReturn(spa);
		 when(studioPartyActivityRepository.findOne(10)).thenReturn(spa);
		 
		 JSONObject res = studioPartyActivityServiceImpl.userCheckout(10, dateFormat.parse("30/04/2016 16:30:30"));
			System.out.println("PARTY CHECK OUT "+res);
			assertNotNull(res);
	}
	
}
