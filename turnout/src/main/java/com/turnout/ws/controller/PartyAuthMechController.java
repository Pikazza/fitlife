
package com.turnout.ws.controller;


import javax.annotation.security.PermitAll;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.exception.PartyDeviceNotFoundException;
import com.turnout.ws.exception.PartyEmailValidateException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNameValidateException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.exception.WrongAccessTokenException;
import com.turnout.ws.exception.WrongVerifyCodeException;
import com.turnout.ws.helper.EncryptDecrypt;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.service.InfoService;
import com.turnout.ws.service.PartyAuthMechService;


/**
*
* PartyAuthMechController class is front face for accessing and manipulating parties authentication related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* This class allows the users to create new account, signin, change their passwords and ensuring their email trust worthy.
*/
@RestController
@Path("/")
public class PartyAuthMechController {	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyAuthMechController.class);

	private final PartyAuthMechService partyAuthMechService;	
	private final SmtpMailSender smtpMailHelper;
	private final EncryptDecrypt encryptDecrypt;
	private final InfoService infoService;
	
	/**
	 * An injectable constructor with dependencies as argument.
	 * 
	 * @param partyAuthMechService An Object of partyAuthMechService as an injectable member.
	 * @param smtpMailHelper An Object of smtpMailHelper as an injectable member.
	 * @param encryptDecrypt An Object of encryptDecrypt as an injectable member.
	 * @param infoService An Object of infoService as an injectable member.
	 * @see partyAuthMechService
	 * @see smtpMailHelper
	 * @see encryptDecrypt
	 * @see infoService
	 * 
	 */ 
	@Autowired
	 public PartyAuthMechController(final PartyAuthMechService partyAuthMechService,
			 final SmtpMailSender smtpMailHelper,final EncryptDecrypt encryptDecrypt,
			 final InfoService infoService) {
		 this.partyAuthMechService = partyAuthMechService;
		 this.smtpMailHelper = smtpMailHelper;
		 this.encryptDecrypt = encryptDecrypt;
		 this.infoService = infoService;
	}
	/**
	 * This method used to sign up users. The users information will be store into database and send verification code to the respective user.
	 * 
	 * @param signUpReq JSONObject contain information about the users.
	 * @return Returns JSONObject that has a value of party id.
	 * @throws MessagingException Exception thrown when the message content empty.
	 * @throws PartyExistException Exception thrown when entered party is already exist in database.
	 * @throws PartyEmailValidateException Exception thrown when entered email id is not an usual format or when it may not be an valid email id.
	 * @throws PartyDeviceNotFoundException Exception thrown when the device type and token is empty.
	 * @throws PartyNameValidateException Exception thrown when entered party name is not valid or it may contain alpha numeric value.
	 *
	 */
	
	@PermitAll
	@Path("/signUp")
	@POST
	@Produces("application/json")
	public JSONObject signUpUser(@Valid JSONObject  signUpReq) throws MessagingException, PartyExistException, PartyEmailValidateException, PartyDeviceNotFoundException, PartyNameValidateException {
		String result;	
		JSONObject jsonResult = new JSONObject();
		String partyName = signUpReq.get("PTY_NAME").toString();
		String partyLastName = signUpReq.get("PTY_LAST_NAME").toString();
		String authId = signUpReq.get("AMH_ID").toString();
		String partyAuthid = signUpReq.get("PAM_AUTH_ID").toString();
		String partyAuthtoken = encryptDecrypt.encrypt(signUpReq.get("PAM_AUTH_TOKEN").toString());
		String mail = signUpReq.get("MAIL").toString();	

		String ptyDeviceToken = signUpReq.get("PTY_DEVICE_TOKEN").toString();
		String ptyDeviceType = signUpReq.get("PTY_DEVICE_TYPE").toString();
		
		LOGGER.debug("Signupuser Initialized");
		boolean emailValidation = smtpMailHelper.isValidEmailId(partyAuthid);
		
		if(!partyName.matches("[a-zA-Z_ ]+")) {
			JSONObject msgObj = infoService.getListInfo("invalid-name");
			throw new PartyNameValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signup");
			//throw new PartyNameValidateException(propsControl.getPartynamenotvalidTitle()+"~"+propsControl.getPartynamenotvalidText()+"~Signup");
		}		
		if(!partyLastName.matches("[a-zA-Z_ ]+")) {
			JSONObject msgObj = infoService.getListInfo("invalid-name");
			throw new PartyNameValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signup");
			//throw new PartyNameValidateException(propsControl.getPartynamenotvalidTitle()+"~"+propsControl.getPartynamenotvalidText()+"~Signup");
		}
		if(emailValidation == false) {
			JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
			throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signup");
			//throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~Signup");
		}
		
		
		if(ptyDeviceToken.isEmpty() || ptyDeviceToken == null) {
			JSONObject msgObj = infoService.getListInfo("invalid-device");
			throw new PartyDeviceNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signup");
			//throw new PartyDeviceNotFoundException(propsControl.getPartydevicenotfoundTitle()+"~"+propsControl.getPartydevicenotfoundText()+"~Signup");
		} else if (ptyDeviceType.isEmpty() || ptyDeviceType == null) {
			JSONObject msgObj = infoService.getListInfo("invalid-device");
			throw new PartyDeviceNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signup");
			//throw new PartyDeviceNotFoundException(propsControl.getPartydevicenotfoundTitle()+"~"+propsControl.getPartydevicenotfoundText()+"~Signup");
		}		
		
		if(mail.equalsIgnoreCase("send"))
		{		
			LOGGER.debug("send Service called");
			System.out.println("Send service cal");
			result = partyAuthMechService.signUpUser(partyName,partyLastName,authId,partyAuthid,partyAuthtoken,ptyDeviceToken,ptyDeviceType);
			jsonResult.put("RESULT", result);
		}
		else if(mail.equalsIgnoreCase("resend"))
		{
			LOGGER.debug("resend Service called");
			result = partyAuthMechService.resendCode(partyName,partyLastName,authId,partyAuthid,partyAuthtoken);
			jsonResult.put("RESULT", result);
		}
				
		return jsonResult;
	}
	
	/**
	 * This method used to verify the user by checking the verification code.
	 * 
	 * @param verifyReq JSONObject contain information about the verification code.
	 * @return Returns JSONObject that has a value of success message.
	 * @throws WrongVerifyCodeException Exception thrown when temporary verification code does not match with user's entered code.
	 * @throws PartyEmailValidateException Exception thrown when entered email id is not an usual format or when it may not be an valid email id.
	 * @throws PartyNotFoundException Exception thrown when entered party is not found.
	 */
	
	@PermitAll
	@Path("/verifyEmail")
	@POST
	@Produces("application/json")
	public JSONObject verifyEmail(@Valid JSONObject  verifyReq ) throws WrongVerifyCodeException, PartyEmailValidateException, PartyNotFoundException{
		String result;	
		JSONObject jsonResult = new JSONObject();
		String partyAuthid = verifyReq.get("PAM_AUTH_ID").toString();
		String mailVerifyCode = verifyReq.get("PAM_VERIFY_CODE").toString();
		
		boolean emailValidation = smtpMailHelper.isValidEmailId(partyAuthid);
		if(emailValidation == false) {
			JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
			throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Verifyemail");
			//throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~Verifyemail");
		} 
		
		result = partyAuthMechService.verifyEmail(partyAuthid,mailVerifyCode);
		jsonResult.put("RESULT", result);
		return jsonResult;
	}
	
	/**
	 * This method used to sign in the users, User need to enter the auth id and password to sign in.
	 * 
	 * @param signUpReq JSONObject contain information about the users details.
	 * @return Returns JSONObject that has a value of party id.
	 * @throws MessagingException Exception thrown when the message is empty.
	 * @throws PartyExistException Exception thrown when entered party is already exist in database.
	 * @throws PartyEmailValidateException Exception thrown when entered email id is not an usual format or when it may not be an valid email id.
	 * @throws PartyDeviceNotFoundException Exception thrown when the device token and type is empty.
	 */
	
	@PermitAll
	@Path("/signIn")
	@POST
	@Produces("application/json")
	public JSONObject signInUser(@Valid JSONObject  signUpReq) throws MessagingException, PartyExistException, PartyEmailValidateException, PartyDeviceNotFoundException{
		String result;	
		JSONObject jsonResult = new JSONObject();
		String authId = signUpReq.get("AMH_ID").toString();
		String partyAuthid = signUpReq.get("PAM_AUTH_ID").toString();
		String partyAuthtoken = encryptDecrypt.encrypt(signUpReq.get("PAM_AUTH_TOKEN").toString());
		
		String deviceToken = signUpReq.get("DEVICE_TOKEN").toString();
		String deviceType = signUpReq.get("DEVICE_TYPE").toString();
		
		boolean emailValidation = smtpMailHelper.isValidEmailId(partyAuthid);
		if(emailValidation == false) {
			JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
			throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signin");
			//throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~Signin");
		}		
		if(deviceToken.isEmpty() || deviceToken == null) {
			JSONObject msgObj = infoService.getListInfo("invalid-device");
			throw new PartyDeviceNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signin");
			
			//throw new PartyDeviceNotFoundException(propsControl.getPartydevicenotfoundTitle()+"~"+propsControl.getPartydevicenotfoundText()+"~Signin");
		} else if (deviceType.isEmpty() || deviceType == null) {
			JSONObject msgObj = infoService.getListInfo("invalid-device");
			throw new PartyDeviceNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Signin");
			//throw new PartyDeviceNotFoundException(propsControl.getPartydevicenotfoundTitle()+"~"+propsControl.getPartydevicenotfoundText()+"~Signin");
		}
		
		result = partyAuthMechService.signIn(authId,partyAuthid,partyAuthtoken,deviceToken,deviceType);		
		jsonResult.put("RESULT", result);
		
		return jsonResult;
	}
	
	/**
	 * This method used for forget password. If the user forgot their password they request forgot password option.
	 * 
	 * @param fgetReq A JSONObject contains information of user email and type.
	 * @return Returns JSONObject that has a success or error message.
	 * @throws PartyEmailValidateException Exception thrown when entered email id is not an usual format or when it may not be an valid email id.
	 */
	
	@PermitAll
	@Path("/forgetPassword")
	@POST
	@Produces("application/json")
	public JSONObject forgetPassword(@Valid JSONObject fgetReq ) throws PartyEmailValidateException
	{
		String result;
		JSONObject jsonResult = new JSONObject();		
		String authId = fgetReq.get("AMH_ID").toString();
		String pamAuthId = fgetReq.get("PAM_AUTH_ID").toString();
		
		boolean emailValidation = smtpMailHelper.isValidEmailId(pamAuthId);
		if(emailValidation == false) {
			JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
			throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~ForgetPassword");
			//throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~ForgetPassword");
		}		
		result = partyAuthMechService.forgetPwd(authId, pamAuthId);
		jsonResult.put("RESULT", result);		
		return jsonResult;
		
	}
	
	/**
	 * This method used to update new password, User needs to enter the received temporary password and need to enter new and confirm password.
	 * 
	 * @param resetReq A JSONObject contains information of party new and old password.
	 * @return Returns JSONObject that has a success or error message.
	 * @throws PartyEmailValidateException Exception thrown when entered email id is not an usual format or when it may not be an valid email id. 
	 */
	
	@PermitAll
	@Path("/updatePassword")
	@POST
	@Produces("application/json")
	public JSONObject resetPassword(@Valid JSONObject resetReq) throws PartyEmailValidateException
	{
		String result;
		JSONObject jsonResult = new JSONObject();
		String authId = resetReq.get("AMH_ID").toString();
		String pamAuthId = resetReq.get("PAM_AUTH_ID").toString();
		String pamAuthToken = resetReq.get("PAM_AUTH_TOKEN").toString();
		String newPamAuthToken = encryptDecrypt.encrypt(resetReq.get("NEW_AUTH_TOKEN").toString());
		
		boolean emailValidation = smtpMailHelper.isValidEmailId(pamAuthId);
		if(emailValidation == false) {
			JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
			throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~UpdatePassword");
			//throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~UpdatePassword");
		}
		
		result = partyAuthMechService.resetPwd(authId,pamAuthId,pamAuthToken,newPamAuthToken);
		jsonResult.put("RESULT", result);
		
		return jsonResult;
	}
	
	/**
	 * This method used to sign up the users through instagram. Instagram user details will be store into database. It will return party id of just inserted. 
	 * 
	 * @param instReq A JSONObject contains information of instagram user information.
	 * @return Returns JSONObject that has a value of party id.
	 * @throws UnirestException Exception thrown when the http connection fails.
	 * @throws WrongAccessTokenException Exception thrown when entered access token of instagram is wrong or not a valid one.
	 * @throws PartyDeviceNotFoundException Exception thrown when device token and type is empty.
	 */
	
	@PermitAll
	@Path("/instagramCode")
	@POST
	@Produces("application/json")
	public JSONObject signUpInstagram(@Valid JSONObject instReq) throws UnirestException , WrongAccessTokenException, PartyDeviceNotFoundException
	{
		String result=null;
		JSONObject jsonResult = new JSONObject();
		String authId = instReq.get("AMH_ID").toString();
		String authCode = instReq.get("AMH_CODE").toString();		

		String deviceToken = instReq.get("DEVICE_TOKEN").toString();
		String deviceType = instReq.get("DEVICE_TYPE").toString();	
		
		if(deviceToken.isEmpty() || deviceToken == null) {
			JSONObject msgObj = infoService.getListInfo("invalid-device");
			throw new PartyDeviceNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~SignupInstagram");
			//throw new PartyDeviceNotFoundException(propsControl.getPartydevicenotfoundTitle()+"~"+propsControl.getPartydevicenotfoundText()+"~SignupInstagram");
		} else if (deviceType.isEmpty() || deviceType == null) {
			JSONObject msgObj = infoService.getListInfo("invalid-device");
			throw new PartyDeviceNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~SignupInstagram");
			//throw new PartyDeviceNotFoundException(propsControl.getPartydevicenotfoundTitle()+"~"+propsControl.getPartydevicenotfoundText()+"~SignupInstagram");
		}		

		if(authId.equalsIgnoreCase("IN"))
		{
			result = partyAuthMechService.signupWithInstagram(authId,authCode,deviceToken,deviceType);
		}
		
		jsonResult.put("RESULT", result);		
		return jsonResult;
		
	}
	
}
