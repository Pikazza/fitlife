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

import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.Voucher;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.NoSufficientGainedPointsException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.CustomRewardRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.RewardRepository;
import com.turnout.ws.repository.StudioRepository;
import com.turnout.ws.repository.VoucherPartyRepository;
import com.turnout.ws.repository.VoucherRepository;

/**
 * This is a testing class for RewardServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking RewardServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RewardServiceImplTest {
	
	@Mock
	private VoucherRepository voucherRepository;
	
	@Mock
	private VoucherPartyRepository voucherPartyRepository;
	
	@Mock
	private CustomRewardRepository customRewardRepository;
	
	@Mock
	private PartyRepository partyRepository;
	
	@Mock
	private RewardRepository rewardRepository;
	
	@Mock
	private StudioRepository studioRepository;
	
	@Mock
	private SmtpMailSender smtpMailSender;	
	
	private RewardServiceImpl rewardServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		rewardServiceImpl = new RewardServiceImpl(voucherRepository,voucherPartyRepository,customRewardRepository,partyRepository,rewardRepository,smtpMailSender, studioRepository);
	}
	
	/**
	 * This test case will test reedemReward method.
	 * This should redeem gained points of user in order to get active rewards.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 * @throws NoSufficientGainedPointsException It throws when there is no sufficient gained point while redeeming reward.
	 */
	@Test
	public void testShouldReedemReward() throws ParseException, NoSufficientGainedPointsException {
		Party pty = new Party(); //mock(Party.class);
		pty.setPtyName("Uthirapathi");
		pty.setPtyEmail("silaluthira@gmail.com");
		pty.setPtyGainedPoints(100);
		pty.setPtyId(10);
		
		Reward rwd = new Reward(); //mock(Reward.class);
		rwd.setRwdPoints(10);
		rwd.setRwdName("Reward name");
		rwd.setRwdId(3);
		
		Voucher voc = new Voucher(); //mock(Voucher.class);
		voc.setVocCode("voc_123");
		voc.setVocId(3);
		
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(rewardRepository.findOne(10)).thenReturn(rwd);
		when(customRewardRepository.getVoucherId(10)).thenReturn(3);
		when(voucherRepository.findByVocId(3)).thenReturn(voc);
		when(partyRepository.saveAndFlush(pty)).thenReturn(pty);
		JSONObject res = rewardServiceImpl.reedemReward(10, 10);
		System.out.println("REEDEM REWARD "+res);
		assertNotNull(res);
		
	}
	/**
	   * This test case will test getRewardListing method. It should return all rewards from database.
	   * 
	   */
	@Test
	public void testShouldGetRewardListing() {
		List aray = new ArrayList();
		Map<String, Object> mapObj,mapObj1;
		mapObj = new HashMap<String, Object>();
		mapObj.put("STD_NAME","barrecore");
		mapObj.put("RWD_POINTS",100);
		mapObj.put("RWD_DESCRIPTION","The barrecore Accessory Pack is presented in a stylish purple");
		mapObj.put("STD_COMPANY_LOGO","");
		mapObj.put("RWD_IMG_URL","");
		mapObj.put("RWD_ID",21);
		mapObj.put("RWD_NAME","20% off barrecore Accessory Pack");
		mapObj.put("RWD_STD_ID",38);
		
		mapObj1 = new HashMap<>();
		mapObj1.put("STD_NAME","BARREtoned");
		mapObj1.put("RWD_POINTS",100);
		mapObj1.put("RWD_DESCRIPTION","A great deal on BARREtoned class packs");
		mapObj1.put("STD_COMPANY_LOGO","");
		mapObj1.put("RWD_IMG_URL","");
		mapObj1.put("RWD_ID",22);
		mapObj1.put("RWD_NAME","10% off 10, 20, and 30 Class Packs");
		mapObj1.put("RWD_STD_ID",39);
		
		aray.add(mapObj);
		aray.add(mapObj1);
		when(customRewardRepository.getRewardListingByCustomQuery(1, 7)).thenReturn(aray);
		JSONArray res = rewardServiceImpl.getRewardListing(1, 7);
		System.out.println("REWARD LISTING "+res);
		assertNotNull(res);
	}
	/**
	   * This test case will test getRewardDetails method.
	   * It should get all the details of reward based on given reward id.
	   */
	@Test
	public void testShouldGetRewardDetail() {
		Map<String, Object> mapObj;
		mapObj = new HashMap<String, Object>();
		
		mapObj.put("RWD_ID", 10);
		mapObj.put("RWD_NAME", "20% off barrecore Accessory Pack");
		mapObj.put("RWD_POINTS", 10);
		mapObj.put("RWD_IMG_URL", "");
		mapObj.put("RWD_DESCRIPTION", "Get 1GBP Off Any of Our 8 Smoothie Flavours");
		mapObj.put("RWD_EXPIRY_DATE", "");
		mapObj.put("RWD_CREATED_DATE", "");
		mapObj.put("RWD_MODIFIED_DATE", "");
		mapObj.put("RWD_STD_ID", 3);	
		mapObj.put("RWD_STATUS","ACTIVE");	
		mapObj.put("STD_NAME", "barrecore");
		mapObj.put("STD_COMPANY_LOGO", "");
		when(customRewardRepository.getRewardDetailById(10)).thenReturn(mapObj);
		JSONObject res = rewardServiceImpl.getRewardDetail(10);
		System.out.println("REWARD DETAIL "+res);
		assertNotNull(res);
	}
	
	/**
	   * This test case will test saveReward method.
	   * It should return new or existing reward id of recently changed reward from database when all of its details has been passed as parameter.
	   *
	   * @throws ParseException An exception occurs when you fail to parse a Object.
	   */
	@Test
	public void testShouldSaveReward() throws ParseException {
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdId(2);
		
		Reward rwd = new Reward(); //mock(Reward.class);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYY");
		Date credDate = dateFormat.parse("05/07/2016");
		Date expDate = dateFormat.parse("15/07/2016");
		
		rwd.setRwdName("20% off barrecore Accessory Pack");
		rwd.setRwdDescription("Get 1GBP Off Any of Our 8 Smoothie Flavours");
		rwd.setRwdImgUrl("");
		rwd.setRwdCreatedDate(credDate);
		rwd.setRwdModifiedDate(credDate);
		rwd.setRwdPoints(10);
		rwd.setRwdExpiryDate(expDate);
		rwd.setRwdStatus("ACTIVE");
		rwd.setStudio(std);
		rwd.setRwdId(10);
		
		when(studioRepository.findOne(2)).thenReturn(std);
		when(rewardRepository.findOne(10)).thenReturn(rwd);
		when(rewardRepository.saveAndFlush(rwd)).thenReturn(rwd);
		JSONObject res = rewardServiceImpl.saveReward(10, "reward name", "desc", "expDate", 10, "15/07/2016", 2, "ACTIVE");
		System.out.println("SAVE REWARD "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case should test rewardSearch method.
	 * It should return list of beacons from database where the parameter may have the value 'ALL' to get all rewards or it can have a string that has to be searched to get the hit result.
	 * 
	 */
	@Test
	public void testShouldRewardSearch() {
		List aray = new ArrayList();
		
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdId(38);
		std.setStdName("BARREtoned");
		
		Map<String, Object> mapObj,mapObj1;
		mapObj = new HashMap<String, Object>();
		mapObj.put("STD_NAME","barrecore");
		mapObj.put("RWD_POINTS",100);
		mapObj.put("RWD_DESCRIPTION","The barrecore Accessory Pack is presented in a stylish purple");
		mapObj.put("STD_COMPANY_LOGO","");
		mapObj.put("RWD_IMG_URL","");
		mapObj.put("RWD_ID",21);
		mapObj.put("RWD_NAME","20% off barrecore Accessory Pack");
		mapObj.put("RWD_STD_ID",38);
		

		mapObj1 = new HashMap<>();
		mapObj1.put("STD_NAME","BARREtoned");
		mapObj1.put("RWD_POINTS",100);
		mapObj1.put("RWD_DESCRIPTION","A great deal on BARREtoned class packs");
		mapObj1.put("STD_COMPANY_LOGO","");
		mapObj1.put("RWD_IMG_URL","");
		mapObj1.put("RWD_ID",22);
		mapObj1.put("RWD_NAME","10% off 10, 20, and 30 Class Packs");
		mapObj1.put("RWD_STD_ID",39);
		
		aray.add(mapObj);
		aray.add(mapObj1);
		
		when(studioRepository.findOne(38)).thenReturn(std);
		when(studioRepository.findOne(39)).thenReturn(std);
		when(customRewardRepository.rewardSearch("as", 38, "E")).thenReturn(aray);
		JSONArray res = rewardServiceImpl.rewardSearch("as", 38, "E");
		System.out.println("REWARD SEARCH "+res);
		assertNotNull(res);		
	}

}
