package com.turnout.ws.controller;

import java.text.ParseException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.exception.NoSufficientGainedPointsException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.exception.RewardNotFoundException;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.service.InfoService;
import com.turnout.ws.service.RewardService;
import com.turnout.ws.service.StudioService;

/**
*
* RewardController class is front face for accessing and manipulating reward related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* Basic CRUD operation is done like update, retrieve, search and redeeming reward points.
*/
@RestController
@Singleton
@Path("/")
public class RewardController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RewardController.class);
	
	private final RewardService rewardService;
	private final StudioService stdService;
	private final InfoService infoService;
	
	/**
	 * An injectable constructor with dependencies as argument.
	 * 
	 * @param rewardService An Object of rewardService as an injectable member.
	 * @param stdService An Object of stdService as an injectable member.
	 * @param infoService An Object of infoService as an injectable member.
	 * @see rewardService
	 * @see stdService
	 * @see infoService
	 */ 
	@Autowired
	public RewardController(final RewardService rewardService, 
			final StudioService stdService,	final InfoService infoService) {
		this.rewardService = rewardService;
		this.stdService = stdService;
		this.infoService = infoService;
	}
	
	/**
	 * This method used to validate the voucher code.
	 * 
	 * @param redemReq A JSONOject contains information of redeem.
	 * @return Returns JSONObject that has a value of success message.
	 * @throws NumberFormatException It throws when alphanumeric converted to numeric value.
	 * @throws NoSufficientGainedPointsException It throws when there is no sufficient gained point while redeeming reward.
	 */
	
	@RolesAllowed("USER")
	@POST	
	@Path("/validateVoucher")
	@Produces("application/json")
	public JSONObject reedemReward(@Valid JSONObject redemReq) throws NumberFormatException, NoSufficientGainedPointsException
	{
		String ptyId = redemReq.get("PTY_ID").toString();
		String rwdId = redemReq.get("RWD_ID").toString();
		return rewardService.reedemReward(Integer.parseInt(ptyId),Integer.parseInt(rwdId));
		
	}
	
	/**
	 * This method used to get all reward listing from database.
	 * 
	 * @param pageno A integer value for which page record to display.
	 * @param pagesize A integer value to show how many records need to display.
	 * @return Returns JSONArray contains all the reward listing.
	 */
	
	@RolesAllowed("USER")
	  @GET
	  @Path("/rewardListing/{pageno}/{pagesize}")
	  @Produces("application/json")
	  public JSONArray rewardListing(@PathParam("pageno") int pageno,@PathParam("pagesize") int pagesize){
		  
		return rewardService.getRewardListing(pageno,pagesize) ;
		  
	  }
	
	/**
	 * This method used to get reward detail from database based on reward id passed.
	 * 
	 * @param rewardid A primary key of reward element.
	 * @return Returns JSONObject which contains whole details of reward element.
	 * @throws RewardNotFoundException
	 */
	  
		@RolesAllowed({"ADMIN","USER"})
	  	@GET
		@Path("/rewardDetails/rewardid/{rewardid}")
		@Produces("application/json")
		public JSONObject getRewardDetailById(@PathParam("rewardid") int rewardid) throws RewardNotFoundException 
		{
			if(rewardid == 0) {
				throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
			} else if(!rewardService.isRewardExist(rewardid)) {
				  JSONObject msgObj = infoService.getListInfo("no-rwds");
				  throw new RewardNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Rewarddetails");
				//throw new RewardNotFoundException(propsControl.getRewardnotfoundTitle()+"~"+propsControl.getRewardnotfoundText()+"~Rewarddetails");
			}
			return rewardService.getRewardDetail(rewardid);		
		}
	

	/*	@RolesAllowed("ADMIN")
	  	@POST
	  	@Path("/addReward")
	  	@Produces("application/json")
	  	public JSONObject addReward(@Valid JSONObject rwdReq) throws NumberFormatException, ParseException
	  	{
	  		String rwdName    = rwdReq.get("RWD_NAME").toString();
	  		String rwdDesc    = rwdReq.get("RWD_DESCRIPTION").toString();
	  		String rwdImg     = rwdReq.get("RWD_IMG_URL").toString();
	  		String rwdPoints  = rwdReq.get("RWD_POINTS").toString();	  		
	  		String rwdExpDate = rwdReq.get("RWD_EXPIRY_DATE").toString();          
	  		String rwdStdid   = rwdReq.get("RWD_STD_ID").toString();
	  		String rwdStatus  = rwdReq.get("RWD_STATUS").toString().toUpperCase();
	  		
	  		JSONObject resultJson;
	  		if(rwdStdid == null || rwdStdid == "") {
	  			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
	  		} else {
	  			boolean resStd = stdService.isStudioExist(Integer.parseInt(rwdStdid));	  			
	  		}
	  		resultJson = rewardService.addReward(rwdName,rwdDesc,rwdImg,Integer.parseInt(rwdPoints),rwdExpDate,Integer.parseInt(rwdStdid),rwdStatus); 
	  		 		 
			return resultJson;
	  		
	  	}
	  	
	  	@RolesAllowed("ADMIN")
	  	@POST
	  	@Path("/updateReward")
	  	@Produces("application/json")
	  	public JSONObject updateReward(@Valid JSONObject rwdReq) throws NumberFormatException, ParseException
	  	{
	  		int rwdId         = Integer.parseInt(rwdReq.get("RWD_ID").toString());
	  		String rwdName    = rwdReq.get("RWD_NAME").toString();
	  		String rwdDesc    = rwdReq.get("RWD_DESCRIPTION").toString();
	  		String rwdImg     = rwdReq.get("RWD_IMG_URL").toString();
	  		String rwdPoints  = rwdReq.get("RWD_POINTS").toString();	  		
	  		String rwdExpDate = rwdReq.get("RWD_EXPIRY_DATE").toString();          
	  		String rwdStdid   = rwdReq.get("RWD_STD_ID").toString();
	  		String rwdStatus  = rwdReq.get("RWD_STATUS").toString().toUpperCase();
	  		
	  		JSONObject resultJson;
	  		if(rwdStdid == null || rwdStdid == "") {
	  			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
	  		} else if(rwdId == 0) {
	  			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
	  		} else {
	  			boolean resStd = stdService.isStudioExist(Integer.parseInt(rwdStdid));	  			
	  		}
	  		resultJson = rewardService.updateReward(rwdId,rwdName,rwdDesc,rwdImg,Integer.parseInt(rwdPoints),rwdExpDate,Integer.parseInt(rwdStdid),rwdStatus); 
	  		 		 
			return resultJson;	  		
	  	}
	  	*/
	  	/**
	  	 * This method used to save a reward details into database.It will return a reward id of just inserted or updated reward
	  	 * 
	  	 * @param rwdReq A JSONOject contains information of rewards.
	  	 * @return Returns JSONObject that has a value of reward id.
	  	 * @throws NumberFormatException Exception thrown when the application has attempted to convert a string to one of the numeric types, but that the string does not have the appropriate format.
	  	 * @throws ParseException Exception thrown when we try to parsing.
	  	 */
	  	@POST
	  	@Path("/saveReward")
	  	@Produces("application/json")
	  	public JSONObject saveReward(@Valid JSONObject rwdReq) throws NumberFormatException, ParseException
	  	{
	  		int rwdId         = Integer.parseInt(rwdReq.get("RWD_ID").toString());
	  		String rwdName    = rwdReq.get("RWD_NAME").toString();
	  		String rwdDesc    = rwdReq.get("RWD_DESCRIPTION").toString();
	  		String rwdImg     = rwdReq.get("RWD_IMG_URL").toString();
	  		String rwdPoints  = rwdReq.get("RWD_POINTS").toString();	  		
	  		String rwdExpDate = rwdReq.get("RWD_EXPIRY_DATE").toString();          
	  		int rwdStdid      = Integer.parseInt(rwdReq.get("RWD_STD_ID").toString());
	  		String rwdStatus  = rwdReq.get("RWD_STATUS").toString().toUpperCase();
	  		
	  		JSONObject resultJson;

	  		if(rwdStdid != 0) {
				if(!stdService.isStudioExist(rwdStdid)) {
					throw new IllegalArgumentException("studio id "+rwdStdid+ " Not found" );
				}	  			
	  		}
	  		resultJson = rewardService.saveReward(rwdId,rwdName,rwdDesc,rwdImg,Integer.parseInt(rwdPoints),rwdExpDate,rwdStdid,rwdStatus); 
	  		 		 
			return resultJson;	  		
	  	}
	  	
	  	/**
	  	 * This method used to get all the rewards from database and helps to search the rewards based on reward name, studio id, welcome / exist notification type. 
	  	 * 
	  	 * @param rwdname A string holds a text that has to be searched.
	  	 * @param stdid A integer value holds studio id has to be searched.
	  	 * @param type 
	  	 * @return Returns JSONArray contains all the results after searching is done.
	  	 */
	  	
	  	@RolesAllowed("ADMIN")
		@GET
		@Path("/rewardSearch/rwdname/{rwdname}/stdid/{stdid}/type/{rwdtype}")
		@Produces("application/json")	
		public JSONArray rewardSearch(@PathParam("rwdname") String rwdname,@PathParam("stdid") int stdid,@PathParam("rwdtype") String type) {
			
			if(rwdname == "" || rwdname == null) {
				throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
			} else if(type == "" || type == null) {
				throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
			}
			return rewardService.rewardSearch(rwdname, stdid, type);
		}	  	
}