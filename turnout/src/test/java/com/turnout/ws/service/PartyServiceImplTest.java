package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyRepository;

/**
 * This is a testing class for PartyServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking PartyServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyServiceImplTest {
	
	@Mock
	private PartyRepository partyRepository;
	
	@Mock
	private PartyAuthMechRepository partyAuthMechRepository;
	
	@Mock
	private CustomPartyRepository customPartyRepository;
	
	@Mock
	private InstagramPostReceiver instagramPostReceiver;	
	
	@Mock
	private InfoRepository infoRepository;		

	private PartyServiceImpl partyServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		partyServiceImpl = new PartyServiceImpl(partyRepository, customPartyRepository,partyAuthMechRepository, instagramPostReceiver,infoRepository);
	}
	
	/**
	 * This test case will test retrievePartyDetailsById method.
	 * It should get all the details of party based on given party id.
	 * 
	 * @throws PartyNotFoundException It throws when there is no given party exist.
	 */	
	@Test
	public void testShouldRetrievePartyDetailsById() throws PartyNotFoundException {
		
		Party pty = new Party(); //mock(Party.class);
		
		PartyAuthMech ptyAuth = new PartyAuthMech(); //mock(PartyAuthMech.class);
		ptyAuth.setPtyId(10);
		ptyAuth.setAmhId("SL");		
		
		pty.setPtyId(10);
		pty.setPtyName("Uthirapathi");		
		pty.setPtyAddressLine1("Kodampakkam High road");
		pty.setPtyAddressLine2("West mambalam");
		pty.setPtyAddressLine3("Chennai");
		pty.setPtyTown("Chennai");
		pty.setPtyPostCode("600003");
		pty.setPtyCountry("India");
		pty.setPtyTel("1234567899");
		pty.setPtyMobile("123456789");
		pty.setPtyEmail("uthirapathi@raisingibrows.com");
		pty.setPtyPhoto("");
		pty.setPtyDescription("sample description");
		pty.setPtyGainedPoints(0);
		pty.setPtyShowupPreference("ONLYME");
		pty.setPtyActivityPreference("PUBLIC");
		pty.setPtyStatus("ACTIVE");
			
		
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(partyAuthMechRepository.findByPtyId(10)).thenReturn(ptyAuth);
		JSONObject res = partyServiceImpl.retrievePartyDetailsById(10);
		System.out.println("Retrieve party details"+res);
		assertNotNull(res);
	}
	
	/**
	 * /**
	 * This test case will test updatePartyDetailsById method.
	 * It should return updated party id of recently changed party from database when all of its new details has been passed.
	 * 
	 * @throws PartyNotFoundException It throws when there is no given party exist.
	 * @throws CurrentPasswordNotMatchedException It throws when current pass word not match with user's entered password.
	 * @throws PartyExistException It throws when there is no party exist.
	 */
	@Test
	public void testShouldUpdatePartyDetailsById() throws PartyNotFoundException, CurrentPasswordNotMatchedException, PartyExistException {
		Party pty = new Party(); //mock(Party.class);
		
		PartyAuthMech ptyAuth = new PartyAuthMech(); //mock(PartyAuthMech.class);
		ptyAuth.setPtyId(10);
		ptyAuth.setAmhId("SL");
		ptyAuth.setPamAuthToken("12345");
		
		pty.setPtyId(10);
		pty.setPtyName("Uthirapathi");
		pty.setPtyLastName("Devanarayanan");
		pty.setPtyEmail("uthirapathi@raisingibrows.com");
		pty.setPtyPhoto("");
		pty.setPtyDescription("sample description");
		pty.setPtyShowupPreference("ONLYME");
		pty.setPtyActivityPreference("PUBLIC");
		pty.setPtyStatus("ACTIVE");
		
		Info info = new Info(); mock(Info.class);
		info.setDescription("PASSWORD MIS-MATCHED");
		info.setTitle("PASSWORD INCORRECT");
			
		
		when(infoRepository.findByType("pwd-mismatch")).thenReturn(info);
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(partyAuthMechRepository.findByPtyId(10)).thenReturn(ptyAuth);
		when(partyAuthMechRepository.save(ptyAuth)).thenReturn(ptyAuth);
		when(partyRepository.saveAndFlush(pty)).thenReturn(pty);
		JSONObject res = partyServiceImpl.updatePartyDetailsById(10, "uthirapathi@raisingibrows.com", "", "sample description", "ONLYME", "PUBLIC", "SL", "12345", "12345", "Uthirapathi","Devnarayanan");
		System.out.println("UPDATE PARTY DETAILS "+res);
		assertNotNull(res);		
	}
	
	/**
	 * This test case will test changeUserStatus method.
	 * It should return updated party id when their status is changed.
	 * 
	 * @throws PartyNotFoundException It throws when there is no given party exist.
	 */	
	@Test
	public void testShouldChangeUserStatus() throws PartyNotFoundException {
		Party pty = new Party(); //mock(Party.class);
		pty.setPtyId(10);
		pty.setPtyStatus("ACTIVE");
		
		when(partyRepository.saveAndFlush(pty)).thenReturn(pty);
		when(partyRepository.findOne(10)).thenReturn(pty);
		JSONObject res = partyServiceImpl.changeUserStatus(10, "ACTIVE");
		System.out.println("CHANGE USER STATUS "+res);
		assertNotNull(res);
	}
	
	/**
	 * This test case will test partySearch method.
	 * This should return all of party from database and helps to search the party based on passed string.
	 * The string may have the value of first name,last name,email,mobile no.
	 * 
	 * @throws PartyNotFoundException It throws when there is no given party exist.
	 */
	@Test
	public void testShouldPartySearch() {
		List aray = new ArrayList();
		Map<String, Object> mapObj,mapObj1;
		mapObj = new HashMap<String,Object>();
		mapObj.put("PTY_NAME","Tim Berners Lee");
		mapObj.put("PTY_MOBILE", "");
		mapObj.put("AMH_ID","SL");
		mapObj.put("PTY_ID",1);
		mapObj.put("PAM_VERIFIED","Y");
		mapObj.put("PTY_TEL","");
		mapObj.put("PTY_STATUS","ACTIVE");
		mapObj.put("PTY_EMAIL","sathya@raisingibrows.com");
		
		mapObj1 = new HashMap<String,Object>();
		mapObj1.put("PTY_NAME","Richard Parker");
		mapObj1.put("PTY_MOBILE", "");
		mapObj1.put("AMH_ID","SL");
		mapObj1.put("PTY_ID",2);
		mapObj1.put("PAM_VERIFIED","Y");
		mapObj1.put("PTY_TEL","");
		mapObj1.put("PTY_STATUS","ACTIVE");
		mapObj1.put("PTY_EMAIL","viswa@raisingibrows.com");
		
		aray.add(mapObj);
		aray.add(mapObj1);
		
		when(customPartyRepository.partySearch("ALL")).thenReturn(aray);
		JSONArray res = partyServiceImpl.partySearch("ALL");
		System.out.println("PARTY SEARCH "+res);
		assertNotNull(res);
	}
}
