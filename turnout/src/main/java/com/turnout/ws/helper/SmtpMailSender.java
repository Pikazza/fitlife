package com.turnout.ws.helper;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;

@Component
public class SmtpMailSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmtpMailSender.class);
	private static final String SENDER_ID = "AIzaSyB_5npFJ8HjZVizSUmsS5CJMbO680bqhnw";
	
	@Autowired
	private JavaMailSender javaMailSender; 
	
	/**
	 * 
	 * @param to Email id of the user to whom the mail to be sent.
	 * @param subject A subject of an email.
	 * @param body A body part of email.
	 * @throws MessagingException This exception when any failures happens related to messaging.
	 */
	@Async
	public void send(String to,String subject,String body) throws MessagingException
	{
		MimeMessage message= javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		
		helper = new MimeMessageHelper(message,true);
		
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setText(body, true);
		
		javaMailSender.send(message);		
		
		
	}
	
	/**
	 * This function will generate an random number as verification code so users email can be verified.
	 * 
	 * @param from A number that will be a starting point of number
	 * @return some random number.
	 */
	public int gen(int from) {
	    System.out.println("CODE FROM "+from);
		Random r = new Random( System.currentTimeMillis() );
	    return ((1 + r.nextInt(10)) * from + r.nextInt(from));
	}
	
	
	public void sendPushMsgToIOS(String devicetoken)
	{
		 
		ApnsService service = null;
        try {
            InputStream certStream = this.getClass().getClassLoader().getResourceAsStream("Push_dev.p12");
            service = APNS.newService().withCert(certStream, "rimac@123").withSandboxDestination().build();
            
            service.start();
            
            System.out.println("IOS Device");  
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
            try {
                   
                    int days = (int) (System.currentTimeMillis() / 1000 / 60 / 60 / 24);
                    PayloadBuilder payloadBuilder = APNS.newPayload();
                    
                    JSONObject jsonMsg = new JSONObject();
                    jsonMsg.put("NOTIFY_IMG", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/106.jpg");
                    jsonMsg.put("NOTIFY_DATE", "14/05/2016 13:00:00");
                    jsonMsg.put("NOTIFY_MESSAGE", "PUSH TEST MESSAGE");                    
                    
                    payloadBuilder = payloadBuilder.alertTitle("Fiternity").alertBody("Fiternity message").customField("notifydata", jsonMsg.toJSONString());
                    
                    payloadBuilder.instantDeliveryOrSilentNotification(); //content available : 1
                    
                    if (payloadBuilder.isTooLong()) {
                        payloadBuilder = payloadBuilder.shrinkBody();
                    }
                    String payload = payloadBuilder.build();
                   
                    System.out.println("OBJECT "+payloadBuilder);
                    String token  = devicetoken;

                	System.out.println("Token "+token);
                	System.out.println("Payload "+payload);
                	service.push(token, payload);
                	
                	System.out.println("Message sent");               	
                	
                   
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
	
	
	public void  sendPushMsgToAndroid(String devicetoken)
	{
		System.out.println("TEST1 ENTERED");
		String collapseKey = "";
        String userMessage = "";
        Map<String, Object> record = new HashMap<String,Object>();      
        	        	
        	        	
        	List<String> androidTargets = new ArrayList<String>();		
     		//androidTargets.addAll(tokenlist);
     		androidTargets.add("dCL8pmOQWtA:APA91bHDjZcYA36O-ot5VI7NZpKEHvdNYc5RHQ9ByEe21IPAzO__I660ahtDwuYa4VWs1pUOADQknpGqLxMKNuA4leRsR8setbbqYcvJ83j-1FwfjFk9FbPm9giKUqTwNRGApWjsa_yF");
     		 userMessage = "Fiternity message";
             collapseKey = "GCM_Message";
             
             DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       	     Date date = new Date();
       	     
       	     Sender sender = new Sender(SENDER_ID);
       	     System.out.println("TEST1 CREATE SENDER OBJECT");
       	     
	       	  JSONObject jsonMsg = new JSONObject();
	          jsonMsg.put("NOTIFY_IMG", "http://ec2-52-50-25-84.eu-west-1.compute.amazonaws.com:8080/Showupimages/106.jpg");
	          jsonMsg.put("NOTIFY_DATE", "27/04/2016 15:00:00");
	          jsonMsg.put("NOTIFY_MESSAGE", "Andriod Push message");	         
	          
	         // Message message = new Message.Builder().collapseKey(collapseKey).timeToLive(30).delayWhileIdle(true).addData("message", userMessage).addData("title", "Fiternity").addData("content-available", "1").addData("notifydata", jsonMsg.toString()).build();
	          Message message = new Message.Builder().collapseKey(collapseKey).timeToLive(30).delayWhileIdle(true).addData("message", userMessage).addData("title", "Fiternity").addData("notifydata", jsonMsg.toString()).build();
	          try {
	        	  System.out.println("Andriod device token "+androidTargets);
	  			MulticastResult result = sender.send(message, androidTargets, 1);
	  			System.out.println("SEND  MESSAGES");
	  			 if (result.getResults() != null) {
	  	                int canonicalRegId = result.getCanonicalIds();
	  	                if (canonicalRegId != 0) {	   
	  	                	System.out.println("Broadcast Successfull");
	  	                }
	  	            } else {
	  	                int error = result.getFailure();
	  	                System.out.println("Broadcast failure: " + error);
	  	            }
	  		} catch (IOException e) {
	  			e.printStackTrace();
	  		}    

        	
        }
	
	/**
	 * This function validate the structure of email id and return true if the email looks good else return false.
	 * 
	 * @param email email has to be checked.
	 * @return It returns boolean value.
	 */
	public static boolean isValidEmailId(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }	 
	
}
