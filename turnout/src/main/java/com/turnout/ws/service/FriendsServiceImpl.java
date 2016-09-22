package com.turnout.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turnout.ws.controller.FriendsController;
import com.turnout.ws.domain.Friends;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.FriendsRepository;


/**
 * FriendsServiceImpl is class that contains collection of methods that can be accessed for manipulating friends related informations. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
@Service
public class FriendsServiceImpl implements FriendsService {
	private static final Logger LOGGER=LoggerFactory.getLogger(FriendsServiceImpl.class);
	private final FriendsRepository friendsRepository;
	private final CustomPartyRepository customPartyRepository;
	
	/**
	 * An injectable constructor with a dependency of friendsRepository,customPartyRepository as argument.
	 * 
	 * @param friendsRepository An Object of friendsRepository as an injectable member.
	 * @param customPartyRepository An Object of customPartyRepository as an injectable member.
	 */
	@Autowired
	public FriendsServiceImpl(final FriendsRepository friendsRepository,final CustomPartyRepository customPartyRepository) {
		this.friendsRepository = friendsRepository;
		this.customPartyRepository = customPartyRepository;
	}
	/**
	 * Its used for follow and unfollow the friend request.
	 * 
	 * @param frndId1 Friend request send party id.
	 * @param frndId2 Follower id.
	 * @param frndStatus requesting status.
	 * @return Returns JSONObject that has a value of success or error message.
	 */
	@Override
	public JSONObject addfollower(int frndId1, int frndId2, String frndStatus) {
		JSONObject result = new JSONObject();
		Friends frnd1 = friendsRepository.findByFrndId1AndFrndId2(frndId1, frndId2);
		int fId1 =0,fId2=0;
		if(frnd1 == null)
		{			
			Friends frnd2 = friendsRepository.findByFrndId1AndFrndId2(frndId2, frndId1);
			if(frnd2 == null)
			{				
				Friends frnd = new Friends();
				frnd.setFrndId1(frndId1);
				frnd.setFrndId2(frndId2);
				frnd.setFrndStatus(frndStatus);
				friendsRepository.saveAndFlush(frnd);
				fId1 = frnd.getFrndId1();
				fId2 = frnd.getFrndId2();
			}
			else {	
				if(frndStatus.equals("UNFOLLOW"))
				{
					fId1 = frnd2.getFrndId1();
					fId2 = frnd2.getFrndId2();
					friendsRepository.delete(frnd2);
					
				}
				else
				{
					frnd2.setFrndStatus(frndStatus);
					friendsRepository.saveAndFlush(frnd2);
					fId1 = frnd2.getFrndId1();
					fId2 = frnd2.getFrndId2();
				}
			}
			
		}
		else
		{	
			if(frndStatus.equals("UNFOLLOW"))
			{
				fId1 = frnd1.getFrndId1();
				fId2 = frnd1.getFrndId2();
				friendsRepository.delete(frnd1);
				
			}
			else
			{
				frnd1.setFrndStatus(frndStatus);
				friendsRepository.saveAndFlush(frnd1);
				fId1 = frnd1.getFrndId1();
				fId2 = frnd1.getFrndId2();
			}
		}
		if(fId1 != 0 && fId2 != 0)
		{
			result.put("RESULT", TurnOutConstant.SUCCESS);
		}
		else
		{
			result.put("RESULT", TurnOutConstant.FAILED);
		}		
		
		return result;
	}
	/**
	 * To get all pending and accepted friends list.
	 * 
	 * @param ptyid An primary key of party.
	 * @return Returns JSONObject contains all the friends that the party accepted.
	 */
	@Override
	public JSONObject getFriendsList(int ptyid) {
		JSONObject friends = new JSONObject();
		List friendsList = customPartyRepository.getFriendsList(ptyid);		
		JSONArray friendsjson = new JSONArray();
		JSONArray requestfrnd = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(friendsList != null)
		{
			for(Iterator itr=friendsList.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();
				if(recrod.containsValue("REQUEST"))
				{
					for (Map.Entry<String, Object> entry : recrod.entrySet())
					{				
						object.put(entry.getKey(), entry.getValue());
					}
					requestfrnd.add(object);
				}
				else if(recrod.containsValue("FOLLOW"))
				{
					for (Map.Entry<String, Object> entry : recrod.entrySet())
					{				
						object.put(entry.getKey(), entry.getValue());
					}
					friendsjson.add(object);
				}
				
			}
			friends.put("REQUEST", requestfrnd);
			friends.put("FRIENDS", friendsjson);
		}	
		return friends;
	}
	/**
	 * This method used to get all the friends from database and helps to search the friends based on passed string.
	 * 
	 * @param  partyid A integer has a value of party id.
	 * @return returns JSONArray contains all the friends. 
	 */
	@Transactional
	public JSONArray findFriends(int partyid) {
		List friendsList = customPartyRepository.findFriends(partyid);
		LOGGER.debug("Friends List array "+friendsList);
		JSONArray friendsjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(friendsList != null) {
			for(Iterator itr=friendsList.iterator();itr.hasNext();) {
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet()) {
					object.put(entry.getKey(), entry.getValue());
				}
				friendsjson.add(object);
			}
		}	
		return friendsjson;
	}
}
