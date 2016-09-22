package com.turnout.ws.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.Likes;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.StudioActivityType;
import com.turnout.ws.domain.StudiosActivity;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CustomCommentsRepository;
import com.turnout.ws.repository.CustomLikeRepository;
import com.turnout.ws.repository.CustomStudioActivityRepository;
import com.turnout.ws.repository.LikesRepository;
import com.turnout.ws.repository.StudioActivityRepository;
import com.turnout.ws.repository.StudioRepository;

/**
 * StudioActivityServiceImpl is class that contains collection of methods that can be accessed for manipulating activities. All the methods declared in service interface is implemented here.
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
public class StudioActivityServiceImpl implements StudioActivityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudioActivityServiceImpl.class);

	
	private final CustomStudioActivityRepository customStudioActivityRepository;
	
	private final StudioActivityRepository studioActivityRepository;
	
	private final CustomCommentsRepository customcmtrepository;
	
	private final CustomLikeRepository customLikeRepository;
	
	private final LikesRepository likesRepository;
	
	private final StudioRepository studioRepository;
	
	
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param customStudioActivityRepository An Object of customStudioActivityRepository as an injectable member.
	 * @param studioActivityRepository       An Object of studioActivityRepository as an injectable member.
	 * @param customcmtrepository            An Object of customcmtrepository as an injectable member.
	 * @param customLikeRepository           An Object of customLikeRepository as an injectable member.
	 * @param likesRepository                An Object of likesRepository as an injectable member.
	 * @param studioRepository               An Object of studioRepository as an injectable member.
	 */
	
	@Autowired
	public StudioActivityServiceImpl(final CustomStudioActivityRepository customStudioActivityRepository,
			final StudioActivityRepository studioActivityRepository,
			final CustomCommentsRepository customcmtrepository,
			final CustomLikeRepository customLikeRepository,
			final LikesRepository likesRepository,
			final StudioRepository studioRepository) {
		this.customStudioActivityRepository=customStudioActivityRepository;
		this.studioActivityRepository = studioActivityRepository;
		this.customcmtrepository = customcmtrepository;
		this.customLikeRepository = customLikeRepository;
		this.likesRepository = likesRepository;
		this.studioRepository = studioRepository;
	}
	
	/**
	 * This method used to get all the active events from the database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONArray contains all the events details.
	 */
	@Transactional
	public JSONArray getListedActivity(int pageno, int pagesize) {
		JSONArray jsonArray = new JSONArray();
		List activity = customStudioActivityRepository.getListedActivity(pageno,pagesize);

		if (activity != null && activity.size() > 0)
			jsonArray = buildJsonArray(activity);
		return jsonArray;
	}
	/**
	 * This function used to convert list into json array
	 * 
	 * @param activity List array contains the values.
	 * @return Returns JSONArray values.
	 */
	
	@Transactional
	private JSONArray buildJsonArray(List activity)
	{
		LOGGER.debug("testing");
		JSONArray activityjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(activity != null)
		{
			for(Iterator itr=activity.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
				object.put(entry.getKey(), entry.getValue());	
				}
				activityjson.add(object);
			}
		}
		else
		{
			object = new JSONObject();
			object.put("RESULT", TurnOutConstant.NOT_EXIST);
			activityjson.add(object);
		}
		LOGGER.debug(activityjson.toString());
		
		return activityjson;
	}
	/**
	 * This method used to get event details from database based on the activity id passing.
	 * 
	 * @param eventid an unique id of studio activity element.
	 * @param partyid an unique id party element.
	 * @return JSONObject it will contains specific event details.
	 */

	public JSONObject getActivityDetails(int eventid,int partyid) {
		Map<String, Object> evnt = customStudioActivityRepository.getActivityDetails(eventid,TurnOutConstant.STUDIOS_ACTIVITY);			
		JSONObject evntjson = new JSONObject();		
		evntjson.put("STA_ID", evnt.get("STA_ID"));
		evntjson.put("STA_NAME", evnt.get("STA_NAME"));
		evntjson.put("STD_ID", evnt.get("STD_ID"));
		evntjson.put("STA_DESCRIPTION", evnt.get("STA_DESCRIPTION"));		
		evntjson.put("START_DATE", evnt.get("START_DATE"));
		evntjson.put("START_TIME", evnt.get("START_TIME"));
		evntjson.put("END_DATE", evnt.get("END_DATE"));
		evntjson.put("END_TIME", evnt.get("END_TIME"));		
		evntjson.put("STA_GOAL_POINTS", evnt.get("STA_GOAL_POINTS"));
		evntjson.put("STA_TYPE_ID", evnt.get("STA_TYPE_ID"));
		String status = (evnt.get("STA_TYPE_ID").toString().equals("1"))?"INTERESTED":"ACCEPTED";
		evntjson.put("STA_PRICE", evnt.get("STA_PRICE"));
		evntjson.put("STA_IMAGE_URL", evnt.get("STA_IMAGE_URL"));		
		evntjson.put("STA_ADDRESS_LINE1", evnt.get("STA_ADDRESS_LINE1"));
		evntjson.put("STA_ADDRESS_LINE2", evnt.get("STA_ADDRESS_LINE2"));
		evntjson.put("STA_ADDRESS_LINE3", evnt.get("STA_ADDRESS_LINE3"));		
		evntjson.put("STA_TOWN", evnt.get("STA_TOWN"));
		evntjson.put("STA_COUNTY", evnt.get("STA_COUNTY"));
		evntjson.put("STA_COUNTRY", evnt.get("STA_COUNTRY"));
		evntjson.put("STA_POST_CODE", evnt.get("STA_POST_CODE"));
		evntjson.put("STA_LATITUDE", evnt.get("STA_LATITUDE"));
		evntjson.put("STA_LONGITUDE", evnt.get("STA_LONGITUDE"));
		evntjson.put("STA_EXTERNAL_LINK", evnt.get("STA_EXTERNAL_LINK"));	
		evntjson.put("STA_LOCATION", evnt.get("STA_LOCATION"));
		evntjson.put("STD_NAME", evnt.get("STD_NAME"));
		evntjson.put("STD_COMPANY_LOGO", evnt.get("STD_COMPANY_LOGO"));
		evntjson.put("CMTS_CNT", evnt.get("CMTS_CNT"));		
		evntjson.put("STA_LIKE_CNT", evnt.get("LIKE_CNT"));
		evntjson.put("STA_INFLUENCER",getInfluencer(evnt.get("STA_INFLUENCER").toString()));
		evntjson.put("INTRESTED_USER", getInterestedParty(eventid,status))	;
		evntjson.put("STA_EXPIRY_DATE", evnt.get("STA_EXPIRY_DATE"));
		evntjson.put("STA_STATUS", evnt.get("STA_STATUS"));
		
		if(Integer.parseInt(evnt.get("STA_TYPE_ID").toString()) == 1) {			
		//	evntjson.put("COMMENTS", listComments(eventid,TurnOutConstant.STUDIOS_ACTIVITY));			
			evntjson.put("PTY_LIKED", chkPartyLike(eventid,partyid));
		} else {	
			evntjson.put("STA_SHORT_DESCRIPTION", evnt.get("STA_SHORT_DESCRIPTION"));	
			evntjson.put("STA_BADGE", getBadges(evnt.get("STA_BADGE").toString()));
		}	
		return evntjson;
	}
	
	/**
	 * 
	 * 
	 * @param eventid An unique studio activity id
	 * @param partyid An unique party id.
	 * @return Return string Yes or no
	 */
	private String chkPartyLike(int eventid, int partyid) {
		String partyLiked= "NO";
		Map<String, Object> lkMap=customLikeRepository.chkPartyLike(eventid,TurnOutConstant.STUDIOS_ACTIVITY,partyid);
		int chkCnt =  Integer.parseInt(lkMap.get("CNT").toString());	
		LOGGER.debug("chkCnt:::::"+chkCnt);
		if(chkCnt != 0 )
		{
			String chkLikeStatus = lkMap.get("LIKE_YN").toString();
			LOGGER.debug("chkLikeStatus:::::"+chkLikeStatus);
			partyLiked = chkLikeStatus.equals("Y")?"YES":"NO";
		}
		return partyLiked;
		
	}

	public JSONArray getInfluencer(String influencerDet){
		String Str = influencerDet;
		JSONArray jarray = new JSONArray();
		JSONObject obj;
		if(!(Str.equals("")|| Str.equalsIgnoreCase("null") ||!(Str.contains("~")))){
		for (String retval: Str.split("%")){
			obj = new JSONObject();
			String img = retval.substring(0, retval.indexOf("~"));
			String desc = retval.substring(retval.indexOf("~")+1);	         
	         obj.put("IMG", img);
	         obj.put("DESC", desc);
	         jarray.add(obj);
	      }
		}
		
		
		return jarray;
		
	}

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
	public JSONArray getInterestedParty(int eventid, String status)
	{
		List partylist = customStudioActivityRepository.getInterestedParty(eventid,status);
		
		JSONArray partyjson = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;
		
		if(partylist != null)
		{
			for(Iterator itr=partylist.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{
				object.put(entry.getKey(), entry.getValue());	
				}
				partyjson.add(object);
			}
		}		
		return partyjson;
		
	}
	/**
	 * Used to check whether the studio based activity exist or not.
	 * If already data exist it will return true otherwise return false.
	 * 
	 * @param stdid an unique id of studio id.
	 * @param stdactid an unique id of studio activity id.
	 * @return it will return boolean value true or false.
	 */

	@Override
	public boolean isStudiosActivityExist(int stdid, int stdactid) {
		
		StudiosActivity studiosActivity= studioActivityRepository.findByStdIdAndStaId(stdid, stdactid);		

		if(studiosActivity == null){
			return false;			
		}		
		return true;
	}

/*	@Override
	public JSONObject addStudioActivity(String staname, int stdid, String stadesc,String staShotDesc, Date stastartdate, Date staenddate,
			Date stacredate, Date stamoddate, int staupdby, int stagoalpoints, String stabatch, int statypeid,
			String staimage, String stainfluen,int stalikes, double staprice, String staaddr1, String staaddr2, String staaddr3, String statown,
			String stacounty, String stacountry, String stapostcode, float stalet, float stalong, Date stastarttime, Date staendtime, String staextlink,
			String stalocation, Date staExpiryDate, String stastatus,int staNoDays) {
		
		
		JSONObject activity=new JSONObject();
		
		if(staname!=null || stdid!=0){	
			
		StudiosActivity sa=new StudiosActivity();
		sa.setStaName(staname);
		sa.setStdId(stdid);
		sa.setStaDescription(stadesc);
		sa.setStaShortDescription(staShotDesc);
		sa.setStaStartDate(stastartdate);
		sa.setStaEndDate(staenddate);
		sa.setStaCreatedDate(stacredate);
		sa.setStaModifiedDate(stamoddate);
		sa.setStaUpdatedBy(staupdby);
		sa.setStaGoalPoints(stagoalpoints);
		sa.setStaBadge(stabatch);
		StudioActivityType sat = new StudioActivityType();
		sat.setStaTypeId(statypeid);
		sa.setStudioActivityType(sat);
		sa.setStaImageUrl(staimage);
		sa.setStaInfluencer(stainfluen);
		sa.setStaAddressLine1(staaddr1);
		sa.setStaAddressLine2(staaddr2);
		sa.setStaAddressLine3(staaddr3);
		sa.setStaTown(statown);
		sa.setStaCounty(stacounty);
		sa.setStaCountry(stacountry);
		sa.setStaPostCode(stapostcode);
		sa.setStaLatitude(stalet);
		sa.setStaLongitude(stalong);
		sa.setStaPrice(staprice);
		sa.setStaLikeCnt(stalikes);	
		sa.setStaStartTime(stastarttime);
		sa.setStaEndTime(staendtime);
		sa.setStaExpiryDate(staExpiryDate);		
		sa.setStaExternalLink(staextlink);
		sa.setStaLocation(stalocation);
		sa.setStaStatus(stastatus);
		sa.setStaNoOfDays(staNoDays);
			
//		StudiosActivity studioActivity = studioActivityRepository.saveAndFlush(sa);
		StudiosActivity studioActivity =studioActivityRepository.save(sa);
		studioActivityRepository.flush();
		
		LOGGER.debug("the added studio id is "+studioActivity.getStaId());		
	
		int staid=sa.getStaId();		
			activity.put("STA_ID",studioActivity.getStaId());
		} else {
			activity.put("RESULT",TurnOutConstant.FAILED );	
		}
		return activity;
	}
	
	
	@Override
	public JSONObject updateStudioActivity(int staid, String staname, int stdid, String stadesc,String staShotDesc, Date stastartdate, Date staenddate,
			Date stacredate, Date stamoddate, int staupdby, int stagoalpoints, String stabatch, int statypeid,
			String staimage, String stainfluen,int stalikes, double staprice, String staaddr1, String staaddr2, String staaddr3, String statown,
			String stacounty, String stacountry, String stapostcode, float stalet, float stalong, Date stastarttime, Date staendtime, String staextlink,
			String stalocation, Date staExpiryDate, String stastatus,int staNoDays) {
		
		
		JSONObject activity=new JSONObject();
		
		if(staname != null || stdid != 0) {			
			StudiosActivity sa = studioActivityRepository.findOne(staid);
			sa.setStaName(staname);
			sa.setStdId(stdid);
			sa.setStaDescription(stadesc);
			sa.setStaShortDescription(staShotDesc);
			sa.setStaStartDate(stastartdate);
			sa.setStaEndDate(staenddate);
			sa.setStaCreatedDate(stacredate);
			sa.setStaModifiedDate(stamoddate);
			sa.setStaUpdatedBy(staupdby);
			sa.setStaGoalPoints(stagoalpoints);
			sa.setStaBadge(stabatch);
			StudioActivityType sat = new StudioActivityType();
			sat.setStaTypeId(statypeid);
			sa.setStudioActivityType(sat);
			sa.setStaImageUrl(staimage);
			sa.setStaInfluencer(stainfluen);
			sa.setStaAddressLine1(staaddr1);
			sa.setStaAddressLine2(staaddr2);
			sa.setStaAddressLine3(staaddr3);
			sa.setStaTown(statown);
			sa.setStaCounty(stacounty);
			sa.setStaCountry(stacountry);
			sa.setStaPostCode(stapostcode);
			sa.setStaLatitude(stalet);
			sa.setStaLongitude(stalong);
			sa.setStaPrice(staprice);
			sa.setStaLikeCnt(stalikes);	
			sa.setStaStartTime(stastarttime);
			sa.setStaEndTime(staendtime);
			sa.setStaExpiryDate(staExpiryDate);		
			sa.setStaExternalLink(staextlink);
			sa.setStaLocation(stalocation);
			sa.setStaStatus(stastatus);
			sa.setStaNoOfDays(staNoDays);
			
			StudiosActivity studioActivity = studioActivityRepository.save(sa);
			studioActivityRepository.flush();
			LOGGER.debug("Studio activity id "+studioActivity.getStaId());
		
			activity.put("STA_ID",studioActivity.getStaId());
		} else {
			activity.put("RESULT",TurnOutConstant.FAILED );	
		}
		return activity;
	}*/
	/**
	 * This function used to save events and challenges into database. It will return just added or updated studio activity id.
	 * 
	 * @param staid A primary key that holds activity id.
	 * @param staname Activity (event / challenge) name.
	 * @param stdid Which studio going to conduct the activity.
	 * @param stadesc Activity detailed description.
	 * @param staShotDesc Short description about the activity.
	 * @param stastartdate Activity start date.
	 * @param staenddate Activity end date.
	 * @param stacredate  Activity created date.
	 * @param stamoddate Activity modified date.
	 * @param staupdby Activity updated person name.
	 * @param stagoalpoints Activity attend points.
	 * @param stabatch 
	 * @param statypeid Activity type(event / challenge).
	 * @param staimage Activity image
	 * @param stainfluen
	 * @param stalikes
	 * @param staprice 
	 * @param staaddr1 Activity held place (Within a studio / outside area) address line1.
	 * @param staaddr2 Activity held place address line2.
	 * @param staaddr3 Activity held place address line2.
	 * @param statown Activty held town name.
	 * @param stacounty Activty held coutnty name.
	 * @param stacountry Activty held country name.
	 * @param stapostcode Activity held place postcode.
	 * @param stalet Studio latitude.
	 * @param stalong Studio longtitude.
	 * @param stastarttime Activity start time.
	 * @param staendtime Activity end time.
	 * @param staextlink Activity information external link.
	 * @param stalocation Activity location place.
	 * @param staExpiryDate Challenge expiry date.
	 * @param stastatus Activity status.
	 * @param staNoDays No of day that activity going to happen.
	 * @return last inserted or updated id.
	 */
	@Override
	public JSONObject saveStudioActivity(int staid, String staname, int stdid, String stadesc,String staShotDesc, Date stastartdate, Date staenddate,
			Date stacredate, Date stamoddate, int staupdby, int stagoalpoints, String stabatch, int statypeid,
			String staimage, String stainfluen,int stalikes, double staprice, String staaddr1, String staaddr2, String staaddr3, String statown,
			String stacounty, String stacountry, String stapostcode, float stalet, float stalong, Date stastarttime, Date staendtime, String staextlink,
			String stalocation, Date staExpiryDate, String stastatus,int staNoDays) {
		
		
		JSONObject activity=new JSONObject();
		
		if(staname != null || stdid != 0) {			
			StudiosActivity sa;
			if(staid != 0) {
				sa = studioActivityRepository.findOne(staid);
			} else {
				sa = new StudiosActivity();
			}			
			sa.setStaName(staname);
			sa.setStdId(stdid);
			sa.setStaDescription(stadesc);
			sa.setStaShortDescription(staShotDesc);
			sa.setStaStartDate(stastartdate);
			sa.setStaEndDate(staenddate);
			sa.setStaCreatedDate(stacredate);
			sa.setStaModifiedDate(stamoddate);
			sa.setStaUpdatedBy(staupdby);
			sa.setStaGoalPoints(stagoalpoints);
			sa.setStaBadge(stabatch);
			StudioActivityType sat = new StudioActivityType();
			sat.setStaTypeId(statypeid);
			sa.setStudioActivityType(sat);
			sa.setStaImageUrl(staimage);
			sa.setStaInfluencer(stainfluen);
			sa.setStaAddressLine1(staaddr1);
			sa.setStaAddressLine2(staaddr2);
			sa.setStaAddressLine3(staaddr3);
			sa.setStaTown(statown);
			sa.setStaCounty(stacounty);
			sa.setStaCountry(stacountry);
			sa.setStaPostCode(stapostcode);
			sa.setStaLatitude(stalet);
			sa.setStaLongitude(stalong);
			sa.setStaPrice(staprice);
			sa.setStaLikeCnt(stalikes);	
			sa.setStaStartTime(stastarttime);
			sa.setStaEndTime(staendtime);
			sa.setStaExpiryDate(staExpiryDate);		
			sa.setStaExternalLink(staextlink);
			sa.setStaLocation(stalocation);
			sa.setStaStatus(stastatus);
			sa.setStaNoOfDays(staNoDays);
			
			
			StudiosActivity studioActivity = studioActivityRepository.save(sa);
			studioActivityRepository.flush();
			LOGGER.debug("Studio activity id "+studioActivity.getStaId());
		
			activity.put("STA_ID",studioActivity.getStaId());
		} else {
			activity.put("RESULT",TurnOutConstant.FAILED );	
		}
		return activity;
	}	
	/**
	 * Check whether the studio activity already exist or not.
	 * If exist return false otherwise true.
	 * 
	 * @param activityid An unique activity id.
	 * @return return boolean true or false.
	 */
	@Override
	public boolean isStudiosActivityExist(int activityid) {
		StudiosActivity stdActivity = studioActivityRepository.findByStaId(activityid);
		if(stdActivity!=null)
		return true;
		else
			return false;
	}	
	@Transactional
	public boolean updateLikeCount(int sta_id, int sta_like_cnt)
	{
		StudiosActivity stdActivity = studioActivityRepository.findByStaId(sta_id);
		int likeCnt = stdActivity.getStaLikeCnt()+1;
		stdActivity.setStaLikeCnt(likeCnt);
		studioActivityRepository.saveAndFlush(stdActivity);
		return true;
	}
	/**
	 * This method used to get challenge details from database based on the activity id passing.
	 * 
	 * @param pageno current page number
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONArray contains all the challenge details.
	 */
	@Transactional
	public JSONArray getAllChallenges(int pageno, int pagesize) {
		JSONArray jsonArray = new JSONArray(); 
		List activity = customStudioActivityRepository.getAllChallenges(pageno,pagesize);
		if (activity != null && activity.size() > 0)
			jsonArray = buildJsonArray(activity);
		return jsonArray;
	}
	/**
	 * In this method we will get the challenge details from the database based on activity id passing.
	 * 
	 * @param activityid an unique id of studio activity
	 * @param partyid an unique id of party.
	 * @return return JSONObject contains challenge details.
	 */

	@Override
	public JSONObject getChallengeWall(int activityid, int partyid) {
		Map<String, Object> evnt = customStudioActivityRepository.getChallengeWall(activityid,TurnOutConstant.STUDIOS_ACTIVITY);			
		JSONObject evntjson = new JSONObject();		
		evntjson.put("STA_ID", evnt.get("STA_ID"));
		evntjson.put("STA_NAME", evnt.get("STA_NAME"));
		evntjson.put("STD_ID", evnt.get("STD_ID"));
		evntjson.put("STA_DESCRIPTION", evnt.get("STA_DESCRIPTION"));		
		evntjson.put("START_DATE", evnt.get("START_DATE"));
		evntjson.put("START_TIME", evnt.get("START_TIME"));
		evntjson.put("END_DATE", evnt.get("END_DATE"));
		evntjson.put("END_TIME", evnt.get("END_TIME"));			
		evntjson.put("STA_TYPE_ID", evnt.get("STA_TYPE_ID"));
		evntjson.put("STA_PRICE", evnt.get("STA_PRICE"));
		evntjson.put("STA_IMAGE_URL", evnt.get("STA_IMAGE_URL"));
		evntjson.put("STA_EXTERNAL_LINK", evnt.get("STA_EXTERNAL_LINK"));			
		evntjson.put("STD_NAME", evnt.get("STD_NAME"));
		evntjson.put("STD_COMPANY_LOGO", evnt.get("STD_COMPANY_LOGO"));			
		evntjson.put("INTRESTED_USER", getInterestedParty(activityid,"ACCEPTED"))	;
		evntjson.put("STA_EXPIRY_DATE", evnt.get("STA_EXPIRY_DATE"));
		evntjson.put("COMMENTS", listCommentswithcmtlikecnt(activityid,TurnOutConstant.STUDIOS_ACTIVITY,partyid));			
		evntjson.put("STA_SHORT_DESCRIPTION", evnt.get("STA_SHORT_DESCRIPTION"));
		evntjson.put("STA_STATUS", evnt.get("STA_STATUS"));
		return evntjson;
	}	
	
	@Transactional
	public JSONArray listCommentswithcmtlikecnt(int activityid,String commentType,int ptyid)
	{
		JSONArray getallcomments = new JSONArray();
		JSONObject obj;
		Map<String, Object> record;
		List commentslist = customcmtrepository.listCommentswithcmtlikecnt(activityid,TurnOutConstant.STUDIOS_ACTIVITY,ptyid);
		for (Iterator itr = commentslist.iterator(); itr.hasNext();) {
			record = (Map) itr.next();
			obj = new JSONObject();
			for (Map.Entry<String, Object> entry : record.entrySet()) {
				LOGGER.debug(entry.getKey() + "/" + entry.getValue());
				obj.put(entry.getKey(), entry.getValue().toString());				
			}
			getallcomments.add(obj);		
	    }
		return getallcomments;
	}
	
	/**
	 * This method used to get all the activity from database and helps to search the activities based on passed string.
	 * 
	 * @param name name of the activity.
	 * @param status status of the activity.
	 * @param type activity type(event / challenge).
	 * @param stdid an unique id of studio.
	 * @return returns JSONArray contains all the studio activity details.
	 */
	
	@Transactional
	public JSONArray activitySearch(String name, String status, int type, int stdid) {

		JSONArray listarr = new JSONArray();
		JSONObject listobj;
		Map<String, Object> record;
		
		List rs = customStudioActivityRepository.activitySearch(name, status, type, stdid);
		
		if( rs.size() != 0) {
			for (Iterator itr = rs.iterator(); itr.hasNext();) {
				record = (Map) itr.next();
				listobj = new JSONObject();
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					listobj.put(entry.getKey(), entry.getValue());	
					
					if(entry.getKey().equals("STD_ID")) {
						int stdId = Integer.parseInt(entry.getValue().toString());
						Studio std = studioRepository.findOne(stdId);
						listobj.put("STD_NAME", std.getStdName());
					}
				}
				listarr.add(listobj);		
		    }
		} else {
			listobj = new JSONObject();
			listobj.put("Records", TurnOutConstant.NOT_EXIST);
			listarr.add(listobj);
		}	
		LOGGER.debug("OBJECT => "+rs);
		return listarr;
	}
	/**
	 * In this method we will get the activity detail from the database based on activity id passing.
	 * 
	 * @param  eventid an unique id of studio activity.
	 * @return return JSONObject contains challenge details.
	 */
	public JSONObject activityDetails(int eventid) {
		Map<String, Object> evnt = customStudioActivityRepository.activityDetails(eventid);
		JSONObject evntjson = new JSONObject();		
		evntjson.put("STA_ID", evnt.get("STA_ID"));
		evntjson.put("STA_NAME", evnt.get("STA_NAME"));
		evntjson.put("STD_ID", evnt.get("STD_ID"));
		evntjson.put("STA_DESCRIPTION", evnt.get("STA_DESCRIPTION"));		
		evntjson.put("START_DATE", evnt.get("START_DATE"));
		evntjson.put("START_TIME", evnt.get("START_TIME"));
		evntjson.put("END_DATE", evnt.get("END_DATE"));
		evntjson.put("END_TIME", evnt.get("END_TIME"));		
		evntjson.put("STA_GOAL_POINTS", evnt.get("STA_GOAL_POINTS"));
		evntjson.put("STA_TYPE_ID", evnt.get("STA_TYPE_ID"));
		String status = (evnt.get("STA_TYPE_ID").toString().equals("1"))?"INTERESTED":"ACCEPTED";
		evntjson.put("STA_PRICE", evnt.get("STA_PRICE"));
		evntjson.put("STA_IMAGE_URL", evnt.get("STA_IMAGE_URL"));		
		evntjson.put("STA_ADDRESS_LINE1", evnt.get("STA_ADDRESS_LINE1"));
		evntjson.put("STA_ADDRESS_LINE2", evnt.get("STA_ADDRESS_LINE2"));
		evntjson.put("STA_ADDRESS_LINE3", evnt.get("STA_ADDRESS_LINE3"));		
		evntjson.put("STA_TOWN", evnt.get("STA_TOWN"));
		evntjson.put("STA_COUNTY", evnt.get("STA_COUNTY"));
		evntjson.put("STA_COUNTRY", evnt.get("STA_COUNTRY"));
		evntjson.put("STA_POST_CODE", evnt.get("STA_POST_CODE"));
		evntjson.put("STA_LATITUDE", evnt.get("STA_LATITUDE"));
		evntjson.put("STA_LONGITUDE", evnt.get("STA_LONGITUDE"));
		evntjson.put("STA_EXTERNAL_LINK", evnt.get("STA_EXTERNAL_LINK"));	
		evntjson.put("STA_LOCATION", evnt.get("STA_LOCATION"));
		evntjson.put("STD_NAME", evnt.get("STD_NAME"));
		evntjson.put("STD_COMPANY_LOGO", evnt.get("STD_COMPANY_LOGO"));
		evntjson.put("CMTS_CNT", evnt.get("CMTS_CNT"));		
		evntjson.put("STA_LIKE_CNT", evnt.get("LIKE_CNT"));
		evntjson.put("STA_INFLUENCER",getInfluencer(evnt.get("STA_INFLUENCER").toString()));
		evntjson.put("INTRESTED_USER", getInterestedParty(eventid,status))	;
		evntjson.put("STA_EXPIRY_DATE", evnt.get("STA_EXPIRY_DATE"));
		evntjson.put("STA_STATUS", evnt.get("STA_STATUS"));
		evntjson.put("STA_NoOfClasses", evnt.get("STA_NO_OF_DAYS"));
		
		if(Integer.parseInt(evnt.get("STA_TYPE_ID").toString()) == 1) {			
		//	evntjson.put("COMMENTS", listComments(eventid,TurnOutConstant.STUDIOS_ACTIVITY));			
			
		} else {	
			evntjson.put("STA_SHORT_DESCRIPTION", evnt.get("STA_SHORT_DESCRIPTION"));	
			evntjson.put("STA_BADGE", getBadges(evnt.get("STA_BADGE").toString()));
		}	
		return evntjson;
	}	
}
