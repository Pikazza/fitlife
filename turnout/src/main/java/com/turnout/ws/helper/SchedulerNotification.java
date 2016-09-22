package com.turnout.ws.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.turnout.ws.domain.Info;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.repository.CustomStudioActivityRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.service.InfoService;
import com.turnout.ws.service.StudioActivityService;
import com.turnout.ws.service.StudioService;

/**
 * 
 *
 */
@Component
public class SchedulerNotification {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerNotification.class);
	
	private final CustomStudioActivityRepository customStudioActivityRepository;
	private final PushNotification pushNotification; 
	private final InfoRepository infoRepository;
	
	/**
	 * An injectable constructor with three dependencies as argument.
	 * 
	 * @param customStudioActivityRepository An Object of CustomStudioActivityRepository as an injectable member.
	 * @param pushNotification An Object of PushNotification as an injectable member.
	 * @param infoRepository An Object of InfoRepository as an injectable member.
	 * @see CustomStudioActivityRepository
	 * @see PushNotification
	 * @see InfoRepository
	 */
	@Autowired
	public SchedulerNotification(final CustomStudioActivityRepository customStudioActivityRepository,
			final PushNotification pushNotification,final InfoRepository infoRepository) {
		this.customStudioActivityRepository = customStudioActivityRepository;
		this.pushNotification = pushNotification ;
		this.infoRepository = infoRepository;
	}
	
	

	/**
	 * 
	 * This method will trigger push notification to the user to remind their interested events.
	 * This will run based on the scheduler and push notification will be send to both android and ios devices.
	 */
	@Scheduled(cron = "0 20 9 * * *")
	public void evntRemainder()
	{
		LOGGER.debug("Event Remainder");
		List<String> iphoneList = new ArrayList();
		List<String> androidList = new ArrayList();
		List eventList = new ArrayList();
		List tokenList;
		Map<String, Object> record = new HashMap<String,Object>();
		Map<String, Object> record1 ;
				
			String photo ="img/Reminder.png";
			eventList = customStudioActivityRepository.getEventfornotification();
			//String message = propsControl.getEventRemainder();
			Info msgObj = infoRepository.findByType("event-reminder");
			String message = msgObj.getDescription();
			String orgMessage ="";
			String link = "";
			if(eventList.size()>0)
			{
				for(Iterator itr=eventList.iterator();itr.hasNext();)
				{
					record = (Map<String, Object>) itr.next();			
					message = message.replace("<EVENTNAME>", record.get("STA_NAME").toString());
					orgMessage = message.replace("<TIME>", record.get("STA_START_TIME").toString());
					link= "/home/events/details/"+record.get("STA_ID").toString();
					tokenList = customStudioActivityRepository.evntRemainder(Integer.parseInt(record.get("STA_ID").toString()), Integer.parseInt(record.get("STD_ID").toString()),"Android");
					record1 =  new HashMap<String,Object>();
					for(Iterator itr1 = tokenList.iterator();itr1.hasNext();)
					{
						record1 = (Map<String, Object>) itr1.next();
						androidList.add(record1.get("PTY_DEVICE_TOKEN").toString());
					}					 
					LOGGER.debug("androidList:::::::::::::"+androidList);
					
					if(androidList.size() > 0)
						pushNotification.sendPushAndroidNotification(androidList,orgMessage,0,photo,link);
				
					tokenList = customStudioActivityRepository.evntRemainder(Integer.parseInt(record.get("STA_ID").toString()), Integer.parseInt(record.get("STD_ID").toString()),"iPhone");
					record1 =  new HashMap<String,Object>();
					for(Iterator itr1 = tokenList.iterator();itr1.hasNext();)
					{
						record1 = (Map<String, Object>) itr1.next();
						iphoneList.add(record1.get("PTY_DEVICE_TOKEN").toString());
					}					
					LOGGER.debug("iphoneList:::::::::::::"+iphoneList);
					
					if(iphoneList.size() > 0)
						pushNotification.sendPushIosNotification(iphoneList,orgMessage,0,photo,link);
				}
					
			}		
			
			
	}
	

	/**
	 * This method will trigger push notification to the user to remind their total gained points is more enough to redeem a reward.
	 * This will run based on the scheduler and push notification will be send to both android and ios devices.
	 */
	//@Scheduled(cron = "0 */5 * * * *")
	@Scheduled(cron = "0 20 10 * * *")
	public void reedemEligible()
	{
		LOGGER.debug("reedemEligible Scheduler");
		List tokenList;
		List<String> iphoneList = new ArrayList();
		List<String> androidList = new ArrayList();		
		Map<String, Object> record = new HashMap<String,Object>();
		
			String photo ="img/Reward.png";		
			
			Info msgObj = infoRepository.findByType("reedem-eligible");
			String orgMessage = msgObj.getDescription();			
			
			String link = "/home/rewards";
			tokenList = new ArrayList<>();
			tokenList = customStudioActivityRepository.reedemEligible("iPhone");
			for(Iterator itr = tokenList.iterator();itr.hasNext();)
			{
				record = (Map<String, Object>) itr.next();
				iphoneList.add(record.get("PTY_DEVICE_TOKEN").toString());
			}
			
			LOGGER.debug(iphoneList.toString());
			if(iphoneList.size() > 0)
				pushNotification.sendPushIosNotification(iphoneList,orgMessage,0,photo,link);
			tokenList = new ArrayList<>();
			tokenList = customStudioActivityRepository.reedemEligible("Android");
			for(Iterator itr = tokenList.iterator();itr.hasNext();)
			{
				record = (Map<String, Object>) itr.next();
				androidList.add(record.get("PTY_DEVICE_TOKEN").toString());
			}			 
			LOGGER.debug(androidList.toString());
			if(androidList.size() > 0)
				pushNotification.sendPushAndroidNotification(androidList,orgMessage,0,photo,link);
	
	}

}
