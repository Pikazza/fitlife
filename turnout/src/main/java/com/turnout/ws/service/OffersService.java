package com.turnout.ws.service;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * OffersService is an interface that contains collection of methods that can be accessed for manipulating offers (promos and upsells). So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in OffersServiceImpls class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface OffersService {

	/**
	 * Get active offers based on beacon id and type of the offer.
	 * 
	 * @param beaconId an unique string mapped with studio.
	 * @param type welcome or exit type offer.
	 * @return return all active offers mapped with studio.
	 */
	public JSONObject getActiveOffers(String beaconId, String type);
	
	/*
	public JSONObject addOffers(int ofrStdId,String ofrType,String ofrStatus,String ofrImg,String ofrDescription,String ofrExternalLink,Date ofrCreatedDate);

	public JSONObject updateOffers(int ofrId, int ofrStdId, String ofrType, String ofrStatus, String ofrImg, String ofrDescription, String ofrExternalLink, Date ofrCreatedDate);
	*/
	
	/**
	 * Used to store the offers into table. It will return (primary key) offer id.
	 * 
	 * @param ofrId the primary key of offers.
	 * @param ofrStdId the primary key of studio.
	 * @param ofrType welcome or exit type offer.
	 * @param ofrStatus active or inactive status.
	 * @param ofrImg offer image.
	 * @param ofrDescription description about the offer.
	 * @param ofrExternalLink external link.
	 * @param ofrCreatedDate offer created date.
	 * @return return offer id.
	 */
	public JSONObject saveOffers(int ofrId, int ofrStdId, String ofrType, String ofrStatus, String ofrImg, String ofrDescription, String ofrExternalLink, Date ofrCreatedDate);
	
	/**
	 * This method used to get all the offers from database and helps to search the offers based on offer name passed string.
	 * 
	 * @param ofrname name of the offer that has to be searched.
	 * @return eturns JSONArray contains all the results after searching is done.
	 */
	public JSONArray offerSearch(String ofrname);
	
	/**
	 * used to get offer details based on the offer id pass.
	 * 
	 * @param ofrId the primary key offer.
	 * @return return full details about offers. 
	 */
	
	public JSONObject offerDetails(int ofrId);
	
}
