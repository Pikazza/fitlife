package com.turnout.ws.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.turnout.ws.exception.CommentsNotEmptyException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.service.CommentsService;
import com.turnout.ws.service.InfoService;
import com.turnout.ws.service.PartyService;
import com.turnout.ws.service.StudioActivityService;

/**
 *
 * CommentsController class is front face for accessing and manipulating comments related resources. Class and all of its method is mapped with URI path template to which the resource responds.
 * This class is registered with jersey resource configuration, So user can access it and its method by specified URI path template.
 * 
 */

@RestController
@Path("/")
public class CommentsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentsController.class);
	private final CommentsService commentService;
	private final StudioActivityService studioActivityService;
	private final PartyService partyService;
	private final InfoService infoService;
	
	/**
	 * An injectable constructor with dependencies as argument.
	 * 
	 * @param commentService An Object of commentService as an injectable member.
	 * @param studioActivityService An Object of studioActivityService as an injectable member.
	 * @param partyService An Object of partyService as an injectable member.
	 * @param infoService An Object of infoService as an injectable member.
	 * @see commentService
	 * @see studioActivityService
	 * @see partyService
	 * @see infoService
	 */
	@Autowired
	public CommentsController(final CommentsService commentService,
			final StudioActivityService studioActivityService,final PartyService partyService,
			final InfoService infoService) {		
		this.commentService = commentService;
		this.studioActivityService = studioActivityService;
		this.partyService = partyService;
		this.infoService = infoService;
	}
	/**
	 * This method used to save a comments details into database. It will return a added comment details.
	 * 
	 * @param comments A JSONObject contains details of comments.
	 * @return Returns JSONObject has a value of comments.
	 * @throws Exception When the required post value empty or party id is not found in database.
	 */
	@RolesAllowed("USER")
	@POST
	@Path("/addComments")
	@Produces("application/json")
	public JSONObject addComment(@Valid JSONObject comments) throws Exception
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String cmtLines = comments.get("CMT_LINES").toString();
		int ptyId      = Integer.parseInt(comments.get("CMT_PTY_ID").toString());
		int cmttypeId  = Integer.parseInt(comments.get("CMT_TYPE_ID").toString());
		String cdate   = comments.get("CMT_DATE").toString();	
		String cmtType = comments.get("CMT_TYPE").toString();
		Date cmtDate   = dateFormat.parse(cdate);
		JSONObject jsonResult = new JSONObject();
		
		if(ptyId == 0) {
			throw new IllegalArgumentException("input parameter(s) cannot be null or empty");
		} else if(!partyService.isParty(ptyId)) {
			JSONObject msgObj = infoService.getListInfo("party-not-found");
			throw new PartyNotFoundException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Addcomments");
			//throw new PartyNotFoundException(propsControl.getPartynotfoundTitle()+"~"+propsControl.getPartynotfoundText()+"~Addcomments");		
		}
		
		if(cmtLines.isEmpty() || cmtLines == null) {
			JSONObject msgObj = infoService.getListInfo("no-cmts");
			throw new CommentsNotEmptyException(msgObj.get("TITLE")+"~"+msgObj.get("DESCRIPTION")+"~Addcomments");
			//throw new CommentsNotEmptyException(propsControl.getCommentnotfoundTitle()+"~"+propsControl.getCommentnotfoundText()+"~Addcomments");
		}
				
		return commentService.addComments(cmtType,cmttypeId,ptyId,cmtLines,cmtDate);
	}
	/**
	 * This method used to get all the comments from database.
	 * 
	 * @param  cmtypeid  This integer value holds activity type id.
	 * @param  cmttype   It has a constant value "COMMENTS".
	 * @param  pageno    Current page number of comments listing. 
	 * @param  pagesize  Total number of records shown in current page.
	 * @return Returns JSONArray contains all the results of comments.
	 */
	
	@RolesAllowed("USER")
	@GET
	@Path("/comments/{cmtypeid}/{cmttype}/{pageno}/{pagesize}")
	@Produces("application/json")
	public JSONArray getCommentswithPagination(@PathParam("cmtypeid") int cmtypeid,@PathParam("cmttype") String cmttype,
			@PathParam("pageno") int pageno,@PathParam("pagesize") int pagesize)
	{
		return commentService.getCommentswithPagination(cmtypeid,cmttype,pageno,pagesize);
		
	}
}
