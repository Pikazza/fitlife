package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.turnout.ws.domain.Comments;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.repository.CommentsRepository;
import com.turnout.ws.repository.CustomCommentsRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.StudioActivityRepository;

/**
 * This is a testing class for CommentsServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking CommentsServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentsServiceImplTest {
	
	@Mock
	private CommentsRepository cmtrepository;
	@Mock
	private StudioActivityRepository studioActivityRepository;	
	@Mock
	private PartyRepository partyRepository;	
	@Mock
	private CustomCommentsRepository customCommentsRepository;	
	
	@Mock
	private PushNotification pushNotification; 
	
	@Mock
	private InfoRepository infoRepository;	
	
	private CommentsServiceImpl commentsServiceImpl;

	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		commentsServiceImpl = new CommentsServiceImpl(cmtrepository, studioActivityRepository, partyRepository, customCommentsRepository, pushNotification, infoRepository);
	}
	
	/**
	 * This test case will test saveBeacon method.
	 * It should get added comment id from database when all of valid comment's details has been passed.
	 */
	@Test
	public void testShouldAddComments() {
		Comments cmt = mock(Comments.class);
		cmt.setCmtLines("test1");			
		cmt.setCmtDate(new Date());
		cmt.setCmtId(3);
		
		cmt.setCmtPtyId(17);
		cmt.setCmtTypeId(3);
		cmt.setCmtType("STUDIOS_ACTIVITY");
		
		when(cmtrepository.saveAndFlush(cmt)).thenReturn(cmt);
		JSONObject res = commentsServiceImpl.addComments("STUDIOS_ACTIVITY", 13, 17, "test1",new Date());		
		System.out.println("Add Comments "+res);
	}
	/**
	 * This test case should test getCommentswithPagination method.
	 * It should get all the details of comments with pagination based on given activity id.
	 */
	@Test
	public void testShouldGetCommentswithPagination() {
		
		List commentlist = new ArrayList();
		Map<String, Object> listedparty;
		listedparty = new HashMap<String,Object>();
		listedparty.put("PTY_NAME", "Anand");
		listedparty.put("CMT_DATE", "2016-04-29 11:50:39");
		listedparty.put("CMT_LINES", "Testing");
		commentlist.add(listedparty);
		listedparty = new HashMap<String,Object>();
		listedparty.put("PTY_NAME", "Sathya");
		listedparty.put("CMT_DATE", "2016-04-27 19:08:29");
		listedparty.put("CMT_LINES", "Push test");
		commentlist.add(listedparty);
		
		when(customCommentsRepository.getallcomments(37, "STUDIOS_ACTIVITY", 1, 10)).thenReturn(commentlist);
		JSONArray clst = commentsServiceImpl.getCommentswithPagination(37, "STUDIOS_ACTIVITY", 1, 10);
		System.out.println("Comments listing "+clst);
		assertNotNull(clst);
	}
}

