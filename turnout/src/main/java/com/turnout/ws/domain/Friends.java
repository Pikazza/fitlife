package com.turnout.ws.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the  FRIENDS table.
 * 
 */
@Entity
@Table(name="FRIENDS")
@NamedQuery(name="Friends.findAll", query="SELECT f FROM Friends f")
@IdClass(FriendsPK.class)
public class Friends implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FRND_ID1", insertable=false, updatable=false)
	private int frndId1;

	@Id
	@Column(name="FRND_ID2", insertable=false, updatable=false)
	private int frndId2;
	
	@Column(name="FRND_STATUS")
	private String frndStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FRND_MODIFY_DATE")
	private Date frndModifyDate;

	public int getFrndId1() {
		return frndId1;
	}

	public void setFrndId1(int frndId1) {
		this.frndId1 = frndId1;
	}

	public int getFrndId2() {
		return frndId2;
	}

	public void setFrndId2(int frndId2) {
		this.frndId2 = frndId2;
	}

	public String getFrndStatus() {
		return frndStatus;
	}

	public void setFrndStatus(String frndStatus) {
		this.frndStatus = frndStatus;
	}

	public Date getFrndModifyDate() {
		return frndModifyDate;
	}

	public void setFrndModifyDate(Date frndModifyDate) {
		this.frndModifyDate = frndModifyDate;
	}
	

}
