package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the AUTH_MECH database table.
 * 
 */
@Entity
@Table(name="AUTH_MECH")
@NamedQuery(name="AuthMech.findAll", query="SELECT a FROM AuthMech a")
public class AuthMech implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AMH_ID")
	private String amhId;

	@Column(name="AMH_NAME")
	private String amhName;

	//bi-directional many-to-one association to PartyAuthMech
	@OneToMany(mappedBy="authMech")
	private List<PartyAuthMech> partyAuthMeches;

	/**
	 * Creates an empty property list with no default values.
	 */
	public AuthMech() {
	}

	/**
	 * This is getter method, it will return the authid.
	 * 
	 * @return return authid.
	 */
	public String getAmhId() {
		return this.amhId;
	}
	/**
	 * This is setter method used to set the amhid.
	 * 
	 * @param amhId the amhid to be set.
	 */
	public void setAmhId(String amhId) {
		this.amhId = amhId;
	}
	/**
	 * This is getter method, it will return the amhname.
	 * 
	 * @return return amhname.
	 */
	public String getAmhName() {
		return this.amhName;
	}
	/**
	 * This setter method used to set amhname.
	 * @param amhName the amhName to be set.
	 */
	public void setAmhName(String amhName) {
		this.amhName = amhName;
	}

	public List<PartyAuthMech> getPartyAuthMeches() {
		return this.partyAuthMeches;
	}

	public void setPartyAuthMeches(List<PartyAuthMech> partyAuthMeches) {
		this.partyAuthMeches = partyAuthMeches;
	}

	public PartyAuthMech addPartyAuthMech(PartyAuthMech partyAuthMech) {
		getPartyAuthMeches().add(partyAuthMech);
		partyAuthMech.setAuthMech(this);

		return partyAuthMech;
	}

	public PartyAuthMech removePartyAuthMech(PartyAuthMech partyAuthMech) {
		getPartyAuthMeches().remove(partyAuthMech);
		partyAuthMech.setAuthMech(null);

		return partyAuthMech;
	}

}