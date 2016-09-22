package com.turnout.ws.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.BeaconMaster;
import com.turnout.ws.domain.Offers;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.BeaconMasterRepository;
import com.turnout.ws.repository.CustomOffersRepository;
import com.turnout.ws.repository.OffersRepository;
import com.turnout.ws.repository.StudioRepository;

/**
 * OffersServiceImpl is class that contains collection of methods that can be accessed for manipulating offers(promos and upsells). All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
@Validated
public class OffersServiceImpl implements OffersService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OffersServiceImpl.class);

	 private final OffersRepository ofrRepository;
	 private final BeaconMasterRepository beaconRepository;
	 private final StudioRepository stdRepository;
	 private final CustomOffersRepository customOffersRepository;
	 
	 /**
	  * An injectable constructor with a dependencies as argument.
	  * 
	  * @param ofrRepository An Object of ofrRepository as an injectable member.
	  * @param beaconRepository An Object of beaconRepository as an injectable member.
	  * @param stdRepository An Object of stdRepository as an injectable member.
	  * @param customOffersRepository An Object of customOffersRepository as an injectable member.
	  * @see ofrRepository
	  * @see beaconRepository
	  * @see stdRepository
	  * @see customOffersRepository
	  */
	 @Autowired
	 public OffersServiceImpl(final OffersRepository ofrRepository,final BeaconMasterRepository beaconRepository, final StudioRepository stdRepository, final CustomOffersRepository customOffersRepository) {
		 this.ofrRepository = ofrRepository;
		 this.beaconRepository = beaconRepository;
		 this.stdRepository = stdRepository;
		 this.customOffersRepository = customOffersRepository;
	}
	/**
	 * This method used to get active offers from database based on the beacon id and offer type.
	 * 
	 * @param beaconId  an unique string mapped with studio.
	 * @param type  welcome or exit type offer.
	 * @return return all active offers mapped with studio.
	 */
	@Transactional
	public JSONObject getActiveOffers(String beaconId , String type) {
		List<BeaconMaster> bmlist = beaconRepository.findByBconIdOrderByBconDetectTypeDesc(beaconId);
		int stdId =0;		
		JSONObject obj = new JSONObject();
		for(BeaconMaster beaconMaster : bmlist)
		{
		if(beaconMaster.getBconDetectType().equals("OWN"))
		{
			stdId = beaconMaster.getBconStdId();
			List<Offers> offersList = new ArrayList<Offers>() ;
			offersList = ofrRepository.findByOfrStdIdAndOfrStatus(stdId, "ACTIVE");				
		
			if(offersList.size() > 0) {	
			
				Studio stdObj = stdRepository.findOne(stdId);				
				obj.put("STD_NAME", stdObj.getStdName());
				obj.put("STD_LOGO", stdObj.getStdCompanyLogo());
				JSONArray arayList1 = new JSONArray();			
				for(Offers ofr:offersList) {
					if(ofr.getOfrType().equals(type)) {
						JSONObject obj1 = new JSONObject();
						obj1.put("OFR_ID", ofr.getOfrId());
						obj1.put("OFR_STD_ID", ofr.getOfrStdId());
						obj1.put("OFR_IMG", ofr.getOfrImg());
						obj1.put("OFR_DESCRIPTION", ofr.getOfrDescription());
						obj1.put("OFR_EXTERNAL_LINK", ofr.getOfrExternalLink());
						obj1.put("OFR_CREATED_DATE", ofr.getOfrCreatedDate());
						obj1.put("OFR_TYPE", ofr.getOfrType());
						arayList1.add(obj1);
						} 
					}
				obj.put("OFFERS", arayList1);
			}	
		}
		}
		return obj;
    }
	
/*	
 *  @Transactional
	public JSONObject addOffers(int ofrStdId,String ofrType,String ofrStatus,String ofrImg,String ofrDescription,String ofrExternalLink,Date ofrCreatedDate) {
		
		JSONObject result = new JSONObject();
		if(ofrStdId != 0 || ofrType != null || ofrStatus != null ) {
			Offers ofr = new Offers();
			ofr.setOfrStdId(ofrStdId);
			ofr.setOfrType(ofrType);
			ofr.setOfrStatus(ofrStatus);
			ofr.setOfrImg(ofrImg);
			ofr.setOfrDescription(ofrDescription);
			ofr.setOfrExternalLink(ofrExternalLink);
			ofr.setOfrCreatedDate(ofrCreatedDate);
			ofrRepository.saveAndFlush(ofr);
			result.put("OFR_ID", ofr.getOfrId());
		} else {
			result.put("RESULT", TurnOutConstant.FAILED);
		}
		
		return result;		
	}
	@Transactional
	public JSONObject updateOffers(int ofrId, int ofrStdId, String ofrType, String ofrStatus, String ofrImg, String ofrDescription, String ofrExternalLink, Date ofrCreatedDate) {
		
		JSONObject result = new JSONObject();
		if(ofrId != 0 ) {
			Offers ofr = ofrRepository.findOne(ofrId);
			ofr.setOfrStdId(ofrStdId);
			ofr.setOfrType(ofrType);
			ofr.setOfrStatus(ofrStatus);
			ofr.setOfrImg(ofrImg);
			ofr.setOfrDescription(ofrDescription);
			ofr.setOfrExternalLink(ofrExternalLink);
			ofr.setOfrCreatedDate(ofrCreatedDate);
			ofrRepository.saveAndFlush(ofr);
			result.put("RESULT", TurnOutConstant.SUCCESS);
		} else {
			result.put("RESULT", TurnOutConstant.FAILED);
		}
		
		return result;		
	}*/
	/**
	 * This method is used to save welcome/ exit notification offers into database. It will return a offer id of just inserted or updated.
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
	@Transactional
	public JSONObject saveOffers(int ofrId, int ofrStdId, String ofrType, String ofrStatus, String ofrImg, String ofrDescription, String ofrExternalLink, Date ofrCreatedDate) {
		
		JSONObject result = new JSONObject();
		if(ofrStdId != 0 ) {
			Offers ofr;
			if(ofrId != 0) {
				ofr = ofrRepository.findOne(ofrId);	
			} else {
				ofr = new Offers();
			}			
			ofr.setOfrStdId(ofrStdId);
			ofr.setOfrType(ofrType);
			ofr.setOfrStatus(ofrStatus);
			ofr.setOfrImg(ofrImg);
			ofr.setOfrDescription(ofrDescription);
			ofr.setOfrExternalLink(ofrExternalLink);
			ofr.setOfrCreatedDate(ofrCreatedDate);
			ofrRepository.saveAndFlush(ofr);
			result.put("OFR_ID", ofr.getOfrId());
		} else {
			result.put("RESULT", TurnOutConstant.FAILED);
		}
		
		return result;		
	}	
	/**
	 * This method used to get all the offers from database and helps to search the offers based on offer name passed string.
	 * 
	 * @param ofrname name of the offer that has to be searched.
	 * @return returns JSONArray contains all the results after searching is done.
	 */
	@Transactional
	public JSONArray offerSearch(String ofrname) {

		JSONArray listarr = new JSONArray();
		JSONObject listobj;
		Map<String, Object> record;
		
		List rs = customOffersRepository.offerSearch(ofrname);
		if( rs.size() != 0) {
			for (Iterator itr = rs.iterator(); itr.hasNext();) {
				record = (Map) itr.next();
				listobj = new JSONObject();
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					listobj.put(entry.getKey(), entry.getValue().toString());
				}
				listarr.add(listobj);
		    }
		} else {
			listobj = new JSONObject();
			listobj.put("Records", TurnOutConstant.NOT_EXIST);
			listarr.add(listobj);
		}		
		return listarr;
	}
	
	/**
	 * This method will get offer details from database based on offer id. 
	 * 
	 * @param ofrId the primary key offer.
	 * @return return full details about offers. 
	 */
	@Transactional
	public JSONObject offerDetails(int ofrId) {
		Offers ofr = ofrRepository.findOne(ofrId);
		JSONObject obj = new JSONObject();
		Studio stdObj = stdRepository.findOne(ofr.getOfrStdId());
		obj.put("OFR_ID", ofr.getOfrId());
		obj.put("OFR_STD_ID", ofr.getOfrStdId());
		obj.put("OFR_IMG", ofr.getOfrImg());
		obj.put("OFR_DESCRIPTION", ofr.getOfrDescription());
		obj.put("OFR_EXTERNAL_LINK", ofr.getOfrExternalLink());
		obj.put("OFR_CREATED_DATE", ofr.getOfrCreatedDate().toString());
		obj.put("OFR_TYPE", ofr.getOfrType());
		obj.put("STD_NAME", stdObj.getStdName());
		obj.put("OFR_STATUS", ofr.getOfrStatus());
		return obj;
    }	
}
