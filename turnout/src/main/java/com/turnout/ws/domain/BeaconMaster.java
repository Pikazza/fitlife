package com.turnout.ws.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the BEACON_MASTER database table.
 * 
 */
@Entity
@Table(name="BEACON_MASTER")
@NamedQuery(name="BeaconMaster.findAll", query="SELECT b FROM BeaconMaster b")
@IdClass(BeaconMasterPk.class)
public class BeaconMaster implements Serializable {
	
	@Id	
	@Column(name="BCON_ID")
	private String bconId;
	
	@Id
	@Column(name="BCON_STD_ID")
	private int bconStdId;
	
	@Column(name="BCON_SITE")
	private String bconSite;
	
	public String getBconSite() {
		return bconSite;
	}

	public void setBconSite(String bconSite) {
		this.bconSite = bconSite;
	}

	@Column(name="BCON_STA_TYPE_ID")
	private int bconStaTypeId;
	
	@Column(name="BCON_DETECT_TYPE")
	private String bconDetectType;
	
	@Column(name="BCON_STATUS")
	private String bconStatus;
	

	public String getBconStatus() {
		return bconStatus;
	}

	public void setBconStatus(String bconStatus) {
		this.bconStatus = bconStatus;
	}

	public String getBconDetectType() {
		return bconDetectType;
	}

	public void setBconDetectType(String bconDetectType) {
		this.bconDetectType = bconDetectType;
	}

	public String getBconId() {
		return bconId;
	}

	public void setBconId(String bconId) {
		this.bconId = bconId;
	}

	public int getBconStaTypeId() {
		return bconStaTypeId;
	}

	public void setBconStaTypeId(int bconStaTypeId) {
		this.bconStaTypeId = bconStaTypeId;
	}

	public int getBconStdId() {
		return bconStdId;
	}

	public void setBconStdId(int bconStdId) {
		this.bconStdId = bconStdId;
	}

}
