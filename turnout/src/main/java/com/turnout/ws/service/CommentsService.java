package com.turnout.ws.service;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * CommentsService is an interface that contailns collection of methods that can be accessed for manipulating comments. So controller can access all of methods written in this interface.
 * 
 * It has its own listing,add,edit and get details method. All of it's logic are implemented in CommentsServiceimpl class.
 * 
 * All of this methods actions are determined. So while executing, it has some restrictions on parameters.
 *  
 * Attempting to query the presence of an ineligible data may throw an exception, or it may simply return false, or an empty value.
 *
 */
public interface CommentsService {
	
	/**
	 * This method used to save a comments details into database.It will return a added comment details.
	 * 
	 * @param cmtType It has a constant value "COMMENTS".
	 * @param cmtTypeid This integer value holds activity type id.
	 * @param pty_id A primary key of beacon element.
	 * @param cmt_lines This string holds an comment text.
	 * @param cmt_date This parameter holds current system date and time.
	 * @return Returns JSONObject has a value of recently added comments.
	 */
	
	public JSONObject addComments(String cmtType, int cmtTypeid, int pty_id, String cmt_lines, Date cmt_date);

	/**
	 * This method used to get all the comments from database. Comments are listing with pagination.
	 * 
	 * @param cmtypeid This integer value holds activity type id.
	 * @param cmttype It has a constant value "COMMENTS".
	 * @param pageno current page number. 
	 * @param pagesize Total number of records shown in current page.
	 * @return Returns JSONArray contains all the results of comments.
	 */
	
	public JSONArray getCommentswithPagination(int cmtypeid, String cmttype, int pageno, int pagesize);
}