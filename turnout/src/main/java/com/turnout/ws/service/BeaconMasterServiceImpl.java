package com.turnout.ws.service;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.turnout.ws.domain.BeaconMaster;
import com.turnout.ws.repository.BeaconMasterRepository;
import com.turnout.ws.repository.CustomBeaconMasterRepository;
/**
 * BeaconMasterServiceImpl is class that contailns collection of methods that can be accessed for manipulating beacons. All the methods declared in service interface is implemented here.
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
public class BeaconMasterServiceImpl implements BeaconMasterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BeaconMasterServiceImpl.class);
	private final BeaconMasterRepository beaconMasterRepository;
	private final CustomBeaconMasterRepository customBeaconMasterRepository;
	
	/**
	 * An injectable constructor with a dependency of beaconMasterRepository,customBeaconMasterRepository as argument.
	 * 
	 * @param beaconMasterRepository An Object of beaconMasterRepository as an injectable member.
	 * @param customBeaconMasterRepository An Object of beaconMasterRepository as an injectable member.
	 * @see BeaconMasterRepository
	 * @see customBeaconMasterRepository
	 */
	@Autowired
	public BeaconMasterServiceImpl(final BeaconMasterRepository beaconMasterRepository,
			final CustomBeaconMasterRepository customBeaconMasterRepository) {
		this.beaconMasterRepository = beaconMasterRepository;
		this.customBeaconMasterRepository = customBeaconMasterRepository;
	}
	
	/**
	 * This beaconList function used to get beacons details from database and store that value into {@link JSONArray} 
	 * 
	 * @param search {String} The string may have the value of beacon id and studio name. 
	 * @return It holds all the beacons in jsonarray format.
	 */

	@Override
	public JSONArray beaconsList(String search) {	
		JSONArray beaconsAray = new JSONArray();
		JSONObject obj;
		Map<String, Object> record;
		List beaconslist = customBeaconMasterRepository.getAllBeacons(search);
		for (Iterator itr = beaconslist.iterator(); itr.hasNext();) {
			record = (Map) itr.next();
			obj = new JSONObject();
			for (Map.Entry<String, Object> entry : record.entrySet()) {					
				obj.put(entry.getKey(), entry.getValue().toString());				
			}
			beaconsAray.add(obj);
	    }
		return beaconsAray;	
	}
	/**
	 * This beaconInfo method will get beacon details from database based on studio id and beacon id.
	 * 
	 * @param bconId A primary key of beacon element.
	 * @param stdId  A primary key of studio element
	 * @return Returns JSONObject which contains whole details of beacon element.
	 */
	@Override
	public JSONObject beaconInfo(String bconId, int stdId) {
		JSONObject obj = new JSONObject();
		BeaconMaster beaconObj;
		beaconObj = beaconMasterRepository.findByBconIdAndBconStdId(bconId, stdId);
		obj.put("BCON_ID", beaconObj.getBconId());
		obj.put("BCON_SITE", beaconObj.getBconSite());
		obj.put("BCON_STD_ID", beaconObj.getBconStdId());
		obj.put("BCON_STA_TYPE_ID", beaconObj.getBconStaTypeId());
		obj.put("BCON_DETECT_TYPE", beaconObj.getBconDetectType());
		obj.put("BCON_STATUS", beaconObj.getBconStatus());
		return obj;
	}
	/**
	 * This method used to save a beacon details into database.It will return a beacon id (primary key) of just inserted or updated beacon.
	 * If already beacon data exist in database then it will update information otherwise it will add into new record. 
	 * 
	 * @param bconId This string holds beacon unique website address.
	 * @param staType This integer holds studio activity type (event / challenge).
	 * @param stdId This integer holds the unique studio id.
	 * @param detectType This string holds beacon type (Own / Fiternity). 
	 * @param bconStatus This string holds beacon status (Active / Inactive).
	 * @return  it will return just inserted beacon id.
	 * 
	 */
	@Override
	public JSONObject saveBeacon(String bconId,String bconSite, int staType, int stdId, String detectType,String bconStatus) {

		JSONObject obj = new JSONObject();
		BeaconMaster bcon;
		bcon=	beaconMasterRepository.findByBconId(bconId);
		if(bcon!=null){
			System.out.println("the beacon exists");
			beaconMasterRepository.delete(bcon);
			beaconMasterRepository.flush();
		}
			bcon = new BeaconMaster();		
			bcon.setBconId(bconId);
			bcon.setBconSite(bconSite);
			bcon.setBconStaTypeId(staType);
			bcon.setBconStdId(stdId);
			bcon.setBconDetectType(detectType);
			bcon.setBconStatus(bconStatus);
			
			beaconMasterRepository.saveAndFlush(bcon);
			obj.put("BCON_ID", bcon.getBconId());
		return obj;
	}	
}
	
