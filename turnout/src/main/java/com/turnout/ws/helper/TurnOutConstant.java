package com.turnout.ws.helper;

/**
 * This interface defines all status codes that can be used for identifying outcome of request.
 * These constants will be sent as part of output so users can rectify their problems.
 * 
 */
public interface TurnOutConstant {
	
	public int PARTY_ALREADY_EXIST = 801;
	
	public int WRONG_VERIFY_CODE = 802;
	
	public int ILLEGAL_ARGUMENTS = 803;
	
	public int NULL_VALUES_IN_DB = 804;
	
	public int DB_EXPECTIONS = 805;
	
	public int INVALID_URL = 806;	
	
	public int WRONG_ACCESS_TOKEN = 807;
	
	public int PARTY_NOT_EXIST = 808;
	
	public int NOTSUFFICIENT_GAINED_POINTS = 809;
	
	public int CURRENT_PASSWORD_NOT_MATCHED = 810;
	
	public int PARTY_NAME_CHAR_ONLY = 811;
	
	public int EMAIL_VALIDATE = 812;
	
	public int DEVICE_INFO_NOT_EXIST = 813;
	
	public int COMMENT_LINES_NOT_EMPTY = 814;	
	
	public String SUCCESS="SUCCESS";
	
	public String FAILED="FAILED";
	
	public String WRONG_PWD="WRONG PASSWORD";
	
	public String NOT_EXIST="NOT EXIST";	
	
	public String ACCOUNT_INACTIVE = "ACCOUNT INACTIVE";
	
		
	
	public String STUDIOS_ACTIVITY="STUDIOS_ACTIVITY";
	
	public String STUDIOS="STUDIOS";
	
	public String STUDIO_PARTY_ACTIVITY="STUDIO_PARTY_ACTIVITY";
	
	public String VOUCHER_PARTY="VOUCHER_PARTY";
	
	public String COMMENTS="COMMENTS";
	
}
