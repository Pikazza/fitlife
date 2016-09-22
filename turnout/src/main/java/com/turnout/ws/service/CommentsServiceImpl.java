package com.turnout.ws.service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.Comments;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.StudiosActivity;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CommentsRepository;
import com.turnout.ws.repository.CustomCommentsRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.StudioActivityRepository;

/**
 * CommentsServiceImpl is class that contains collection of methods that can be accessed for manipulating beacons. All the methods declared in service interface is implemented here.
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
public class CommentsServiceImpl implements CommentsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentsServiceImpl.class);
	private final CommentsRepository cmtrepository;
	private final StudioActivityRepository studioActivityRepository;
	private final PartyRepository partyRepository;
	private final CustomCommentsRepository customCommentsRepository;
	private final PushNotification pushNotification; 
	private final InfoRepository infoRepository;
	
	/**
	 * An injectable constructor with dependencies.
	 * 
	 * @param cmtrepository An Object of cmtrepository as an injectable member.
	 * @param studioActivityRepository An Object of studioActivityRepository as an injectable member.
	 * @param partyRepository An Object of partyRepository as an injectable member.
	 * @param customCommentsRepository An Object of customCommentsRepository as an injectable member.
	 * @param pushNotification An Object of pushNotification as an injectable member.
	 * @param infoRepository An Object of infoRepository as an injectable member.
	 */
	@Autowired
	public CommentsServiceImpl(final CommentsRepository cmtrepository,
			final StudioActivityRepository studioActivityRepository,
			final PartyRepository partyRepository,
			final CustomCommentsRepository customCommentsRepository,
			final PushNotification pushNotification,final InfoRepository infoRepository) {
		this.cmtrepository = cmtrepository;
		this.studioActivityRepository =studioActivityRepository;
		this.partyRepository = partyRepository;
		this.customCommentsRepository = customCommentsRepository;
		this.pushNotification = pushNotification;
		this.infoRepository = infoRepository;
	}
	/**
	 * while add new comment this method will update the likes count.
	 * 
	 * @param sta_id A primary key of studio activity element. It will return true or false message.
	 * @param sta_like_cnt This param used to update like count.
	 * @return Return boolean true or false message.
	 */
	
	@Transactional
	public boolean updateLikeCount(int sta_id, int sta_like_cnt)
	{
		StudiosActivity stdActivity = studioActivityRepository.findByStaId(sta_id);
		int likeCnt = stdActivity.getStaLikeCnt()+sta_like_cnt;
		stdActivity.setStaLikeCnt(likeCnt);
		studioActivityRepository.saveAndFlush(stdActivity);		
		return true;
		
	}
	/**
	 * This method used to save a comments details into database.It will return a added comment details.
	 * 
	 * @param cmtType It has a constant value "COMMENTS".
	 * @param cmtTypeId This integer value holds activity type id.
	 * @param pry_id A primary key of beacon element.
	 * @param cmt_lines This string holds an comment text.
	 * @param cmt_date This parameter holds current system date and time.
	 * @return Returns JSONObject has a value of recently added comments.
	 */
	@Transactional
	@Override
	public JSONObject addComments(String cmtType, int cmtTypeId, int  pry_id, String cmt_lines, Date cmt_date) {
		LOGGER.debug("Comments stated");		
				
		JSONObject jsonRes = new JSONObject();
		if(!(cmtTypeId == 0 || pry_id ==0 || cmt_lines==null || cmt_date==null || cmt_lines.isEmpty() ||cmt_lines.equals("null"))) {
			Comments cmt = new Comments();	
			cmt.setCmtLines(cmt_lines);			
			
			LOGGER.debug("date got "+cmt_date);
			
			cmt.setCmtDate(cmt_date);
			
			cmt.setCmtPtyId(pry_id);
			cmt.setCmtTypeId(cmtTypeId);
			cmt.setCmtType(cmtType);
			cmtrepository.saveAndFlush(cmt);	
			LOGGER.debug("Save Successfully");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String cmtDate = formatter.format(cmt.getCmtDate());
			
			LOGGER.debug("Create json...............");
			
			jsonRes.put("CMT_ID",cmt.getCmtId());			
			jsonRes.put("PTY_ID", pry_id);
			jsonRes.put("CMT_LINES", cmt.getCmtLines());
			jsonRes.put("CMT_DATE", cmtDate);
			
			Map<String, Object> record = new HashMap<String,Object>();
			List<String> tokenlist = new ArrayList<String>();
			
			if(cmtType.equals("COMMENTS"))
			{
				Party p = partyRepository.findOne(pry_id);
				
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
				record = customCommentsRepository.chkpartyDevicetoken(cmtTypeId);
				if(record.size() > 0)
				{
					tokenlist.add(record.get("PTY_DEVICE_TOKEN").toString());
					link= "/checkin/"+record.get("ACTIVITY_ID").toString();
					if(Integer.parseInt(record.get("CMTMEMCNT").toString()) == 1) {
						Info msgObj = infoRepository.findByType("cmt-notification-first");
						message = msgObj.getDescription();
						orgmessage = message.replace("<NAME>", ptyname);
					} else if(Integer.parseInt(record.get("CMTMEMCNT").toString()) == 2) {
						Info msgObj = infoRepository.findByType("cmt-notification-second");
						message = msgObj.getDescription();
						orgmessage = message.replace("<NAME>", ptyname);
					} else {
						cnt = Integer.toString(Integer.parseInt(record.get("CMTMEMCNT").toString())-1);
						Info msgObj = infoRepository.findByType("cmt-notification-morethan2");
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
				notifyList = customCommentsRepository.chkpartyDevicetoken(pry_id, cmtTypeId,"iPhone");
				System.out.println("NOTIFY LIST "+notifyList);
				System.out.println("TOTAL DEIVCE TOKEN"+ notifyList.size());
				if(notifyList.size() > 0)
				{
					System.out.println("S device type is iphone");
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
						 ptyphoto = info.substring(info.indexOf('<')+1);
						 message = record.get("MESSAGES").toString();	
						orgmessage = message.replace(message.substring(message.indexOf('>')+1,message.indexOf('<',message.indexOf('>') )),ptyname);
						 curTime = record.get("CUR_TIME").toString();
						tokenlist.add(record.get("PTY_DEVICE_TOKEN").toString());					}
					
						System.out.println("curTime:::::::"+curTime+"::::::"+orgmessage);
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
				  
				  notifyList = customCommentsRepository.chkpartyDevicetoken(pry_id,cmtTypeId,"Android");
					System.out.println("NOTIFY LIST "+notifyList);
					System.out.println("TOTAL DEIVCE TOKEN"+ notifyList.size());				  
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
							 ptyId=Integer.parseInt(info.substring(0, info.indexOf('<'))); 							
							 ptyname = info.substring(info.indexOf('<')+1, info.indexOf('>'));							
							 ptyphoto = info.substring(info.indexOf('>')+1);							 
							 message = record.get("MESSAGES").toString();								
							 orgmessage = message.replace(message.substring(message.indexOf('>')+1,message.indexOf('<',message.indexOf('>') )),ptyname);
							 curTime = record.get("CUR_TIME").toString();
							 System.out.println("record.get(PTY_DEVICE_TOKEN).toString():::::"+record.get("PTY_DEVICE_TOKEN").toString());
							tokenlist.add(record.get("PTY_DEVICE_TOKEN").toString());
							System.out.println("curTime:::::::"+curTime+orgmessage);
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
				
			*/
					
			
		
			
		} else {
			LOGGER.debug("FAILED");
			jsonRes.put("RESULT",TurnOutConstant.FAILED);
		}
		LOGGER.debug("comments service executed successfully");
		return jsonRes;
	}
	
	/**
	 * This method used to get all the comments from database. Comments are listing with pagination.
	 * 
	 * @param cmtypeid This integer value holds activity type id.
	 * @param cmttype It has a constant value "COMMENTS".
	 * @param pageno current page number. 
	 * @param pagesize Total number of records shown in current page.
	 * @return Returns JSONArray contains all the results of comments.
	 */

	@Override
	public JSONArray getCommentswithPagination(int cmtypeid, String cmttype, int pageno,int pagesize) {	
			JSONArray getallcomments = new JSONArray();
			JSONObject obj;
			Map<String, Object> record;
			List commentslist = customCommentsRepository.getallcomments(cmtypeid,cmttype,pageno,pagesize);
			for (Iterator itr = commentslist.iterator(); itr.hasNext();) {
				record = (Map) itr.next();
				obj = new JSONObject();
				for (Map.Entry<String, Object> entry : record.entrySet()) {					
					obj.put(entry.getKey(), entry.getValue().toString());				
				}
				getallcomments.add(obj);		
		    }
			return getallcomments;	    
	}
}