package com.turnout.ws.repository;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turnout.ws.helper.TurnOutConstant;

/**
 * Custom actions have been defined for CustomCommentsRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomCommentsRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * Returns list of comments object with their details.
	 * 
	 * @param cmtTypeid This integer value holds activity type id.
	 * @param cmtType It has a constant value "COMMENTS".
	 * @param pageno Current page number.
	 * @param pagesize Total number of records shown in current page.
	 * @return list of comments.
	 */
	public List getallcomments(int cmtTypeid, String cmtType, int pageno,int pagesize) {
		int page = (pageno-1) * pagesize ;
		String sql="SELECT (SELECT CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) FROM PARTY AS P WHERE PTY_ID=C.CMT_PTY_ID ) AS PTY_NAME,C.CMT_PTY_ID AS PTY_ID,C.CMT_LINES, DATE_FORMAT(C.CMT_DATE, '%Y-%m-%d %H:%i:%s') AS CMT_DATE  FROM COMMENTS AS C WHERE C.CMT_TYPE='"+cmtType+"' AND C.CMT_TYPE_ID="+cmtTypeid+" ORDER BY C.CMT_ID DESC LIMIT "+page+","+pagesize;
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * Returns list of comments and it's like count with their details.
	 * 
	 * @param activityid An unique activity id.
	 * @param studiosActivity type of an activity.
	 * @param ptyid An unique party id.
	 * @return List of comments and likes.
	 */
	public List listCommentswithcmtlikecnt(int activityid, String studiosActivity,int ptyid) {

		String sql = "SELECT C.CMT_ID,(SELECT CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) FROM PARTY AS P WHERE PTY_ID=C.CMT_PTY_ID ) AS PTY_NAME,(SELECT IFNULL(P.PTY_PHOTO,'') AS PTY_PHOTO  FROM PARTY AS P WHERE PTY_ID=C.CMT_PTY_ID ) AS PTY_PHOTO,C.CMT_PTY_ID AS PTY_ID,C.CMT_LINES, DATE_FORMAT(C.CMT_DATE, '%Y-%m-%d %H:%i:%s') AS CMT_DATE,"
				+ " (SELECT COUNT(*) FROM COMMENTS AS CC WHERE CC.CMT_TYPE='COMMENTS' AND CC.CMT_TYPE_ID = C.CMT_ID) AS CMT_CNT ,"
				+ "(SELECT COUNT(*) FROM LIKES AS L WHERE L.LIKE_TYPE='"+TurnOutConstant.COMMENTS+"' AND L.LIKE_YN='Y' AND L.LIKE_TYPE_ID=C.CMT_ID) AS LIKE_CNT ,"
				+ "(SELECT CASE WHEN COUNT(*) > 0 THEN 'YES' ELSE 'NO' END FROM LIKES AS PL WHERE PL.LIKE_TYPE='"+TurnOutConstant.COMMENTS+"' AND PL.LIKE_YN='Y' AND PL.LIKE_PTY_ID="+ptyid+" AND PL.LIKE_TYPE_ID=C.CMT_ID) AS PTY_LIKED  FROM COMMENTS AS C"
				+ " WHERE C.CMT_TYPE='"+studiosActivity+"' AND C.CMT_TYPE_ID="+activityid+" ORDER BY C.CMT_ID DESC";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * Returns Map object with combination of party and device token.
	 *  
	 * @param cmtTypeId This integer value holds activity type id.
	 * @return  Return notification based on device token.
	 */
	public Map<String, Object>  chkpartyDevicetoken(int cmtTypeId) {
		
		String sql="SELECT P.PTY_ID,CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) AS PTY_NAME,P.PTY_DEVICE_TOKEN,P.PTY_DEVICE_TYPE,(SELECT COUNT(DISTINCT CMT_PTY_ID) FROM COMMENTS WHERE CMT_TYPE_ID ="+cmtTypeId+") AS CMTMEMCNT,"
				+ " C.CMT_TYPE_ID AS ACTIVITY_ID FROM COMMENTS AS C"
				+ " LEFT JOIN PARTY AS P ON C.CMT_PTY_ID = P.PTY_ID "
				+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON C.CMT_PTY_ID = PN.NOTIFY_PTY_ID "
				+ " WHERE C.CMT_ID ="+cmtTypeId+" AND PN.NOTIFY_OTH_CMTS = 'Y'";
	       
/*
		String sql="SELECT (SELECT CONCAT(PTY_ID,'<',PTY_NAME,'>',PTY_PHOTO) FROM PARTY WHERE PTY_ID="+pty_id+") AS INFO,P.PTY_DEVICE_TOKEN,P.PTY_DEVICE_TYPE,NOW() AS CUR_TIME,"
				+ "CASE  (SELECT COUNT(DISTINCT CMT_PTY_ID) FROM COMMENTS WHERE CMT_TYPE_ID ="+cmtTypeId+" )  WHEN 1  THEN CONCAT('<strong>',P.PTY_NAME,'</strong> commented on your post') ELSE CONCAT('<strong>',P.PTY_NAME,'</strong> and ',(SELECT COUNT(DISTINCT CMT_PTY_ID) FROM COMMENTS WHERE CMT_TYPE_ID ="+cmtTypeId+" )-1,' others has commented on your post') END  AS MESSAGES"
				+ " FROM COMMENTS AS C"
				+ " LEFT JOIN PARTY AS P ON C.CMT_PTY_ID = P.PTY_ID"
				+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON C.CMT_PTY_ID = PN.NOTIFY_PTY_ID"
				+ " WHERE C.CMT_ID ="+cmtTypeId+" AND PN.NOTIFY_OTH_CMTS = 'Y' AND P.PTY_DEVICE_TYPE='"+deviceType+"'";*/    
			
		return jdbcTemplate.queryForMap(sql);		
	}

}
