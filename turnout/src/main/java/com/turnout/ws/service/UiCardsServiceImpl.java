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

import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.UiCards;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.repository.CustomUiCardsRepository;
import com.turnout.ws.repository.StudioRepository;
import com.turnout.ws.repository.UiCardsRepository;

/**
 * UiCardsServiceImpl is class that contains collection of methods that can be accessed for manipulating ui cards. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
public class UiCardsServiceImpl  implements UiCardsService{
	private static final Logger LOGGER = LoggerFactory.getLogger(UiCardsServiceImpl.class);
private final UiCardsRepository uiCardsRepository;
private final CustomUiCardsRepository customUiCardsRepository;
	
	/**
	 * An injectable constructor with a dependency of uiCardsRepository,customUiCardsRepository as argument.
	 * 
	 * @param uiCardsRepository An Object of uiCardsRepository as an injectable member.
	 * @param customUiCardsRepository An Object of customUiCardsRepository as an injectable member.
	 * @see uiCardsRepository
	 * @see customUiCardsRepository
	 */
	@Autowired
	public UiCardsServiceImpl(final UiCardsRepository uiCardsRepository,final CustomUiCardsRepository customUiCardsRepository) {
		
		this.uiCardsRepository = uiCardsRepository;
		this.customUiCardsRepository = customUiCardsRepository;
	}
	/**
	 * This method used to get premium and stream details from database.
	 * Premium details shown in first card of the streaming.
	 * Streaming details will come after the premium card.
	 * 
	 * @param ptyid the primary key of the party.
	 * @param pageno current page number.
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONObject contains all the records.
	 */
	public JSONObject getStream(int ptyid,int pageno, int pagesize) {
		
		List<UiCards> uicards = uiCardsRepository.findAll();
		LOGGER.debug(uicards.toString());
		JSONObject result = new JSONObject();
		for(UiCards u : uicards)
		{
			LOGGER.debug("-----------------------------------------------------"+u.getName());
			switch(u.getName())
			{
			case "PREMIUM" :
				LOGGER.debug("Enters into premium");
				List prmStudio = customUiCardsRepository.getPremiumStudio(ptyid);	
				if(prmStudio != null || prmStudio.size() > 0)
					result.put("PREMIUM",  convertListtoJson(prmStudio));
				break;
			case "STREAMING" :
				LOGGER.debug("Enters into STREAMING");
				List streamList = customUiCardsRepository.getStream(ptyid,pageno,pagesize);
				result.put("STREAM", convertListtoJson(streamList));
				break;
			default:
				
				break; 
			}
		}
		return result;
	}
	/**
	 * This method used to convert ArrayList into JSONArray
	 * 
	 * @param selList This variable holds ArrayList. 
	 * @return itcontains ArrayList
	 */
	private JSONArray convertListtoJson(List selList) {
		JSONArray jarray = new JSONArray();
		Map<String, Object> recrod;
		JSONObject object ;		
		if(selList != null && selList.size() > 0)
		{
			for(Iterator itr=selList.iterator();itr.hasNext();)
			{
				object=new JSONObject();
				recrod = (Map) itr.next();	
				for (Map.Entry<String, Object> entry : recrod.entrySet())
				{					
					LOGGER.debug(entry.getKey()+ entry.getValue());
					object.put(entry.getKey(), (entry.getValue() == null)?"" :entry.getValue().toString());	
				}
				jarray.add(object);
			}
			
		}	
		return jarray;
	}
}
