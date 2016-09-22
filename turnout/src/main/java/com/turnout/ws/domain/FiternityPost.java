package com.turnout.ws.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the FITERNITY_POST database table.
 * 
 */
@Entity
@Table(name="FITERNITY_POST")
@NamedQuery(name="FiternityPost.findAll", query="SELECT f FROM FiternityPost f")
public class FiternityPost implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="IN_POST_ID")
	private int InPostId;
	
	@Column(name="IN_POST_TYPE")
	private String InPostType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="IN_POST_CRTIME")
	private Date InPostCrtime;
	
	@Column(name="IN_POST_DATA_PATH")
	private String InPostDataPath;
	
	@Column(name ="IN_POST_DATA_LINK")
	private String InPostDatalink;
	
	@Column(name="IN_POST_CMT_CNT")
	private int InPostCmtCnt;
	
	@Column(name="IN_POST_LIKES_CNT")
	private int InPostLikesCnt;
	
	@Column(name="IN_POST_USR_NAME")
	private String InPostUsrName;
	
	@Column(name="IN_POST_USR_FULLNAME")
	private String InPostUsrFullname;
	
	@Column(name="IN_POST_USR_PIC")
	private String InPostUsrPic;
	
	@Column(name="IN_POST_VIDEO_URL")
	private String InPostVideoUrl;
	
	@Column(name="IN_POST_INSTA_ID")
	private String InPostInstaId;
	
	

	public String getInPostInstaId() {
		return InPostInstaId;
	}

	public void setInPostInstaId(String inPostInstaId) {
		InPostInstaId = inPostInstaId;
	}

	public int getInPostId() {
		return InPostId;
	}

	public void setInPostId(int inPostId) {
		InPostId = inPostId;
	}

	public String getInPostType() {
		return InPostType;
	}

	public void setInPostType(String inPostType) {
		InPostType = inPostType;
	}

	public Date getInPostCrtime() {
		return InPostCrtime;
	}

	public void setInPostCrtime(Date inPostCrtime) {
		InPostCrtime = inPostCrtime;
	}

	public String getInPostDataPath() {
		return InPostDataPath;
	}

	public void setInPostDataPath(String inPostDataPath) {
		InPostDataPath = inPostDataPath;
	}

	public String getInPostDatalink() {
		return InPostDatalink;
	}

	public void setInPostDatalink(String inPostDatalink) {
		InPostDatalink = inPostDatalink;
	}

	public int getInPostCmtCnt() {
		return InPostCmtCnt;
	}

	public void setInPostCmtCnt(int inPostCmtCnt) {
		InPostCmtCnt = inPostCmtCnt;
	}

	public int getInPostLikesCnt() {
		return InPostLikesCnt;
	}

	public void setInPostLikesCnt(int inPostLikesCnt) {
		InPostLikesCnt = inPostLikesCnt;
	}

	public String getInPostUsrName() {
		return InPostUsrName;
	}

	public void setInPostUsrName(String inPostUsrName) {
		InPostUsrName = inPostUsrName;
	}

	public String getInPostUsrFullname() {
		return InPostUsrFullname;
	}

	public void setInPostUsrFullname(String inPostUsrFullname) {
		InPostUsrFullname = inPostUsrFullname;
	}

	public String getInPostUsrPic() {
		return InPostUsrPic;
	}

	public void setInPostUsrPic(String inPostUsrPic) {
		InPostUsrPic = inPostUsrPic;
	}

	public String getInPostVideoUrl() {
		return InPostVideoUrl;
	}

	public void setInPostVideoUrl(String inPostVideoUrl) {
		InPostVideoUrl = inPostVideoUrl;
	}
	
	

}
