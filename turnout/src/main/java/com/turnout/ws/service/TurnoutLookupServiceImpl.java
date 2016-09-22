package com.turnout.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.repository.CustomTurnoutLookupRepository;

/**
 * TurnoutLookupService is an interface that contains a methods that can be accessed to read tables data. 
 * 
 * It has some restrictions on parameters while attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
@Validated
public class TurnoutLookupServiceImpl implements TurnoutLookupService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TurnoutLookupServiceImpl.class);
	private final CustomTurnoutLookupRepository customTurnoutLookupRepository;
	
	/**
	 * An injectable constructor with a dependency of customTurnoutLookupRepository as argument.
	 * 
	 * @param customTurnoutLookupRepository An Object of beaconMasterRepository as an injectable member.
	 * @see customTurnoutLookupRepository
	 */
	@Autowired
	public TurnoutLookupServiceImpl(final CustomTurnoutLookupRepository customTurnoutLookupRepository ) {
		this.customTurnoutLookupRepository=customTurnoutLookupRepository;
	}
	
	/**
	 * This method will list records of given table when filtering condition satisfies.
	 * 
	 * @param tablename a table name from database that has to be listed.
	 * @param filtercond a String as filter condition that has to be matched in table.
	 * @return return records list in JSONArray when satisfies filtering condition on given table name.
	 */
	
	//@Override
	public JSONArray getLookupdatawithCond(String tablename, String filtercond) {
		JSONArray getLkpdatawithCond = new JSONArray();
		JSONObject obj;
		Map<String, Object> record;
		List lookupDataList = customTurnoutLookupRepository.getLookupdatawithCond(tablename,filtercond);		
		for (Iterator itr = lookupDataList.iterator(); itr.hasNext();) {
			record = (Map) itr.next();
			obj = new JSONObject();
			for (Map.Entry<String, Object> entry : record.entrySet()) {
				LOGGER.debug(entry.getKey() + "/" + entry.getValue());
				obj.put(entry.getKey(), entry.getValue().toString());				
			}
			getLkpdatawithCond.add(obj);		
		}
		return getLkpdatawithCond;
	}
}