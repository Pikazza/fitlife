package com.turnout.ws.service;

import org.json.simple.JSONArray;

/**
 * TurnoutLookupService is an interface that contains a methods that can be accessed to read tables data. So controller can access this methods written in this interface and implemented in TurnoutLookupServiceImpl class.
 * 
 * It has some restrictions on parameters while attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface TurnoutLookupService {
	
	/**
	 * This method will list records of given table when filtering condition satisfies.
	 * 
	 * @param tablename a table name from database that has to be listed.
	 * @param filtercond a String as filter condition that has to be matched in table.
	 * @return return records list in JSONArray when satisfies filtering condition on given table name.
	 */
	JSONArray getLookupdatawithCond(String tablename, String filtercond);
}