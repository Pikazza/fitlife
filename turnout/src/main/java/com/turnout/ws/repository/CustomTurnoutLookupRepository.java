package com.turnout.ws.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Custom actions have been defined for CustomTurnoutLookupRepository and encapsulates custom retrieval and search behavior with own queries.
 *
 */
@Repository
public class CustomTurnoutLookupRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * Returns List of entities where filter condition is satisfied.
	 * @param tablename a table name from database.
	 * @param filtercond a condition that has to be checked for filtering results from given table.
	 * @return list of entities
	 */
	public List getLookupdatawithCond(String tablename, String filtercond) {
		String condition="";
		if(!filtercond.equalsIgnoreCase("")|| filtercond.length()>1)
		{
			condition=" where "+filtercond;
		}
		String sql="Select * from "+tablename+condition;
		return jdbcTemplate.queryForList(sql);
	}
	

}
