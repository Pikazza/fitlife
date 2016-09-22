package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.fasterxml.jackson.core.format.DataFormatDetector;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.StudioActivityType;
import com.turnout.ws.domain.StudiosActivity;
import com.turnout.ws.domain.Voucher;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.NoSufficientGainedPointsException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.CustomCommentsRepository;
import com.turnout.ws.repository.CustomLikeRepository;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.CustomRewardRepository;
import com.turnout.ws.repository.CustomStudioActivityRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.LikesRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.RewardRepository;
import com.turnout.ws.repository.StudioActivityRepository;
import com.turnout.ws.repository.StudioRepository;
import com.turnout.ws.repository.VoucherPartyRepository;
import com.turnout.ws.repository.VoucherRepository;

/**
 * This is a testing class for StudioActivityServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking StudioActivityServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class StudioActivityServiceImplTest {
	
	@Mock
	private CustomStudioActivityRepository customStudioActivityRepository;
	
	@Mock
	private StudioActivityRepository studioActivityRepository;
	
	@Mock
	private CustomCommentsRepository customcmtrepository;
	
	@Mock
	private CustomLikeRepository customLikeRepository;
	
	@Mock
	private LikesRepository likesRepository;
	
	@Mock
	private StudioRepository studioRepository;
	
	private StudioActivityServiceImpl studioActivityServiceImpl;
	
	/**
     * This method to be run before the Test method.So several tests need similar objects created before they can run.
     * 
     * @throws MockitoException It throws when runtime errors happens while testing.
     */
	@Before
	public void setUp() throws MockitoException {
		studioActivityServiceImpl = new StudioActivityServiceImpl(customStudioActivityRepository,studioActivityRepository,customcmtrepository,customLikeRepository,likesRepository, studioRepository);
	}
	/**
     * This test case should test getListedActivity method. This method should get all the active events and challenges from the database.
     */
	@Test
	public void testShouldGetListedActivity() {
		List aray = new ArrayList();
		Map<String, Object> mapObj,mapObj1;
		mapObj = new HashMap<String, Object>();
		mapObj.put("STA_BADGE","");
		mapObj.put("STA_TYPE_ID",1);
		mapObj.put("STD_COMPANY_LOGO","");
		mapObj.put("STA_IMAGE_URL","");
		mapObj.put("STA_NO_OF_DAYS",0);
		mapObj.put("STA_GOAL_POINTS",0);
		mapObj.put("STA_START_DATE","2016-06-15");
		mapObj.put("STD_ID",42);
		mapObj.put("STD_NAME","Fierce Grace");
		mapObj.put("STA_END_DATE","2016-06-15");
		mapObj.put("STA_ID",40);
		mapObj.put("STA_NAME","Mindful Relaxation Workshop at Hothouse");
		mapObj.put("STA_DESCRIPTION","Mindful Relaxation Workshop at Hothouse Description");
		mapObj.put("STA_CREATED_DATE","2016-06-15");
		mapObj.put("STA_SHORT_DESCRIPTION","");
		
		mapObj1 = new HashMap<>();
		mapObj1.put("STA_BADGE","");
		mapObj1.put("STA_TYPE_ID",1);
		mapObj1.put("STD_COMPANY_LOGO","");
		mapObj1.put("STA_IMAGE_URL","");
		mapObj1.put("STA_NO_OF_DAYS",0);
		mapObj1.put("STA_GOAL_POINTS",0);
		mapObj1.put("STA_START_DATE","2016-06-24");
		mapObj1.put("STD_ID",42);
		mapObj1.put("STD_NAME","Fierce Grace");
		mapObj1.put("STA_END_DATE","2016-06-24");
		mapObj1.put("STA_ID",42);
		mapObj1.put("STA_NAME","Core Power Workshop");
		mapObj1.put("STA_DESCRIPTION","Core Power Workshop Description");
		mapObj1.put("STA_CREATED_DATE","2016-06-24");
		mapObj1.put("STA_SHORT_DESCRIPTION","");
		
		aray.add(mapObj);
		aray.add(mapObj1);
		when(customStudioActivityRepository.getListedActivity(1, 10)).thenReturn(aray);
		JSONArray res = studioActivityServiceImpl.getListedActivity(1, 10);
		System.out.println("EVENTS LISTING "+res);
		assertNotNull(res);
	}
	/**
     * This test case will test getActivityDetails method.
     * It should get all the details of activity(challenge/event) based on given activity id.
     * 
     */
	@Test
	public void testShouldGetActivityDetails() {
		Map<String, Object> mapObj;
		mapObj = new HashMap<String, Object>();
		
		mapObj.put("STA_ID", 37);
		mapObj.put("STA_NAME", "Life Amplified 60 Day Challenge");
		mapObj.put("STD_ID", 2);
		mapObj.put("STA_DESCRIPTION", "Life Amplified 60 Day Challenge Description");		
		mapObj.put("START_DATE", "2016-04-01");
		mapObj.put("START_TIME", "00:00");
		mapObj.put("END_DATE", "2016-05-31");
		mapObj.put("END_TIME", "00:00");		
		mapObj.put("STA_GOAL_POINTS", 0);
		mapObj.put("STA_TYPE_ID", 2);
		
		mapObj.put("STA_PRICE", 0);
		mapObj.put("STA_IMAGE_URL", "");		
		mapObj.put("STA_ADDRESS_LINE1", "");
		mapObj.put("STA_ADDRESS_LINE2", "");
		mapObj.put("STA_ADDRESS_LINE3", "");		
		mapObj.put("STA_TOWN", "");
		mapObj.put("STA_COUNTY", "");
		mapObj.put("STA_COUNTRY", "");
		mapObj.put("STA_POST_CODE", "");
		mapObj.put("STA_LATITUDE", 0);
		mapObj.put("STA_LONGITUDE", 0);
		mapObj.put("STA_EXTERNAL_LINK", "http://www.fiternity.co.uk");	
		mapObj.put("STA_LOCATION", "");
		mapObj.put("STD_NAME", "Xtend Barre");
		mapObj.put("STD_COMPANY_LOGO", "");
		mapObj.put("CMTS_CNT", 10);		
		mapObj.put("STA_LIKE_CNT", 10);
		mapObj.put("STA_INFLUENCER","");
		mapObj.put("INTRESTED_USER", "");
		mapObj.put("STA_EXPIRY_DATE", "2016-06-25");
		mapObj.put("STA_STATUS", "ACCEPTED");
		
		if(Integer.parseInt(mapObj.get("STA_TYPE_ID").toString()) == 1) {			
			mapObj.put("PTY_LIKED", "");
		} else {	
			mapObj.put("STA_SHORT_DESCRIPTION", mapObj.get("STA_SHORT_DESCRIPTION"));	
			mapObj.put("STA_BADGE", "");
		}	
		
		
		
		when(customStudioActivityRepository.getActivityDetails(1,"STUDIOS_ACTIVITY")).thenReturn(mapObj);
		JSONObject res = studioActivityServiceImpl.getActivityDetails(1,10);
		System.out.println("EVENT DETAIL "+res);
		assertNotNull(res);
	}
	
	/**
     * This test case will test saveStudioActivity method.
     * It should return new or existing activity id of recently changed activity from database when all of its details has been passed as parameter.
     *
     * @throws ParseException An exception occurs when you fail to parse a Object.
     */
	@Test
	public void testShouldSaveStudioActivity() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		StudioActivityType sat = new StudioActivityType();
		sat.setStaTypeActive("Y");
		sat.setStaTypeDesc("CHALLENGE");
		sat.setStaTypeId(2);
		
		StudiosActivity sa  = new StudiosActivity(); //mock(StudiosActivity.class);
		sa.setStaId(10);
		sa.setStdId(3);
		sa.setStaName("New challenge");
		sa.setStaDescription("Get up, lace up, and ShowUp at London's hott - update");
		sa.setStaStartDate(dateFormat.parse("02/02/2016"));
		sa.setStaEndDate(dateFormat.parse("02/02/2016"));
		sa.setStaCreatedDate(dateFormat.parse("02/02/2016"));
		sa.setStaModifiedDate(dateFormat.parse("02/02/2016"));
		sa.setStaUpdatedBy(0);
		sa.setStaGoalPoints(0);
		sa.setStaBadge("2");
		sa.setStudioActivityType(sat);
		sa.setStaImageUrl("");
		sa.setStaInfluencer("image ~ text % image ~text");
		sa.setStaLikeCnt(2);
		sa.setStaPrice(100);
		sa.setStaAddressLine1("");
		sa.setStaAddressLine2("");
		sa.setStaAddressLine3("");
		sa.setStaTown("");
		sa.setStaCounty("");
		sa.setStaCountry("UK");
		sa.setStaPostCode("sd23");
		sa.setStaLatitude((float) 232643.12);
		sa.setStaLongitude((float) 45345345.78);
		sa.setStaStartDate(timeformat.parse("22:30:30"));
		sa.setStaEndTime(timeformat.parse("23:30:30"));
		sa.setStaExternalLink("www.fiternity.co.uk");
		sa.setStaLocation("");
		sa.setStaShortDescription("It's 4 sessions, per week for 4 weeks. ");
		sa.setStaExpiryDate(dateFormat.parse("02/02/2016"));
		sa.setStaStatus("ACTIVE");
		sa.setStaNoOfDays(10);
		
		StudioActivityType stdType = new StudioActivityType(); //mock(StudioActivityType.class);
		sa.setStudioActivityType(stdType);
		
		when(studioActivityRepository.findOne(10)).thenReturn(sa);
		when(studioActivityRepository.save(sa)).thenReturn(sa);
		
		JSONObject res = studioActivityServiceImpl.saveStudioActivity(10,"New challenge",3, "Get up, lace up, and ShowUp at London's hott - update", "It's 4 sessions, per week for 4 weeks. ", dateFormat.parse("02/02/2016"), dateFormat.parse("02/02/2016"),dateFormat.parse("02/02/2016"), dateFormat.parse("02/02/2016"), 1, 0,"2", 2, "", "image ~ text % image ~text", 10, (double)10.00, "", "", "", "", "", "UK","sd23", (float) 232643.12, (float) 45345345.78, timeformat.parse("22:30:30"),timeformat.parse("22:30:30"), "www.fiternity.co.uk", "", dateFormat.parse("02/02/2016"), "ACTIVE", 10);
		System.out.println("SAVE STUDIO ACTIVITY "+res);
		assertNotNull(res);
	}
	/**
     * This test case will test updateLikeCount method. It should get list of challenges.
     */
	@Test
	public void testShouldUpdateLikeCount() {
		StudiosActivity sa = new StudiosActivity(); //mock(StudiosActivity.class);
		sa.setStaId(10);
		sa.setStaLikeCnt(10);
		when(studioActivityRepository.findByStaId(10)).thenReturn(sa);
		when(studioActivityRepository.saveAndFlush(sa)).thenReturn(sa);
		boolean res = studioActivityServiceImpl.updateLikeCount(10, 10);
		System.out.println("Update Like count "+res);
	}
	/**
     * This test case will test getAllChallenges method. It should get list of challenges.
     */
	@Test
	public void testShouldGetAllChallenges() {
		List aray = new ArrayList();
		Map<String, Object> mapObj,mapObj1;
		mapObj = new HashMap<String, Object>();
		mapObj.put("STA_BADGE","");
		mapObj.put("STA_TYPE_ID",1);
		mapObj.put("STD_COMPANY_LOGO","");
		mapObj.put("STA_IMAGE_URL","");
		mapObj.put("STA_NO_OF_DAYS",0);
		mapObj.put("STA_GOAL_POINTS",0);
		mapObj.put("STA_START_DATE","2016-06-15");
		mapObj.put("STD_ID",42);
		mapObj.put("STD_NAME","Fierce Grace");
		mapObj.put("STA_END_DATE","2016-06-15");
		mapObj.put("STA_ID",40);
		mapObj.put("STA_NAME","Mindful Relaxation Workshop at Hothouse");
		mapObj.put("STA_DESCRIPTION","Mindful Relaxation Workshop at Hothouse Description");
		mapObj.put("STA_CREATED_DATE","2016-06-15");
		mapObj.put("STA_SHORT_DESCRIPTION","");
		
		mapObj1 = new HashMap<>();
		mapObj1.put("STA_BADGE","");
		mapObj1.put("STA_TYPE_ID",1);
		mapObj1.put("STD_COMPANY_LOGO","");
		mapObj1.put("STA_IMAGE_URL","");
		mapObj1.put("STA_NO_OF_DAYS",0);
		mapObj1.put("STA_GOAL_POINTS",0);
		mapObj1.put("STA_START_DATE","2016-06-24");
		mapObj1.put("STD_ID",42);
		mapObj1.put("STD_NAME","Fierce Grace");
		mapObj1.put("STA_END_DATE","2016-06-24");
		mapObj1.put("STA_ID",42);
		mapObj1.put("STA_NAME","Core Power Workshop");
		mapObj1.put("STA_DESCRIPTION","Core Power Workshop Description");
		mapObj1.put("STA_CREATED_DATE","2016-06-24");
		mapObj1.put("STA_SHORT_DESCRIPTION","");
		
		aray.add(mapObj);
		aray.add(mapObj1);
		when(customStudioActivityRepository.getAllChallenges(1, 10)).thenReturn(aray);
		JSONArray res = studioActivityServiceImpl.getAllChallenges(1, 10);
		System.out.println("CHALLENGES LISTING "+res);
		assertNotNull(res);
	}
	/**
     * This test case will test getChallengeWall method. It should listout details of given challenge.
     */
	@Test
	public void testShouldGetChallengeWall() {
		Map<String, Object> mapObj;
		mapObj =  new HashMap<String, Object>();
		mapObj.put("STA_TYPE_ID", 2);
		mapObj.put("START_TIME", "00:00");
		mapObj.put("INTRESTED_USER", "");
		mapObj.put("STA_IMAGE_URL", "");
		mapObj.put("STD_COMPANY_LOGO", "");
		mapObj.put("END_DATE", "2016-06-30");
		mapObj.put("COMMENTS", "");
		mapObj.put("START_DATE", "2016-04-01");
		mapObj.put("STA_STATUS", "ACTIVE");
		mapObj.put("STD_NAME", "1Rebel");
		mapObj.put("END_TIME", "00:00");
		mapObj.put("STA_EXPIRY_DATE", "2016-06-21");
		mapObj.put("STD_ID", 4);
		mapObj.put("STA_EXTERNAL_LINK", "");
		mapObj.put("STA_ID", 5);
		mapObj.put("STA_PRICE", 10);
		mapObj.put("STA_NAME", "#RebelSleepRepeat");
		mapObj.put("STA_DESCRIPTION", "");
		mapObj.put("STA_SHORT_DESCRIPTION", "");
		
		when(customStudioActivityRepository.getChallengeWall(10, "STUDIOS_ACTIVITY")).thenReturn(mapObj);
		JSONObject res = studioActivityServiceImpl.getChallengeWall(10, 10);
		System.out.println("CHALLENGE WALL "+res);
		assertNotNull(res);
	}
	/**
     * This test case will test activitySearch method.
     * It should get all the details of activity(challenge/event) based on given search terms.
     */
	@Test
	public void testShouldActivitySearch() {
		List aray = new ArrayList();
		
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdId(10);
		std.setStdName("BARREtoned");
		
		Map<String, Object> mapObj,mapObj1;		
		mapObj =  new HashMap<String, Object>();
		mapObj.put("STA_BADGE", "");
		mapObj.put("STA_TYPE_ID", 2);
		mapObj.put("STD_COMPANY_LOGO", "");
		mapObj.put("STA_IMAGE_URL", "");
		mapObj.put("STA_GOAL_POINTS", 0);
		mapObj.put("STA_START_DATE", "2016-06-30");
		mapObj.put("STA_END_DATE", "2016-06-30");
		mapObj.put("STA_EXPIRY_DATE", "2016-06-21");
		mapObj.put("STA_CREATED_DATE", "2016-04-01");		
		mapObj.put("VIEW", 0);		
		mapObj.put("STA_STATUS", "ACTIVE");
		mapObj.put("STD_NAME", "1Rebel");		
		mapObj.put("STD_ID", 10);
		mapObj.put("STA_EXTERNAL_LINK", "");
		mapObj.put("STA_ID", 5);
		mapObj.put("STD_POINTS", 10);
		mapObj.put("STA_NAME", "#RebelSleepRepeat");
		mapObj.put("STA_DESCRIPTION", "");
		mapObj.put("STA_SHORT_DESCRIPTION", "");
		
		mapObj1 =  new HashMap<String, Object>();
		mapObj1.put("STA_BADGE", "");
		mapObj1.put("STA_TYPE_ID", 2);
		mapObj1.put("STD_COMPANY_LOGO", "");
		mapObj1.put("STA_IMAGE_URL", "");
		mapObj1.put("STA_GOAL_POINTS", 0);
		mapObj1.put("STA_START_DATE", "2016-06-30");
		mapObj1.put("STA_END_DATE", "2016-06-30");
		mapObj1.put("STA_EXPIRY_DATE", "2016-06-21");
		mapObj1.put("STA_CREATED_DATE", "2016-04-01");		
		mapObj1.put("VIEW", 0);		
		mapObj1.put("STA_STATUS", "ACTIVE");
		mapObj1.put("STD_NAME", "STD_POINTS");		
		mapObj1.put("STD_ID", 11);
		mapObj1.put("STA_EXTERNAL_LINK", "");
		mapObj1.put("STA_ID", 32);
		mapObj1.put("STD_POINTS", 10);
		mapObj1.put("STA_NAME", "Re-Boot Challenge");
		mapObj1.put("STA_DESCRIPTION", "Win a block of 10 classes,a 60 minutes sports");
		mapObj1.put("STA_SHORT_DESCRIPTION", "Win a block of 10 classes,a 60 minutes sports");
		
		aray.add(mapObj);
		aray.add(mapObj1);
				
		when(studioRepository.findOne(10)).thenReturn(std);
		when(studioRepository.findOne(11)).thenReturn(std);
		when(customStudioActivityRepository.activitySearch("ALL", "ALL", 2, 10)).thenReturn(aray);
		JSONArray res = studioActivityServiceImpl.activitySearch("ALL", "ALL", 2, 10);
		System.out.println("Studio Activity SEARCH "+res);
		assertNotNull(res);		
	}	
	/**
     * This test case will test activityDetails method.
     * It should return details of an activity(challenge/event) based on given activity id.
     */
	@Test
	public void testShouldActivityDetails() {
		Map<String, Object> mapObj;
		mapObj = new HashMap<String, Object>();
		
		mapObj.put("STA_ID", 37);
		mapObj.put("STA_NAME", "Life Amplified 60 Day Challenge");
		mapObj.put("STD_ID", 2);
		mapObj.put("STA_DESCRIPTION", "Life Amplified 60 Day Challenge Description");		
		mapObj.put("START_DATE", "2016-04-01");
		mapObj.put("START_TIME", "00:00");
		mapObj.put("END_DATE", "2016-05-31");
		mapObj.put("END_TIME", "00:00");		
		mapObj.put("STA_GOAL_POINTS", 0);
		mapObj.put("STA_TYPE_ID", 2);
		
		mapObj.put("STA_PRICE", 0);
		mapObj.put("STA_IMAGE_URL", "");		
		mapObj.put("STA_ADDRESS_LINE1", "");
		mapObj.put("STA_ADDRESS_LINE2", "");
		mapObj.put("STA_ADDRESS_LINE3", "");		
		mapObj.put("STA_TOWN", "");
		mapObj.put("STA_COUNTY", "");
		mapObj.put("STA_COUNTRY", "");
		mapObj.put("STA_POST_CODE", "");
		mapObj.put("STA_LATITUDE", 0);
		mapObj.put("STA_LONGITUDE", 0);
		mapObj.put("STA_EXTERNAL_LINK", "http://www.fiternity.co.uk");	
		mapObj.put("STA_LOCATION", "");
		mapObj.put("STD_NAME", "Xtend Barre");
		mapObj.put("STD_COMPANY_LOGO", "");
		mapObj.put("CMTS_CNT", 10);		
		mapObj.put("STA_LIKE_CNT", 10);
		mapObj.put("STA_INFLUENCER","");
		mapObj.put("INTRESTED_USER", "");
		mapObj.put("STA_EXPIRY_DATE", "2016-06-25");
		mapObj.put("STA_STATUS", "ACCEPTED");
		
		if(Integer.parseInt(mapObj.get("STA_TYPE_ID").toString()) == 1) {			
			mapObj.put("PTY_LIKED", "");
		} else {	
			mapObj.put("STA_SHORT_DESCRIPTION", mapObj.get("STA_SHORT_DESCRIPTION"));	
			mapObj.put("STA_BADGE", "");
		}
		
		when(customStudioActivityRepository.activityDetails(1)).thenReturn(mapObj);
		JSONObject res = studioActivityServiceImpl.activityDetails(1);
		System.out.println("ACTIVITY DETAILS "+res);
		assertNotNull(res);
	}

}