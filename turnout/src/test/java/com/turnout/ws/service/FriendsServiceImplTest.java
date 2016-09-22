package com.turnout.ws.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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

import com.turnout.ws.domain.Friends;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.FriendsRepository;

/**
 * This is a testing class for FriendsServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking FriendsServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FriendsServiceImplTest {
	
	@Mock
	private FriendsRepository friendsRepository;
	
	@Mock
	private CustomPartyRepository customPartyRepository;
	
	private FriendsServiceImpl friendsServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws Exception MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws Exception {
		
		friendsServiceImpl = new FriendsServiceImpl(friendsRepository, customPartyRepository);
	}

	/**
	 * This test case will test addfollower method.
	 * It should add a new friend to user friend list.
	 */
	
	@Test
	public void testShouldAddfollower() {		
		Friends frnd = mock(Friends.class);
		frnd.setFrndId1(1);
		frnd.setFrndId2(2);
		frnd.setFrndStatus("FOLLOW");
		when(friendsRepository.save(any(Friends.class))).thenReturn(frnd);
		JSONObject res = friendsServiceImpl.addfollower(1, 2, "FOLLOW");
		System.out.println("Add Follower "+res);
		assertNotNull(res);
	}

	/**
	 * This test case should test getFriendsList method.
	 * It should return list of friends to whom current user has followed already.
	 * 
	 */
	@Test
	public void testGetFriendsList() {
		List frndList = new ArrayList();
		Map< String, Object> m;
		m = new HashMap<String,Object>();
		m.put("PTY_NAME", "PRASANA");
		m.put("PTY_ID", 1);
		m.put("FRND_ID1", 104);
		m.put("PTY_PHOTO", "profile-5.jpg");
		m.put("FRND_ID2", 1);
		frndList.add(m);
		m = new HashMap<String,Object>();
		m.put("PTY_NAME", "DHEERA");
		m.put("PTY_ID", 2);
		m.put("FRND_ID1", 104);
		m.put("PTY_PHOTO", "profile-3.jpg");
		m.put("FRND_ID2", 2);
		frndList.add(m);
		
		when(customPartyRepository.getFriendsList(104)).thenReturn(frndList);

		//JSONArray resArray = friendsServiceImpl.getFriendsList(104);
		//System.out.println(resArray);
	}

	/**
	 * This test case should test findFriends method.
	 * While we search party with their name, it should return list of parties who are not actively friends.
	 * 
	 */
	@Test
	public void testFindFriends() {
		List findlist = new ArrayList();
		Map<String, Object> m ;
		m = new HashMap<String,Object>();
		m.put("PTY_COUNTRY", "uk");
		m.put("PTY_SHOWUP_PREFERENCE","ONLYME");
		m.put("PTY_ID", 13);
		m.put("PTY_DESCRIPTION", "Winner");
		m.put("PTY_DEVICE_TYPE", "Android");
		m.put("PTY_DEVICE_TOKEN", "");
		m.put("PTY_TEL", null);
		m.put("PTY_EMAIL", "sathyakumar@raisingibrows.com");
		m.put("PTY_ADDRESS_LINE_1", null);
		m.put("PTY_MOBILE", null);
		m.put("PTY_NAME", "SATHYAkumar");
		m.put("PTY_ADDRESS_LINE_3", null);
		m.put("PTY_TOWN", null);
		m.put("PTY_ADDRESS_LINE_2", null);
		m.put("PTY_POST_CODE", null);
		m.put("PTY_GAINED_POINTS", 800);
		m.put("PTY_PHOTO", "profile-10.jpg");
		m.put("PTY_ACTIVITY_PREFERENCE", "PUBLIC");
		m.put("PTY_STATUS", "ACTIVE");
		findlist.add(m);
		when(customPartyRepository.findFriends(104)).thenReturn(findlist);
		JSONArray resArray = friendsServiceImpl.findFriends(104);
		System.out.println("Find friends "+resArray);
	}

}
