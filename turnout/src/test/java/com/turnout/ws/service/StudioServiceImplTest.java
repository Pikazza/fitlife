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
import com.turnout.ws.repository.CustomStudioPartyActivityRepository;
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
 * This is a testing class for StudioServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking StudioServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class StudioServiceImplTest {
	
	@Mock
	private StudioRepository studioRepository;
	
	@Mock
	private CustomRewardRepository customRewardRepository;
	
	@Mock
	private CustomStudioActivityRepository customStudioActivityRepository;
	
	@Mock
	private CustomStudioPartyActivityRepository customStudioPartyActivityRepository;
	

	
	private StudioServiceImpl studioServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		studioServiceImpl = new StudioServiceImpl(studioRepository,customRewardRepository,customStudioActivityRepository,customStudioPartyActivityRepository);
	}
	/**
	 * This test case should test getStudiosList method.
	 * It should return list of studios from database based on the given status.
	 * For example, it will list all the active studios if status is passed as active.
	 * 
	 */
	@Test
	public void testShouldGetStudiosList() {
		List<Studio> stdList = new ArrayList<Studio>();
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdBillingAddrsCountry("");
		std.setStdStreamDescription("");
		std.setStdVatNumber("");
		std.setStdBillingAddrsCounty("");
		std.setStdStatus("ACTIVE");
		std.setStdPostCode("EC3A 8LE");
		std.setStdDescription("Engineered layouts that inspire and motivate.");
		std.setStdType("NORMAL");
		std.setStdAddressLine1("63 St Mary Axe,London");
		std.setStdAddressLine2("");
		std.setStdAddressLine3("");
		std.setStdTradingName("");
		std.setStdMailId("support@1rebel.co.uk");
		std.setStdImageUrl("");
		std.setStdPamAuthToken("");
		std.setStdPamAuthId("");
		std.setStdPhoneNo("020 3714 0710");
		std.setStdPoints(10);
		std.setStdLatitude((float) 51.5156);
		std.setStdLongitude((float) -0.0821128);
		std.setStdCountry("UK");
		std.setStdCounty("UK");
		std.setStdCompanyLogo("");
		std.setStdName("1Rebel");
		std.setStdId(10);
		std.setStdTimeSpent(30);
		std.setStdTown("UK");
		
		stdList.add(std);
		Sort srt = new Sort(Sort.Direction.ASC, "stdName");
		
		when(studioRepository.findAll(srt)).thenReturn(stdList);
		when(studioRepository.findByStdStatus("ACTIVE", srt)).thenReturn(stdList);
		JSONArray res = studioServiceImpl.getStudiosList("ALL");
		System.out.println("STUDIO LIST "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case will test getStudioProfile method.
	 * It should get all the details of studio based on given studio id. 
	 * @throws UnirestException It throws when the http connection fails while getting instagram access token.
	 */
	@Test
	public void testShouldGetStudioProfile() throws UnirestException {
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdBillingAddrsCountry("");
		std.setStdStreamDescription("");
		std.setStdVatNumber("");
		std.setStdBillingAddrsCounty("");
		std.setStdStatus("ACTIVE");
		std.setStdPostCode("EC3A 8LE");
		std.setStdDescription("Engineered layouts that inspire and motivate.");
		std.setStdType("NORMAL");
		std.setStdAddressLine1("63 St Mary Axe,London");
		std.setStdAddressLine2("");
		std.setStdAddressLine3("");
		std.setStdTradingName("");
		std.setStdMailId("support@1rebel.co.uk");
		std.setStdImageUrl("");
		std.setStdPamAuthToken("");
		std.setStdPamAuthId("");
		std.setStdPhoneNo("020 3714 0710");
		std.setStdPoints(10);
		std.setStdLatitude((float) 51.5156);
		std.setStdLongitude((float) -0.0821128);
		std.setStdCountry("UK");
		std.setStdCounty("UK");
		std.setStdCompanyLogo("");
		std.setStdName("1Rebel");
		std.setStdId(10);
		std.setStdTimeSpent(30);
		std.setStdTown("UK");
		
		
		when(studioRepository.findOne(10)).thenReturn(std);
		JSONObject res = studioServiceImpl.getStudioProfile(10);
		System.out.println("STUDIO Detail "+res);
		assertNotNull(res);		
	}
	
	/**
	 * This test case will test saveStudio method.
	 * It should return id of added or updated studio when all of its valid details has been passed to store in database.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldSaveStudio() throws ParseException {
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdBillingAddrsCountry("");
		std.setStdStreamDescription("");
		std.setStdVatNumber("");
		std.setStdBillingAddrsCounty("");
		std.setStdStatus("ACTIVE");
		std.setStdPostCode("EC3A 8LE");
		std.setStdDescription("Engineered layouts that inspire and motivate.");
		std.setStdType("NORMAL");
		std.setStdAddressLine1("63 St Mary Axe,London");
		std.setStdAddressLine2("");
		std.setStdAddressLine3("");
		std.setStdTradingName("");
		std.setStdMailId("support@1rebel.co.uk");
		std.setStdImageUrl("");
		std.setStdPamAuthToken("");
		std.setStdPamAuthId("");
		std.setStdPhoneNo("020 3714 0710");
		std.setStdPoints(10);
		std.setStdLatitude((float) 51.5156);
		std.setStdLongitude((float) -0.0821128);
		std.setStdCountry("UK");
		std.setStdCounty("UK");
		std.setStdCompanyLogo("");
		std.setStdName("1Rebel");
		std.setStdId(10);
		std.setStdTimeSpent(30);
		std.setStdTown("UK");
		
		when(studioRepository.findOne(10)).thenReturn(std);
		when(studioRepository.saveAndFlush(std)).thenReturn(std);
		JSONObject res = studioServiceImpl.saveStudio(10,"1Rebel","","","","","","UK","UK","EC3A 8LE","63 St Mary Axe,London","","", "", "", "", "", "", "","","",(float) 51.5156, (float) -0.0821128, "", "", dateformat.parse("16/06/2016"), "", dateformat.parse("16/06/2016"), "", "", "", "ACTIVE", "SL", "", "", 10, 10, "");
		System.out.println("SAVE STUDIO "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case will test studioSearch method.
	 * It should identify and get the list of studios when  given search string equals to studio name.
	 */
	@Test
	public void testShouldDoStudioSearch() {
		List<Studio> stdList = new ArrayList<Studio>();
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdBillingAddrsCountry("");
		std.setStdStreamDescription("");
		std.setStdVatNumber("");
		std.setStdBillingAddrsCounty("");
		std.setStdStatus("ACTIVE");
		std.setStdPostCode("EC3A 8LE");
		std.setStdDescription("Engineered layouts that inspire and motivate.");
		std.setStdType("NORMAL");
		std.setStdAddressLine1("63 St Mary Axe,London");
		std.setStdAddressLine2("");
		std.setStdAddressLine3("");
		std.setStdTradingName("");
		std.setStdMailId("info@xtendbarre.com");
		std.setStdImageUrl("");
		std.setStdPamAuthToken("");
		std.setStdPamAuthId("");
		std.setStdPhoneNo("020 3714 0710");
		std.setStdPoints(10);
		std.setStdLatitude((float) 51.5156);
		std.setStdLongitude((float) -0.0821128);
		std.setStdCountry("UK");
		std.setStdCounty("UK");
		std.setStdCompanyLogo("");
		std.setStdName("Xtend Barre");
		std.setStdId(10);
		std.setStdTimeSpent(30);
		std.setStdTown("UK");
		
		stdList.add(std);
		
		when(studioRepository.searchByText("Xtend")).thenReturn(stdList);
		JSONArray res = studioServiceImpl.doStudioSearch("Xtend");
		System.out.println("STUDIO SEARCH "+res);
		assertNotNull(res);
	}	
	
}
