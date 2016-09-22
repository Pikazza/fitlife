package com.turnout.ws.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.Comments;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Likes;
import com.turnout.ws.domain.Party;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CommentsRepository;
import com.turnout.ws.repository.CustomLikeRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.LikesRepository;
import com.turnout.ws.repository.PartyRepository;


/**
 * LikesServiceImpl is class that contains collection of methods that can be accessed for manipulating beacons. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
@Service
@Validated
public class LikesServiceImpl implements LikesService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LikesServiceImpl.class);

	 private final LikesRepository likesRepository;	 
	 private final CustomLikeRepository customLikeRepository;	 
	 private final CommentsRepository commentsRepository;
	 private final PartyRepository partyRepository;	 
	 private final PushNotification pushNotification;
	 private final InfoRepository infoRepository;
	 
	 /**
	  * An injectable constructor with a dependencies as argument.
	  * 
	  * @param likesRepository An Object of likesRepository as an injectable member.
	  * @param customLikeRepository An Object of customLikeRepository as an injectable member.
	  * @param commentsRepository An Object of commentsRepository as an injectable member.
	  * @param partyRepository An Object of partyRepository as an injectable member.
	  * @param pushNotification An Object of pushNotification as an injectable member.
	  * @param infoRepository An Object of infoRepository as an injectable member.
	  * @see likesRepository
	  * @see customLikeRepository
	  * @see commentsRepository
	  * @see partyRepository
	  * @see pushNotification
	  * @see infoRepository
	  */
	 @Autowired
	 public LikesServiceImpl(final LikesRepository likesRepository,final CustomLikeRepository customLikeRepository,
			 final CommentsRepository commentsRepository,
			 final PartyRepository partyRepository,
			 final PushNotification pushNotification,final InfoRepository infoRepository) {
		 this.likesRepository = likesRepository;
		 this.customLikeRepository = customLikeRepository;		 
		 this.commentsRepository = commentsRepository;
		 this.partyRepository = partyRepository;
		 this.pushNotification = pushNotification;
		 this.infoRepository = infoRepository;
	}
	
	/**
	 *  Used to add and remove likes for challenges post. It will return primary key like id.
	 * 
	 * @param likeyn it has a constant value "Y /N ".
	 * @param likeType type of the like "COMMENTS / STUDIOS_ACTIVITY".
	 * @param likeTypeId the primary key of studio activity type. 
	 * @param likePtyId the primary key of party.
	 * @return return JSONObject that has a value of like id.
	 */
	@Transactional
	@Override
	public JSONObject addLikes(String likeyn, String likeType, int likeTypeId, int likePtyId) {
		JSONObject lkJson = new JSONObject();
		int likeId=0;
		Map<String, Object> likeMap = customLikeRepository.chkPartyLike(likeTypeId, likeType, likePtyId);
		int chkExist = Integer.parseInt(likeMap.get("CNT").toString());
		
		Likes lk ;
		if(chkExist !=0)
		{
			likeId = Integer.parseInt(likeMap.get("LIKE_ID").toString());
			lk=likesRepository.findOne(likeId);
		}
		else
		{
			lk = new Likes();
		}		
		lk.setLikeYn(likeyn);
		lk.setLikeType(likeType);
		lk.setLikePtyId(likePtyId);
		lk.setLikeTypeId(likeTypeId);		
		likesRepository.saveAndFlush(lk);
		likeId = lk.getLikeId();
		if(likeId == 0)
		{			
			lkJson.put("RESULT", TurnOutConstant.FAILED);
		}
		else
		{
			
			Map<String, Object> record = new HashMap<String,Object>();
			List<String> tokenlist = new ArrayList<String>();
			if(likeyn.equals("Y") && likeType.equals("COMMENTS"))
			{
				Party p = partyRepository.findOne(likePtyId);
				
				
				String message="";
				String cnt = "";
				String ptyFirstname =  p.getPtyName();
				String ptyLAstname = p.getPtyLastName();
				String ptyname = ptyFirstname +" "+ptyLAstname;
				int ptyId =p.getPtyId();
				String ptyphoto =p.getPtyPhoto();
								
				String link="";
				String orgmessage ="";			
				try
				{
				record = customLikeRepository.chkpartyDevicetoken(likeTypeId);
				System.out.println("::::::::::::::size===========::"+record.size());
				if(record.size() > 0 )
				{
					tokenlist.add(record.get("PTY_DEVICE_TOKEN").toString());
					link = "/checkin/"+record.get("ACTIVITY_ID").toString();
					if(Integer.parseInt(record.get("LIKEMEMCNT").toString())==1) {
						Info msgObj = infoRepository.findByType("like-notification-first");
						message = msgObj.getDescription();
						orgmessage = message.replace("<NAME>", ptyname);
					} else if(Integer.parseInt(record.get("LIKEMEMCNT").toString()) == 2) {
						Info msgObj = infoRepository.findByType("like-notification-second");
						message = msgObj.getDescription();
						orgmessage = message.replace("<NAME>", ptyname);
					} else {
						cnt = Integer.toString(Integer.parseInt(record.get("LIKEMEMCNT").toString())-1);
						Info msgObj = infoRepository.findByType("like-notification-morethan2");
						message = msgObj.getDescription();
						message = message.replace("<NAME>", ptyname);
						orgmessage = message.replace("<COUNT>", cnt);					
					}
				
					if(record.get("PTY_DEVICE_TYPE").toString().equals("Android"))				
						pushNotification.sendPushAndroidNotification(tokenlist,orgmessage,ptyId,ptyphoto,link);					
					else if(record.get("PTY_DEVICE_TYPE").toString().equals("iPhone"))
						pushNotification.sendPushIosNotification(tokenlist,orgmessage,ptyId,ptyphoto,link);
					
				}
				}
				catch(EmptyResultDataAccessException e)
				{
					e.printStackTrace();
				}
				
			}
			
			/*
			
			List notifyList = new ArrayList();
			Map<String, Object> record = new HashMap<String,Object>();
			List<String> tokenlist = new ArrayList<String>();
			
			if(likeyn.equals("Y") && likeType.equals("COMMENTS"))
			{
				 notifyList = customLikeRepository.chkpartyDevicetoken(likePtyId, likeTypeId,"iPhone");
				if( notifyList.size() > 0)
				{
					String message="";
					String ptyname="";
					int ptyId =0;
					String ptyphoto ="";
					String curTime ="";
					String info="";
					String orgmessage ="";
					
					for(Iterator itr=notifyList.iterator();itr.hasNext();)
					{
						record = (Map<String, Object>) itr.next();
						info = record.get("INFO").toString();
						ptyId=Integer.parseInt(info.substring(0, info.indexOf('<'))); 
						 ptyname = info.substring(info.indexOf('<')+1, info.indexOf('>'));
						 ptyphoto = info.substring(info.indexOf('>')+1);
						 System.out.println(ptyId+"::::::"+ptyname+"::::::::"+ptyphoto);
						 message = record.get("MESSAGES").toString();
						 orgmessage = message.replace(message.substring(message.indexOf('>')+1,message.indexOf('<',message.indexOf('>') )),ptyname);
						 curTime = record.get("CUR_TIME").toString();
						tokenlist.add(record.get("PTY_DEVICE_TOKEN").toString());	
					}					
					System.out.println(orgmessage);
					System.out.println("tokenlist::::"+tokenlist);
					System.out.println("orgmessage::::"+orgmessage);					
					System.out.println("ptyId::::::"+ptyId);
					System.out.println("ptyphoto::::"+ptyphoto);
					System.out.println("curTime:::::"+curTime);
					
					smtpMailSender.sendPushIosNotification(tokenlist,orgmessage,ptyId,ptyphoto,curTime);							
					
				}	
				else
				{
				  notifyList = customLikeRepository.chkpartyDevicetoken(likePtyId,likeTypeId,"Android");
				  if(notifyList.size() > 0)
					{
					  String message="";
					  String ptyname="";
						int ptyId =0;
						String ptyphoto ="";
						String curTime ="";
						String info="";
						String orgmessage ="";
						
						for(Iterator itr=notifyList.iterator();itr.hasNext();)
						{
							record = (Map<String, Object>) itr.next();
							
							info = record.get("INFO").toString();
							System.out.println(info);
							ptyId=Integer.parseInt(info.substring(0, info.indexOf('<'))); 
							 ptyname = info.substring(info.indexOf('<')+1, info.indexOf('>'));
							 ptyphoto = info.substring(info.indexOf('>')+1);	
							 System.out.println(ptyId+":::::"+ptyname+"::::::"+ptyphoto);
							 message = record.get("MESSAGES").toString();
							 orgmessage = message.replace(message.substring(message.indexOf('>')+1,message.indexOf('<',message.indexOf('>') )),ptyname);
							 curTime = record.get("CUR_TIME").toString();
							tokenlist.add(record.get("PTY_DEVICE_TOKEN").toString());
						}
						System.out.println(orgmessage);
						System.out.println("tokenlist::::"+tokenlist);
						System.out.println("orgmessage::::"+orgmessage);					
						System.out.println("ptyId::::::"+ptyId);
						System.out.println("ptyphoto::::"+ptyphoto);
						System.out.println("curTime:::::"+curTime);
						smtpMailSender.sendPushAndroidNotification(tokenlist,orgmessage,ptyId,ptyphoto,curTime);							
					}
				}
				
			}*/
			lkJson.put("LIKE_ID", likeId);
		}
		return lkJson;		
	}
}