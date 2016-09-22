package com.turnout.ws.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


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
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import com.turnout.ws.domain.Info;
import com.turnout.ws.repository.InfoRepository;


/**
 * This is a testing class for InfoServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking InfoServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class InfoServiceImplTest {
	
	@Mock
	private InfoRepository infoRepository;
	
	private InfoServiceImpl infoServiceImpl;
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		infoServiceImpl = new InfoServiceImpl(infoRepository);
	}
	
	/**
	 * This test case will test getListInfo method.
	 * It should get list of app's notification content from database.
	 * 
	 */
	@Test
	public void testShouldGetListInfo()  {
		Info infoObj = mock(Info.class);
		infoObj.setId(27);
		infoObj.setDescription("You have enough points to get a <strong>reward<strong>");
		infoObj.setTitle("Reedem Eligible");
		infoObj.setType("reedem-eligible");
		
		when(infoRepository.findByType("reedem-eligible")).thenReturn(infoObj);		
		JSONObject res = infoServiceImpl.getListInfo("reedem-eligible");
		System.out.println(res);
		assertNotNull(res);
	}
	

	/**
	 * This test case will test saveInfo method.
	 * It should get updated notification content id from database when all of contents has been passed for updation.
	 * 
	 */
	@Test
	public void testShouldSaveInfo() {
		Info infoObj = new Info(); //mock(Info.class);
		infoObj.setId(2);
		infoObj.setDescription("You have enough points to get a reward");
		infoObj.setType("reedem-eligible");
		infoObj.setTitle("Reedem Eligible");
		
		when(infoRepository.findOne(2)).thenReturn(infoObj);
		JSONObject res = infoServiceImpl.saveInfo(2, "You have enough points to get a reward", "reedem-eligible","Reedem Eligible");
		assertNotNull(res);
		System.out.println(res);
	}
	
	/**
	 * This test case will test getAllListInfo method.
	 * It should get all the details of content based on passed notification type.
	 * 
	 * @throws Exception It throws unchecked runtime exception.
	 */
	@Test
	public void testShouldGetAllListInfo() throws Exception {
		List<Info> infoList = new ArrayList<Info>();
		Info infoObj = mock(Info.class);
		infoObj.setId(2);
		infoObj.setDescription("Description");
		infoObj.setTitle("Title");
		infoObj.setType("info-title");
		infoList.add(infoObj);
		
		Info infoObj1 = mock(Info.class);
		infoObj1.setId(2);
		infoObj1.setDescription("Description");
		infoObj1.setTitle("Title");
		infoObj1.setType("info-title");
		infoList.add(infoObj1);		
		
		when(infoRepository.findAllByOrderByIdAsc()).thenReturn(infoList);
		JSONArray clst = infoServiceImpl.getAllListInfo();		
		assertNotNull(clst);
	}	
}
