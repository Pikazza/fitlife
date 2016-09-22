package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the OFFERS database table.
 * 
 */
@Entity
@Table(name="OFFERS")
@NamedQuery(name="Offers.findAll", query="SELECT s FROM Offers s ")
public class Offers implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="OFR_ID")
	private int ofrId;

	@Column(name="OFR_STD_ID")
	private int ofrStdId;

	@Column(name="OFR_TYPE")
	private String ofrType;

	@Column(name="OFR_STATUS")
	private String ofrStatus;

	@Column(name="OFR_IMG")
	private String ofrImg;	
	
	@Column(name="OFR_DESCRIPTION")
	private String ofrDescription;	

	@Column(name="OFR_EXTERNAL_LINK")
	private String ofrExternalLink;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OFR_CREATED_DATE")
	private Date ofrCreatedDate;

	public Offers() {
		
	}
	
	public int getOfrId() {
		return ofrId;
	}

	public void setOfrId(int ofrId) {
		this.ofrId = ofrId;
	}

	public int getOfrStdId() {
		return ofrStdId;
	}

	public void setOfrStdId(int ofrStdId) {
		this.ofrStdId = ofrStdId;
	}

	public String getOfrType() {
		return ofrType;
	}

	public void setOfrType(String ofrType) {
		this.ofrType = ofrType;
	}

	public String getOfrStatus() {
		return ofrStatus;
	}

	public void setOfrStatus(String ofrStatus) {
		this.ofrStatus = ofrStatus;
	}

	public String getOfrImg() {
		return ofrImg;
	}

	public void setOfrImg(String ofrImg) {
		this.ofrImg = ofrImg;
	}

	public String getOfrDescription() {
		return ofrDescription;
	}

	public void setOfrDescription(String ofrDescription) {
		this.ofrDescription = ofrDescription;
	}

	public String getOfrExternalLink() {
		return ofrExternalLink;
	}

	public void setOfrExternalLink(String ofrExternalLink) {
		this.ofrExternalLink = ofrExternalLink;
	}

	public Date getOfrCreatedDate() {
		return ofrCreatedDate;
	}

	public void setOfrCreatedDate(Date ofrCreatedDate) {
		this.ofrCreatedDate = ofrCreatedDate;
	}
	
}
