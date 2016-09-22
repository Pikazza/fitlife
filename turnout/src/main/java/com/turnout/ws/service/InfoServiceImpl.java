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
import org.springframework.transaction.annotation.Transactional;

import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.repository.InfoRepository;

/**
 * InfoServiceImpl is class that contains collection of methods that can be accessed for manipulating notifications. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
public class InfoServiceImpl implements InfoService {
	private static final Logger LOGGER=LoggerFactory.getLogger(InfoServiceImpl.class);
	private final InfoRepository infoRepository;
	
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param infoRepository An Object of infoRepository as an injectable member.
	 * @see infoRepository
	 */
	@Autowired
	public InfoServiceImpl(final InfoRepository infoRepository) {
		this.infoRepository = infoRepository;
	}
	
	/**
	 * Return all the notification and error messge title and description from database.
	 * 
	 * @param type message type.
	 * @return return JSONObject contains value of messages.
	 */
	@Override
	public JSONObject getListInfo(String type) {
		JSONObject obj = new JSONObject();
		Info infoObj = infoRepository.findByType(type);
		obj.put("ID", infoObj.getId());
		obj.put("DESCRIPTION", infoObj.getDescription());
		obj.put("TYPE", infoObj.getType());
		obj.put("TITLE",infoObj.getTitle());
		return obj;
	}
	/**
	 * Return all the error and notification from the database.
	 * 
	 * @return return jsonarray contains all the details.
	 */
	@Override
	public JSONArray getAllListInfo() {
		JSONObject obj;
		JSONArray aray  = new JSONArray();
		Map<String, Object> record;
		List<Info> infoObj = infoRepository.findAllByOrderByIdAsc();
		for (Info single:infoObj) {
			obj = new JSONObject();
			obj.put("ID", single.getId());
			obj.put("TYPE", single.getType());
			obj.put("TITLE", single.getTitle());
			obj.put("DESCRIPTION", single.getDescription());
			
			aray.add(obj);		
	    }
		return aray;
	}
	
	/**
	 * Used to save message details into database and it will return updated data primary key id.
	 * 
	 * @param id the primary key of info.
	 * @param desc notification or error message description.
	 * @param type message type.
	 * @param title message title.
	 * @return return just updated message id.
	 */
		
	@Override
	public JSONObject saveInfo(int id,String desc,String type,String title)
	{
		
		Info infoObj = infoRepository.findOne(id);		
		infoObj.setDescription(desc);
		infoObj.setTitle(title);
		infoObj.setType(type);
		infoRepository.saveAndFlush(infoObj);
		JSONObject obj = new JSONObject();
		obj.put("ID", infoObj.getId());
		return obj;
	}

}
