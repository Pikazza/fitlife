package com.turnout.ws.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.SystemPropertyUtils;

import com.turnout.ws.domain.StudioPartyActivity;

/**
 * Custom actions have been defined for CustomStudioPartyActivityRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomStudioPartyActivityRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * This method used to check whether the party already accepted or not.
	 * 
	 * @param ptyid an unique id of party element.
	 * @param stdid an unique id of studio element.
	 * @param stdactid an unique id of studio activity id element.
	 * @param status it has constant text "ACCEPTED".
	 * @return return total count.
	 */
	public Map<String, Object> getInterested(int ptyid, int stdid, int stdactid,String status) {		
		String sql = "SELECT COUNT(*) AS TOTAL FROM SUL.STUDIO_PARTY_ACTIVITY WHERE PTY_STA_STATUS='"+status+"' AND PTY_ID="+ptyid+" AND STA_ID="+stdactid+"  AND STA_STD_ID="+stdid;
		return jdbcTemplate.queryForMap(sql);
	}

	/**
	 * Return activity count based on the party id,studio id and status.
	 *  
	 * @param ptyId an unique id of party element.
	 * @param sqlcheckintime party checkin time in studio.
	 * @param stdid an unique id of studio element.
	 * @param chkStatus type of status. Whether "INTERESTED" or "ACCEPTED".
	 * @return return activity count.
	 */
	public Map<String, Object> getActivityIdByTimeandByPartyandByBcondata(int ptyId, String sqlcheckintime, int stdid, String chkStatus) {
		String sql = "SELECT COUNT(SPA.STA_ID) JOINACTIVITYCNT,GROUP_CONCAT(SPA.STA_ID SEPARATOR ',') STA_ID FROM STUDIOS_ACTIVITY AS SA LEFT JOIN STUDIO_PARTY_ACTIVITY  AS SPA ON SA.STA_ID = SPA.STA_ID"
				+ " WHERE SA.STD_ID="+stdid +" AND SPA.PTY_ID="+ptyId+" AND SA.STA_START_DATE <='"+sqlcheckintime+"'  AND SA.STA_END_DATE >='"+sqlcheckintime+"' AND PTY_STA_STATUS='"+chkStatus+"'";
		return jdbcTemplate.queryForMap(sql);
		
	}

	/**
	 * Returns all studio activities list based on party.
	 * 
	 * @param partyid an unique id of party element.
	 * @return return list of activities.
	 */
	public List myProfile(int partyid) {
	String sql = "SELECT ID,STD_ID,HAPPEN_TIME,POINTS,NAME,STD_NAME,STD_COMPANY_LOGO,ARTYPE  FROM (" 
			+ "SELECT SPA.STA_ID AS ID,SPA.STA_STD_ID AS STD_ID,SPA.CHECKOUT_TIME AS HAPPEN_TIME,SPA.GAINED_POINTS AS POINTS,SA.STA_NAME AS NAME,S.STD_NAME AS STD_NAME,"
			+ "S.STD_COMPANY_LOGO,'ACTIVITY' AS ARTYPE FROM STUDIO_PARTY_ACTIVITY AS SPA "
			+ " LEFT JOIN STUDIOS_ACTIVITY AS SA ON SPA.STA_ID = SA.STA_ID "
			+ " LEFT JOIN STUDIOS AS S ON SPA.STA_STD_ID = S.STD_ID	 WHERE PTY_STA_STATUS='ATTEND' AND PTY_ID = "+partyid +" AND GAINED_POINTS > 0"
					+ " UNION "
			+ " SELECT R.RWD_ID AS ID,R.RWD_STD_ID AS STD_ID,VP.VOC_STATUS_DATE AS HAPPEN_TIME,VP.VOC_REEDEMED_POINTS AS POINTS,R.RWD_NAME AS NAME,S.STD_NAME AS STD_NAME,"
			+ "S.STD_COMPANY_LOGO,'REWARD' AS ARTYPE FROM VOUCHER_PARTY AS VP "
			+ " LEFT JOIN  REWARDS_HAS_VOUCHER  AS RHV ON VP.VOC_ID = RHV.VOC_VOC_ID "
			+ " LEFT JOIN REWARDS AS R ON RHV.RWD_RWD_ID = R.RWD_ID "
			+ " LEFT JOIN STUDIOS AS S ON R.RWD_STD_ID = S.STD_ID "
			+ " WHERE PTY_ID ="+partyid+" ) AS X ORDER BY HAPPEN_TIME DESC";		
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * Return all accepted challenges based on party id passing.
	 * 
	 * @param partyid an unique id of party element.
	 * @return return list of accepted challenges list.
	 */
	public List myChallenge(int partyid) {
		String sql = "SELECT X.STA_ID,SUM(ATTEND) AS ATTEND,SUM(ACCEPTED) AS ACCEPTED,SA.STA_NO_OF_DAYS,SA.STA_NAME,SA.STD_ID,SD.STD_NAME,SD.STD_COMPANY_LOGO,SA.STA_SHORT_DESCRIPTION,DATE_FORMAT(SA.STA_START_DATE,'%Y-%m-%d') AS STA_START_DATE,"
				+ "	DATE_FORMAT(SA.STA_END_DATE, '%Y-%m-%d') AS STA_END_DATE,DATE_FORMAT(SA.STA_EXPIRY_DATE,'%Y-%m-%d') AS STA_EXPIRY_DATE,SA.STA_IMAGE_URL  FROM ("
				+ "	SELECT STA_ID,0 AS ATTEND,COUNT(*) AS ACCEPTED FROM STUDIO_PARTY_ACTIVITY AS SPA WHERE  SPA.PTY_ID ="+partyid +" AND SPA.PTY_STA_STATUS = 'ACCEPTED'"
				+ "	GROUP BY STA_ID "
				+ "	UNION ALL"
				+ " SELECT STA_ID,COUNT(*) AS ATTEND,0 AS ACCEPTED FROM STUDIO_PARTY_ACTIVITY AS SPA WHERE  SPA.PTY_ID ="+partyid +" AND SPA.PTY_STA_STATUS = 'ATTEND' AND SPA.GAINED_POINTS > 0"
				+ " GROUP BY STA_ID) AS X "
				+ " LEFT JOIN STUDIOS_ACTIVITY SA ON X.STA_ID = SA.STA_ID "
				+ " LEFT JOIN STUDIOS SD ON SA.STD_ID=SD.STD_ID "
				+ " WHERE   SA.STA_TYPE_ID=2 "
				+ "	GROUP BY X.STA_ID ORDER BY SA.STA_EXPIRY_DATE ";
		
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * Returns all won badges list.
	 * 
	 * @param partyid an unique id of party element.
	 * @return return list which contains all won badges.
	 */
	public List myBadges(int partyid) {
	String sql= " SELECT SPA.PTY_STA_ID,SPA.GAINED_TASK_BAGE,SPA.STA_ID,SPA.STA_STD_ID,SA.STA_NAME,S.STD_NAME FROM STUDIO_PARTY_ACTIVITY AS SPA "
			+ " LEFT JOIN STUDIOS_ACTIVITY AS SA ON SPA.STA_ID = SA.STA_ID "
			+ " LEFT JOIN STUDIOS AS S ON SPA.STA_STD_ID = S.STD_ID "
			+ " WHERE PTY_STA_STATUS = 'WONBADGE' AND PTY_ID="+partyid ;	
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * It will get one or more participants who are actively accepted or attended given challenge based on their name.
	 * 
	 * @param name the name of the participants.
	 * @param staid A challenge's id on which participants search will take place.
	 * @return return list which contains all the participant information.
	 */
	public List participantsSearch(String name, int staid)
	{
		String strSql = "";
		String strStdId;
		if(!name.equals("ALL")) {
			strSql = "AND PA.PTY_NAME LIKE '%"+name+"%' AND PA.PTY_LAST_NAME LIKE '%"+name+"%'";
		}
		
		if(staid != 0) {
			strStdId = " ='"+staid+"'";
		} else {
			strStdId = "!='0'";
		}	
		
		String sql = "SELECT X.STA_ID,SUM(ATTEND) AS ATTEND,SUM(ACCEPTED) AS ACCEPTED,SUM(WONBADGE) AS WINNER,SA.STA_NO_OF_DAYS,SA.STD_ID,CONCAT(PA.PTY_NAME,' ',PA.PTY_LAST_NAME) AS PTY_NAME,PA.PTY_ID,SA.STA_BADGE FROM ("
					  + " SELECT STA_ID,PTY_ID,0 AS ATTEND,COUNT(*) AS ACCEPTED,0 AS WONBADGE FROM STUDIO_PARTY_ACTIVITY AS SPA WHERE  SPA.STA_ID "+strStdId+" AND SPA.PTY_STA_STATUS = 'ACCEPTED'"
					  + " GROUP BY PTY_ID" 
					  + " UNION ALL"
					  + " SELECT STA_ID,PTY_ID,0 AS ATTEND,0 AS ACCEPTED,COUNT(*) AS WONBADGE   FROM STUDIO_PARTY_ACTIVITY AS SPA WHERE  SPA.STA_ID "+strStdId+" AND SPA.PTY_STA_STATUS = 'WONBADGE'"
					  + " GROUP BY PTY_ID" 
					  + " UNION ALL"
					  + " SELECT STA_ID,PTY_ID,COUNT(*) AS ATTEND,0 AS ACCEPTED,0 AS WONBADGE FROM STUDIO_PARTY_ACTIVITY AS SPA WHERE  SPA.STA_ID "+strStdId+" AND SPA.PTY_STA_STATUS = 'ATTEND' AND SPA.GAINED_POINTS > 0"
					  + " GROUP BY PTY_ID) AS X" 
					  + " LEFT JOIN STUDIOS_ACTIVITY SA ON X.STA_ID = SA.STA_ID" 
					  + " LEFT JOIN PARTY PA ON X.PTY_ID = PA.PTY_ID" 
					  + " WHERE SA.STA_TYPE_ID = 2 "+strSql
					  + " GROUP BY X.PTY_ID";		
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * Returns activity name and activity for event notification
	 * 
	 * @param staid an unique id of studio activity element.
	 * @param stdid an unique id of studio element.
	 * @param status party status.
	 * @return returnactivity details.
	 */
	public Map<String, Object> getInterstedUserInfoforNotification(int staid,int stdid,String status) {
		String sql = "SELECT COUNT(PTY_ID) as MEMCOUNT,SA.STA_ID,SA.STA_NAME,SPA.PTY_STA_STATUS FROM STUDIO_PARTY_ACTIVITY AS SPA "
				+ " LEFT JOIN STUDIOS_ACTIVITY AS SA ON SPA.STA_ID = SA.STA_ID AND SPA.STA_STD_ID = SA.STD_ID"
				+ " WHERE SPA.STA_ID ="+staid+" AND SPA.STA_STD_ID = "+stdid+" AND SPA.PTY_STA_STATUS = '"+status+"'";
		return jdbcTemplate.queryForMap(sql);
		
	}
	 
/**
 * 
 * @param ptyactid
 * @param ptystatus
 * @param devicetype
 * @return
 */
	public List chkpartyPointsDevicetoken(int ptyactid, String ptystatus, String devicetype) {
		String sql = " SELECT SPA.STA_ID,P.PTY_PHOTO,P.PTY_DEVICE_TOKEN,P.PTY_DEVICE_TYPE,NOW() AS CUR_TIME,CASE WHEN SPA.STA_ID = 0 THEN CONCAT(SPA.GAINED_POINTS,' points credited for attendind class at ', S.STD_NAME) ELSE CONCAT(SPA.GAINED_POINTS,' points credited for attendind ',SA.STA_NAME, 'class at', S.STD_NAME) END AS MESSAGES FROM STUDIO_PARTY_ACTIVITY AS SPA"
					+ " LEFT JOIN STUDIOS_ACTIVITY AS SA ON SPA.STA_ID = SA.STA_ID "
					+ " LEFT JOIN STUDIOS AS S ON SPA.STA_STD_ID = S.STD_ID "
					+ " LEFT JOIN PARTY AS P ON SPA.PTY_ID = P.PTY_ID "
					+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON SPA.PTY_ID = PN.NOTIFY_PTY_ID "
					+ " WHERE PN.NOTIFY_PERSONAL_POINTS_CRDT ='Y' AND SPA.PTY_STA_STATUS = '"+ptystatus+"' AND SPA.PTY_STA_ID ="+ptyactid+ " AND P.PTY_DEVICE_TYPE='"+devicetype+"'";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * Returns last checked in users information
	 * 
	 * @param stdid the primary key studio id.
	 * @return list of parties information.
	 */
	public List getCheckinUser(int stdid)
	{
		String sql = " SELECT DISTINCT P.PTY_ID,CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) AS PTY_NAME,P.PTY_PHOTO FROM STUDIO_PARTY_ACTIVITY AS SPA"
				+ "	LEFT JOIN PARTY AS P ON SPA.PTY_ID = P.PTY_ID"
				+ " WHERE PTY_STA_STATUS='ATTEND' AND STA_STD_ID="+stdid;		
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * Returns parties device token who are all interested events or accepted challenges. 
	 * 
	 * @param ptyId an unique of party element.
	 * @param devicetype type of mobile os.
	 * @return list of device tokens.
	 */
	public List getPartyDeviceToken(int ptyId, String devicetype) 
	{
		String sql = "SELECT P.PTY_DEVICE_TOKEN FROM PARTY AS P RIGHT JOIN PARTY_NOTIFICATION AS PN ON P.PTY_ID = PN.NOTIFY_PTY_ID  WHERE "
					+ " P.PTY_ID IN(SELECT FRND_ID2 AS PARTY FROM SUL.FRIENDS WHERE FRND_ID1 = "+ptyId+" AND FRND_STATUS = 'FOLLOW'"
					+ " UNION"
					+ " SELECT FRND_ID1 FROM SUL.FRIENDS WHERE FRND_ID2 = "+ptyId+" AND FRND_STATUS = 'FOLLOW') AND"
					+ " CASE '"+devicetype+"' WHEN 'INTERESTED' THEN NOTIFY_OTH_INTRST_EVNT = 'Y' ELSE NOTIFY_OTH_ACPT_CHLNG='Y' END AND IFNULL(PTY_DEVICE_TYPE,'')='"+devicetype+"'";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 
	 * @param ptyactid an unique id studio party activity id.
	 * @return list all device tokens.
	 */
	public List getCheckinUserTokenList(int ptyactid) {
		String sql = "SELECT P.PTY_ID,CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) AS PTY_NAME,STD_NAME,PTY_DEVICE_TOKEN,PTY_DEVICE_TYPE,CONCAT('Hi ',PTY_NAME, ', Welcome to ',STD_NAME) AS MESSAGES, NOW() AS CUR_TIME FROM STUDIO_PARTY_ACTIVITY AS SPA"
				+ "	LEFT JOIN PARTY AS P ON SPA.PTY_ID = P.PTY_ID"
				+ " LEFT JOIN STUDIOS AS S ON STA_STD_ID = STD_ID"
				+ " WHERE PTY_STA_STATUS='ATTEND' AND PTY_STA_ID ="+ptyactid;		
		
		return jdbcTemplate.queryForList(sql);
		
	}

}
