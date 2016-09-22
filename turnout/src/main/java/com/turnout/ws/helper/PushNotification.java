package com.turnout.ws.helper;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.turnout.ws.controller.StudioController;

/**
 * PushNotifications class let Fiternity application notify a user with new messages, events, reminders and friends comments,their likes and activities. 
 * It works even when the user is not actively using that application.
 * 
 *
 */
@Component
public class PushNotification {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PushNotification.class);
	private static final String SENDER_ID = "AIzaSyD_iFsNeNE2pqOUE86VFrvecTZQqyEb7ic";

	/**
	 * On Android devices, this method send push notification to user's device.
	 * Devices can receive and display sent notification. 
	 * 
	 * @param tokenlist List of users device token.
	 * @param con_message Actual push message content that will be added to push notification.
	 * @param ptyId A party id to whom push notification to be sent.
	 * @param ptyphoto photo of the party
	 * @param link A string that has a link 
	 */
	public void  sendPushAndroidNotification(List<String> tokenlist, String con_message, int ptyId, String ptyphoto,  String link)
	{
		LOGGER.debug("TEST1 ENTERED");
		String collapseKey = "";
        String userMessage = "";
        Map<String, Object> record = new HashMap<String,Object>();      
        	        	
        	List<String> androidTargets = new ArrayList<String>();		
     		androidTargets.addAll(tokenlist);
     		 
            collapseKey = "GCM_Message";
             
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 			Calendar cal = Calendar.getInstance();				
 			String curTime = dateFormat.format(cal.getTime());
             
       	    Date date = new Date();
       	     
       	     Sender sender = new Sender(SENDER_ID);
       	     LOGGER.debug("TEST1 CREATE SENDER OBJECT");
       	     
	       	  JSONObject jsonMsg = new JSONObject();
	          jsonMsg.put("NOTIFY_IMG", ptyphoto);
	          jsonMsg.put("NOTIFY_DATE", curTime);
	          jsonMsg.put("NOTIFY_MESSAGE", con_message);	
	          jsonMsg.put("LINK_ID", link );
	          
	          //Message message = new Message.Builder().collapseKey(collapseKey).timeToLive(30).delayWhileIdle(true).addData("message", con_message).addData("title", "Fiternity").addData("content-available", "1").addData("notifydata", jsonMsg.toString()).build();
	          Message message = new Message.Builder().collapseKey(collapseKey).timeToLive(30).delayWhileIdle(true).addData("message", con_message).addData("title", "Fiternity").addData("notifydata", jsonMsg.toString()).build();
	          try {
	  			MulticastResult result = sender.send(message, androidTargets, 1);
	  			LOGGER.debug("SEND  MESSAGES");
	  			 if (result.getResults() != null) {
	  	                int canonicalRegId = result.getCanonicalIds();
	  	                if (canonicalRegId != 0) {	   
	  	                	LOGGER.debug("Broadcast Successfull");
	  	                }
	  	            } else {
	  	                int error = result.getFailure();
	  	              LOGGER.debug("Broadcast failure: " + error);
	  	            }
	  		} catch (IOException e) {
	  			e.printStackTrace();
	  		}    

        	
        }
      
	
	/**
	 * On Ios devices, this method send push notification to user's device.
	 * Devices can receive and display sent notification. 
	 * 
	 * @param tokenlist List of users device token.
	 * @param con_message Actual push message content that will be added to push notification.
	 * @param ptyId A party id to whom push notification to be sent.
	 * @param ptyphoto photo of the party
	 * @param link A string that has a link 
	 * 
	 */
	public void sendPushIosNotification(List<String> tokenlist, String con_message, int ptyId, String ptyphoto,  String link)
	{
		 
		ApnsService service = null;
        try {
            InputStream certStream = this.getClass().getClassLoader().getResourceAsStream("Push_dev.p12");
            service = APNS.newService().withCert(certStream, "rimac@123").withSandboxDestination().build();
            
            service.start();
            
            LOGGER.debug("IOS Device");  
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 			Calendar cal = Calendar.getInstance();				
 			String curTime =dateFormat.format(cal.getTime());
 			
            try {
                   
                    int days = (int) (System.currentTimeMillis() / 1000 / 60 / 60 / 24);
                    PayloadBuilder payloadBuilder = APNS.newPayload();
                    
                    JSONObject jsonMsg = new JSONObject();
                    jsonMsg.put("NOTIFY_IMG", ptyphoto);
                    jsonMsg.put("NOTIFY_DATE", curTime);
                    jsonMsg.put("NOTIFY_MESSAGE", con_message); 
                    jsonMsg.put("LINK_ID", link );
                    
                    payloadBuilder = payloadBuilder.alertTitle("Fiternity").alertBody(con_message).customField("notifydata", jsonMsg.toJSONString());

                   // payloadBuilder.instantDeliveryOrSilentNotification();
                    
                    if (payloadBuilder.isTooLong()) {
                        payloadBuilder = payloadBuilder.shrinkBody();
                    }
                    String payload = payloadBuilder.build();
                   
                    LOGGER.debug("OBJECT "+payloadBuilder);
                    
                    
                    for(String token : tokenlist)
                    {
                    	LOGGER.debug("Token "+token);
                    	LOGGER.debug("Payload "+payload);
                    	service.push(token, payload);
                    }
                   
                } catch (Exception ex) {                   
            }
        } catch (Exception ex) {
        } finally {
            // check if the service was successfully initialized and stop it here, if it was
            if (service != null) {
                service.stop();
            }
 
        }
		
		}

}
