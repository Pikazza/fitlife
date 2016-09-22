package com.turnout.ws.exception;

import java.sql.SQLException;
import java.util.HashMap;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ParamException;
import org.glassfish.jersey.server.ParamException.QueryParamException;
import org.springframework.dao.DataIntegrityViolationException;

import com.turnout.ws.helper.TurnOutConstant;



/**
 * This class used to map Java exceptions to Response.
 */
@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception>{

	/**
	 * This method will return response object which contains cause of runtime failure happened to clients request.
	 * Map an exception to a Response.
	 * @param exception An exception object that will have the cause of runtime failure for mapping.
	 * @return a response mapped from the supplied exception
	 */
	public Response toResponse(Exception exception) {
		
		HashMap<String, String> errormsg = new HashMap<String, String>();
		int statuscode = 0;
		if (exception instanceof PartyExistException) {
			errormsg = getErrorResponse(TurnOutConstant.PARTY_ALREADY_EXIST, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.PARTY_ALREADY_EXIST;
		} else if (exception instanceof WrongVerifyCodeException) {
			errormsg = getErrorResponse(TurnOutConstant.WRONG_VERIFY_CODE, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.WRONG_VERIFY_CODE;
		} else if (exception instanceof WrongAccessTokenException ){
			errormsg = getErrorResponse(TurnOutConstant.WRONG_ACCESS_TOKEN, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.WRONG_ACCESS_TOKEN;		
		} else if (exception instanceof NoSufficientGainedPointsException ){
			errormsg = getErrorResponse(TurnOutConstant.NOTSUFFICIENT_GAINED_POINTS, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.NOTSUFFICIENT_GAINED_POINTS;
		} else if (exception instanceof CurrentPasswordNotMatchedException ){
			errormsg = getErrorResponse(TurnOutConstant.CURRENT_PASSWORD_NOT_MATCHED, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.CURRENT_PASSWORD_NOT_MATCHED;
		} else if (exception instanceof NotFoundException) {
			errormsg = getErrorResponse(TurnOutConstant.INVALID_URL,
					"there is no resource available for given url, wrong url", exception.getClass().toString(),"Invalid URL",
					"turnout/*");
			statuscode = TurnOutConstant.INVALID_URL;
				
		} else if (exception instanceof IllegalArgumentException || exception instanceof NumberFormatException
				|| exception instanceof QueryParamException || exception instanceof ParamException
				|| exception instanceof ParamException) {

			errormsg = getErrorResponse(TurnOutConstant.ILLEGAL_ARGUMENTS, exception.getMessage(),"Illegal Argument",
					exception.getClass().toString(), "turnout/*");

			statuscode = TurnOutConstant.ILLEGAL_ARGUMENTS;
		} else if (exception instanceof NullPointerException) {
			errormsg = getErrorResponse(TurnOutConstant.NULL_VALUES_IN_DB, exception.getMessage(),"Null value in Database",
					exception.getClass().toString(), "turnout/*");
			statuscode = TurnOutConstant.NULL_VALUES_IN_DB;

		} else if (exception instanceof SQLException) {
			errormsg = getErrorResponse(TurnOutConstant.DB_EXPECTIONS, exception.getMessage(),"Database Exception",
					exception.getClass().toString(), "turnout/*");
			statuscode = TurnOutConstant.DB_EXPECTIONS;

		} else if (exception instanceof DataIntegrityViolationException) {
			errormsg = getErrorResponse(TurnOutConstant.DB_EXPECTIONS, exception.getMessage(),"Data Integrity Violation",
					exception.getClass().toString(), "turnout/*");

		} else if(exception instanceof CommentsNotEmptyException) {
			errormsg = getErrorResponse(TurnOutConstant.COMMENT_LINES_NOT_EMPTY, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.COMMENT_LINES_NOT_EMPTY;
		} else if(exception instanceof PartyNameValidateException) {
			errormsg = getErrorResponse(TurnOutConstant.PARTY_NAME_CHAR_ONLY, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.PARTY_NAME_CHAR_ONLY;
		} else if(exception instanceof PartyEmailValidateException) {
			errormsg = getErrorResponse(TurnOutConstant.EMAIL_VALIDATE, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.EMAIL_VALIDATE;
		} else if(exception instanceof PartyDeviceNotFoundException) {
			errormsg = getErrorResponse(TurnOutConstant.DEVICE_INFO_NOT_EXIST, exception.getMessage().substring(exception.getMessage().indexOf("~")+1,exception.getMessage().lastIndexOf("~")),
					exception.getMessage().substring(0, exception.getMessage().indexOf("~")),exception.getClass().toString(), exception.getMessage().substring(exception.getMessage().lastIndexOf("~")+1));
			statuscode = TurnOutConstant.DEVICE_INFO_NOT_EXIST;
		}	
		else {
			errormsg = getErrorResponse(404, exception.getMessage(), "Resource not found",exception.getClass().toString(), "turnout/*");
			statuscode = 404;
		}
		return Response.status(statuscode).entity(errormsg).build();
	}
	
	private HashMap<String, String> getErrorResponse(int statuscode, String message,String title, String exceptionType,
			String servicename) {

		HashMap<String, String> errormsg = new HashMap<String, String>();
		errormsg.put("statuscode", String.valueOf(statuscode));
		errormsg.put("message", message);
		errormsg.put("title", title);
		errormsg.put("exceptiontype", exceptionType);
		errormsg.put("servicename", servicename);
		return errormsg;

	}

}
