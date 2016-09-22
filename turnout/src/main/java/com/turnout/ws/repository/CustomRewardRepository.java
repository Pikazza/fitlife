package com.turnout.ws.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Custom actions have been defined for CustomRewardRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomRewardRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	/**
	 * Returns voucher id based on given reward id.
	 * 
	 * @param rwdid An unique reward id.
	 * @return returns voucher id.
	 */
	public int getVoucherId(int rwdid)
	{
		String sql="SELECT  V.VOC_ID FROM REWARDS_HAS_VOUCHER AS RV LEFT JOIN VOUCHER AS V "
				+ " ON RV.VOC_VOC_ID = V.VOC_ID AND RV.RWD_RWD_ID ="+rwdid 
				+ " WHERE V.VOC_STATUS='ACTIVE' ORDER BY V.VOC_ID LIMIT 1";		
		int vchId= jdbcTemplate.queryForObject(sql, Integer.class);
		return vchId;
	}
	/**
	 * This method used to get all reward listing from database.
	 * 
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return returns list which contains all the rewards.
	 */ 
	public List getRewardListingByCustomQuery(int pageno, int pagesize) {
		int page = (pageno-1) * pagesize ;
		String sql = "SELECT S.STD_NAME,S.STD_COMPANY_LOGO,R.RWD_ID,R.RWD_NAME,R.RWD_DESCRIPTION,R.RWD_IMG_URL,RWD_POINTS,R.RWD_STD_ID "
				+ "FROM REWARDS AS R LEFT JOIN STUDIOS AS S ON R.RWD_STD_ID=S.STD_ID"
				+ " WHERE DATE(NOW())<= DATE(R.RWD_EXPIRY_DATE) AND RWD_STATUS = 'ACTIVE' AND  R.RWD_ID IN  ( SELECT  RV.RWD_RWD_ID FROM  REWARDS_HAS_VOUCHER AS RV LEFT JOIN VOUCHER AS V ON RV.VOC_VOC_ID=V.VOC_ID  WHERE V.VOC_STATUS = 'ACTIVE' GROUP BY RV.RWD_RWD_ID )  ORDER BY R.RWD_CREATED_DATE DESC LIMIT "+page+","+pagesize;		
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * Returns details of reward based on reward id.
	 * 
	 * @param rwdid An unique reward id.
	 * @return a map object with reward details
	 */
	public Map<String, Object> getRewardDetailById(int rwdid)
	{
		jdbcTemplate.execute("SET SESSION group_concat_max_len = 10000");
		String sql="SELECT S.STD_NAME,S.STD_COMPANY_LOGO,R.RWD_ID,R.RWD_NAME,R.RWD_DESCRIPTION,R.RWD_IMG_URL,RWD_POINTS,R.RWD_STD_ID,R.RWD_CREATED_DATE,R.RWD_MODIFIED_DATE,R.RWD_EXPIRY_DATE,R.RWD_STATUS, "
				+ " IFNULL((SELECT GROUP_CONCAT(VOC_CODE SEPARATOR ',') FROM REWARDS_HAS_VOUCHER AS RV LEFT JOIN VOUCHER AS V ON RV.VOC_VOC_ID = V.VOC_ID "
				+ " WHERE RWD_RWD_ID ="+rwdid+" ),'') AS ADDED_VOCCODE,"
				+ " IFNULL((SELECT GROUP_CONCAT(VOC_CODE SEPARATOR ',') FROM REWARDS_HAS_VOUCHER AS RV LEFT JOIN VOUCHER AS V ON RV.VOC_VOC_ID = V.VOC_ID"
				+ " WHERE RWD_RWD_ID ="+rwdid+" AND V.VOC_STATUS = 'CLAIMED' ),'') AS REEDEMED_VOCCODE"
				+ " FROM REWARDS AS R LEFT JOIN STUDIOS AS S ON R.RWD_STD_ID=S.STD_ID"
				+ " WHERE R.RWD_ID="+rwdid;
		return jdbcTemplate.queryForMap(sql);	
		
	}
	
	/**
	 * Returns list of rewards based on given studio id.
	 * 
	 * @param stdid An unique studio id.
	 * @return It will return list of rewards 
	 */
	public List getRewardByStdId(int stdid)
	{		
		String sql="SELECT S.STD_NAME,S.STD_COMPANY_LOGO,R.RWD_ID,R.RWD_NAME,R.RWD_DESCRIPTION,R.RWD_IMG_URL,RWD_POINTS,R.RWD_STD_ID,R.RWD_CREATED_DATE,R.RWD_MODIFIED_DATE,R.RWD_EXPIRY_DATE "
				+ " FROM REWARDS AS R LEFT JOIN STUDIOS AS S ON R.RWD_STD_ID=S.STD_ID"
				+ " WHERE DATE(NOW())<=DATE(R.RWD_EXPIRY_DATE) AND R.RWD_STD_ID="+stdid+" AND R.RWD_ID IN  ( SELECT  RV.RWD_RWD_ID FROM  REWARDS_HAS_VOUCHER AS RV LEFT JOIN VOUCHER AS V ON RV.VOC_VOC_ID=V.VOC_ID  WHERE V.VOC_STATUS = 'ACTIVE' GROUP BY RV.RWD_RWD_ID )  ORDER BY R.RWD_CREATED_DATE DESC ";
		return jdbcTemplate.queryForList(sql);
		
	}
	
	/**
	 * Returns list of rewards after search is completed with given search string 
	 * @param rwdname a reward name.
	 * @param stdid an unique studio id.
	 * @param type type of reward weather current or expired.
	 * @return list of rewards
	 */
	public List rewardSearch(String rwdname, int stdid, String type)
	{
		String strSql = "";
		String strStdId;
		if(stdid != 0) {
			strStdId = " ='"+stdid+"'";
		} else {
			strStdId = "!='0'";
		}
		if(!rwdname.equals("ALL")) {
			strSql = "AND RWD_NAME LIKE '%"+rwdname+"%'";
		}

		if(!type.equals("A")) {
			if(type.equals("C")) {
				strSql = "AND RWD_EXPIRY_DATE >= CURRENT_DATE";
			} else {
				strSql = "AND RWD_EXPIRY_DATE < CURRENT_DATE";
			}
		}
		
		String sql = "SELECT * FROM REWARDS WHERE RWD_STD_ID"+strStdId+ " "+strSql;
		return jdbcTemplate.queryForList(sql);		
	}
	

}