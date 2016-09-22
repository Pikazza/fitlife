package com.turnout.ws.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Custom actions have been defined for CustomPartyRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomPartyRepository {
  
	@Autowired
	protected JdbcTemplate jdbcTemplate;
/**
 * Returns party details, gained points, won badges and number of attended studios
 * 
 * @param partyid a unique party id.
 * @return Returns Map object with party details.
 */
public Map<String, Object> getProfileById(int partyid) {
	String sql ="SELECT PTY_ID,PTY_NAME,PTY_LAST_NAME,IFNULL(PTY_EMAIL,'') AS PTY_EMAIL,IFNULL(PTY_DESCRIPTION,'') PTY_DESCRIPTION,PTY_GAINED_POINTS,IFNULL(PTY_PHOTO,'') PTY_PHOTO,(SELECT COUNT(DISTINCT STA_STD_ID) CNTGYM FROM STUDIO_PARTY_ACTIVITY  WHERE PTY_STA_STATUS='ATTEND' AND PTY_ID ="+partyid+") CNTGYM,"
			+ "(SELECT COUNT(GAINED_TASK_BAGE) CNTBADGE FROM STUDIO_PARTY_ACTIVITY  WHERE PTY_STA_STATUS='ATTEND' AND PTY_ID ="+partyid+" AND IFNULL(GAINED_TASK_BAGE,'')<>'' ) CNTBADGE,"
			+ "(SELECT COUNT(GAINED_POINTS) CNTSHOWUPS FROM STUDIO_PARTY_ACTIVITY  WHERE PTY_STA_STATUS='ATTEND' AND PTY_ID ="+partyid+" AND IFNULL(GAINED_POINTS,0)<>0 ) CNTSHOWUPS"
			+ " FROM PARTY WHERE PTY_ID="+partyid;
	return jdbcTemplate.queryForMap(sql);
}
/**
 * Returns list of party's interested event details with its studio details. 
 * 
 * @param partyid a unique party id.
 * @return a list object
 */
public List getPartyEvents(int partyid) {
	String sql="SELECT SPA.PTY_STA_ID,SPA.PTY_ID,SPA.PTY_STA_STATUS,SA.STA_ID,SA.STA_NAME,SA.STD_ID,SD.STD_NAME,SD.STD_COMPANY_LOGO,"
			+ " DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') AS STA_START_DATE,DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE"
			+ " FROM STUDIO_PARTY_ACTIVITY SPA LEFT JOIN STUDIOS_ACTIVITY SA ON  SPA.STA_ID = SA.STA_ID AND SPA.STA_STD_ID = SA.STD_ID"
			+ " LEFT JOIN STUDIOS SD ON SA.STD_ID=SD.STD_ID "
			+ " WHERE SA.STA_TYPE_ID=1 AND DATE(NOW())<=DATE(SA.STA_END_DATE)  AND SPA.PTY_ID="+partyid+" AND SPA.PTY_STA_STATUS='INTERESTED'  ORDER BY SA.STA_START_DATE";

	return jdbcTemplate.queryForList(sql);
}
/**
 * Returns list of party's accepted challenge details with its studio details.
 * 
 * @param partyid a unique party id.
 * @return a list object
 */
public List getPartyChallenges(int partyid) {
	String sql = "SELECT SPA.PTY_STA_ID,SPA.PTY_ID,SPA.PTY_STA_STATUS,SA.STA_ID,SA.STA_NAME,SA.STD_ID,SD.STD_NAME,SD.STD_COMPANY_LOGO,"
			+ " DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') AS STA_START_DATE,DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE"
			+ " FROM STUDIO_PARTY_ACTIVITY SPA LEFT JOIN STUDIOS_ACTIVITY SA  ON  SPA.STA_ID = SA.STA_ID AND SPA.STA_STD_ID = SA.STD_ID"
			+ " LEFT JOIN STUDIOS SD ON SA.STD_ID=SD.STD_ID"
			+ " WHERE SA.STA_TYPE_ID=2 AND DATE(NOW())<=DATE(SA.STA_EXPIRY_DATE) AND SPA.PTY_ID="+partyid+" AND SPA.PTY_STA_STATUS='ACCEPTED' ORDER BY SA.STA_EXPIRY_DATE";
	
	return jdbcTemplate.queryForList(sql);
}
/**
 * Returns list of friends based on given party id.
 * 
 * @param partyid a unique party id.
 * @return a list object
 */
public List getFriendsList(int partyid) {
	String sql = "SELECT FRND_ID1,FRND_ID2,PTY_ID,CONCAT(PTY_NAME,' ',PTY_LAST_NAME) AS PTY_NAME,PTY_PHOTO,FRND_STATUS,FRND_MODIFY_DATE FROM FRIENDS AS F LEFT JOIN PARTY AS P ON F.FRND_ID2 = P.PTY_ID WHERE FRND_STATUS ='FOLLOW' AND FRND_ID1 ="+partyid
			+ " UNION "
			+ " SELECT FRND_ID2,FRND_ID1,PTY_ID,CONCAT(PTY_NAME,' ',PTY_LAST_NAME) AS PTY_NAME,PTY_PHOTO,FRND_STATUS,FRND_MODIFY_DATE FROM FRIENDS AS F LEFT JOIN PARTY AS P ON F.FRND_ID1 = P.PTY_ID WHERE FRND_STATUS = 'FOLLOW'  AND FRND_ID2 ="+partyid
			+ " UNION "
		    + " SELECT FRND_ID1,FRND_ID2,PTY_ID,CONCAT(PTY_NAME,' ',PTY_LAST_NAME) AS PTY_NAME,PTY_PHOTO,FRND_STATUS,FRND_MODIFY_DATE FROM FRIENDS AS F LEFT JOIN PARTY AS P ON F.FRND_ID1 = P.PTY_ID WHERE FRND_STATUS = 'REQUEST'  AND FRND_ID2 ="+partyid+ " ORDER BY FRND_MODIFY_DATE DESC";
	return jdbcTemplate.queryForList(sql);
	
}
/**
 *  This method allows party to search new parties and make new fiends.
 * 
 * @param partyid a unique party id.
 * @return a list object
 */
public List findFriends(int partyid) {
	String sql = "SELECT * FROM PARTY WHERE PTY_ID NOT IN("
			+ " SELECT FRND_ID2 FROM FRIENDS WHERE FRND_ID1 = "+partyid+" AND (FRND_STATUS='FOLLOW' OR FRND_STATUS='REQUEST') "
			+ " UNION "
			+ " SELECT FRND_ID1 FROM FRIENDS WHERE FRND_ID2 = "+partyid+" AND (FRND_STATUS='FOLLOW' OR FRND_STATUS='REQUEST')) AND PTY_ID !="+partyid;
	return jdbcTemplate.queryForList(sql);	
}  

/**
 * This method allows party to search new parties.
 * 
 * @param search a search string
 * @return List of parties
 */
@Transactional
public List partySearch(String search)
{
	String ptySql = "";	
	if(!search.equals("ALL")) {		
		ptySql = " AND (P.PTY_NAME LIKE '%"+search+"%' || P.PTY_LAST_NAME LIKE '%"+search+"%' || P.PTY_EMAIL LIKE '%"+search+"%' || P.PTY_MOBILE LIKE '%"+search+"%')";
	}	
	String sql ="SELECT P.PTY_ID,CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) AS PTY_NAME,P.PTY_EMAIL,P.PTY_TEL,P.PTY_MOBILE,PA.AMH_ID,PA.PAM_VERIFIED,P.PTY_STATUS"
			+ " FROM PARTY AS P  LEFT JOIN PARTY_AUTH_MECH PA ON P.PTY_ID = PA.PTY_ID  WHERE P.PTY_ID != 0 "+ptySql;
	
	return jdbcTemplate.queryForList(sql);	
}
   
}
