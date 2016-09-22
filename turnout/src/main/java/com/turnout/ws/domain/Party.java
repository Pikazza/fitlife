package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PARTY database table.
 * 
 */
@Entity
@Table(name="PARTY")
@NamedQuery(name="Party.findAll", query="SELECT p FROM Party p")
public class Party implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PTY_ID")
	private int ptyId;

	@Column(name="PTY_ADDRESS_LINE_1")
	private String ptyAddressLine1;

	@Column(name="PTY_ADDRESS_LINE_2")
	private String ptyAddressLine2;

	@Column(name="PTY_ADDRESS_LINE_3")
	private String ptyAddressLine3;

	@Column(name="PTY_COUNTRY")
	private String ptyCountry;

	@Column(name="PTY_EMAIL")
	private String ptyEmail;

	@Column(name="PTY_GAINED_POINTS")
	private int ptyGainedPoints;

	@Column(name="PTY_MOBILE")
	private String ptyMobile;

	@Column(name="PTY_NAME")
	private String ptyName;	

	@Column(name="PTY_LAST_NAME")
	private String ptyLastName;

	@Column(name="PTY_PHOTO")
	private String ptyPhoto;

	@Column(name="PTY_POST_CODE")
	private String ptyPostCode;

	@Column(name="PTY_TEL")
	private String ptyTel;

	@Column(name="PTY_TOWN")
	private String ptyTown;
	
	@Column(name="PTY_DESCRIPTION")
	private String ptyDescription;
	
	@Column(name="PTY_SHOWUP_PREFERENCE")
	private String ptyShowupPreference;
	
	@Column(name="PTY_ACTIVITY_PREFERENCE")
	private String ptyActivityPreference;
	
	@Column(name="PTY_STATUS")
	private String ptyStatus;	

	@Column(name="PTY_DEVICE_TOKEN")
	private String ptyDeviceToken;	

	@Column(name="PTY_DEVICE_TYPE")
	private String ptyDeviceType;	

	
	/*//bi-directional many-to-one association to BeaconTran
	@OneToMany(mappedBy="party")
	private List<BeaconTran> beaconTrans;*/

	public String getPtyDeviceToken() {
		return ptyDeviceToken;
	}

	public void setPtyDeviceToken(String ptyDeviceToken) {
		this.ptyDeviceToken = ptyDeviceToken;
	}

	public String getPtyDeviceType() {
		return ptyDeviceType;
	}

	public void setPtyDeviceType(String ptyDeviceType) {
		this.ptyDeviceType = ptyDeviceType;
	}

	public String getPtyStatus() {
		return ptyStatus;
	}

	public void setPtyStatus(String ptyStatus) {
		this.ptyStatus = ptyStatus;
	}

	//bi-directional many-to-one association to PartyAuthMech
	@OneToMany(mappedBy="party")
	private List<PartyAuthMech> partyAuthMeches;

/*	//bi-directional many-to-one association to PersonalisedUiCard
	@OneToMany(mappedBy="party")
	private List<PersonalisedUiCard> personalisedUiCards;*/

	/*//bi-directional many-to-one association to VoucherParty
	@OneToMany(mappedBy="party")
	private List<VoucherParty> voucherParties;*/

	public Party() {
	}

	public int getPtyId() {
		return this.ptyId;
	}

	public void setPtyId(int ptyId) {
		this.ptyId = ptyId;
	}

	public String getPtyAddressLine1() {
		return this.ptyAddressLine1;
	}

	public void setPtyAddressLine1(String ptyAddressLine1) {
		this.ptyAddressLine1 = ptyAddressLine1;
	}

	public String getPtyAddressLine2() {
		return this.ptyAddressLine2;
	}

	public void setPtyAddressLine2(String ptyAddressLine2) {
		this.ptyAddressLine2 = ptyAddressLine2;
	}

	public String getPtyAddressLine3() {
		return this.ptyAddressLine3;
	}

	public void setPtyAddressLine3(String ptyAddressLine3) {
		this.ptyAddressLine3 = ptyAddressLine3;
	}

	public String getPtyCountry() {
		return this.ptyCountry;
	}

	public void setPtyCountry(String ptyCountry) {
		this.ptyCountry = ptyCountry;
	}

	public String getPtyEmail() {
		return this.ptyEmail;
	}

	public void setPtyEmail(String ptyEmail) {
		this.ptyEmail = ptyEmail;
	}

	public int getPtyGainedPoints() {
		return this.ptyGainedPoints;
	}

	public void setPtyGainedPoints(int ptyGainedPoints) {
		this.ptyGainedPoints = ptyGainedPoints;
	}

	public String getPtyMobile() {
		return this.ptyMobile;
	}

	public void setPtyMobile(String ptyMobile) {
		this.ptyMobile = ptyMobile;
	}

	public String getPtyName() {
		return this.ptyName;
	}

	public void setPtyName(String ptyName) {
		this.ptyName = ptyName;
	}

	public String getPtyPhoto() {
		return this.ptyPhoto;
	}

	public void setPtyPhoto(String ptyPhoto) {
		this.ptyPhoto = ptyPhoto;
	}

	public String getPtyPostCode() {
		return this.ptyPostCode;
	}

	public void setPtyPostCode(String ptyPostCode) {
		this.ptyPostCode = ptyPostCode;
	}

	public String getPtyTel() {
		return this.ptyTel;
	}

	public void setPtyTel(String ptyTel) {
		this.ptyTel = ptyTel;
	}

	public String getPtyTown() {
		return this.ptyTown;
	}

	public void setPtyTown(String ptyTown) {
		this.ptyTown = ptyTown;
	}

	/*beacon data mapping
	 * 
	 * public List<BeaconTran> getBeaconTrans() {
		return this.beaconTrans;
	}

	public void setBeaconTrans(List<BeaconTran> beaconTrans) {
		this.beaconTrans = beaconTrans;
	}

	public BeaconTran addBeaconTran(BeaconTran beaconTran) {
		getBeaconTrans().add(beaconTran);
		beaconTran.setParty(this);

		return beaconTran;
	}

	public BeaconTran removeBeaconTran(BeaconTran beaconTran) {
		getBeaconTrans().remove(beaconTran);
		beaconTran.setParty(null);

		return beaconTran;
	}*/

	public String getPtyDescription() {
		return ptyDescription;
	}

	public void setPtyDescription(String ptyDescription) {
		this.ptyDescription = ptyDescription;
	}

	public String getPtyShowupPreference() {
		return ptyShowupPreference;
	}

	public void setPtyShowupPreference(String ptyShowupPreference) {
		this.ptyShowupPreference = ptyShowupPreference;
	}

	public String getPtyActivityPreference() {
		return ptyActivityPreference;
	}

	public void setPtyActivityPreference(String ptyActivityPreference) {
		this.ptyActivityPreference = ptyActivityPreference;
	}

	public List<PartyAuthMech> getPartyAuthMeches() {
		return this.partyAuthMeches;
	}

	public void setPartyAuthMeches(List<PartyAuthMech> partyAuthMeches) {
		this.partyAuthMeches = partyAuthMeches;
	}

	public PartyAuthMech addPartyAuthMech(PartyAuthMech partyAuthMech) {
		getPartyAuthMeches().add(partyAuthMech);
		partyAuthMech.setParty(this);

		return partyAuthMech;
	}

	public PartyAuthMech removePartyAuthMech(PartyAuthMech partyAuthMech) {
		getPartyAuthMeches().remove(partyAuthMech);
		partyAuthMech.setParty(null);

		return partyAuthMech;
	}
	
	public String getPtyLastName() {
		return ptyLastName;
	}

	public void setPtyLastName(String ptyLastName) {
		this.ptyLastName = ptyLastName;
	}

	/*Personalised ui card mapping
	 * 
	 * 
	 * 
	 * 
	 * public List<PersonalisedUiCard> getPersonalisedUiCards() {
		return this.personalisedUiCards;
	}

	public void setPersonalisedUiCards(List<PersonalisedUiCard> personalisedUiCards) {
		this.personalisedUiCards = personalisedUiCards;
	}

	public PersonalisedUiCard addPersonalisedUiCard(PersonalisedUiCard personalisedUiCard) {
		getPersonalisedUiCards().add(personalisedUiCard);
		personalisedUiCard.setParty(this);

		return personalisedUiCard;
	}

	public PersonalisedUiCard removePersonalisedUiCard(PersonalisedUiCard personalisedUiCard) {
		getPersonalisedUiCards().remove(personalisedUiCard);
		personalisedUiCard.setParty(null);

		return personalisedUiCard;
	}
*/
	
	
	/*Voucher Party mapping
	 * 
	 * 
	 * 
	 * public List<VoucherParty> getVoucherParties() {
		return this.voucherParties;
	}

	public void setVoucherParties(List<VoucherParty> voucherParties) {
		this.voucherParties = voucherParties;
	}

	public VoucherParty addVoucherParty(VoucherParty voucherParty) {
		getVoucherParties().add(voucherParty);
		voucherParty.setParty(this);

		return voucherParty;
	}

	public VoucherParty removeVoucherParty(VoucherParty voucherParty) {
		getVoucherParties().remove(voucherParty);
		voucherParty.setParty(null);

		return voucherParty;
	}*/

}