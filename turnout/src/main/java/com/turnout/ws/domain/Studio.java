package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the STUDIOS database table.
 * 
 */
@Entity
@Table(name="STUDIOS")
@NamedQuery(name="Studio.findAll", query="SELECT s FROM Studio s ")
public class Studio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="STD_ID")
	private int stdId;

	@Column(name="STD_ADDRESS_LINE1")
	private String stdAddressLine1;

	@Column(name="STD_ADDRESS_LINE2")
	private String stdAddressLine2;

	@Column(name="STD_ADDRESS_LINE3")
	private String stdAddressLine3;

	@Column(name="STD_BILLING_ADDRS_COUNTRY")
	private String stdBillingAddrsCountry;	
	
	@Column(name="STD_BILLING_ADDRS_COUNTY")
	private String stdBillingAddrsCounty;	

	@Column(name="STD_BILLING_ADDRS_LINE1")
	private String stdBillingAddrsLine1;

	@Column(name="STD_BILLING_ADDRS_LINE2")
	private String stdBillingAddrsLine2;

	@Column(name="STD_BILLING_ADDRS_LINE3")
	private String stdBillingAddrsLine3;

	@Column(name="STD_BILLING_ADDRS_POSTCODE")
	private String stdBillingAddrsPostcode;

	@Column(name="STD_BILLING_ADDRS_TOWN")
	private String stdBillingAddrsTown;

	@Column(name="STD_COMPANY_HOUSE_NUMBER")
	private String stdCompanyHouseNumber;

	@Column(name="STD_COMPANY_LOGO")
	private String stdCompanyLogo;

	@Column(name="STD_COUNTY")
	private String stdCounty;
	
	@Column(name="STD_COUNTRY")
	private String stdCountry;

	@Column(name="STD_CREATED_BY")
	private String stdCreatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STD_CREATED_ON")
	private Date stdCreatedOn;

	@Column(name="STD_IMAGE_URL")
	private String stdImageUrl;

	@Column(name="STD_MODIFIED_BY")
	private String stdModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="STD_MODIFIED_ON")
	private Date stdModifiedOn;

	@Column(name="STD_NAME")
	private String stdName;

	@Column(name="STD_POST_CODE")
	private String stdPostCode;

	@Column(name="STD_TOWN")
	private String stdTown;

	@Column(name="STD_TRADING_NAME")
	private String stdTradingName;

	@Column(name="STD_VAT_NUMBER")
	private String stdVatNumber;
	
	@Column(name="STD_LATITUDE")
	private float stdLatitude;
	
	@Column(name="STD_LONGITUDE")
	private float stdLongitude;
	
	@Column(name="STD_DESCRIPTION")
	private String stdDescription;
	
	@Column(name="STD_STREAM_DESC")
	private String stdStreamDescription;
	
	public String getStdStreamDescription() {
		return stdStreamDescription;
	}

	public void setStdStreamDescription(String stdStreamDescription) {
		this.stdStreamDescription = stdStreamDescription;
	}

	@Column(name="STD_PHONE_NO")
	private String stdPhoneNo;
	
	@Column(name="STD_MAIL_ID")
	private String stdMailId;
	
	
	@Column(name="STD_SITE_URL")
	private String stdSiteUrl;
	

	@Column(name="STD_PAM_AUTH_ID")
	private String stdPamAuthId;
	
	@Column(name="STD_PAM_AUTH_TOKEN")
	private String stdPamAuthToken;	

	@Column(name="STD_TYPE")
	private String stdType;
	
	@Column(name="STD_STATUS")
	private String stdStatus;
	
	@Column(name="STD_POINTS")
	private int stdPoints;
	
	@Column(name="STD_TIME_SPENT")
	private int stdTimeSpent;
	
	
	public String getStdSiteUrl() {
		return stdSiteUrl;
	}

	public void setStdSiteUrl(String stdSiteUrl) {
		this.stdSiteUrl = stdSiteUrl;
	}

	/*//bi-directional many-to-one association to BeaconMaster
	@OneToMany(mappedBy="studio")
	private List<BeaconMaster> beaconMasters;
*/
	//bi-directional many-to-one association to Reward
	@OneToMany(mappedBy="studio",fetch=FetchType.LAZY)
	private List<Reward> rewards;

	//bi-directional many-to-one association to StudiosActivity
	@OneToMany(mappedBy="studio",fetch=FetchType.LAZY)
	private List<StudiosActivity> studiosActivities;

	public float getStdLatitude() {
		return stdLatitude;
	}

	public void setStdLatitude(float stdLatitude) {
		this.stdLatitude = stdLatitude;
	}

	public float getStdLongitude() {
		return stdLongitude;
	}

	public void setStdLongitude(float stdLongitude) {
		this.stdLongitude = stdLongitude;
	}

	public Studio() {
	}

	public int getStdId() {
		return this.stdId;
	}

	public void setStdId(int stdId) {
		this.stdId = stdId;
	}

	public String getStdAddressLine1() {
		return this.stdAddressLine1;
	}

	public void setStdAddressLine1(String stdAddressLine1) {
		this.stdAddressLine1 = stdAddressLine1;
	}

	public String getStdAddressLine2() {
		return this.stdAddressLine2;
	}

	public void setStdAddressLine2(String stdAddressLine2) {
		this.stdAddressLine2 = stdAddressLine2;
	}

	public String getStdAddressLine3() {
		return this.stdAddressLine3;
	}

	public void setStdAddressLine3(String stdAddressLine3) {
		this.stdAddressLine3 = stdAddressLine3;
	}

	public String getStdBillingAddrsCountry() {
		return this.stdBillingAddrsCountry;
	}

	public void setStdBillingAddrsCountry(String stdBillingAddrsCountry) {
		this.stdBillingAddrsCountry = stdBillingAddrsCountry;
	}

	public String getStdBillingAddrsCounty() {
		return this.stdBillingAddrsCounty;
	}

	public void setStdBillingAddrsCounty(String stdBillingAddrsCounty) {
		this.stdBillingAddrsCounty = stdBillingAddrsCounty;
	}

	public String getStdBillingAddrsLine1() {
		return this.stdBillingAddrsLine1;
	}

	public void setStdBillingAddrsLine1(String stdBillingAddrsLine1) {
		this.stdBillingAddrsLine1 = stdBillingAddrsLine1;
	}

	public String getStdBillingAddrsLine2() {
		return this.stdBillingAddrsLine2;
	}

	public void setStdBillingAddrsLine2(String stdBillingAddrsLine2) {
		this.stdBillingAddrsLine2 = stdBillingAddrsLine2;
	}

	public String getStdBillingAddrsLine3() {
		return this.stdBillingAddrsLine3;
	}

	public void setStdBillingAddrsLine3(String stdBillingAddrsLine3) {
		this.stdBillingAddrsLine3 = stdBillingAddrsLine3;
	}

	public String getStdBillingAddrsPostcode() {
		return this.stdBillingAddrsPostcode;
	}

	public void setStdBillingAddrsPostcode(String stdBillingAddrsPostcode) {
		this.stdBillingAddrsPostcode = stdBillingAddrsPostcode;
	}

	public String getStdBillingAddrsTown() {
		return this.stdBillingAddrsTown;
	}

	public void setStdBillingAddrsTown(String stdBillingAddrsTown) {
		this.stdBillingAddrsTown = stdBillingAddrsTown;
	}

	public String getStdCompanyHouseNumber() {
		return this.stdCompanyHouseNumber;
	}

	public void setStdCompanyHouseNumber(String stdCompanyHouseNumber) {
		this.stdCompanyHouseNumber = stdCompanyHouseNumber;
	}

	public String getStdCompanyLogo() {
		return this.stdCompanyLogo;
	}

	public void setStdCompanyLogo(String stdCompanyLogo) {
		this.stdCompanyLogo = stdCompanyLogo;
	}

	public String getStdCounty() {
		return this.stdCounty;
	}

	public void setStdCounty(String stdCounty) {
		this.stdCounty = stdCounty;
	}

	public String getStdCreatedBy() {
		return this.stdCreatedBy;
	}

	public void setStdCreatedBy(String stdCreatedBy) {
		this.stdCreatedBy = stdCreatedBy;
	}

	public Date getStdCreatedOn() {
		return this.stdCreatedOn;
	}

	public void setStdCreatedOn(Date stdCreatedOn) {
		this.stdCreatedOn = stdCreatedOn;
	}

	public String getStdImageUrl() {
		return this.stdImageUrl;
	}

	public void setStdImageUrl(String stdImageUrl) {
		this.stdImageUrl = stdImageUrl;
	}

	public String getStdModifiedBy() {
		return this.stdModifiedBy;
	}

	public void setStdModifiedBy(String stdModifiedBy) {
		this.stdModifiedBy = stdModifiedBy;
	}

	public Date getStdModifiedOn() {
		return this.stdModifiedOn;
	}

	public void setStdModifiedOn(Date stdModifiedOn) {
		this.stdModifiedOn = stdModifiedOn;
	}

	public String getStdName() {
		return this.stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getStdPostCode() {
		return this.stdPostCode;
	}

	public void setStdPostCode(String stdPostCode) {
		this.stdPostCode = stdPostCode;
	}

	public String getStdTown() {
		return this.stdTown;
	}

	public void setStdTown(String stdTown) {
		this.stdTown = stdTown;
	}

	public String getStdTradingName() {
		return this.stdTradingName;
	}

	public void setStdTradingName(String stdTradingName) {
		this.stdTradingName = stdTradingName;
	}

	public String getStdVatNumber() {
		return this.stdVatNumber;
	}

	public void setStdVatNumber(String stdVatNumber) {
		this.stdVatNumber = stdVatNumber;
	}

	public String getStdCountry() {
		return stdCountry;
	}

	public void setStdCountry(String stdCountry) {
		this.stdCountry = stdCountry;
	}

	/*public List<BeaconMaster> getBeaconMasters() {
		return this.beaconMasters;
	}

	public void setBeaconMasters(List<BeaconMaster> beaconMasters) {
		this.beaconMasters = beaconMasters;
	}

	public BeaconMaster addBeaconMaster(BeaconMaster beaconMaster) {
		getBeaconMasters().add(beaconMaster);
		beaconMaster.setStudio(this);

		return beaconMaster;
	}

	public BeaconMaster removeBeaconMaster(BeaconMaster beaconMaster) {
		getBeaconMasters().remove(beaconMaster);
		beaconMaster.setStudio(null);

		return beaconMaster;
	}*/

	public String getStdDescription() {
		return stdDescription;
	}

	public void setStdDescription(String stdDescription) {
		this.stdDescription = stdDescription;
	}

	public String getStdPhoneNo() {
		return stdPhoneNo;
	}

	public void setStdPhoneNo(String stdPhoneNo) {
		this.stdPhoneNo = stdPhoneNo;
	}

	public String getStdMailId() {
		return stdMailId;
	}

	public void setStdMailId(String stdMailId) {
		this.stdMailId = stdMailId;
	}


	public List<Reward> getRewards() {
		return this.rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

	public Reward addReward(Reward reward) {
		getRewards().add(reward);
		reward.setStudio(this);

		return reward;
	}

	public Reward removeReward(Reward reward) {
		getRewards().remove(reward);
		reward.setStudio(null);

		return reward;
	}

	public List<StudiosActivity> getStudiosActivities() {
		return this.studiosActivities;
	}

	public void setStudiosActivities(List<StudiosActivity> studiosActivities) {
		this.studiosActivities = studiosActivities;
	}

	public StudiosActivity addStudiosActivity(StudiosActivity studiosActivity) {
		getStudiosActivities().add(studiosActivity);
		studiosActivity.setStudio(this);

		return studiosActivity;
	}

	public StudiosActivity removeStudiosActivity(StudiosActivity studiosActivity) {
		getStudiosActivities().remove(studiosActivity);
		studiosActivity.setStudio(null);

		return studiosActivity;
	}

	public String getStdPamAuthId() {
		return stdPamAuthId;
	}

	public void setStdPamAuthId(String stdPamAuthId) {
		this.stdPamAuthId = stdPamAuthId;
	}

	public String getStdPamAuthToken() {
		return stdPamAuthToken;
	}

	public void setStdPamAuthToken(String stdPamAuthToken) {
		this.stdPamAuthToken = stdPamAuthToken;
	}

	public String getStdType() {
		return stdType;
	}

	public void setStdType(String stdType) {
		this.stdType = stdType;
	}

	public String getStdStatus() {
		return stdStatus;
	}

	public void setStdStatus(String stdStatus) {
		this.stdStatus = stdStatus;
	}

	public int getStdPoints() {
		return stdPoints;
	}

	public void setStdPoints(int stdPoints) {
		this.stdPoints = stdPoints;
	}

	public int getStdTimeSpent() {
		return stdTimeSpent;
	}

	public void setStdTimeSpent(int stdTimeSpent) {
		this.stdTimeSpent = stdTimeSpent;
	}

}
