package com.turnout.ws.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLogger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.mail.imap.OlderTerm;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.PartyDeviceNotFoundException;
import com.turnout.ws.exception.PartyEmailValidateException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNameValidateException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.helper.EncryptDecrypt;
import com.turnout.ws.helper.PropsControl;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.helper.TurnOutConstant;
import com.turnout.ws.service.InfoService;
import com.turnout.ws.service.PartyService;

/**
*
* PartyController class is front face for accessing and manipulating party related resources. Class and all of its method is mapped with URI path template to which the resource responds.
* This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
* 
* Basic CRUD operation is done like update, retrieve and search.
*/
@RestController
@Path("/")
public class PartyController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PartyController.class);
	
	private final PartyService partyService;
	private final SmtpMailSender smtpMailHelper;
	private final EncryptDecrypt encryptDecrypt;
	private final InfoService infoService;
	
	/**
	 * An injectable constructor with dependencies as argument.
	 *    
	 * @param partyService An Object of partyService as an injectable member.
	 * @param smtpMailHelper An Object of smtpMailHelper as an injectable member.
	 * @param encryptDecrypt An Object of encryptDecrypt as an injectable member.
	 * @param infoService An Object of infoService as an injectable member.
	 * @see partyService
	 * @see smtpMailHelper
	 * @see encryptDecrypt
	 * @see infoService
	 */
	  @Autowired
	 public PartyController(final PartyService partyService,final SmtpMailSender smtpMailHelper,
			 final EncryptDecrypt encryptDecrypt,final InfoService infoService) {
		  
		this.partyService = partyService;
		this.smtpMailHelper = smtpMailHelper;
		this.encryptDecrypt = encryptDecrypt;
		this.infoService = infoService;
	}
	  /**
	   * This method used to get party details from database based on party id.
	   * 
	   * @param partyid A integer value holds party id.
	   * @return Returns JSONObject contains party details.
	   * @throws PartyNotFoundException  Exception thrown when entered party is not found.
	   */
	 // @RolesAllowed({"ADMIN","USER"})
	  @PermitAll
	  @GET
	  @Path("/partyDetails/partyid/{partyid}")
	  @Produces("application/json")
	  public JSONObject retrievePartyDetailsById(@PathParam("partyid")int partyid)  throws PartyNotFoundException 
	  {
		  if(partyid == 0) {
			  throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
		  } else if(!partyService.isParty(partyid)){
			  JSONObject msgObj = infoService.getListInfo("party-not-found");
			  throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Partydetails");
			  //throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~Partydetails");
		  }
		  return partyService.retrievePartyDetailsById(partyid);	  
	  }
	  /**
	   * This method used to get party profile,interested event, accepted challenge and friends lists details from database based on party id passing.
	   * 
	   * @param partyid A integer value holds party id.
	   * @return Returns JSONObject contains party details.
	   * @throws PartyNotFoundException  Exception thrown when entered party is not found.
	   * @throws UnirestException Exception thrown when the http connection fails.
	   */
	  
	  @RolesAllowed("USER")
	  @GET
	  @Path("/profile/partyid/{partyid}")
	  @Produces("application/json")
	  public JSONObject getProfileById(@PathParam("partyid")int partyid)  throws PartyNotFoundException, UnirestException 
	  {
			if(partyid == 0) {
				throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
			} else if(!partyService.isParty(partyid)) {
				  JSONObject msgObj = infoService.getListInfo("party-not-found");
				  throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Profiledetails");
				//throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~Profiledetails");
			}		  
			return partyService.getProfileById(partyid);	  
	  }
	  
	  

	/* //@RolesAllowed("USER")
	 @PermitAll
	 @Path("/updatePartyDetails")
	 @POST
	 @Produces("application/json")
	 @Consumes("multipart/form-data")
	  public JSONObject uploadPhoto(@FormDataParam("PTY_PHOTO") InputStream uploadInputStream,
			  @FormDataParam("PTY_PHOTO") FormDataContentDisposition filename,
			  @FormDataParam("PTY_ID") String ptyid,@FormDataParam("PTY_EMAIL") String ptyemail,
			  @FormDataParam("PTY_NAME") String ptyname, @FormDataParam("PTY_LAST_NAME") String ptyLastname,
			  @FormDataParam("PTY_DESCRIPTION") String ptyDesc, @FormDataParam("PTY_SHOWUP_PREFERENCE") String ptyshowuppref,
			  @FormDataParam("PTY_ACTIVITY_PREFERENCE") String ptyActpref, @FormDataParam("LOGIN") String login,
			  @FormDataParam("NEW_PASSWORD") String ptynewpwd, @FormDataParam("CUR_PASSWORD") String ptycurpwd) throws PartyNameValidateException, PartyEmailValidateException
	  {
		 
		 	try {
				System.out.println(uploadInputStream.available());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 	
		 	String newPwd  = "";
			String curPwd  = "";
			  
			  if(!ptynewpwd.equals(""))
			  {
				  newPwd = ptynewpwd;
				  curPwd = ptycurpwd;
			  }
		 	
		 	JSONObject result = new JSONObject();
		    String fileext = filename.getFileName().substring(filename.getFileName().lastIndexOf("."));
	    	
		    String uploadedFilelocation = "/opt/foo/"+ptyid+fileext;
         	//String uploadedFilelocation = "/opt/apache-tomcat-8.0.32/webapps/Showupimages/"+ptyid+fileext;
	    	String imagePath = "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/"+ptyid+fileext;
	    	
	    	
			  boolean emailValidation = smtpMailHelper.isValidEmailId(ptyemail);
			  if(!ptyname.matches("[a-zA-Z_ ]+")) {
					JSONObject msgObj = infoService.getListInfo("invalid-name");
					throw new PartyNameValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~UpdatePartydetails");
				  //throw new PartyNameValidateException(propsControl.getPartynamenotvalidTitle()+"~"+propsControl.getPartynamenotvalidText()+"~UpdatePartydetails");
			  }
			  
			  if(emailValidation == false) {
				   JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
				   throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~UpdatePartydetails");
				  //throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~UpdatePartydetails");
			  }		  
	    	System.out.println("Update Image Path "+ imagePath );
	    	try {
				OutputStream out = new FileOutputStream(new File(uploadedFilelocation));
				int read = 0;
				byte[] bytes = new byte[1024];
				while((read=uploadInputStream.read(bytes))!=-1)
				{
					out.write(bytes, 0, read);					
				}
				out.flush();
				out.close();
				
				try {
					result = partyService.updatePartyDetailsById(Integer.parseInt(ptyid),ptyemail,imagePath,ptyDesc,ptyshowuppref,ptyActpref,login,ptynewpwd,ptycurpwd,ptyname,ptyLastname);
				} catch (PartyExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (IOException  | NumberFormatException | PartyNotFoundException | CurrentPasswordNotMatchedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.put("RESULT", e);
				
			}
			return result;
		  
	  } */
	
	   /**
	    * This method used to update the users information into database. It will return updated party id.
	    * 
	    * @param custdetails A JSONObject contains information of party.
	    * @return  Returns JSONObject that has a value of party id.
	    * @throws IllegalAccessException Exception thrown when the required input parameters are empty.
	    * @throws PartyNotFoundException Exception thrown when entered party is not found.
	    * @throws CurrentPasswordNotMatchedException Exception thrown when current pass word not match with user's entered password.
	    * @throws PartyNameValidateException Exception thrown when entered party name is not valid or it may contain alpha numeric value.
	    * @throws PartyEmailValidateException Exception thrown when entered email id is not an usual format or when it may not be an valid email id.
	    * @throws PartyExistException Exception thrown when entered party is already exist in database.
	    */
	 	@RolesAllowed("USER")
		@Path("/updatePartyDetails")
		@POST
		@Produces("application/json")
		public JSONObject updatePartyDetailsById(@Valid JSONObject custdetails) throws IllegalAccessException, PartyNotFoundException, CurrentPasswordNotMatchedException, PartyNameValidateException, PartyEmailValidateException, PartyExistException{
		  
		  int ptyid            = Integer.parseInt(custdetails.get("PTY_ID").toString());		 
		  String ptyphoto      = custdetails.get("PTY_PHOTO").toString();		 		
		  String ptyemail      = custdetails.get("PTY_EMAIL").toString();		
		  String ptyname       =  custdetails.get("PTY_NAME").toString();		  
		  String ptyLastname       =  custdetails.get("PTY_LAST_NAME").toString();
		  String ptyDesc       = custdetails.get("PTY_DESCRIPTION").toString();
		  String ptyShowupPreference = custdetails.get("PTY_SHOWUP_PREFERENCE").toString();
		  String ptyActivityPreference = custdetails.get("PTY_ACTIVITY_PREFERENCE").toString();
		  System.out.println("PartyDetails Length"+ptyphoto.length());
		  String login  = custdetails.get("LOGIN").toString();
		  String newPwd  = "";
		  String curPwd  = "";
		  
		  if(!custdetails.get("NEW_PASSWORD").toString().equals(""))
		  {
			  newPwd = encryptDecrypt.encrypt(custdetails.get("NEW_PASSWORD").toString());
			  curPwd = encryptDecrypt.encrypt(custdetails.get("CUR_PASSWORD").toString());
		  }
			  String imagePath = "";		 
		  if(!ptyphoto.equals(""))
		  {
			 
			  byte[] imageByteArray=Base64.decodeBase64(ptyphoto);
			  
			  String fileext = ".jpg";
			  //String uploadedFilelocation = "/opt/foo/"+ptyid+fileext;
			  String uploadedFilelocation = "/opt/apache-tomcat-8.0.32/webapps/Showupimages/"+ptyid+fileext;
			  imagePath = "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/"+ptyid+fileext;
			  try {
				  FileOutputStream fout = new FileOutputStream(uploadedFilelocation);			
				  fout.write(imageByteArray);
				  fout.flush();
				  fout.close();
			  } catch (FileNotFoundException e) {
				  e.printStackTrace();
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
			  
		  }		 
		  
		  if(ptyid == 0) {
			  throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
		  } else if(!partyService.isParty(ptyid)){
			  JSONObject msgObj = infoService.getListInfo("party-not-found");
			  throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~UpdatePartydetails");
			  //throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~UpdatePartydetails");
		  }
		  
		  boolean emailValidation = smtpMailHelper.isValidEmailId(ptyemail);
		  if(!ptyname.matches("[a-zA-Z_ ]+")) {
				JSONObject msgObj = infoService.getListInfo("invalid-name");
				throw new PartyNameValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~UpdatePartydetails");
			  //throw new PartyNameValidateException(propsControl.getPartynamenotvalidTitle()+"~"+propsControl.getPartynamenotvalidText()+"~UpdatePartydetails");
		  }
		  
		  if(emailValidation == false) {
			   JSONObject msgObj = infoService.getListInfo("invalid-email-adrs");
			   throw new PartyEmailValidateException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~UpdatePartydetails");
			  //throw new PartyEmailValidateException(propsControl.getPartyemailnotvalidTitle()+"~"+propsControl.getPartyemailnotvalidText()+"~UpdatePartydetails");
		  }		  
		  return partyService.updatePartyDetailsById(ptyid,ptyemail,imagePath,ptyDesc,ptyShowupPreference,ptyActivityPreference,login,newPwd,curPwd,ptyname,ptyLastname);
		  
	  }	
	 	
	 /**
	  * This method used to change the user status and it will return updated party id.
	  * 
	  * @param custdetails A JSONObject contains information of party details.
	  * @return Returns JSONObject that has a value of party id
	  * @throws IllegalAccessException Exception thrown when the required input parameters are empty.
	  * @throws PartyNotFoundException Exception thrown when entered party is not found.
	  * @throws CurrentPasswordNotMatchedException Exception thrown when current pass word not match with user's entered password.
	  */
	
	@RolesAllowed("ADMIN")
	@Path("/changeUserStatus")
	@POST
	@Produces("application/json")
	public JSONObject changeUserStatus(@Valid JSONObject custdetails) throws IllegalAccessException, PartyNotFoundException, CurrentPasswordNotMatchedException
	{	
		int ptyid        = Integer.parseInt(custdetails.get("PTY_ID").toString());
		String ptyStatus = custdetails.get("PTY_STATUS").toString();
		if(ptyid == 0) {
			throw new IllegalArgumentException(" input parameter(s) cannot be null or empty");
		} else if(!partyService.isParty(ptyid)) {
			  JSONObject msgObj = infoService.getListInfo("party-not-found");
			  throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~ChangeUserStatus");
			//throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~ChangeUserStatus");
		}
		return partyService.changeUserStatus(ptyid,ptyStatus);
	}
	
	/**
	 * This method used to get all the party from database and helps to search the party based on passed string.
	 * The string may have the value of first name,last name,email,mobile no.
	 * 
	 * @param search A string holds a text that has to be searched.
	 * @return Returns JSONArray contains all the results after searching is done.
	 */

	@RolesAllowed("ADMIN")
	@GET
	@Path("/partySearch/search/{search}")
	@Produces("application/json")	
	public JSONArray partySearch(@PathParam("search") String search)
	{
		if((search == "" || search == null) ) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		}
		return partyService.partySearch(search);
	}		

}
