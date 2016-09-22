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

import org.hibernate.validator.internal.util.privilegedactions.GetMethodFromPropertyName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import com.turnout.ws.domain.BeaconMaster;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.StudioPartyActivity;
import com.turnout.ws.domain.StudiosActivity;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.BeaconMasterRepository;
import com.turnout.ws.repository.CustomStudioPartyActivityRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.StudioActivityRepository;
import com.turnout.ws.repository.StudioPartyActivityRepository;
import com.turnout.ws.repository.StudioRepository;


/**
 * StudioPartyActivityServiceImpl is class that contains collection of methods that can be accessed for manipulating party and studio activity relations. All the methods declared in service interface is implemented here.
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
public class StudioPartyActivityServiceImpl implements StudioPartyActivityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudioPartyActivityServiceImpl.class);
	
	private final StudioPartyActivityRepository studioPartyActivityRepository;
	private final CustomStudioPartyActivityRepository customStudioPartyActivityRepository;
	private final StudioRepository studioRepository;
	private final PartyRepository partyRepository;
	private final BeaconMasterRepository beaconMasterRepository;
	private final PushNotification pushNotification; 
	private final StudioActivityRepository studioActivityRepository;
	private final InfoRepository infoRepository;
	private final PartyNotificationRepository partyNotificationRepository;
	
	/**
	 * An injectable constructor with three dependencies as argument.
	 * 
	 * @param studioPartyActivityRepository An Object of studioPartyActivityRepository as an injectable member.
	 * @param customStudioPartyActivityRepository An Object of customStudioPartyActivityRepository as an injectable member.
	 * @param studioRepository An Object of studioRepository as an injectable member.
	 * @param partyRepository An Object of partyRepository as an injectable member.
	 * @param beaconMasterRepository An Object of beaconMasterRepository as an injectable member.
	 * @param pushNotification An Object of pushNotification as an injectable member.
	 * @param studioActivityRepository An Object of studioActivityRepository as an injectable member.
	 * @param infoRepository An Object of infoRepository as an injectable member.
	 * @see studioPartyActivityRepository.
	 * @see customStudioPartyActivityRepository.
	 * @see studioRepository.
	 * @see partyRepository.
	 * @see beaconMasterRepository.
	 * @see pushNotification.
	 * @see studioActivityRepository.
	 * @see infoRepository.
	 * @see partyNotificationRepository
	 */
	@Autowired
	public StudioPartyActivityServiceImpl(final StudioPartyActivityRepository studioPartyActivityRepository,
			final CustomStudioPartyActivityRepository customStudioPartyActivityRepository,
			final StudioRepository studioRepository,final PartyRepository partyRepository,
			final BeaconMasterRepository beaconMasterRepository,final PushNotification pushNotification,
			final StudioActivityRepository studioActivityRepository,
			final InfoRepository infoRepository,final PartyNotificationRepository partyNotificationRepository) {
		this.studioPartyActivityRepository = studioPartyActivityRepository;
		this.customStudioPartyActivityRepository=customStudioPartyActivityRepository;
		this.studioRepository = studioRepository;
		this.partyRepository = partyRepository;
		this.beaconMasterRepository = beaconMasterRepository;
		this.studioActivityRepository = studioActivityRepository;
		this.pushNotification = pushNotification;
		this.infoRepository = infoRepository;
		this.partyNotificationRepository = partyNotificationRepository;
	}
	/**
	 * This method allows to show a party who are interested in given event.
	 * So he can get latest notification about that event.
	 * 
	 * @param stdid An unique id of studio element.
	 * @param stdactid An unique id of studio activity id element.
	 * @param ptyid An unique id of party element.
	 * @param ptystatus It has constant text "ACCEPTED".
	 * @return Returns success message with inserted row id.
	 */
	@Transactional
	public JSONObject addInterestedPartyToEvent(int stdid, int stdactid, int ptyid, String ptystatus) {		
		int stdptyactid = 0;
		JSONObject result = new JSONObject();		
		if(isInterested(ptyid,stdid, stdactid,ptystatus )== false) {
		LOGGER.debug("adding interested partyid"+ ptyid +"into the studio activity id "+ stdactid );
		
		StudioPartyActivity insparty = new StudioPartyActivity();		
		Party party = partyRepository.findOne(ptyid);	
		Studio std = studioRepository.findOne(stdid);	
		
		insparty.setStaId(stdactid);
		insparty.setParty(party);
		insparty.setStudio(std);				   
		Date date = new Date();		  
		Calendar cal = Calendar.getInstance();		
		insparty.setChechinTime(cal.getTime());		
		
		insparty.setPtaStaStatus(ptystatus);		
		
		studioPartyActivityRepository.saveAndFlush(insparty);	
		
		stdptyactid = insparty.getPtyStaId();
		if(stdptyactid != 0)
		{
			result.put("PTY_STA_ID",stdptyactid  );
			
			Map<String, Object> record;
			List<String> androidlist = new ArrayList<String>();
			List<String> iphonelist = new ArrayList<String>();
			String cnt = "";
			String message = "";			
			//String ptyname = party.getPtyName();
			String ptyFirstname =  party.getPtyName();
			String ptyLAstname = party.getPtyLastName();
			String ptyname = ptyFirstname +" "+ptyLAstname;
			
			int ptyId = party.getPtyId();
			String ptyphoto = party.getPtyPhoto();
			List tokenList;
			String link="";
			String activity="";
			String orgmessage ="";	
			Map<String , Object> record1;
			
			record = customStudioPartyActivityRepository.getInterstedUserInfoforNotification(stdactid, stdid, ptystatus);
			String staname  = record.get("STA_NAME").toString();
			
			if( ptystatus.equals("INTERESTED"))
			{
				activity  = "event";
				link = "/home/events/details"+record.get("STA_ID").toString();
			}
			else
			{
				activity  = "challenge";
				link = "/home/challenges/detail/"+record.get("STA_ID").toString();
			}
			if(Integer.parseInt(record.get("MEMCOUNT").toString()) == 1) {
				Info msgObj = infoRepository.findByType("attend-notification-first");
				message = msgObj.getDescription();
				message  = message.replace("<NAME>", ptyname);
				message = message.replace("<ACTIVITYNAME>", staname);
				orgmessage = message.replace("<ACTIVITY>", activity);
			} else if(Integer.parseInt(record.get("MEMCOUNT").toString()) == 2) {
				Info msgObj = infoRepository.findByType("attend-notification-second");
				message = msgObj.getDescription();
				message  = message.replace("<NAME>", ptyname);
				message = message.replace("<ACTIVITYNAME>", staname);
				orgmessage = message.replace("<ACTIVITY>", activity);				
			} else {
				cnt = Integer.toString(Integer.parseInt(record.get("MEMCOUNT").toString())-1);
				Info msgObj = infoRepository.findByType("attend-notification-morethan2");
				message = msgObj.getDescription();				
				message  = message.replace("<NAME>", ptyname);
				message  = message.replace("<COUNT>", cnt);
				message = message.replace("<ACTIVITYNAME>", staname);
				orgmessage = message.replace("<ACTIVITY>", activity);
				
			}
			
			tokenList = new ArrayList();
			tokenList = customStudioPartyActivityRepository.getPartyDeviceToken(ptyid,"Android");
			for(Iterator itr = tokenList.iterator();itr.hasNext();)
			{
				record1 = (Map<String, Object>) itr.next();
				androidlist.add(record1.get("PTY_DEVICE_TOKEN").toString());
			}					
			if(androidlist.size() > 0)
			{
				pushNotification.sendPushAndroidNotification(androidlist,orgmessage,ptyId,ptyphoto,link);
			}
			tokenList = new ArrayList();
			tokenList = customStudioPartyActivityRepository.getPartyDeviceToken(ptyId,"iPhone");
			for(Iterator itr = tokenList.iterator();itr.hasNext();)
			{
				record1 = (Map<String, Object>) itr.next();
				iphonelist.add(record1.get("PTY_DEVICE_TOKEN").toString());
			}
			if(iphonelist.size() > 0)
			{
				pushNotification.sendPushIosNotification(iphonelist,orgmessage,ptyId,ptyphoto,link);
			}		
			
			/*List notifyList = new ArrayList();		
			Map<String, Object> record = new HashMap<String,Object>();
			List tokenlist ;
			List<String> andriodToken,iPhoneToken;
			Map<String, Object> record1 = new HashMap<String,Object>();
			
			String message = "";
			int ptyId = 0;
			String ptyphoto = "";
			String curTime = "";

			notifyList = customStudioPartyActivityRepository.chkpartyDevicetoken(stdptyactid,ptystatus,stdactid,stdid);
			if(!notifyList.isEmpty()) {
				
				for(Iterator itr = notifyList.iterator();itr.hasNext();) {
					record = (Map<String, Object>) itr.next();
					message = record.get("MESSAGES").toString();
					ptyId = Integer.parseInt(record.get("PTY_ID").toString());
					ptyphoto = record.get("PTY_PHOTO").toString();
					curTime = record.get("CUR_TIME").toString();
				}
				tokenlist = customStudioPartyActivityRepository.getPartyDeviceToken(ptyid);				
				if(tokenlist.size() > 0) {					
					iPhoneToken = new ArrayList<String>();
					andriodToken =new ArrayList<String>();
					for(Iterator tokenitr = tokenlist.iterator();tokenitr.hasNext();) {
						record1 = (Map<String, Object>) tokenitr.next();
					
						if(record1.get("PTY_DEVICE_TYPE").toString().equals("iPhone")) {							
							iPhoneToken.add(record1.get("PTY_DEVICE_TOKEN").toString());
						} else {
							andriodToken.add(record1.get("PTY_DEVICE_TOKEN").toString());
						}
					}
					
					
				}
			}
			
		*/}
		else
		{
			result.put("RESULT",  TurnOutConstant.FAILED);
		}			
		LOGGER.debug("adding interested party into the studio activity is done ");				
		}
		else
		result.put("RESULT",  TurnOutConstant.FAILED);
		return result;
	}
	
	/**
	 * This method used to check whether the party already accepted or not.
	 * If already accepted, It will return true message.
	 * If not accepted, It will return false message.
	 * 
	 * @param ptyid an unique id of party element.
	 * @param stdid an unique id of studio element.
	 * @param stdactid an unique id of studio activity id element.
	 * @param status it has constant text "ACCEPTED".
	 * @return return boolean true or false message.
	 */
	@Override
	public boolean isInterested(int ptyid, int stdid, int stdactid,String status) {
		Map<String, Object>  mapinterest  = customStudioPartyActivityRepository.getInterested(ptyid, stdid, stdactid,status);
		int total =  Integer.parseInt(mapinterest.get("TOTAL").toString());
		if(total!= 0){
			return true;		
		} else {
			return false;
		}
	}
	/**
	 * Beacons will call this method when party check in any studio to register their entry.
	 * 
	 * @param bconId beacon id it has mapped with the studio.
	 * @param bconrange beacon detecting range.
	 * @param ptyId  an unique id of party element.
	 * @param checkintime party entering time into studio.
	 * @return returns success message.
	 */
	@Override
	public JSONObject userCheckin(String bconId, double bconrange, int ptyId, Date checkintime) {
		JSONObject result = new JSONObject();
		int stdid = 0;
		String chkStatus = null;
		String stdactId=null;
		int stdactid = 0;
		int ptyactid = 0;
		int stdtimespent=0;
		LOGGER.debug("Checkin TIME <->"+checkintime);
		
		List<BeaconMaster> bmlist = beaconMasterRepository.findByBconId(bconId,new Sort(Direction.DESC,"bconDetectType"));
		
		if(bmlist.size()>0) {			
		for(BeaconMaster bm : bmlist)
		{					
				chkStatus = (bm.getBconStaTypeId() == 1)?"INTERESTED" : "ACCEPTED";
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				String sqlcheckintime = df.format(checkintime);			
				if(bm.getBconDetectType().toString().equals("OWN"))
					stdid = bm.getBconStdId();
				Map<String, Object> activity;
				activity =  customStudioPartyActivityRepository.getActivityIdByTimeandByPartyandByBcondata(ptyId,sqlcheckintime,bm.getBconStdId(),chkStatus);
				if(Integer.parseInt(activity.get("JOINACTIVITYCNT").toString()) > 1 )
				{
					stdactid = 0 ;
					stdactId = activity.get("STA_ID").toString();
					result.put("RESULT", "USER PARTICIPATE MORE THAN ONE CHALLENGE IN THIS STUDIO");
					return result;
				}
				else if(Integer.parseInt(activity.get("JOINACTIVITYCNT").toString()) == 1)
				{
					stdactid = Integer.parseInt(activity.get("STA_ID").toString());
					stdid = bm.getBconStdId();
					break ;
				}
		}
		
		LOGGER.debug(ptyId+"--"+stdid+"--"+stdactid+"--"+checkintime);
				StudioPartyActivity attendParty = new StudioPartyActivity();		
				Party party = partyRepository.findOne(ptyId);	
				Studio std = studioRepository.findOne(stdid);	
				stdtimespent = std.getStdTimeSpent();				
				
				attendParty.setStaId(stdactid);
				attendParty.setParty(party);
				attendParty.setStudio(std);
				attendParty.setChechinTime(checkintime);				
				attendParty.setPtaStaStatus("ATTEND");
				attendParty.setBconId(bconId);
				attendParty.setBconRange(bconrange);
				studioPartyActivityRepository.saveAndFlush(attendParty);				
				ptyactid = attendParty.getPtyStaId();
				
				
				if(ptyactid != 0) {
					result.put("PTY_STA_ID", ptyactid);
					result.put("STD_TIME_SPENT", stdtimespent);
					result.put("CHECKIN_TIME", checkintime);
					LOGGER.debug(result.toString());
					
					
					
					List<String> tokenlist = new ArrayList<String>() ;					
					String orgMessage = "";
					Info msgObj = infoRepository.findByType("checked-in");
					String message = msgObj.getDescription();					
					message = message.replace("<NAME>", party.getPtyName()+" "+party.getPtyLastName());
					orgMessage = message.replace("<STUDIONAME>", std.getStdName());					
					String photo = std.getStdCompanyLogo();
					
					String link = "/welcome-notification/"+bconId+"/W";
					
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Calendar cal = Calendar.getInstance();				
					String curTime =dateFormat.format(cal.getTime());
					tokenlist.add(party.getPtyDeviceToken().toString());
					
					
					if(party.getPtyDeviceType().equals("iPhone"))
						pushNotification.sendPushIosNotification(tokenlist,orgMessage,0,photo,link);
					else if(party.getPtyDeviceType().equals("Android"))
						pushNotification.sendPushAndroidNotification(tokenlist,orgMessage,0,photo,link);
														
				} else {
					result.put("RESULT", TurnOutConstant.FAILED);
				}
			}      		
		else
		{
			result.put("RESULT", "BCONID NOT MAPPED WITH ANY STUDIO/UNKNOWN BECONID");
		}		
	
		return result;
	}

	/**
	 * Beacons will call this method when party check out any studio to register their entry.
	 * 
	 * @param ptystaId an unique id of studio party activity element.
	 * @param checkouttime party exist time from the studio.
	 * @return returns success message.
	 */
  @Override
	public JSONObject userCheckout(int ptystaId, Date checkouttime) {
		int ptystaid = 0;
		int points = 0;
		long timespent = 0;
		int stdactid = 0;
		Date checkin;
		String studioname="";
		String bconid="";
		Party p = null ;
		int partyid=0;
		
		JSONObject result = new JSONObject();
		StudioPartyActivity spa = studioPartyActivityRepository.findOne(ptystaId);
		if(spa != null) {
			LOGGER.debug(spa.getCheckoutTime()+"studiopartyactivity"+spa);
						
				if(spa.getCheckoutTime() == null && spa.getPtaStaStatus().equals("ATTEND"))
				{
					stdactid = spa.getStaId();
					checkin = spa.getChechinTime();
					bconid = spa.getBconId();
					Studio std = spa.getStudio();			
					timespent =  std.getStdTimeSpent();
					points = std.getStdPoints();
					studioname = std.getStdName();
					
					spa.setCheckoutTime(checkouttime);
					
					long diffMs = checkouttime.getTime() - checkin.getTime();
					long diffSec = diffMs / 1000;
					long min = diffSec / 60;
					long sec = diffSec % 60;
					
					LOGGER.debug(timespent+"difference:::::::::::;"+min+":::::::"+sec);
					/*points= (timespent < min) ? 0 : points ;*/
					LOGGER.debug("timedifference-----------------:::::"+(min-timespent));
					
					if( (min-timespent) < 0)
					{
						points = 0;
					}
					
					spa.setGainedPoints(points);
					p = spa.getParty();
					int ptypoins = p.getPtyGainedPoints();
					partyid = p.getPtyId();
					p.setPtyGainedPoints((points+ptypoins));
					partyRepository.save(p);
					
					studioPartyActivityRepository.saveAndFlush(spa);
					ptystaid = spa.getPtyStaId();
				}
		}
		if(ptystaid !=0)
		{
			result.put("PTY_STA_ID", ptystaid);
			result.put("PTY_POINTS", points);
			PartyNotification pn =  partyNotificationRepository.findByNotifyPtyId(partyid);
			if(pn.getNotifyPersonalPointsCrdt().equals("Y"))
			{			
				List<String> tokenlist = new ArrayList<String>() ;					
				String orgMessage = "";
				String message = "";
				
				if(stdactid == 0)
				{
					Info msgObj = infoRepository.findByType("points-credited-class");
					message = msgObj.getDescription();
					message = message.replace("<NOOFPOINTS>", Integer.toString(points));
					orgMessage = message.replace("<STUDIONAME>", studioname);							
				}
				else
				{
					StudiosActivity activity  = studioActivityRepository.findByStaId(stdactid);
					Info msgObj = infoRepository.findByType("points-credited-activity");
					message = msgObj.getDescription();				
					message = message.replace("<NOOFPOINTS>", Integer.toString(points));
					message = message.replace("<ACTIVITYNAME>", activity.getStaName().toString());
					orgMessage = message.replace("<STUDIONAME>", studioname);
				}
								
				String photo = "img/Points.png";
				String link = "/welcome-notification/"+bconid+"/E/"+points;			
				tokenlist.add(p.getPtyDeviceToken().toString());
				
				if(p.getPtyDeviceType().equals("iPhone"))
					pushNotification.sendPushIosNotification(tokenlist,orgMessage,0,photo,link);
				else if(p.getPtyDeviceType().equals("Android"))
				pushNotification.sendPushAndroidNotification(tokenlist,orgMessage,0,photo,link);
			}
		}
		else
			result.put("RESULT", TurnOutConstant.FAILED);
		
		return result;
	}
  
  /**
   * This method will return total points the party has earned so far. 
   * 
   * @param partyid An unique id of party.
   * @return Returns total gained points of given party.
   */
	@Override
	public JSONObject myProfile(int partyid) {
		JSONObject result = new JSONObject();
		JSONArray historyjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		Party party = partyRepository.findOne(partyid);
		result.put("PTY_ID", party.getPtyId());
		result.put("PTY_NAME", party.getPtyName());
		result.put("PTY_LAST_NAME", party.getPtyLastName());
		result.put("PTY_PHOTO", party.getPtyPhoto());
		result.put("PTY_GAINED_POINTS", party.getPtyGainedPoints());
		List myhistory = customStudioPartyActivityRepository.myProfile(partyid);
		if(myhistory != null && myhistory.size() > 0)
		{
			for(Iterator itr=myhistory.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
					object.put(entry.getKey(), entry.getValue());	
				}
				historyjson.add(object);
			}
		}
		result.put("HISTORY", historyjson);
		return result;
	}
	
	/**
	 * All of accepted challenge will be listed here based on given party id.
	 * 
	 * @param partyid an unique id of party.
	 * @return returns list of accepted challenge as a JSONArray.
	 */
	@Transactional
	@Override
	public JSONArray myChallenge(int partyid) {
		
		JSONArray challenges = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		List mychallenge = customStudioPartyActivityRepository.myChallenge(partyid);
		if(mychallenge != null && mychallenge.size() > 0)
		{
			for(Iterator itr=mychallenge.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
					if(entry.getKey().equals("GAINED_TASK_BAGE"))				
						object.put(entry.getKey(), getBadges(entry.getValue().toString()));	
					else
						object.put(entry.getKey(), entry.getValue());	
				}
				challenges.add(object);
			}
			
		}	
		return challenges;
	  }
	/**
	 * This method used to split the badges description and images by using tidle(~) symbol.
	 * The seperated information will store into two new json object.
	 * 
	 * @param badgeDet Badge description text.
	 * @return return jsonobject contain badge details.
	 */
	public JSONArray getBadges(String badgeDet) {
		String Str = badgeDet;
		JSONArray jarray = new JSONArray();
		JSONObject obj;
		if(!(Str.equals("")|| Str.equalsIgnoreCase("null") ||!(Str.contains("~")))) {
				obj = new JSONObject();
				String img = Str.substring(0, Str.indexOf("~"));
				String desc = Str.substring(Str.indexOf("~")+1);	         
				obj.put("IMG", img);
				obj.put("DESC", desc);
			    jarray.add(obj);
		}
		return jarray;		
	}	
	
	/**
	 * It will get a list of badges a party has already won.
	 * 
	 * @param partyid An unique id of party.
	 * @return returns list of badges a party has won.
	 */
	@Transactional
	@Override
	public JSONArray myBadges(int partyid) {
		JSONArray badges = new JSONArray();
		Map<String, Object> record;
		JSONObject object;
		List myBadges = customStudioPartyActivityRepository.myBadges(partyid);
		if(myBadges != null && myBadges.size()>0)
		{
			for(Iterator itr=myBadges.iterator();itr.hasNext();)
			{
				object = new JSONObject();
				record = (Map) itr.next();
				for(Map.Entry<String, Object> entry : record.entrySet())
				{
					if(entry.getKey().equals("GAINED_TASK_BAGE"))
					{
						String Str=entry.getValue().toString();				
						if(!(Str.equals("")|| Str.equalsIgnoreCase("null") ||!(Str.contains("~")))) {
							String img = Str.substring(0, Str.indexOf("~"));
							String desc = Str.substring(Str.indexOf("~")+1);	         
							object.put("BADGE_IMG", img);
							object.put("BADGE_DESC", desc);
						}
					}
					else
					{
						object.put(entry.getKey(), entry.getValue());
					}
					
				}
				badges.add(object);
			}
			
		}
		return badges;
	}
	/**
	 * It will get one or more participants who are actively accepted or attended given challenge based on their name.
	 * 
	 * @param name  the name of the participants.
	 * @param staid A challenge's id on which participants search will take place.
	 * @return return jsonarray contains all the participant information.
	 */
	public JSONArray participantsSearch(String name, int staid) {

		JSONArray listarr = new JSONArray();
		JSONObject listobj;
		Map<String, Object> record;
		
		List rs = customStudioPartyActivityRepository.participantsSearch(name, staid);
		if( rs.size() != 0) {
			for (Iterator itr = rs.iterator(); itr.hasNext();) {
				record = (Map) itr.next();
				listobj = new JSONObject();
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					listobj.put(entry.getKey(), entry.getValue());
				}
				listarr.add(listobj);		
		    }
		}/* else {
			listobj = new JSONObject();
			listobj.put("Records", TurnOutConstant.NOT_EXIST);
			listarr.add(listobj);
		}*/		
		return listarr;
	}
	/**
	 * This function used for selecting winners from list of participants who are all finished their challenge.
	 * 
	 * @param ptyId An unique id of party element.
	 * @param staId An unique id of studio activity element.
	 * @param staStdId An unique id of studio element.
	 * @param checkintime Winner selected time.
	 * @param gainedTaskBage 
	 * @return returns challenge's id on which new winner has been selected.
	 */
	@Override
	public JSONObject selectWinner(int ptyId,int staId,int staStdId,Date checkintime,String gainedTaskBage) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();		
		StudioPartyActivity insparty = new StudioPartyActivity();
		Party party = partyRepository.findOne(ptyId);
		Studio std = studioRepository.findOne(staStdId);
		
		insparty.setStaId(staId);
		insparty.setParty(party);
		insparty.setStudio(std);
		insparty.setPtaStaStatus("WONBADGE");
		insparty.setChechinTime(checkintime);
		insparty.setGainedTaskBadge(gainedTaskBage);
		
		insparty.setBconId("0");
		insparty.setBconRange(0);	
			
		studioPartyActivityRepository.saveAndFlush(insparty);	
		int ptyStaId = insparty.getPtyStaId();
		LOGGER.debug("Insert end here "+ ptyStaId);
		if(ptyStaId != 0) {
			result.put("PTY_STA_ID", ptyStaId);
		}
		else {
			result.put("RESULT", TurnOutConstant.FAILED);	
		}
		return result;
	}	
}