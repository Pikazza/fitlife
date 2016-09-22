package com.turnout.ws.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the COMMENTS database table.
 * 
 */
@Entity
@Table(name="COMMENTS")
@NamedQuery(name="Comments.findAll", query="SELECT c FROM Comments c")
public class Comments implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="CMT_ID")
	private int cmtId;
	
	@Column(name="CMT_LINES")
	private String cmtLines;
	
	@Column(name="CMT_PTY_ID")
	private int CmtPtyId;
	
	@Column(name="CMT_TYPE")
	private String cmtType;
	
	@Column(name="CMT_TYPE_ID")
	private int CmtTypeId;
	
	public int getCmtId() {
		return cmtId;
	}

	public void setCmtId(int cmtId) {
		this.cmtId = cmtId;
	}

	public String getCmtLines() {
		return cmtLines;
	}

	public void setCmtLines(String cmtLines) {
		this.cmtLines = cmtLines;
	}

	

	public int getCmtPtyId() {
		return CmtPtyId;
	}

	public void setCmtPtyId(int cmtPtyId) {
		CmtPtyId = cmtPtyId;
	}

	

	public String getCmtType() {
		return cmtType;
	}

	public void setCmtType(String cmtType) {
		this.cmtType = cmtType;
	}

	public int getCmtTypeId() {
		return CmtTypeId;
	}

	public void setCmtTypeId(int cmtTypeId) {
		CmtTypeId = cmtTypeId;
	}

	public Date getCmtDate() {
		return cmtDate;
	}

	public void setCmtDate(Date cmtDate) {
		this.cmtDate = cmtDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CMT_DATE")
	private Date cmtDate;
	

}
