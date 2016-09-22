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
 * The persistent class for the PARTY_NOTIFICATION database table.
 * 
 */
@Entity
@Table(name="PARTY_NOTIFICATION")
@NamedQuery(name="PartyNotification.findAll", query="SELECT p FROM PartyNotification p")
public class PartyNotification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="NOTIFY_ID")
	private int notifyId;
	
	@Column(name="NOTIFY_PTY_ID")
	private int notifyPtyId;
	
	@Column(name="NOTIFY_OTH_LIKES")
	private String notifyOthLikes;
	
	@Column(name="NOTIFY_OTH_CMTS")
	private String notifyOthCmts;
	
	@Column(name="NOTIFY_OTH_INTRST_EVNT")
	private String notifyOthIntrstEvnt;
	
	@Column(name="NOTIFY_OTH_ACPT_CHLNG")
	private String notifyOthAcptChlng;
	
	@Column(name="NOTIFY_PERSONAL_POINTS_CRDT")
	private String notifyPersonalPointsCrdt;	
	
	@Column(name="NOTIFY_PERSONAL_EVNT_REMAINDER")
	private String notifyPersonalEvntRemainder;
	
	@Column(name="NOTIFY_PERSONAL_READY_REEDEM")
	private String notifyPersonalReadyReedem;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NOTIFY_MODIFY_DATE")
	private Date notifyModifyDate;

	public PartyNotification() {
		
	}
	
	public int getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(int notifyId) {
		this.notifyId = notifyId;
	}

	public int getNotifyPtyId() {
		return notifyPtyId;
	}

	public void setNotifyPtyId(int notifyPtyId) {
		this.notifyPtyId = notifyPtyId;
	}

	public String getNotifyOthLikes() {
		return notifyOthLikes;
	}

	public void setNotifyOthLikes(String notifyOthLikes) {
		this.notifyOthLikes = notifyOthLikes;
	}

	public String getNotifyOthCmts() {
		return notifyOthCmts;
	}

	public void setNotifyOthCmts(String notifyOthCmts) {
		this.notifyOthCmts = notifyOthCmts;
	}

	public String getNotifyOthIntrstEvnt() {
		return notifyOthIntrstEvnt;
	}

	public void setNotifyOthIntrstEvnt(String notifyOthIntrstEvnt) {
		this.notifyOthIntrstEvnt = notifyOthIntrstEvnt;
	}

	public String getNotifyOthAcptChlng() {
		return notifyOthAcptChlng;
	}

	public void setNotifyOthAcptChlng(String notifyOthAcptChlng) {
		this.notifyOthAcptChlng = notifyOthAcptChlng;
	}

	public String getNotifyPersonalPointsCrdt() {
		return notifyPersonalPointsCrdt;
	}

	public void setNotifyPersonalPointsCrdt(String notifyPersonalPointsCrdt) {
		this.notifyPersonalPointsCrdt = notifyPersonalPointsCrdt;
	}

	public String getNotifyPersonalEvntRemainder() {
		return notifyPersonalEvntRemainder;
	}

	public void setNotifyPersonalEvntRemainder(String notifyPersonalEvntRemainder) {
		this.notifyPersonalEvntRemainder = notifyPersonalEvntRemainder;
	}

	public String getNotifyPersonalReadyReedem() {
		return notifyPersonalReadyReedem;
	}

	public void setNotifyPersonalReadyReedem(String notifyPersonalReadyReedem) {
		this.notifyPersonalReadyReedem = notifyPersonalReadyReedem;
	}

	public Date getNotifyModifyDate() {
		return notifyModifyDate;
	}

	public void setNotifyModifyDate(Date notifyModifyDate) {
		this.notifyModifyDate = notifyModifyDate;
	}
	

}
