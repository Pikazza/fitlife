package com.turnout.ws.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.StudiosActivity;

/**
 * Custom actions have been defined for CustomStudioActivityRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomStudioActivityRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * This method used to get list of activities from database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return returns list which contains all the activities.
	 */
	@Transactional
	public List getListedActivity(int pageno, int pagesize) {
		int page = (pageno-1) * pagesize ;
		String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SD.STD_NAME,SD.STD_COMPANY_LOGO,SA.STA_DESCRIPTION,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_CREATED_DATE,'%Y-%m-%d') AS STA_CREATED_DATE,DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') as STA_START_DATE,"
				+ " DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,SA.STA_IMAGE_URL,SA.STA_NO_OF_DAYS"
				+ "  FROM STUDIOS_ACTIVITY SA LEFT JOIN STUDIOS SD ON SA.STD_ID=SD.STD_ID"
				+ "  WHERE SA.STA_TYPE_ID=1 AND SA.STA_STATUS = 'ACTIVE' AND DATE(NOW())<=DATE(SA.STA_END_DATE) ORDER BY SA.STA_START_DATE LIMIT "+page+","+pagesize;
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * Returns details of activity based on given string with activity id.
	 * 
	 * @param eventid an unique activity id
	 * @param studiosActivity It may have the value of 'comment' or 'like'
	 * @return a map object with activity details
	 */
	public Map<String, Object> getActivityDetails(int eventid, String studiosActivity) {
		String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SA.STA_NO_OF_DAYS,SA.STA_DESCRIPTION,SA.STA_STATUS,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_START_DATE, '%Y-%m-%d') AS START_DATE,"
				+ "DATE_FORMAT(SA.STA_START_TIME,'%H:%i') AS START_TIME,DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS END_DATE,"
				+ "DATE_FORMAT(SA.STA_END_TIME,'%H:%i') AS END_TIME,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,IFNULL(SA.STA_PRICE,0) AS STA_PRICE,"
				+ "SA.STA_IMAGE_URL,SA.STA_INFLUENCER,SA.STA_ADDRESS_LINE1,SA.STA_ADDRESS_LINE2,SA.STA_ADDRESS_LINE3,SA.STA_TOWN,"
				+ "SA.STA_COUNTY,SA.STA_COUNTRY,SA.STA_POST_CODE,SA.STA_LATITUDE,SA.STA_LONGITUDE,SA.STA_EXTERNAL_LINK,SA.STA_LOCATION,S.STD_NAME,S.STD_COMPANY_LOGO,"
				+ "(SELECT COUNT(*) FROM COMMENTS AS C WHERE C.CMT_TYPE='"+studiosActivity+"' AND C.CMT_TYPE_ID="+eventid+" ) AS CMTS_CNT,"
				+ "(SELECT COUNT(*) FROM LIKES AS L WHERE L.LIKE_YN='Y' AND L.LIKE_TYPE='"+studiosActivity+"' AND L.LIKE_TYPE_ID="+eventid+" ) AS LIKE_CNT"
				+ " FROM STUDIOS_ACTIVITY AS SA   LEFT JOIN STUDIOS S ON SA.STD_ID = S.STD_ID"
				+ "   WHERE SA.STA_ID=" +eventid;
		return jdbcTemplate.queryForMap(sql);
	}
	
	/**
	 * Returns details of event based on given string with event id.
	 * 
	 * @param eventid an unique event id
	 * @return a map object with event details
	 */
	public Map<String, Object> activityDetails(int eventid) {
		String sql = "SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SA.STA_NO_OF_DAYS,SA.STA_DESCRIPTION,SA.STA_STATUS,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_START_DATE, '%Y-%m-%d') AS START_DATE,"
				+ "DATE_FORMAT(SA.STA_START_TIME,'%H:%i') AS START_TIME,DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS END_DATE,"
				+ "DATE_FORMAT(SA.STA_END_TIME,'%H:%i') AS END_TIME,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,IFNULL(SA.STA_PRICE,0) AS STA_PRICE,"
				+ "SA.STA_IMAGE_URL,SA.STA_INFLUENCER,SA.STA_ADDRESS_LINE1,SA.STA_ADDRESS_LINE2,SA.STA_ADDRESS_LINE3,SA.STA_TOWN,"
				+ "SA.STA_COUNTY,SA.STA_COUNTRY,SA.STA_POST_CODE,SA.STA_LATITUDE,SA.STA_LONGITUDE,SA.STA_EXTERNAL_LINK,SA.STA_LOCATION,S.STD_NAME,S.STD_COMPANY_LOGO"
				+ " FROM STUDIOS_ACTIVITY AS SA LEFT JOIN STUDIOS S ON SA.STD_ID = S.STD_ID"
				+ " WHERE SA.STA_ID = " +eventid;
		return jdbcTemplate.queryForMap(sql);
	}

	/**
	 * Returns list of parties who are all interested with given event.
	 * 
	 * @param eventid an unique event id
	 * @param status status of event
	 * @return List of interested party
	 */
	public List getInterestedParty(int eventid, String status) {
		String sql = "SELECT P.PTY_ID,CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) AS PTY_NAME,P.PTY_PHOTO FROM STUDIO_PARTY_ACTIVITY  AS PA"
				+ " LEFT JOIN PARTY AS P ON PA.PTY_ID = P.PTY_ID  WHERE PTY_STA_STATUS='"+status+"' AND STA_ID="+eventid;		
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * Returns list of studio's events based on given studio id.
	 * 
	 * @param stdid an unique studio id
	 * @return List of events.
	 */
	public List getStudioActivityByStdid(int stdid){
	String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SA.STA_DESCRIPTION,SA.STA_NO_OF_DAYS,SA.STA_STATUS,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_CREATED_DATE,'%Y-%m-%d') AS STA_CREATED_DATE,DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') as STA_START_DATE,"
			+ " DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,SA.STA_IMAGE_URL,SD.STA_TYPE_DESC "
			+ "  FROM STUDIOS_ACTIVITY SA LEFT JOIN STUDIO_ACTIVITY_TYPE SD ON SA.STA_TYPE_ID=SD.STA_TYPE_ID"
			+ "  WHERE SA.STD_ID="+stdid+" AND SA.STA_TYPE_ID=1 AND SA.STA_STATUS = 'ACTIVE' AND  DATE(NOW())<=DATE(SA.STA_END_DATE) ORDER BY SA.STA_START_DATE";
	return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * Returns list of studio's challenges based on given studio id.
	 * 
	 * @param stdid an unique studio id
	 * @return List of challenges.
	 */
	public List getStudioChallengesByStdid(int stdid){
		String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SA.STA_DESCRIPTION,SA.STA_NO_OF_DAYS,SA.STA_STATUS,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_CREATED_DATE,'%Y-%m-%d') AS STA_CREATED_DATE,DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') as STA_START_DATE,"
				+ " DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,SA.STA_IMAGE_URL,SD.STA_TYPE_DESC "
				+ "  FROM STUDIOS_ACTIVITY SA LEFT JOIN STUDIO_ACTIVITY_TYPE SD ON SA.STA_TYPE_ID=SD.STA_TYPE_ID"
				+ "  WHERE SA.STD_ID="+stdid+" AND SA.STA_TYPE_ID=2 AND SA.STA_STATUS = 'ACTIVE' AND DATE(NOW())<=DATE(SA.STA_EXPIRY_DATE) ORDER BY SA.STA_EXPIRY_DATE";
		return jdbcTemplate.queryForList(sql);
		}
	
	/**
	 * This method used to get list of challenges from database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return  returns list which contains all the challenges.
	 */
	@Transactional
	public List getAllChallenges(int pageno, int pagesize) {
		int page = (pageno-1) * pagesize ;
		String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SD.STD_NAME,SA.STA_NO_OF_DAYS,SD.STD_COMPANY_LOGO,SA.STA_STATUS,SA.STA_DESCRIPTION,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_CREATED_DATE,'%Y-%m-%d') AS STA_CREATED_DATE,DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') as STA_START_DATE,"
				+ " DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,SA.STA_IMAGE_URL"
				+ "  FROM STUDIOS_ACTIVITY SA LEFT JOIN STUDIOS SD ON SA.STD_ID=SD.STD_ID"
				+ "  WHERE SA.STA_TYPE_ID=2 AND SA.STA_STATUS = 'ACTIVE' AND DATE(NOW())<=DATE(SA.STA_EXPIRY_DATE) ORDER BY SA.STA_EXPIRY_DATE LIMIT "+page+","+pagesize;		

		return jdbcTemplate.queryForList(sql);
		
		
	}

	/**
	 * Returns detailed like, comments and challenge informations related to each challenge based on the activity id.
	 * 
	 * @param activityid an unique activity id
	 * @param studiosActivity It may have the value of 'comment' or 'like'
	 * @return an map object with challenge details
	 */
	public Map<String, Object> getChallengeWall(int activityid, String studiosActivity) {
		String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STD_ID,SA.STA_DESCRIPTION,SA.STA_STATUS,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_START_DATE, '%Y-%m-%d') AS START_DATE,"
				+ "DATE_FORMAT(SA.STA_START_TIME,'%H:%i') AS START_TIME,DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS END_DATE,"
				+ " DATE_FORMAT(SA.STA_END_TIME,'%H:%i') AS END_TIME,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_TYPE_ID,IFNULL(SA.STA_PRICE,0) AS STA_PRICE,SA.STA_IMAGE_URL,"
				+ " SA.STA_EXTERNAL_LINK,S.STD_NAME,S.STD_COMPANY_LOGO FROM STUDIOS_ACTIVITY AS SA   LEFT JOIN STUDIOS S ON SA.STD_ID = S.STD_ID"
				+ " WHERE SA.STA_ID="+activityid;		
		System.out.println("SQL QUERY "+sql);
		return jdbcTemplate.queryForMap(sql);
	}	
	
	/**
	 * Returns list of events that has to be notified shortly.
	 * 
	 * @return List of events
	 */
	@Transactional
	public List getEventfornotification()
	{
		String sql=" SELECT SA.STA_START_TIME,SA.STA_ID,SA.STD_ID,SA.STA_NAME FROM STUDIOS_ACTIVITY AS SA  WHERE DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') = ADDDATE(CURDATE(),1) AND SA.STA_TYPE_ID=1 AND SA.STA_STATUS='ACTIVE'"; 
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * Returns list of notification for reminding events time.
	 * 
	 * @param staid an unique activity id.
	 * @param stdid an unique studio id.
	 * @param devicetype a device os type.
	 * @return  list of messages for event reminder
	 */
	@Transactional
	public List evntRemainder(int staid,int stdid,String devicetype)
	{
		String sql = " SELECT P.PTY_DEVICE_TOKEN FROM STUDIO_PARTY_ACTIVITY AS SPA "
				+ " LEFT JOIN PARTY AS P ON SPA.PTY_ID = P.PTY_ID "
				+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON SPA.PTY_ID = PN.NOTIFY_PTY_ID "
				+ " WHERE NOTIFY_PERSONAL_EVNT_REMAINDER = 'Y' AND SPA.STA_ID="+staid+" AND SPA.STA_STD_ID ="+stdid+" AND SPA.PTY_STA_STATUS='INTERESTED' AND P.PTY_DEVICE_TYPE='"+devicetype+"'";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * Returns List of rewards an user can redeem.
	 * @param devicetype a device os type.
	 * @return  List of rewards.
	 */
	public List reedemEligible(String devicetype)
	{
		String sql = " SELECT P.PTY_DEVICE_TOKEN FROM PARTY AS P"
				+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON P.PTY_ID = PN.NOTIFY_PTY_ID "
				+ " WHERE NOTIFY_PERSONAL_READY_REEDEM = 'Y'  AND P.PTY_DEVICE_TYPE='"+devicetype+"' AND PTY_GAINED_POINTS >= ( SELECT MIN(RWD_POINTS) FROM REWARDS AS R "
				+ " WHERE DATE(NOW())<=DATE(R.RWD_EXPIRY_DATE) AND R.RWD_STATUS='ACTIVE' AND R.RWD_ID IN  ( SELECT  RV.RWD_RWD_ID FROM  REWARDS_HAS_VOUCHER AS RV LEFT JOIN VOUCHER AS V ON RV.VOC_VOC_ID=V.VOC_ID  WHERE V.VOC_STATUS = 'ACTIVE' GROUP BY RV.RWD_RWD_ID ) )";
		
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * This method used to get all the activity from database and helps to search the activities based on passed string.
	 * 
	 * 
	 * @param name name of the activity.
	 * @param status status of the activity.
	 * @param type activity type(event / challenge).
	 * @param stdid an unique id of studio.
	 * @return returns JSONArray contains all the studio activity details.
	 */
	@Transactional
	public List activitySearch(String name, String status, int type, int stdid)
	{
		List testlist = new ArrayList();
		String strSql = "";
		String strStdId;
		if(stdid != 0) {
			strStdId = " ='"+stdid+"'";
		} else {
			strStdId = "!='0'";
		}		

		//type 1 means events, type 2 means challenges.
		
		if(type == 1) {
			//A => ALL,U => Upcoming events, C => Completed events.
			if(!status.equals("ALL")) {
				if(status.equals("U")) {
					strSql = "AND SA.STA_END_DATE >= CURRENT_DATE";
				} else {
					strSql = "AND SA.STA_END_DATE < CURRENT_DATE";
				}
			}
		}		
		
		if(type == 2) {
			//A => ALL,F => Future challenges, E => Expired challenges, C => Current challenges.
			if(!status.equals("ALL")) {
				if(status.equals("F")) {
					strSql = "AND SA.STA_EXPIRY_DATE > CURRENT_DATE";
				} else if(status.equals("C")) {
					strSql = "AND SA.STA_EXPIRY_DATE = CURRENT_DATE";
				} else {
					strSql = "AND SA.STA_EXPIRY_DATE < CURRENT_DATE";
				}
			}			
		}
		
		if(!name.equals("ALL")) {
			strSql = "AND SA.STA_NAME LIKE '%"+name+"%'" + strSql;
		}
		
		String sql="SELECT SA.STA_ID,SA.STA_NAME,SA.STA_STATUS,SA.STD_ID,SD.STD_NAME,SD.STD_COMPANY_LOGO,SD.STD_POINTS,SA.STA_DESCRIPTION,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_CREATED_DATE,'%Y-%m-%d') AS STA_CREATED_DATE,DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') as STA_START_DATE,"
				+ " DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_GOAL_POINTS,SA.STA_BADGE,SA.STA_TYPE_ID,SA.STA_IMAGE_URL,"
				+ " (SELECT count(*) FROM STUDIO_PARTY_ACTIVITY WHERE STA_STD_ID = SA.STD_ID AND STA_ID = SA.STA_ID AND PTY_STA_STATUS != 'INTRESTED' AND PTY_STA_STATUS != 'INTERESTED') AS VIEW"
				+ " FROM STUDIOS_ACTIVITY SA LEFT JOIN STUDIOS SD ON SA.STD_ID=SD.STD_ID"
				+ " WHERE SA.STD_ID "+strStdId+" AND  SA.STA_TYPE_ID = "+type+ " "+strSql;
		return jdbcTemplate.queryForList(sql);		
	}	

}
