package com.turnout.ws.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Likes;

/**
 * Custom actions have been defined for CustomLikeRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomLikeRepository {

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	/**
	 * Returns weather a party has already liked like type or not .
	 * 
	 * @param likeTypeId an id of like type.
	 * @param likeType type of the like "COMMENTS / STUDIOS_ACTIVITY".
	 * @param likepartyid an unique id of party 
	 * @return Map contains like count.
	 */
	public Map<String, Object> chkPartyLike(int likeTypeId, String likeType, int likepartyid) {
		
		String sql = "SELECT COUNT(LIKE_YN) AS CNT,LIKE_YN,LIKE_ID FROM LIKES WHERE LIKE_TYPE='"+likeType+"' AND LIKE_TYPE_ID="+likeTypeId+" AND LIKE_PTY_ID="+likepartyid;
		
		return jdbcTemplate.queryForMap(sql);
	}
	
	/**
	 * Check and return party's devices token based on like type id.
	 * @param likeTypeId an id of like type.
	 * @return map object with device token.
	 */
	public Map<String, Object> chkpartyDevicetoken(int likeTypeId)
	{
		String sql = "SELECT P.PTY_ID,CONCAT(P.PTY_NAME,' ',P.PTY_LAST_NAME) AS PTY_NAME,P.PTY_DEVICE_TOKEN,P.PTY_DEVICE_TYPE,(SELECT COUNT(LIKE_PTY_ID) FROM LIKES WHERE LIKE_TYPE_ID ="+likeTypeId+" AND LIKE_YN = 'Y' ) AS LIKEMEMCNT,"
					+ "	C.CMT_TYPE_ID AS ACTIVITY_ID FROM COMMENTS AS C "
					+ "	LEFT JOIN PARTY AS P ON C.CMT_PTY_ID = P.PTY_ID "
					+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON C.CMT_PTY_ID = PN.NOTIFY_PTY_ID "
					+ " WHERE C.CMT_ID ="+likeTypeId+" AND PN.NOTIFY_OTH_CMTS = 'Y'";
		/*String sql="SELECT (SELECT CONCAT(PTY_ID,'<',PTY_NAME,'>',PTY_PHOTO) FROM PARTY WHERE PTY_ID="+pty_id+") AS INFO,P.PTY_DEVICE_TOKEN,P.PTY_DEVICE_TYPE,CASE  (SELECT COUNT(*) FROM LIKES WHERE LIKE_TYPE_ID = "+likeTypeId+" AND LIKE_YN = 'Y') "
				+ " WHEN 1  THEN CONCAT('<strong>',P.PTY_NAME,'</strong> liked your post') ELSE CONCAT('<strong>',P.PTY_NAME,'</strong> and ',(SELECT COUNT(*) FROM LIKES WHERE LIKE_TYPE_ID = "+likeTypeId+" AND LIKE_YN = 'Y')-1,' others has liked your post') END  AS MESSAGES,"
				+ " NOW() AS CUR_TIME FROM COMMENTS AS C "
				+ " LEFT JOIN PARTY AS P ON C.CMT_PTY_ID = P.PTY_ID "
				+ " RIGHT JOIN PARTY_NOTIFICATION AS PN ON C.CMT_PTY_ID = PN.NOTIFY_PTY_ID "
				+ " WHERE C.CMT_ID = "+likeTypeId+" AND PN.NOTIFY_OTH_LIKES = 'Y' AND P.PTY_DEVICE_TYPE='"+deviceType+"'"  ;*/
		
		return jdbcTemplate.queryForMap(sql);			
	}

}
