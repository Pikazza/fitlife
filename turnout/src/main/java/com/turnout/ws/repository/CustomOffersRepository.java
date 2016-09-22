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
 * Custom actions have been defined for CustomOffersRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomOffersRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;


	/**
	 * Returns list of offers after offers searching is done.
	 * @param name a search string
	 * @return list of offers 
	 */
	@Transactional
	public List offerSearch(String name)
	{
		String strSql = "";
		if(!name.equals("ALL")) {
			strSql = "OFR.OFR_DESCRIPTION LIKE '%"+name+"%'";
		} else {
			strSql = "OFR.OFR_DESCRIPTION !=''";
		}
		
		String sql="SELECT OFR.OFR_ID,OFR.OFR_STD_ID,OFR.OFR_TYPE,OFR.OFR_STATUS,OFR.OFR_IMG,OFR.OFR_DESCRIPTION,OFR.OFR_EXTERNAL_LINK,OFR.OFR_CREATED_DATE,OFR.OFR_MODIFIED_DATE,"
				+ " SD.STD_ID,SD.STD_NAME,SD.STD_COMPANY_LOGO"
				+ "  FROM OFFERS OFR LEFT JOIN STUDIOS SD ON OFR.OFR_STD_ID = SD.STD_ID"
				+ "  WHERE "+strSql;
		
		return jdbcTemplate.queryForList(sql);		
	}	

}
