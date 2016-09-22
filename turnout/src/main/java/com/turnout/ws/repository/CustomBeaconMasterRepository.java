package com.turnout.ws.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
/**
 * Custom actions have been defined for CustomBeaconMasterRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomBeaconMasterRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * Returns list of beacon object with their details.
	 * 
	 * @param search A string that is passed for searching a beacons table.
	 * @return List of beacons.
	 */
	public List getAllBeacons(String search) {
		
		String whr_keyword = "";
		String condition = "";
		if(!(search.equals("ALL") || search.isEmpty())) {
			whr_keyword = " WHERE";
			condition = "BCON_ID LIKE '%"+search+"%' OR STD_NAME LIKE '%"+search+"%'";
		}
		String sql = "SELECT BCON_ID,BCON_SITE,BCON_STD_ID,BCON_STA_TYPE_ID,BCON_DETECT_TYPE,BCON_STATUS,"
				   + "(CASE WHEN BCON_STA_TYPE_ID = 2 THEN 'CLASS' ELSE 'EVENT' END) AS STA_TYPE,STD_NAME"
				   + " FROM BEACON_MASTER AS B LEFT JOIN STUDIOS AS S ON B.BCON_STD_ID = S.STD_ID" 
				   +whr_keyword+ " "+condition;
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * Returns total number of beacons the table has.
	 * 
	 * @param bconId an unique beacon id.
	 * @param staType type of an activity (challenges or events).
	 * @param bconType type of beacon.
	 * @return total number of beacons the table has.
	 */
	public int checkBeaconMaster(String bconId,int staType, String bconType) {
		
		String sql = "SELECT COUNT(*) as total FROM BEACON_MASTER WHERE BCON_ID = '"+bconId+"' AND BCON_DETECT_TYPE = '"+bconType+"' AND BCON_STA_TYPE_ID = '"+staType+"' AND BCON_STATUS = 'ACTIVE'";		
		int count= jdbcTemplate.queryForObject(sql, Integer.class);
		return count;
	}
	
	
}
