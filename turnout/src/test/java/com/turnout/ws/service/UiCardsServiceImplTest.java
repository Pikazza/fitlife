package com.turnout.ws.service;


import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import com.turnout.ws.domain.UiCards;
import com.turnout.ws.repository.CustomUiCardsRepository;
import com.turnout.ws.repository.UiCardsRepository;

/**
 * This is a testing class for UiCardsServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking UiCardsServiceImpl methods and it does not return any value.
 * 
 *viswa
 */
@RunWith(MockitoJUnitRunner.class)
public class UiCardsServiceImplTest {

	@Mock
	private  UiCardsRepository uiCardsRepository;
	@Mock
	private  CustomUiCardsRepository customUiCardsRepository;
	
	private UiCardsServiceImpl uiCardsServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	
	@Before
	public void setUp() throws MockitoException{
		uiCardsServiceImpl = new UiCardsServiceImpl(uiCardsRepository, customUiCardsRepository);
	}
	/**
	 * This test case should test getStream method.
	 * It should return all studios activities based on the given party id.
	 */
	
	@Test
	public void testShouldGetStream()
	{
			
		UiCards uic = mock(UiCards.class);
		uic.setUicId("1");
		uic.setName("PREMIUM");
		uic.setHandlerClass("STUDIOS");
		uic.setUserSpecific("N");
		uic.setDisplayOrder(1);
		uic.setCardGroupSize(0);		
		
		List prmStudio = new ArrayList();
		Map<String,Object> premium = new HashMap<String, Object>();		
		premium.put("GAINED_TASK_BAGE", "");
		premium.put("REL_IMG", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/xbarrelogo.png");
		premium.put("LONGITUDE", "-0.152424");
		premium.put("PTY_ID", "");
		premium.put("CTYPE", "STUDIOS");
		premium.put("STA_START_DATE", "");
		premium.put("REL_NAME", "Xtend Barre");
		premium.put("NAME", "Xtend Barre");
		premium.put("LIKES_CNT", "0");
		premium.put("CREATEDDATE", "2016-03-01 10:10:10.0");
		premium.put("IMAGE", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/extend-bar-profile.jpg");
		premium.put("STA_STD_ID", "");
	    premium.put("STA_EXPIRY_DATE", "");
		premium.put("DESCRIPTION", "COMING SOON: The new Xtend Barre studio is due to open in June 2016! The stunning flagship studio is located in beautiful Marylebone at 49 Marylebone High Street, London, W1U 5HH. Meet you at the barre!");
		premium.put("COMMENTS_CNT", "0");
		premium.put("STA_END_DATE", "");
		premium.put("STA_ID", "");
		premium.put("ID", "2");
		premium.put("POINTS", "10");
		premium.put("LATITUDE", "51.5132");
		premium.put("PTY_LIKED", "0");
		prmStudio.add(premium);		
	}
	
	
}


