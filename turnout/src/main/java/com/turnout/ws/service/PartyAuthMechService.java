package com.turnout.ws.service;

import javax.mail.MessagingException;

import org.json.simple.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.exception.WrongAccessTokenException;
import com.turnout.ws.exception.WrongVerifyCodeException;

/**
 * PartyAuthMechService is an interface that contains collection of methods that can be accessed for manipulating party authentication mechanism. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in PartyAuthMechServiceImpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to add or edit an ineligible object throws an MessagingException, typically PartyExistException or PartyNotFoundException or WrongVerifyCodeException.
 *
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */

public interface PartyAuthMechService {

	/**
	 * 
	 * @param partyName party first name.
	 * @param partyLastName party last name.
	 * @param authMech it has a constant text "SL".The SL represent app user.
	 * @param partyAuthid party login email address.
	 * @param partyAuthtoken party login password.
	 * @param ptyDeviceToken it is automatically detect from party using mobile device.
	 * @param ptyDeviceType it is type party using mobile os (ios / Andriod).
	 * @return return last inserted party id.
	 * @throws MessagingException Exception thrown when the message content empty.
	 * @throws PartyExistException Exception thrown when entered party is already exist in database.
	 */
	public String signUpUser(String partyName,String partyLastName, String authMech,String partyAuthid,String partyAuthtoken, String ptyDeviceToken, String ptyDeviceType) throws MessagingException, PartyExistException;

	/**
	 * This method used to verify the user by checking the verification code.
	 * @param partyAuthid  party email address.
	 * @param mailVerifyCode party verification code. When the party successfully registered this code sent to the registered email address.
	 * @return returns JSONObject that has a value of success message.
	 * @throws WrongVerifyCodeException Exception thrown when temporary verification code does not match with user's entered code.
	 * @throws PartyNotFoundException Exception thrown when entered party is not found.
	 */
	
	public String verifyEmail(String partyAuthid, String mailVerifyCode) throws WrongVerifyCodeException, PartyNotFoundException;

	/**
	 * It is used to resend the verification code to the user when the party doesn't received.
	 * @param partyName party first name
	 * @param partyLastName party last name
	 * @param authId type of the user (app / instagram).
	 * @param partyAuthid party registered email address.
	 * @param partyAuthtoken password.
	 * @return return string message success / failure.
	 */
	public String resendCode(String partyName,String partyLastName, String authId, String partyAuthid, String partyAuthtoken);

	/**
	 * This method allow user to sign in the app.
	 * @param authId type of party (app / instagram user).
	 * @param partyAuthid party registered email address.
	 * @param partyAuthtoken party registered password.
	 * @param deviceToken it is automatically detect from party using mobile device.
	 * @param deviceType it is type party using mobile os (ios / Andriod).
	 * @return returns JSONObject that has a value of party id with user session values.
	 */
	public String signIn(String authId, String partyAuthid, String partyAuthtoken, String deviceToken, String deviceType);

	/**
	 * If the party forgot the login password they can able to get password using this method.
	 * @param authId type of the user.
	 * @param partyAuthid party registered email address.
	 * @return it will return success or failure message.
	 */
	public String forgetPwd(String authId,String partyAuthid);

	/**
	 * This method allow user to update new password.
	 * @param authId type of the user.
	 * @param pamAuthId party registered email address.
	 * @param pamAuthToken temporary password. Party received email with temporary password.
	 * @param newPamAuthToken party new password.
	 * @return return success or failure message.
	 */
	public String resetPwd(String authId, String pamAuthId, String pamAuthToken, String newPamAuthToken);

	/**
	 * If the user have instagram access they can able to access the app using own instagram account.
	 * @param authId it has a constant value "IN".
	 * @param authCode instagram auth code.
	 * @param deviceToken it is automatically detect from party using mobile device.
	 * @param deviceType it is type party using mobile os (ios / Andriod).
	 * @return returns JSONObject that has a value of party id.
	 * @throws UnirestException Exception thrown when the http connection fails.
	 * @throws WrongAccessTokenException Exception thrown when entered access token of instagram is wrong or not a valid one.
	 */
	public String signupWithInstagram(String authId, String authCode, String deviceToken, String deviceType) throws UnirestException, WrongAccessTokenException;

}