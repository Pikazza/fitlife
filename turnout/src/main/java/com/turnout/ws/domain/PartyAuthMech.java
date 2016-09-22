package com.turnout.ws.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the PARTY_AUTH_MECH database table.
 * 
 */
@Entity
@Table(name="PARTY_AUTH_MECH")
@NamedQuery(name="PartyAuthMech.findAll", query="SELECT p FROM PartyAuthMech p")
@IdClass(PartyAuthMechPK.class)
public class PartyAuthMech implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PTY_ID", insertable=false, updatable=false)
	private int ptyId;

	@Id
	@Column(name="AMH_ID", insertable=false, updatable=false)
	private String amhId;
	

	public int getPtyId() {
		return ptyId;
	}

	public void setPtyId(int ptyId) {
		this.ptyId = ptyId;
	}

	public String getAmhId() {
		return amhId;
	}
	
	public void setAmhId(String amhId) {
		this.amhId = amhId;
	}

	@Column(name="PAM_AUTH_ID")
	private String pamAuthId;

	@Column(name="PAM_AUTH_TOKEN")
	private String pamAuthToken;
	
	@Column(name="PAM_VERIFY_CODE")
	private String pamVerifyCode;
	
	@Column(name="PAM_VERIFIED")
	private String pamVerified;
	
	@Temporal(TemporalType.DATE)
	@Column(name="PAM_VERIFYCODE_EXPIRY")
	private Date pamVerifycodeExpiry;
	
    public PartyAuthMech() {
		
	}

	//bi-directional many-to-one association to AuthMech
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AMH_ID",insertable=false, updatable=false)
	private AuthMech authMech;

	//bi-directional many-to-one association to Party
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PTY_ID",insertable=false, updatable=false)
	private Party party;	
	

	public String getPamAuthId() {
		return this.pamAuthId;
	}

	public void setPamAuthId(String pamAuthId) {
		this.pamAuthId = pamAuthId;
	}

	public String getPamAuthToken() {
		return this.pamAuthToken;
	}

	public void setPamAuthToken(String pamAuthToken) {
		this.pamAuthToken = pamAuthToken;
	}

	public AuthMech getAuthMech() {
		return this.authMech;
	}

	public void setAuthMech(AuthMech authMech) {
		this.authMech = authMech;
	}

	public Party getParty() {
		return this.party;
	}

	public void setParty(Party party) {
		this.party = party;
	}
	
	public String getPamVerifyCode() {
		return pamVerifyCode;
	}
	
	public void setPamVerifyCode(String pamVerifyCode) {
		this.pamVerifyCode = pamVerifyCode;
	}
	
	public String getPamVerified() {
		return pamVerified;
	}
	
	public void setPamVerified(String pamVerified) {
		this.pamVerified = pamVerified;
	}
	
	public Date getPamVerifycodeExpiry() {
		return pamVerifycodeExpiry;
	}
	
	public void setPamVerifycodeExpiry(Date pamVerifycodeExpiry) {
		this.pamVerifycodeExpiry = pamVerifycodeExpiry;
	}
	
	public String toString()
	{
		return amhId;
		
	}

}