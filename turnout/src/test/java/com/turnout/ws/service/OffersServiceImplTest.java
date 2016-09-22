package com.turnout.ws.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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
import org.mockito.stubbing.OngoingStubbing;

import com.turnout.ws.domain.BeaconMaster;
import com.turnout.ws.domain.Comments;
import com.turnout.ws.domain.Likes;
import com.turnout.ws.domain.Offers;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.BeaconMasterRepository;
import com.turnout.ws.repository.CommentsRepository;
import com.turnout.ws.repository.CustomLikeRepository;
import com.turnout.ws.repository.CustomOffersRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.LikesRepository;
import com.turnout.ws.repository.OffersRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.StudioRepository;

/**
 * This is a testing class for OffersServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking OffersServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class OffersServiceImplTest {
	
	@Mock
	private OffersRepository ofrRepository;
	
	@Mock
	private BeaconMasterRepository beaconRepository;
	
	@Mock
	private StudioRepository stdRepository;
	
	@Mock
	private CustomOffersRepository customOffersRepository;	
	

	private OffersServiceImpl offersServiceImpl;
	
	/**
	 * This method to be run before the Test method.So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		offersServiceImpl = new OffersServiceImpl(ofrRepository, beaconRepository,stdRepository, customOffersRepository);
	}	
	
	/**
	 * This test case should test getActiveOffers method.
	 * It should return list of active and unexpired offers from database based on beacon id and notification type.
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldGetActiveOffers() throws ParseException {
		
		List<BeaconMaster> bmlist = new ArrayList<BeaconMaster>();
		BeaconMaster bmObj = new BeaconMaster();
		bmObj.setBconId("B0005123");
		bmObj.setBconDetectType("OWN");
		bmObj.setBconStatus("ACTIVE");
		bmObj.setBconStaTypeId(1);
		bmObj.setBconStdId(2);
		
		bmlist.add(bmObj);
		
		List<Offers> ofrList = new ArrayList<Offers>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		Offers ofrSearch = new Offers(); //mock(Offers.class);
		ofrSearch.setOfrId(10);
		ofrSearch.setOfrCreatedDate(dateFormat.parse("04/29/2016"));
		ofrSearch.setOfrDescription("At Xtend Barre, we want to help inspire you, our amazing clients");
		ofrSearch.setOfrExternalLink("");
		ofrSearch.setOfrImg("http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/4.png");
		ofrSearch.setOfrStatus("ACTIVE");
		ofrSearch.setOfrStdId(3);
		ofrSearch.setOfrType("W");	
		
		ofrList.add(ofrSearch);

		
		Offers ofrSearch1 = new Offers();//mock(Offers.class);
		ofrSearch1.setOfrId(6);
		ofrSearch1.setOfrCreatedDate(dateFormat.parse("04/30/2016"));
		ofrSearch1.setOfrDescription("At Xtend Barre, we want to help inspire you, our amazing clients");
		ofrSearch1.setOfrExternalLink("www.google.com");
		ofrSearch1.setOfrImg("http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/4.png");
		ofrSearch1.setOfrStatus("ACTIVE");
		ofrSearch1.setOfrStdId(2);
		ofrSearch1.setOfrType("W");	
		
		ofrList.add(ofrSearch1);
		
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdName("Core Collective");
		std.setStdCompanyLogo("http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/5.png");
		
		
		when(beaconRepository.findByBconIdOrderByBconDetectTypeDesc("B0005123")).thenReturn(bmlist);
		when(stdRepository.findOne(2)).thenReturn(std);
		when(ofrRepository.findByOfrStdIdAndOfrStatus(2,"ACTIVE")).thenReturn(ofrList);
		JSONObject clst = offersServiceImpl.getActiveOffers("B0005123","W");
		System.out.println(clst);
		assertNotNull(clst);		
	}
	
	/**
	 * This test case will test saveOffers method.
	 * It should get updated or added offer id from database when all of valid offer's details has been passed.
	 *
	 *@throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldSaveOffers() throws ParseException {
		DateFormat dateformat  = new SimpleDateFormat("dd/MM/yyyy");
		int ofrId = 0;
		Offers ofr = mock(Offers.class);
		ofr.setOfrId(ofrId);
		ofr.setOfrStdId(5);
		ofr.setOfrType("W");
		ofr.setOfrStatus("active");
		ofr.setOfrImg("http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/5.png");
		ofr.setOfrDescription("welcome notification content");
		ofr.setOfrExternalLink("www.facebook.com");
		ofr.setOfrCreatedDate(dateformat.parse("05/25/2016"));
		
		when(ofrRepository.findOne(ofrId)).thenReturn(ofr);
		when(ofrRepository.save(any(Offers.class))).thenReturn(ofr);
		JSONObject res = offersServiceImpl.saveOffers(ofrId,5,"W", "active", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/5.png", "welcome notification content","www.facebook.com",dateformat.parse("05/25/2016"));
		System.out.println(res);
	}	
	/**
	 * This test case should test offerSearch method.
	 * It should return list of offers from database where the parameter may have the value 'ALL' to get all offers or it can have a string that has to be searched to get the hit result.
	 * 
	 */
	@Test
	public void testShouldOfferSearch() {
		List ofrList = new ArrayList();
		Map<String, Object> ofrSearch,ofrSearch1;
		ofrSearch = new HashMap<String,Object>();
		
		ofrSearch.put("OFR_ID", 5);
		ofrSearch.put("OFR_IMG", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/4.png");
		ofrSearch.put("OFR_DESCRIPTION", "At Xtend Barre, we want to help inspire you, our amazing clients");
		ofrSearch.put("STD_NAME", "Core Collective");
		ofrSearch.put("OFR_CREATED_DATE", "2016-04-29 00:00:00.0");
		ofrSearch.put("STD_ID", 3);
		ofrSearch.put("OFR_STATUS", "INACTIVE");
		ofrSearch.put("STD_COMPANY_LOGO", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/corecollectivelogo.png");
		ofrSearch.put("OFR_STD_ID", 3);
		ofrSearch.put("OFR_EXTERNAL_LINK", "www.google.com");
		ofrSearch.put("OFR_TYPE", "E");
		ofrSearch.put("OFR_MODIFIED_DATE", "2016-04-07 11:24:12.0");
		ofrList.add(ofrSearch);
		
		ofrSearch1 = new HashMap<String,Object>();

		ofrSearch1.put("OFR_ID", 10);
		ofrSearch1.put("OFR_IMG", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/cc-welcome-pop-up.jpg");
		ofrSearch1.put("OFR_DESCRIPTION", "Don't forget to join us this weekend for our Lululemon pop-up shop!");
		ofrSearch1.put("STD_NAME", "Core Collective");
		ofrSearch1.put("OFR_CREATED_DATE", "2016-04-08 00:00:00.0");
		ofrSearch1.put("STD_ID", 3);
		ofrSearch1.put("OFR_STATUS", "ACTIVE");
		ofrSearch1.put("STD_COMPANY_LOGO", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/corecollectivelogo.png");
		ofrSearch1.put("OFR_STD_ID", 3);
		ofrSearch1.put("OFR_EXTERNAL_LINK", "");
		ofrSearch1.put("OFR_TYPE", "W");
		ofrSearch1.put("OFR_MODIFIED_DATE", "2016-04-07 11:24:12.0");		
		
		ofrList.add(ofrSearch1);
		when(customOffersRepository.offerSearch("ALL")).thenReturn(ofrList);
		JSONArray clst = offersServiceImpl.offerSearch("ALL");
		System.out.println(clst);
		assertNotNull(clst);
	}	
	
	/**
	 * This test case will test offerDetails method.
	 * It should get all the details of offers (promos and upsells) based on given offers id.
	 *
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldOfferDetails() throws ParseException {
		//JSONObject obj = new JSONObject();
		DateFormat dateformat  = new SimpleDateFormat("dd/MM/yyyy");
		Date ofrDate = dateformat.parse("08/04/2016");
		
		Studio std = new Studio(); //mock(Studio.class);
		std.setStdName("Core Collective");
		std.setStdId(2);
		
		Offers ofr = new Offers(); //mock(Offers.class);
		int ofrId = 10;
		ofr.setOfrId(ofrId);
		ofr.setOfrCreatedDate( ofrDate);
		ofr.setOfrDescription("Don't forget to join us this weekend for our Lululemon pop-up shop!");
		ofr.setOfrExternalLink("");
		ofr.setOfrImg("http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/cc-welcome-pop-up.jpg");
		ofr.setOfrStatus("ACTIVE");
		ofr.setOfrStdId(2);
		ofr.setOfrType("W");
		
		when(stdRepository.findOne(2)).thenReturn(std);
		when(ofrRepository.findOne(10)).thenReturn(ofr);
		JSONObject res = offersServiceImpl.offerDetails(10);
		System.out.println(res);
		assertNotNull(res);
	}	
}
