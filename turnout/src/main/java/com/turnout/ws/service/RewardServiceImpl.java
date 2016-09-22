package com.turnout.ws.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.Voucher;
import com.turnout.ws.domain.VoucherParty;
import com.turnout.ws.exception.NoSufficientGainedPointsException;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.CustomRewardRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.RewardRepository;
import com.turnout.ws.repository.StudioRepository;
import com.turnout.ws.repository.VoucherPartyRepository;
import com.turnout.ws.repository.VoucherRepository;

/**
 * RewardServiceImpl is class that contains collection of methods that can be accessed for manipulating rewards. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 * 
 * Attempting to add or edit an ineligible object throws an NoSufficientGainedPointsException, typically RewardNotFoundException.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
@Service
public class RewardServiceImpl implements RewardService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RewardServiceImpl.class);
	
	private final VoucherRepository voucherRepository;
	
	private final VoucherPartyRepository voucherPartyRepository;
	
	private final CustomRewardRepository customRewardRepository;
	
	private final PartyRepository partyRepository;
	
	private final RewardRepository rewardRepository;
	
	private final SmtpMailSender smtpMailSender;
	
	private final StudioRepository studioRepository;
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param voucherRepository An Object of voucherRepository as an injectable member.
	 * @param voucherPartyRepository An Object of voucherPartyRepository as an injectable member.
	 * @param customRewardRepository An Object of customRewardRepository as an injectable member.
	 * @param partyRepository An Object of partyRepository as an injectable member.
	 * @param rewardRepository An Object of rewardRepository as an injectable member.
	 * @param smtpMailSender An Object of smtpMailSender as an injectable member.
	 * @param studioRepository An Object of studioRepository as an injectable member.
	 * @see voucherRepository
	 * @see voucherPartyRepository
	 * @see customRewardRepository
	 * @see partyRepository
	 * @see rewardRepository
	 * @see smtpMailSender
	 * @see studioRepository
	 */
	@Autowired
	public RewardServiceImpl(final VoucherRepository voucherRepository,
			final VoucherPartyRepository voucherPartyRepository,
			final CustomRewardRepository customRewardRepository,
			final PartyRepository partyRepository ,
			final RewardRepository rewardRepository ,
			final SmtpMailSender smtpMailSender,
			final StudioRepository studioRepository) {
		this.voucherRepository = voucherRepository;
		this.voucherPartyRepository = voucherPartyRepository;
		this.customRewardRepository = customRewardRepository;
		this.partyRepository = partyRepository;
		this.rewardRepository =rewardRepository;
		this.smtpMailSender = smtpMailSender;
		this.studioRepository = studioRepository;
		
	}
	
	/**
	 * This method used to validate the voucher code.
	 * 
	 * @param ptyid an unique id of party id.
	 * @param rwdid an unique id of reward id.
	 * @return returns JSONObject that has a value of success message.
	 * @throws NoSufficientGainedPointsException Exception thrown when there is no sufficient gained point while redeeming reward.
	 */

	@Transactional
	public synchronized JSONObject reedemReward(int ptyid, int rwdid) throws NoSufficientGainedPointsException {
		String subject="";
		String bodyMessage ="";
		String emailId = "";
		String voucherCode ="";
		String partyName ="";
		String rewardName="";
		int vchId =0;
		int rewardPoints =0;
		int gainedPoints = 0;
		String result="";
		JSONObject resultJson = new JSONObject();
		
		Party pty = partyRepository.findOne(ptyid);
		emailId=pty.getPtyEmail();
		partyName = pty.getPtyName();
		gainedPoints = pty.getPtyGainedPoints();
		
		Reward rwd = rewardRepository.findOne(rwdid);
		rewardPoints= rwd.getRwdPoints();
		rewardName = rwd.getRwdName();
		LOGGER.debug("rewardpointcollected");
		if(gainedPoints < rewardPoints)
		{
			throw new NoSufficientGainedPointsException("No Sufficient Gained Points");
		}
		
		vchId = customRewardRepository.getVoucherId(rwdid);
		
		LOGGER.debug("voucheridcollected");
		
		Voucher voucher = voucherRepository.findByVocId(vchId);
		voucherCode=voucher.getVocCode();
		voucher.setVocStatus("CLAIMED");
		voucherRepository.saveAndFlush(voucher);
		
		gainedPoints-=rewardPoints;
		pty.setPtyGainedPoints(gainedPoints);
		partyRepository.saveAndFlush(pty);
		
		pty.setPtyGainedPoints(gainedPoints);
		
		LOGGER.debug("vouchertablestatuschange");
		
		VoucherParty voucherParty = new VoucherParty();
		voucherParty.setPtyId(ptyid);		
		voucherParty.setVocId(vchId);
		voucherParty.setVocReedemedPoints(rewardPoints);
		voucherParty.setVocStatus("ACTIVE");
		
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		dt = c.getTime();
		voucherParty.setVocStatusDate(dt);
		voucherPartyRepository.saveAndFlush(voucherParty);
		
		
		subject = "Fiternity - Voucher Code for your Reward";
		String mailerName = partyName.substring(0,1).toUpperCase()+partyName.substring(1).toLowerCase();
		
		bodyMessage = voucherMailTemplate(mailerName,voucherCode,rewardName,rewardPoints);
	    if(!sendMailVoucherCode(emailId,subject,bodyMessage))
	    {
	    	result = "CODENOTSENT";
	    }
	    else
	    {
	    	result = "SUCCESS";
	    }
	    LOGGER.debug("emailsend");
	    resultJson.put("RESULT", result);
	    
		return resultJson;
	}
	
	/**
	 * This email template used when we send voucher code to the user.
	 * 
	 * @param mailerName email receiver name.
	 * @param voucherCode Reward voucher code.
	 * @param rewardName Reward name.
	 * @param rewardPoints Reward points.
	 * @return Returns mail content
	 */
	
	public String voucherMailTemplate(String mailerName, String voucherCode,String rewardName,int rewardPoints)
	{		
		String bodyMessage = "<table width=\"100%\" style=\"background-color:#eee; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif\">";
		bodyMessage +="<tr>  <td> ";
		bodyMessage += "<table width=\"640\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"background-color:#eee; padding:0px;\">";
		bodyMessage += " <tr style=\"background:#fff\">";
		bodyMessage += "<td align=\"center\"><a href=\"#\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/logo-emailer.png\" alt=\"Fiternity\"></a></td>";
		bodyMessage +="</tr>    <tr style=\"background:#fff\">";
		bodyMessage +="	<td align=\"center\" style=\"color:#404041; padding:35px 12% 25px;\">";
		bodyMessage +=" <h1 style=\" font-size:24px; font-weight:700; padding:0 0 20px; margin:0;\">Hi "+mailerName+"!</h1>";
		bodyMessage +=" <p style=\"font-size:18px; margin:0; line-height:36px;\">Congratulations! Your request to redeem <b>"+rewardName+"</b> for "+rewardPoints+" Fiternity points is successful!</p>";
		bodyMessage +="</td>	</tr> <tr style=\"background-color:#4a4354\">";
		bodyMessage +="<td align=\"center\" style=\"padding:35px 10%;\">";
		bodyMessage +="<p style=\" color:#fff; font-size:24px; font-weight:700; padding:0 0 5px; margin:0;\">Your redemption code is</p>";
		bodyMessage +="<p style=\" color:#f48366; font-size:36px; font-weight:700; margin:0; line-height:48px;\">"+voucherCode+"</p>";
		bodyMessage +="</td>  </tr> <tr style=\"background:#fff\">";
	    bodyMessage +="<td align=\"center\" style=\"color:#404041; padding:25px 19% 55px;\">";
		bodyMessage +="<p style=\"font-size:18px; margin:0; line-height:36px; padding-bottom:25px;\">Use the above code at the gym.</p>";
		bodyMessage +="<a href=\"#\" style=\"color:#f48366; font-size:18px; font-weight:700; margin:0; text-decoration:none\"> Happy Fiternity!</a>";
		bodyMessage +="</td> </tr> 	<tr style=\"background:#eee\">";
		bodyMessage +="<td align=\"center\" style=\"color:#404041; padding:25px 11%\">";
		bodyMessage +="<p style=\" color:#333; display: inline-block; font-size:14px; margin:0; line-height:50px; vertical-align:top;\">Want to join the Fiternity? <strong>Follow us!</strong> </p>";
		bodyMessage +="<a href=\"https://www.instagram.com/fiternity_co/\" style=\"margin-left:20px;\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/instagram.jpg\" alt=\"Instagram\"></a><a href=\"https://twitter.com/Fiternity_Co\" style=\"margin-left:20px;\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/tweet.jpg\" alt=\"Twitter\"></a>";
		bodyMessage +="</td> </tr> ";		
		bodyMessage +="</table> </td> </tr> </table>";
		return bodyMessage;
	}
	/**
	 * This method used to send email to the user.
	 * 
	 * @param emailid receiver email address.
	 * @param subject Subject of the email.
	 * @param body Email body content.
	 * @return return boolean true or flase message.
	 */
	private boolean sendMailVoucherCode(String emailid,String subject,String body)
	{		
		try {
			smtpMailSender.send(emailid,subject,body);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
			
		}
	}
	
	/**
	 * This method used to get all reward listing from database.
	 * 
	 * @param pageno current page no.
	 * @param pagesize total number of records shown in current page.
	 * @return returns JSONArray contains all the reward listing.
	 */
	public JSONArray getRewardListing(int pageno, int pagesize) {
		List rewardlist;
		JSONArray listarr = new JSONArray();
		JSONObject listobj;
		Map<String, Object> record;
		List lookupDataList = customRewardRepository.getRewardListingByCustomQuery(pageno,pagesize);
		if( lookupDataList.size() != 0){
		for (Iterator itr = lookupDataList.iterator(); itr.hasNext();) {
			record = (Map) itr.next();
			listobj = new JSONObject();
			for (Map.Entry<String, Object> entry : record.entrySet()) {
				listobj.put(entry.getKey(), entry.getValue());				
			}
			listarr.add(listobj);		
	    }
		}
		else{
			listobj = new JSONObject();
			listobj.put("Records", TurnOutConstant.NOT_EXIST);
			listarr.add(listobj);
		}
		return listarr;
	}
	
	/**
	 * This method used to get reward detail from database based on reward id passed.
	 * 
	 * @param rewardid the primary key of reward element.
	 * @return returns JSONObject which contains whole details of reward element.
	 */
	public JSONObject getRewardDetail(int rewardid) {
		
		Map<String, Object> reward = customRewardRepository.getRewardDetailById(rewardid);		
		LOGGER.debug(reward.toString());
		
		JSONObject rewardjson = new JSONObject();		
		rewardjson.put("RWD_ID", reward.get("RWD_ID"));
		rewardjson.put("RWD_NAME", reward.get("RWD_NAME"));
		rewardjson.put("RWD_POINTS", reward.get("RWD_POINTS"));
		rewardjson.put("RWD_IMG_URL", reward.get("RWD_IMG_URL"));
		rewardjson.put("RWD_DESCRIPTION", reward.get("RWD_DESCRIPTION"));
		rewardjson.put("RWD_EXPIRY_DATE", reward.get("RWD_EXPIRY_DATE"));
		rewardjson.put("RWD_CREATED_DATE", reward.get("RWD_CREATED_DATE"));
		rewardjson.put("RWD_MODIFIED_DATE", reward.get("RWD_MODIFIED_DATE"));
		rewardjson.put("RWD_STD_ID", reward.get("RWD_STD_ID"));	
		rewardjson.put("RWD_STATUS",reward.get("RWD_STATUS"));	
		rewardjson.put("STD_NAME", reward.get("STD_NAME"));
		rewardjson.put("STD_COMPANY_LOGO", reward.get("STD_COMPANY_LOGO"));	
		rewardjson.put("ADDED_VOCCODE",  reward.get("ADDED_VOCCODE"));
		rewardjson.put("REEDEMED_VOCCODE", reward.get("REEDEMED_VOCCODE"));
		return rewardjson;
	}
	/**
	 * It is used to check whether reward already exist in database or not. It will return true or false message.
	 * 
	 * @param rewardid an unique id of reward element.
	 * @return return boolean true or false.
	 */
	public boolean isRewardExist(int rewardid) {
		Reward reward = rewardRepository.findOne(rewardid);
		if(!(reward == null)){
			return true;
		}
		return false;
	}

/*	@Override
	public JSONObject addReward(String rwdName, String rwdDesc, String rwdImg, int rwdPoints, String rwdExpDate,
			int rwdStdid,String rwdStatus) throws ParseException {
		
		int rwdId = 0;
		String result;
		JSONObject jsonResult = new JSONObject();
		Date crdt = new Date();
		Calendar c=Calendar.getInstance();
		crdt = c.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date expdt = formatter.parse(rwdExpDate);
		
		Studio std = studioRepository.findOne(rwdStdid);		
		
		Reward rwd = new Reward();
		rwd.setRwdName(rwdName);
		rwd.setRwdDescription(rwdDesc);
		rwd.setRwdImgUrl(rwdImg);
		rwd.setRwdCreatedDate(crdt);
		rwd.setRwdModifiedDate(crdt);
		rwd.setRwdPoints(rwdPoints);
		rwd.setRwdExpiryDate(expdt);
		rwd.setRwdStatus(rwdStatus);
		rwd.setStudio(std);
		rewardRepository.saveAndFlush(rwd);
		rwdId = rwd.getRwdId();
		
		if(rwdId != 0 ) {
			result = Integer.toString(rwdId);
		} else {
			result = TurnOutConstant.FAILED;
		}
		jsonResult.put("RESULT", result);
		
		return jsonResult;
	}
	
	@Override
	public JSONObject updateReward(int rwdId, String rwdName, String rwdDesc, String rwdImg, int rwdPoints, String rwdExpDate,
			int rwdStdid,String rwdStatus) throws ParseException {
		String result;
		JSONObject jsonResult = new JSONObject();
		Date crdt = new Date();
		Calendar c= Calendar.getInstance();
		crdt = c.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date expdt = formatter.parse(rwdExpDate);
		
		Studio std = studioRepository.findOne(rwdStdid);		
		
		Reward rwd = rewardRepository.findOne(rwdId);
		rwd.setRwdName(rwdName);
		rwd.setRwdDescription(rwdDesc);
		rwd.setRwdImgUrl(rwdImg);
		rwd.setRwdCreatedDate(crdt);
		rwd.setRwdModifiedDate(crdt);
		rwd.setRwdPoints(rwdPoints);
		rwd.setRwdExpiryDate(expdt);
		rwd.setRwdStatus(rwdStatus);
		rwd.setStudio(std);
		rewardRepository.saveAndFlush(rwd);
		if(rwdId != 0 ) {
			result = Integer.toString(rwd.getRwdId());
		} else {
			result = TurnOutConstant.FAILED;
		}
		jsonResult.put("RESULT", result);
		
		return jsonResult;
	}*/
	/**
	 * This method used to save a reward details into database.It will return a reward id of just inserted or updated reward
	 * 
	 * @param rwdId An primary key of reward elment.
	 * @param rwdName Reward name
	 * @param rwdDesc Reward description
	 * @param rwdImg Reward image
	 * @param rwdPoints Reward points
	 * @param rwdExpDate Reward expiry date
	 * @param rwdStdid Studio id
	 * @param rwdStatus Reward status
	 * @return Returns JSONObject that has a value of reward id.
	 * @throws ParseException Exception thrown when we try to parsing.
	 */
	@Override
	public JSONObject saveReward(int rwdId, String rwdName, String rwdDesc, String rwdImg, int rwdPoints, String rwdExpDate,
			int rwdStdid,String rwdStatus) throws ParseException {
		String result;
		JSONObject jsonResult = new JSONObject();
		Date crdt = new Date();
		Calendar c = Calendar.getInstance();
		crdt = c.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date expdt = formatter.parse(rwdExpDate);
		
		Studio std = studioRepository.findOne(rwdStdid);
		Reward rwd;
		if(rwdId != 0) {
			rwd = rewardRepository.findOne(rwdId);
		} else {
			rwd = new Reward();
		}
		
		rwd.setRwdName(rwdName);
		rwd.setRwdDescription(rwdDesc);
		rwd.setRwdImgUrl(rwdImg);
		rwd.setRwdCreatedDate(crdt);
		rwd.setRwdModifiedDate(crdt);
		rwd.setRwdPoints(rwdPoints);
		rwd.setRwdExpiryDate(expdt);
		rwd.setRwdStatus(rwdStatus);
		rwd.setStudio(std);
		rewardRepository.saveAndFlush(rwd);

		result = Integer.toString(rwd.getRwdId());
		jsonResult.put("RESULT", result);
		
		return jsonResult;
	}	
	
  	/**
  	 * This method used to get all the rewards from database and helps to search the rewards based on reward name, studio id, type of reward.
  	 * 
  	 * @param rwdname name of the reward.
  	 * @param stdid an unique id of the studio.
  	 * @param type type of reward can be eaither current or expired.
  	 * @return returns JSONArray contains all the results after searching is done.
  	 */
	public JSONArray rewardSearch(String rwdname, int stdid, String type) {

		JSONArray listarr = new JSONArray();
		JSONObject listobj;
		Map<String, Object> record;
		
		List rs = customRewardRepository.rewardSearch(rwdname, stdid, type);
		if( rs.size() != 0) {
			for (Iterator itr = rs.iterator(); itr.hasNext();) {
				record = (Map) itr.next();
				listobj = new JSONObject();
				for (Map.Entry<String, Object> entry : record.entrySet()) {
					listobj.put(entry.getKey(), entry.getValue());	
					if(entry.getKey().equals("RWD_STD_ID")) {
						int stdId = Integer.parseInt(entry.getValue().toString());
						Studio std = studioRepository.findOne(stdId);
						listobj.put("STD_NAME", std.getStdName());
					}
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

}
