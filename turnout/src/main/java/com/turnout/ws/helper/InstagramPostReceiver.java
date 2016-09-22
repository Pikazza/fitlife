package com.turnout.ws.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.controller.LikesController;
import com.turnout.ws.domain.FiternityPost;
import com.turnout.ws.repository.FiternityPostRepository;

/**
 * InstagramPostReceiver is used to get all the posts an studio owner posted on their instagram account.
 * 
 */

@Component
public class InstagramPostReceiver {
	private static final Logger LOGGER=LoggerFactory.getLogger(InstagramPostReceiver.class);
	
	@Autowired
	protected FiternityPostRepository fiternityPostRepository;
	
	
	/**
	 * This method will allow the studio owners to update their instagram post in Fiternity application.
	 * 
	 * @throws UnirestException It throws when any runtime error occurs.
	 */
	//cron = "0 */1 * * *")
	@Scheduled(cron = "0 0 */1 * * *")
	public void updateFITERINITYPOST() throws UnirestException
	{
		HttpResponse<JsonNode> response = Unirest.get("https://api.instagram.com/v1/users/2076351984/media/recent/?access_token=2076351984.e1d8835.7639d20dacaa4f919cdfdff9c84a184d")
				  .header("cache-control", "no-cache")
				  .header("postman-token", "961b18f0-806c-bd44-0e8c-bc3ac0eca130")
				  .asJson();
		String type;
		String image;
		String video="";
		String tags;
		int cmtcnt,likescnt;
		String crtime; 
		String linkurl;
		String usrName;
		String usrPhoto;
		String usrFullname;
		String markingid;
		
		JSONArray jsonopdata = new JSONArray();
		JSONObject jsonopobj ;
		if(response.getStatus()==200)
		{
			org.json.JSONObject responseBody = response.getBody().getObject();			
			
			org.json.JSONArray jsondata =  responseBody.getJSONArray("data");
			if(jsondata.length() > 0)
				fiternityPostRepository.deleteAll();
			for (int i = 0; i < jsondata.length(); i++)
			{
				jsonopobj = new JSONObject();					
				
				org.json.JSONObject jsonobj = jsondata.getJSONObject(i);			
				
				markingid = jsonobj.getString("id");
				type = jsonobj.getString("type");			
				
				
				org.json.JSONObject cmtobj = jsonobj.getJSONObject("comments");				
				cmtcnt = cmtobj.getInt("count");
				
				crtime = jsonobj.getString("created_time");
				linkurl = jsonobj.getString("link");
				
				org.json.JSONObject lksobj = jsonobj.getJSONObject("likes");				
				likescnt = lksobj.getInt("count");					
				 
				if(type.equalsIgnoreCase("video"))
				{
					org.json.JSONObject varray = jsonobj.getJSONObject("videos");
					
					org.json.JSONObject vstdres = varray.getJSONObject("standard_resolution");
					video = vstdres.getString("url");	
				}		
				
				org.json.JSONObject imgarray = jsonobj.getJSONObject("images");
				    
			    org.json.JSONObject imgstdres = imgarray.getJSONObject("standard_resolution");			    
			    image = imgstdres.getString("url");
			    
			    org.json.JSONObject usrobj = jsonobj.getJSONObject("user");
			    usrName = usrobj.getString("username");
			    usrPhoto = usrobj.getString("profile_picture");
			    usrFullname = usrobj.getString("full_name");	
			    
			    
			    long unixSeconds = Long.valueOf(crtime);
			    Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
			  //  sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
			    String formattedDate = sdf.format(date);
			    LOGGER.debug(formattedDate);
			    LOGGER.debug("Convert Unixtimestamp to mysql datetime"+ formattedDate);
			    
			    FiternityPost fp = new FiternityPost();
			    fp.setInPostCmtCnt(cmtcnt);
			    fp.setInPostCrtime(date);
			    fp.setInPostDatalink(linkurl);
			    fp.setInPostDataPath(image);
			    fp.setInPostLikesCnt(likescnt);
			    fp.setInPostType(type);
			    fp.setInPostUsrFullname(usrFullname);
			    fp.setInPostUsrName(usrName);
			    fp.setInPostUsrPic(usrPhoto);
			    fp.setInPostVideoUrl(video);
			    fp.setInPostInstaId(markingid);
			    
			    fiternityPostRepository.save(fp);
			    
				   			
			}
			 
			
			
			 
		}

	}
	
	

	/**
	 * This method will allow the studio owners to get their instagram post in Fiternity application by linking them with Instagram .
	 * 
	 * @param stdPamAuthId A username of studio's instagram account.
	 * @param stdPamAuthToken A token of studio's instagram account.
	 * @return JSONArrray of posts.
	 * @throws UnirestException  It throws when any runtime error occurs.
	 */
	public JSONArray getInstagramPost(String stdPamAuthId, String stdPamAuthToken) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get("https://api.instagram.com/v1/users/"+stdPamAuthId+"/media/recent/?access_token="+stdPamAuthToken)
				  .header("cache-control", "no-cache")
				  .header("postman-token", "961b18f0-806c-bd44-0e8c-bc3ac0eca130")
				  .asJson();
		String type;
		String image;
		String video;
		String tags;
		
		JSONArray jsonopdata = new JSONArray();
		JSONObject jsonopobj ;
		if(response.getStatus()==200)
		{
			org.json.JSONObject responseBody = response.getBody().getObject();
			//accessToken= responseBody.getString("access_token")jsondata.length();;
			
			org.json.JSONArray jsondata =  responseBody.getJSONArray("data");
			for (int i = 0; i < jsondata.length(); i++)
			{
				jsonopobj = new JSONObject();					
				LOGGER.debug("tags::::::::::::::::::");
				org.json.JSONObject jsonobj = jsondata.getJSONObject(i);
				
				org.json.JSONArray tagsarray = jsonobj.getJSONArray("tags");
				
				LOGGER.debug("tagsarray.length():::"+tagsarray.length());
				
				if(tagsarray.length() > 0)
				{
					boolean fiternitychecker = false; 
					for(int j=0;j<tagsarray.length();j++){
					
						String septags = tagsarray.getString(j);
						if(septags.equalsIgnoreCase("fiternity"))
							fiternitychecker = true;
					}
					if(fiternitychecker == false)
						 continue;
				}
				else
				{
					continue;
				}
				
				type = jsonobj.getString("type");
				String link = jsonobj.getString("link");
				
				 jsonopobj.put("type", type);
				 jsonopobj.put("link", link);
				 
				 
				if(type.equalsIgnoreCase("video"))
				{
					org.json.JSONObject varray = jsonobj.getJSONObject("videos");
					
					org.json.JSONObject vstdres = varray.getJSONObject("standard_resolution");
					video = vstdres.getString("url");
					
					jsonopobj.put("videos", video);
					
				}
				else
				{
					jsonopobj.put("videos", "");
				}
				
				org.json.JSONObject imgarray = jsonobj.getJSONObject("images");
				    
			    org.json.JSONObject imgstdres = imgarray.getJSONObject("standard_resolution");
			    
			    image = imgstdres.getString("url");				    
				jsonopobj.put("images", image);
				    
				org.json.JSONObject usrarray = jsonobj.getJSONObject("user");			    
				String username = usrarray.getString("username");
				jsonopobj.put("username", username);				    
				    
				jsonopdata.add(jsonopobj);				
			}
			 
			LOGGER.debug("Instagram........"+jsonopdata);
		}
		return jsonopdata;
		
		
	}	

}
