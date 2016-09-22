package com.turnout.ws.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.swing.plaf.BorderUIResource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.text.WordUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.exception.WrongAccessTokenException;
import com.turnout.ws.exception.WrongVerifyCodeException;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.PushNotification;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;

/**
 * PartyAuthMechServiceImpl is class that contains collection of methods that can be accessed for manipulating party authentication mechanism. All the methods declared in service interface is implemented here.
 * 
 * It has its own listing,add,edit and get details method.
 * 
 * All of this methods actions are determined.So while executing, it has some restrictions on parameters.
 * 
 * Attempting to add or edit an ineligible object throws an MessagingException, typically PartyExistException or PartyNotFoundException or WrongVerifyCodeException.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

@Service
@Validated
public class PartyAuthMechServiceImpl implements PartyAuthMechService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyAuthMechServiceImpl.class);
	
	private final PartyRepository partyRepository;	
	private final PartyAuthMechRepository partyAuthMechRepository;	
	private final SmtpMailSender smtpMailSender;	
	private final PartyNotificationRepository partyNotificationRepository;
	private final InfoRepository infoRepository;
	
	/**
	 * An injectable constructor with a dependencies as argument.
	 * 
	 * @param partyRepository An Object of partyRepository as an injectable member.
	 * @param partyAuthMechRepository An Object of partyAuthMechRepository as an injectable member.
	 * @param smtpMailSender An Object of smtpMailSender as an injectable member.
	 * @param partyNotificationRepository An Object of partyNotificationRepository as an injectable member.
	 * @param infoRepository An Object of infoRepository as an injectable member.
	 * @see partyRepository
	 * @see partyAuthMechRepository
	 * @see smtpMailSender 
	 * @see partyNotificationRepository
	 * @see infoRepository
	 */
	@Autowired
	public PartyAuthMechServiceImpl(final PartyRepository partyRepository,final PartyAuthMechRepository partyAuthMechRepository,
			final SmtpMailSender smtpMailSender,final PartyNotificationRepository partyNotificationRepository,final InfoRepository infoRepository) {
		this.partyRepository = partyRepository;
		this.partyAuthMechRepository = partyAuthMechRepository;	
		this.smtpMailSender = smtpMailSender;
		this.partyNotificationRepository = partyNotificationRepository;
		this.infoRepository = infoRepository;
	}
	
	/**
	 * It is used to resend the verification code to the user when the party doesn't received.
	 * 
	 * @param partyName party first name
	 * @param partyLastName party last name
	 * @param authId Party type (Normal / instagram) user.
	 * @param partyAuthid party email address
	 * @param partyAuthtoken party password.
	 * @return return string message success / failure.
	 */
	@Transactional
	public String resendCode(String partyName,String partyLastName,String authId, String partyAuthid,String partyAuthtoken) {
		String verifyCode=null;
		String bodyMessage;
		String subject;		
		int partyId=0;
		if(authId.equalsIgnoreCase("SL"))
		{
			PartyAuthMech resendpartyAuth =  partyAuthMechRepository.findByPamAuthId(partyAuthid);	
			resendpartyAuth.setPamVerifycodeExpiry(calculateExpiryDate());
			verifyCode = verifyCodeGenerator();
			resendpartyAuth.setPamVerifyCode(verifyCode);
			subject = "Fiternity - Verification code";
			String mailerName = partyName+" "+partyLastName;
			bodyMessage = mailTemplate(mailerName,verifyCode);
			
		    if(!sendMailVerificationCode(partyAuthid,subject,bodyMessage))
		    {
		    	return "CODENOTSENT";
		    }
		    partyAuthMechRepository.saveAndFlush(resendpartyAuth);
		    partyId=resendpartyAuth.getPtyId();		
}		

	    if( partyId == 0)
		{
			return "FAILED";
		}		
		else
		{
			return String.valueOf(partyId);
		}
	}
	/**
	 * This signup method used to register new users from app signup. It will return just insered id.
	 * 
	 * @param partyName  party first name.
	 * @param partyLastName party last name.
	 * @param authId it has a constant text "SL".The SL represent app user.
	 * @param partyAuthid party login email address.
	 * @param partyAuthtoken party login password.
	 * @param ptyDeviceToken it is automatically detect from party using mobile device.
	 * @param ptyDeviceType it is type party using mobile os (ios / Andriod).
	 * @return return last inserted party id.
	 * @throws MessagingException Exception thrown when the message content empty.
	 * @throws PartyExistException Exception thrown when entered party is already exist in database.
	 */
	@Transactional
	public String signUpUser(String partyName,String partyLastName,String authId, String partyAuthid, String partyAuthtoken, String ptyDeviceToken, String ptyDeviceType) throws MessagingException, PartyExistException {
		
		PartyAuthMech existParty =  partyAuthMechRepository.findByPamAuthId(partyAuthid);
	
		if(existParty != null) {
			Info msgObj = infoRepository.findByType("email-adrs-exist");
			throw new PartyExistException(msgObj.getTitle()+"~"+msgObj.getDescription()+"~Signup");
			//throw new PartyExistException(propsControl.getEmailaddressexistsTitle()+"~"+propsControl.getEmailaddressexistsText()+"~Signup");
		}
		
		String verifyCode = null;
		String bodyMessage;
		String subject;
		LOGGER.debug("Reach SERVICEiMPL");	
		System.out.println("reach serviceimpl");
		int partyId = 0;
		Party party = new Party();
		party.setPtyName(partyName);
		party.setPtyEmail(partyAuthid);
		party.setPtyGainedPoints(0);
		party.setPtyDeviceToken(ptyDeviceToken);
		party.setPtyDeviceType(ptyDeviceType);
		party.setPtyLastName(partyLastName);
		party.setPtyDescription("");
		party.setPtyActivityPreference("PUBLIC");
		party.setPtyShowupPreference("PUBLIC");
		party.setPtyStatus("ACTIVE");
		
		partyRepository.saveAndFlush(party);
		
		System.out.println("Party Saved");
		
		PartyNotification pn = new PartyNotification();
		pn.setNotifyPtyId(party.getPtyId());
		pn.setNotifyOthAcptChlng("Y");
		pn.setNotifyOthCmts("Y");
		pn.setNotifyOthIntrstEvnt("Y");
		pn.setNotifyOthLikes("Y");
		pn.setNotifyPersonalEvntRemainder("Y");
		pn.setNotifyPersonalPointsCrdt("Y");
		pn.setNotifyPersonalReadyReedem("Y");		
		partyNotificationRepository.saveAndFlush(pn);
		
		System.out.println("Notifivation saved");
		
		PartyAuthMech partyAuth= new PartyAuthMech();
		partyAuth.setPtyId(party.getPtyId());
		partyAuth.setAmhId(authId);		
		partyAuth.setPamAuthId(partyAuthid);
		partyAuth.setPamAuthToken(partyAuthtoken);
		
		
		
		if(authId.equalsIgnoreCase("SL")) {
			partyAuth.setPamVerified("N");		
			partyAuth.setPamVerifycodeExpiry(calculateExpiryDate());
			verifyCode = verifyCodeGenerator();
			partyAuth.setPamVerifyCode(verifyCode);
			subject = "Fiternity - Verification code";
			String mailerName = partyName+" "+partyLastName;
			bodyMessage = mailTemplate(mailerName, verifyCode);			
		    if(!sendMailVerificationCode(partyAuthid,subject,bodyMessage)) {
		    	return "CODENOTSENT";
		    }
		}
		partyAuthMechRepository.saveAndFlush(partyAuth);
		System.out.println("authmech  saved");
		
		partyId = partyAuth.getPtyId();
		
		if( partyId == 0) {
			return "FAILED";
		} else {
			return String.valueOf(partyId);
		}
	
	}
	/**
	 * This method used to verify the user by checking the verification code.
	 * 
	 * @param partyAuthid party email address.
	 * @param mailVerifyCode party received verification code.
	 * @return returns JSONObject that has a value of success message.
	 * @throws WrongVerifyCodeException Exception thrown when temporary verification code does not match with user's entered code.
	 */
	@Transactional
	public String verifyEmail(String partyAuthid, String mailVerifyCode) throws WrongVerifyCodeException, PartyNotFoundException {
		int ptyid = 0;	
		PartyAuthMech partyAuthMech = partyAuthMechRepository.findByPamAuthId(partyAuthid);
		if(partyAuthMech != null)
		{
			if(partyAuthMech.getPamVerifyCode().toString().equalsIgnoreCase(mailVerifyCode))
			{
				ptyid = partyAuthMech.getPtyId();
				partyAuthMech.setPamVerified("Y");
				
				partyAuthMechRepository.saveAndFlush(partyAuthMech);
				return TurnOutConstant.SUCCESS;
			}
			else
			{
				Info msgObj = infoRepository.findByType("code-mismatch");
				throw new WrongVerifyCodeException(msgObj.getTitle()+"~"+msgObj.getDescription()+"~Verifyemail");				
			}
		}
		else
		{
			Info msgObj = infoRepository.findByType("party-not-found");  
			throw new PartyNotFoundException(msgObj.getTitle()+"~"+msgObj.getDescription()+"~Verifyemail");			
		}
			
			
		}	
	/**
	 * This method used to generate the verification code
	 * 
	 * @return string value contains the generated code.
	 */
	private String verifyCodeGenerator()
	{
		int verifyCode = smtpMailSender.gen(10000);
		String strVerifyCode= Integer.toString(verifyCode);			
		return strVerifyCode;
		
	}
	/**
	 * This method used to generate the password when the party requesting forgot password.
	 * 
	 * @return string value contains the generated code.
	 */
	private String passwordGenerator()
	{
		int pwd = smtpMailSender.gen(10000000);
		String strPwd= Integer.toString(pwd);			
		return strPwd;
	}
	/**
	 * It is used to send email to the user.
	 * 
	 * @param emailid Party email address. 
	 * @param subject Subject of the email.
	 * @param body email body content.
	 * @return return true or false.
	 */
	private boolean sendMailVerificationCode(String emailid,String subject,String body)
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
	 * It is used for verification code expiry date.
	 *  
	 * @return return expiry date.
	 */
	private Date calculateExpiryDate()
	{

		Date expDate=new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(expDate); 
		c.add(Calendar.DATE, 2);
		expDate = c.getTime();
		return expDate;
	}
	
	/**
	 * This method allow user to sign in the app.
	 * 
	 * @param authId type of party (app / instagram user).
	 * @param partyAuthid party login email address.
	 * @param partyAuthtoken party login password.
	 * @param deviceToken it is automatically detect from party using mobile device.
	 * @param deviceType it is type party using mobile os (ios / Andriod).
	 * @return returns JSONObject that has a value of party id with user session values.
	 */
	public String signIn(String authId, String partyAuthid, String partyAuthtoken, String deviceToken, String deviceType) {
		String result;		
		PartyAuthMech signInParty =  partyAuthMechRepository.findByPamAuthId(partyAuthid);
		if(signInParty != null) {			
			if(signInParty.getPamAuthToken().equals(partyAuthtoken)) {				
				Party pty = partyRepository.findOne(signInParty.getPtyId());

				if(pty.getPtyStatus().toString().equals("ACTIVE")) {					
					if(!pty.getPtyDeviceToken().equals(deviceToken)) {
						pty.setPtyDeviceToken(deviceToken);
						pty.setPtyDeviceType(deviceType);
						partyRepository.saveAndFlush(pty);
					}	
					result = String.valueOf(signInParty.getPtyId());				
				}else {result = TurnOutConstant.ACCOUNT_INACTIVE;	}
			}
			else {result = TurnOutConstant.WRONG_PWD;	}			
			
		}
		else {result = TurnOutConstant.NOT_EXIST;	}

		return result;
	}
	/**
	 * Verification code email body content. 
	 * 
	 * @param mailerName party name
	 * @param verifyCode Auto generated verification code.
	 * @return email template.
	 */
	public String mailTemplate(String mailerName, String verifyCode)
	{		
		String bodyMessage = "<table width=\"100%\" style=\"background-color:#eee; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif\">";
		bodyMessage +="<tr>  <td> ";
		bodyMessage += "<table width=\"640\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"background-color:#eee; padding:0px 0 0px;\">";
		bodyMessage += " <tr style=\"background:#fff\">";
		bodyMessage += "<td align=\"center\"><a href=\"#\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/logo-emailer.png\" alt=\"Fiternity\"></a></td>";
		bodyMessage +="</tr>    <tr style=\"background:#fff\">";
		bodyMessage +="	<td align=\"center\" style=\"color:#404041; padding:35px 12% 25px;\">";
		bodyMessage +=" <h1 style=\" font-size:24px; font-weight:700; padding:0 0 20px; margin:0;\">Hi "+mailerName+"!</h1>";
		bodyMessage +=" <p style=\"font-size:18px; margin:0; line-height:36px;\">You are one step closer to being a part of Fiternity - London's hottest line of fitness and wellness locations.</p>";
		bodyMessage +="</td>	</tr> <tr style=\"background-color:#4a4354\">";
		bodyMessage +="<td align=\"center\" style=\"padding:35px 10%;\">";
		bodyMessage +="<p style=\" color:#fff; font-size:24px; font-weight:700; padding:0 0 5px; margin:0;\">Your verification code is</p>";
		bodyMessage +="<p style=\" color:#f48366; font-size:36px; font-weight:700; margin:0; line-height:48px;\">"+verifyCode+"</p>";
		bodyMessage +="</td>  </tr> <tr style=\"background:#fff\">";
	    bodyMessage +="<td align=\"center\" style=\"color:#404041; padding:25px 19% 55px;\">";
		bodyMessage +="<p style=\"font-size:18px; margin:0; line-height:36px; padding-bottom:25px;\">Use the above code to verify your account. Please note this code is valid only for 48 hours</p>";
		bodyMessage +="<a href=\"#\" style=\"color:#f48366; font-size:18px; font-weight:700; margin:0; text-decoration:none\"> Happy Fiternity!</a>";
		bodyMessage +="</td> </tr> 	<tr style=\"background:#eee\">";
		bodyMessage +="<td align=\"center\" style=\"color:#404041; padding:25px 11%;\">";
		bodyMessage +="<p style=\" color:#333; display: inline-block; font-size:14px; margin:0; line-height:50px; vertical-align:top;\">Want to join the Fiternity? <strong>Follow us!</strong> </p>"; 
		bodyMessage +="<a href=\"https://www.instagram.com/fiternity_co/\" style=\"margin-left:20px;\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/instagram.jpg\" alt=\"Instagram\"></a><a href=\"https://twitter.com/Fiternity_Co\" style=\"margin-left:20px;\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/tweet.jpg\" alt=\"Twitter\"></a>";
		bodyMessage +="</td> </tr>";		
		bodyMessage +=" </table> </td> </tr> </table>";
		return bodyMessage;
		
	}
	/**
	 * forgot password email body content. 
	 * 
	 * @param mailerName party name
	 * @param pwdGen Auto generated password.
	 * @return email template.
	 */	
	
	public String tempPasswordMailTemplate(String mailerName, String pwdGen)
	{		
		String bodyMessage = "<table width=\"100%\" style=\"background-color:#eee; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif\">";
		bodyMessage +="<tr>  <td> ";
		bodyMessage += "<table width=\"640\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"background-color:#eee; padding:0px 0 0px;\">";
		bodyMessage += " <tr style=\"background:#fff\">";
		bodyMessage += "<td align=\"center\"><a href=\"#\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/logo-emailer.png\" alt=\"Fiternity\"></a></td>";
		bodyMessage +="</tr>    <tr style=\"background:#fff\">";
		bodyMessage +="	<td align=\"center\" style=\"color:#404041; padding:35px 12% 25px;\">";
		bodyMessage +=" <h1 style=\" font-size:24px; font-weight:700; padding:0 0 20px; margin:0;\">Hi "+mailerName+"!</h1>";
		bodyMessage +=" <p style=\"font-size:18px; margin:0; line-height:36px;\"></p>";
		bodyMessage +="</td>	</tr> <tr style=\"background-color:#4a4354\">";
		bodyMessage +="<td align=\"center\" style=\"padding:35px 10%;\">";
		bodyMessage +="<p style=\" color:#fff; font-size:24px; font-weight:700; padding:0 0 5px; margin:0;\">Your temporary password is</p>";
		bodyMessage +="<p style=\" color:#f48366; font-size:36px; font-weight:700; margin:0; line-height:48px;\">"+pwdGen+"</p>";
		bodyMessage +="</td>  </tr> <tr style=\"background:#fff\">";
	    bodyMessage +="<td align=\"center\" style=\"color:#404041; padding:25px 19% 55px;\">";
		bodyMessage +="<p style=\"font-size:18px; margin:0; line-height:36px; padding-bottom:25px;\">Please use this to reset your password.</p>";
		bodyMessage +="<a href=\"#\" style=\"color:#f48366; font-size:18px; font-weight:700; margin:0; text-decoration:none\"> Happy Fiternity!</a>";
		bodyMessage +="</td> </tr> 	<tr style=\"background:#eee\">";
		bodyMessage +="<td align=\"center\" style=\"color:#404041; padding:25px 11%;\">";
		bodyMessage +="<p style=\" color:#333; display: inline-block; font-size:14px; margin:0; line-height:50px; vertical-align:top;\">Want to join the Fiternity? <strong>Follow us!</strong> </p>";
		bodyMessage +="<a href=\"https://www.instagram.com/fiternity_co/\" style=\"margin-left:20px;\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/instagram.jpg\" alt=\"Instagram\"></a><a href=\"https://twitter.com/Fiternity_Co\" style=\"margin-left:20px;\"><img src=\"http://www.madebyfire.co.uk/demo/ShowUp/img/tweet.jpg\" alt=\"Twitter\"></a>";
		bodyMessage +="</td> </tr> ";		
		bodyMessage +="</table> </td> </tr> </table>";
		return bodyMessage;
	}
	/**
	 * If the party forgot the login password they can able to get password using this method..
	 * 
	 * @param authId type of the user.
	 * @param partyAuthid party registered email address.
	 * @return it will return success or failure message.
	 */
	public String forgetPwd(String authId,String partyAuthid) {
		String result;
		String pwdGen;
		String subject;
		String bodyMessage;
		PartyAuthMech fgetParty =  partyAuthMechRepository.findByPamAuthId(partyAuthid);
		if(fgetParty != null)
		{
			Party pty = partyRepository.findOne(fgetParty.getPtyId());
			if(pty.getPtyStatus().toString().equals("ACTIVE")) {
				String partyName = pty.getPtyName();
				
				pwdGen = passwordGenerator();
				fgetParty.setPamAuthToken(pwdGen);
				
				partyAuthMechRepository.saveAndFlush(fgetParty);
				subject = "Fiternity - Temporary Password";
				String mailerName = partyName.substring(0,1).toUpperCase()+partyName.substring(1).toLowerCase();
				
				bodyMessage = tempPasswordMailTemplate(mailerName, pwdGen);
			    if(!sendMailVerificationCode(partyAuthid,subject,bodyMessage)) {
			    	return "CODENOTSENT";
			    }		
			    return TurnOutConstant.SUCCESS;
				
			} else {
				return TurnOutConstant.ACCOUNT_INACTIVE;
			}
		}
		else
		{
			return TurnOutConstant.NOT_EXIST;
		}
		
	}
	/**
	 * It is used to update new passsword.
	 * 
	 * @param authId  type of the user.
	 * @param pamAuthId party registered email address.
	 * @param pamAuthToken temporary password. Party received email with temporary password.
	 * @param newPamAuthToken party new password.
	 * @return return success or failure message.
	 */
	public String resetPwd(String authId, String pamAuthId, String pamAuthToken, String newPamAuthToken) {
		String result=null;
		PartyAuthMech repwdParty = partyAuthMechRepository.findByPamAuthId(pamAuthId);
		if(repwdParty != null)
		{
			if(repwdParty.getPamAuthToken().equals(pamAuthToken))
			{
				repwdParty.setPamAuthToken(newPamAuthToken);
				partyAuthMechRepository.saveAndFlush(repwdParty);
				result =String.valueOf(repwdParty.getPtyId());
			}	
			else
			{
				result = TurnOutConstant.WRONG_PWD;
			}
		}
		else
		{
			result = TurnOutConstant.FAILED;
		}		
		
		return result;
	}

	/**
	 * If the user have instagram access they can able to access the app using own instagram account.
	 * 
	 * @param authId it has a constant value "IN".
	 * @param authCode instagram auth code.
	 * @param deviceToken party using device token.
	 * @param deviceType Party using OS type (andriod / ios).
	 * @return returns JSONObject that has a value of party id.
	 * @throws UnirestException Exception thrown when the http connection fails.
	 */
	public String signupWithInstagram(String authId, String authCode,String deviceToken, String deviceType) throws UnirestException, WrongAccessTokenException {

		String result;
		LOGGER.debug(authCode);
		String accessToken;
		String partyName;
		String profPic;
		String pamAuthId;
		String errormsg;
		
		if(!authId.equals("null") && !authCode.equals("null")) {
		HttpResponse<JsonNode> response = Unirest.post("https://api.instagram.com/oauth/access_token")
				  .header("cache-control", "no-cache")
				  .header("postman-token", "f498c952-3086-b4f7-597b-46a304a8039f")
				  .header("content-type", "application/json")
				  .body("client_id=e1d88356e0354feeb2d071de12feb856&client_secret=3c305b2e42e94b1f9c4758a7682055df&grant_type=authorization_code&redirect_uri=http://fiternity.co/&code="+authCode)
				  .asJson();	
		
		LOGGER.debug(response.getStatusText());
		
		LOGGER.debug(response.getBody().toString());
		
			if(response.getStatus() == 200) {
				org.json.JSONObject responseBody = response.getBody().getObject();
				accessToken= responseBody.getString("access_token");
				org.json.JSONObject userdet =  responseBody.getJSONObject("user");
				partyName = WordUtils.capitalizeFully(userdet.getString("full_name").toString());
				profPic = userdet.getString("profile_picture");
				pamAuthId = userdet.getString("id");
				
				LOGGER.debug("auth id "+pamAuthId);
				
				int partyId = 0;	
				
				PartyAuthMech existParty =  partyAuthMechRepository.findByPamAuthId(pamAuthId);
								
				if(existParty != null) {
					Party pty = partyRepository.findOne(existParty.getPtyId());
					boolean update = false;
					if(!existParty.getPamAuthToken().equals(accessToken)) {
						existParty.setPamAuthToken(accessToken);
						update = true;
						//throw new WrongAccessTokenException("WRONG-ACCESS TOKEN");					
					}
					if(!pty.getPtyDeviceToken().equals(deviceToken)) {
						pty.setPtyDeviceToken(deviceToken);
						pty.setPtyDeviceType(deviceType);
						update = true;
					}
					if(update == true) {
						partyRepository.saveAndFlush(pty);
						partyAuthMechRepository.saveAndFlush(existParty);
						
					}
						
					result = String.valueOf(existParty.getPtyId());
					return result;
				}
				Party party = new Party();
				party.setPtyName(partyName);
				party.setPtyLastName(" ");
				party.setPtyDeviceToken(deviceToken);
				party.setPtyDeviceType(deviceType);
				party.setPtyGainedPoints(0);				
				
				party.setPtyDescription("");
				party.setPtyActivityPreference("PUBLIC");
				party.setPtyShowupPreference("PUBLIC");
				party.setPtyStatus("ACTIVE");
				partyRepository.saveAndFlush(party);	
				
				PartyNotification pn = new PartyNotification();
				pn.setNotifyPtyId(party.getPtyId());				
				pn.setNotifyOthAcptChlng("Y");
				pn.setNotifyOthCmts("Y");
				pn.setNotifyOthIntrstEvnt("Y");
				pn.setNotifyOthLikes("Y");
				pn.setNotifyPersonalEvntRemainder("Y");
				pn.setNotifyPersonalPointsCrdt("Y");
				pn.setNotifyPersonalReadyReedem("Y");		
				partyNotificationRepository.saveAndFlush(pn);
				
				PartyAuthMech partyAuth= new PartyAuthMech();
				partyAuth.setPtyId(party.getPtyId());
				partyAuth.setAmhId(authId);		
				partyAuth.setPamAuthId(pamAuthId);
				partyAuth.setPamAuthToken(accessToken);
				
				partyAuth.setPamVerified("I");
				
				partyAuthMechRepository.saveAndFlush(partyAuth);
				
				partyId = partyAuth.getPtyId();
				
				if( partyId == 0) {
					return "FAILED";
				} else {
					return String.valueOf(partyId);
				}
			} else {
				org.json.JSONObject responseBody = response.getBody().getObject();
				errormsg = responseBody.getString("error_message");
				return errormsg;
			}
	   } else {
		   return "NO DATA";
	   }
	}
}
