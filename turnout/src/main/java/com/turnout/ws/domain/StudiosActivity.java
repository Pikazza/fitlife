package com.turnout.ws.domain;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the STUDIOS_ACTIVITY database table.
 * 
 */
@Entity
@Table(name="STUDIOS_ACTIVITY")
/*@IdClass(StudiosActivityPK.class)*/
@NamedQuery(name="StudiosActivity.findAll", query="SELECT s FROM StudiosActivity s")
public class StudiosActivity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STA_ID")
	private int staId;	

	public int getStaId() {
		return staId;
	}

	public void setStaId(int staId) {
		this.staId = staId;		
	}

	public int getStdId() {
		return stdId;
	}



	public void setStdId(int stdId) {
		this.stdId = stdId;
	}

	/*@Id*/
	@Column(name="STD_ID")
	private int stdId;

	@Column(name="STA_BADGE")
	private String staBadge;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STA_CREATED_DATE")
	private Date staCreatedDate;

	@Column(name="STA_DESCRIPTION")
	private String staDescription;
	
	@Column(name="STA_SHORT_DESCRIPTION")
	private String staShortDescription;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STA_END_DATE")
	private Date staEndDate;	
	

	@Column(name="STA_GOAL_POINTS")
	private int staGoalPoints;

	@Column(name="STA_IMAGE_URL")
	private String staImageUrl;

	@Temporal(TemporalType.DATE)
	@Column(name="STA_MODIFIED_DATE")
	private Date staModifiedDate;

	@Column(name="STA_NAME")
	private String staName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STA_START_DATE")
	private Date staStartDate;

	@Column(name="STA_UPDATED_BY")
	private int staUpdatedBy;
	
	@Column(name="STA_ADDRESS_LINE1")
	private String staAddressLine1;

	@Column(name="STA_ADDRESS_LINE2")
	private String staAddressLine2;

	@Column(name="STA_ADDRESS_LINE3")
	private String staAddressLine3;
	
	@Column(name="STA_COUNTRY")
	private String staCountry;

	@Column(name="STA_COUNTY")
	private String staCounty;
	
	@Lob
	@Column(name="STA_INFLUENCER")
	private String staInfluencer;

	@Column(name="STA_LATITUDE")
	private float staLatitude;

	@Column(name="STA_LIKE_CNT")
	private int staLikeCnt;

	@Column(name="STA_LONGITUDE")
	private float staLongitude;
	
	@Column(name="STA_POST_CODE")
	private String staPostCode;

	@Column(name="STA_PRICE")
	private double staPrice;
	
	@Column(name="STA_TOWN")
	private String staTown;
	
	@Temporal(TemporalType.DATE)
	@Column(name="STA_EXPIRY_DATE")
	private Date staExpiryDate;	
	
	@Temporal(TemporalType.TIME)
	@Column(name="STA_START_TIME")
	private Date staStartTime;
	
	@Temporal(TemporalType.TIME)
	@Column(name="STA_END_TIME")
	private Date staEndTime;

	@Column(name="STA_EXTERNAL_LINK")
	private String staExternalLink;
	
	@Column(name="STA_LOCATION")
	private String staLocation;

	
	@Column(name="STA_NO_OF_DAYS")
	private int staNoOfDays;

	public int getStaNoOfDays() {
		return staNoOfDays;
	}

	public void setStaNoOfDays(int staNoOfDays) {
		this.staNoOfDays = staNoOfDays;
	}

	
	@Column(name="STA_STATUS")
	private String staStatus;	


	public String getStaStatus() {
		return staStatus;
	}

	public void setStaStatus(String staStatus) {
		this.staStatus = staStatus;
	}

	public String getStaLocation() {
		return staLocation;
	}

	public void setStaLocation(String staLocation) {
		this.staLocation = staLocation;
	}

	//bi-directional many-to-one association to StudioActivityType
	@ManyToOne
	@JoinColumn(name="STA_TYPE_ID")
	private StudioActivityType studioActivityType;

	//bi-directional many-to-one association to Studio
	@ManyToOne
	@JoinColumn(name="STD_ID", insertable=false, updatable=false)
	private Studio studio;

	public StudiosActivity() {
	}

	public Date getStaStartTime() {
		return this.staStartTime;
	}

	public void setStaStartTime(Date stastarttime2) {
		this.staStartTime = stastarttime2;
	}
	
	public Date getStaEndTime() {
		return this.staEndTime;
	}

	public void setStaEndTime(Date staEndTime) {
		this.staEndTime = staEndTime;
	}
	

	public String getStaExternalLink() {
		return this.staExternalLink;
	}

	public void setStaExternalLink(String staExternalLink) {
		this.staExternalLink = staExternalLink;
	}

	public String getStaBadge() {
		return this.staBadge;
	}

	public void setStaBadge(String staBadge) {
		this.staBadge = staBadge;
	}

	public Date getStaCreatedDate() {
		return this.staCreatedDate;
	}

	public void setStaCreatedDate(Date staCreatedDate) {
		this.staCreatedDate = staCreatedDate;
	}

	public String getStaDescription() {
		return this.staDescription;
	}

	public void setStaDescription(String staDescription) {
		this.staDescription = staDescription;
	}
	public String getStaShortDescription() {
		return this.staShortDescription;
	}

	public void setStaShortDescription(String staShortDescription) {
		this.staShortDescription = staShortDescription;
	}
	public Date getStaEndDate() {
		return this.staEndDate;
	}

	public void setStaEndDate(Date staEndDate) {
		this.staEndDate = staEndDate;
	}

	public int getStaGoalPoints() {
		return this.staGoalPoints;
	}

	public void setStaGoalPoints(int staGoalPoints) {
		this.staGoalPoints = staGoalPoints;
	}

	public String getStaImageUrl() {
		return this.staImageUrl;
	}

	public void setStaImageUrl(String staImageUrl) {
		this.staImageUrl = staImageUrl;
	}

	public Date getStaModifiedDate() {
		return this.staModifiedDate;
	}

	public void setStaModifiedDate(Date staModifiedDate) {
		this.staModifiedDate = staModifiedDate;
	}

	public String getStaName() {
		return this.staName;
	}

	public void setStaName(String staName) {
		this.staName = staName;
	}

	public Date getStaStartDate() {
		return this.staStartDate;
	}

	public void setStaStartDate(Date staStartDate) {
		this.staStartDate = staStartDate;
	}

	public int getStaUpdatedBy() {
		return this.staUpdatedBy;
	}

	public void setStaUpdatedBy(int staUpdatedBy) {
		this.staUpdatedBy = staUpdatedBy;
	}

	public String getStaAddressLine1() {
		return staAddressLine1;
	}



	public void setStaAddressLine1(String staAddressLine1) {
		this.staAddressLine1 = staAddressLine1;
	}



	public String getStaAddressLine2() {
		return staAddressLine2;
	}



	public void setStaAddressLine2(String staAddressLine2) {
		this.staAddressLine2 = staAddressLine2;
	}



	public String getStaAddressLine3() {
		return staAddressLine3;
	}



	public void setStaAddressLine3(String staAddressLine3) {
		this.staAddressLine3 = staAddressLine3;
	}



	public String getStaCountry() {
		return staCountry;
	}



	public void setStaCountry(String staCountry) {
		this.staCountry = staCountry;
	}



	public String getStaCounty() {
		return staCounty;
	}



	public void setStaCounty(String staCounty) {
		this.staCounty = staCounty;
	}



	public String getStaInfluencer() {
		return staInfluencer;
	}



	public void setStaInfluencer(String staInfluencer) {
		this.staInfluencer = staInfluencer;
	}



	public float getStaLatitude() {
		return staLatitude;
	}



	public void setStaLatitude(float staLatitude) {
		this.staLatitude = staLatitude;
	}



	public int getStaLikeCnt() {
		return staLikeCnt;
	}



	public void setStaLikeCnt(int staLikeCnt) {
		this.staLikeCnt = staLikeCnt;
	}



	public float getStaLongitude() {
		return staLongitude;
	}



	public void setStaLongitude(float staLongitude) {
		this.staLongitude = staLongitude;
	}



	public String getStaPostCode() {
		return staPostCode;
	}



	public void setStaPostCode(String staPostCode) {
		this.staPostCode = staPostCode;
	}



	public double getStaPrice() {
		return staPrice;
	}



	public void setStaPrice(double staPrice) {
		this.staPrice = staPrice;
	}

	public Date getStaExpiryDate() {
		return this.staExpiryDate;
	}
	
	public String getStaTown() {
		return staTown;
	}

	public void setStaTown(String staTown) {
		this.staTown = staTown;
	}

	public StudioActivityType getStudioActivityType() {
		return this.studioActivityType;
	}

	public void setStudioActivityType(StudioActivityType studioActivityType) {
		this.studioActivityType = studioActivityType;
	}

	public Studio getStudio() {
		return this.studio;
	}

	public void setStudio(Studio studio) {
		this.studio = studio;		
	}

	public void setStaExpiryDate(Date staExpiryDate) {
		this.staExpiryDate = staExpiryDate;
	}

}