package com.turnout.ws.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import com.turnout.ws.domain.Comments;
import com.turnout.ws.domain.Likes;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.CommentsRepository;
import com.turnout.ws.repository.CustomLikeRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.LikesRepository;
import com.turnout.ws.repository.PartyRepository;

/**
 * This is a testing class for LikesServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking LikesServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LikesServiceImplTest {
	
	@Mock
	private LikesRepository likesRepository;
	@Mock
	private CustomLikeRepository customLikeRepository;
	@Mock
	private CommentsRepository commentsRepository; 	
	@Mock
	private PartyRepository partyRepository;	
	@Mock
	private  PushNotification pushNotification;
	
	@Mock
	private  InfoRepository infoRepository;	

	private LikesServiceImpl likesServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		likesServiceImpl = new LikesServiceImpl(likesRepository, customLikeRepository, 
				commentsRepository, partyRepository,pushNotification,infoRepository);
	}	
	/**
	 * This test case will test addLikes method.
	 * It should get added like id and number of total likes from database when all of valid like details including challenges has been passed.
	 */
	@Test
	public void testShouldAddLikes() {
		
		Likes lk = mock(Likes.class);
		lk.setLikeYn("Y");
		lk.setLikeType("STUDIOS");
		lk.setLikePtyId(2);
		lk.setLikeTypeId(2);	
		Map<String, Object> likeMap;
		likeMap = new HashMap<String,Object>();
		likeMap.put("CNT", 0);
		when(customLikeRepository.chkPartyLike(2,"STUDIOS",2)).thenReturn(likeMap);
		when(likesRepository.save(any(Likes.class))).thenReturn(lk);
		JSONObject res = likesServiceImpl.addLikes("Y", "STUDIOS", 2, 2);
		System.out.println(res);
	}

}
