package com.turnout.ws.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.turnout.ws.service.TurnoutLookupService;


@Component
@Path("/getRefData")
public class TurnoutLookupController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TurnoutLookupController.class);
	private final TurnoutLookupService trunoutLookupService;
	/**
	 * An injectable constructor with a dependency of TurnoutLookupService as argument.
	 * 
	 * @param trunoutLookupService An Object of TurnoutLookupService as an injectable member.
	 * @see TurnoutLookupService.
	 */
	@Autowired
	public TurnoutLookupController(final TurnoutLookupService trunoutLookupService)
	{
		this.trunoutLookupService=trunoutLookupService;		
	}
	
	/**
	 * This method will list records of given table when filtering condition satisfies.
	 * 
	 * @param tablename A table name from database that has to be listed.
	 * @param filtercond A String as filter condition that has to be matched in table.
	 * @return Returns records list in JSONArray when satisfies filtering condition on given table name.
	 */
	@GET
	@Path("/tablename/{tablename}/filtercond/{filtercond}")
		@Produces("application/json")
	public JSONArray getLookupdatawithCond(@PathParam("tablename")String tablename,@PathParam("filtercond")String filtercond)
	{
		return trunoutLookupService.getLookupdatawithCond(tablename.toUpperCase(),filtercond);
	}
}