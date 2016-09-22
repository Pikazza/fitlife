package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the STUDIO_ACTIVITY_TYPE database table.
 * 
 */
@Entity
@Table(name="STUDIO_ACTIVITY_TYPE")
@NamedQuery(name="StudioActivityType.findAll", query="SELECT s FROM StudioActivityType s")
public class StudioActivityType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="STA_TYPE_ID")
	private int staTypeId;

	@Column(name="STA_TYPE_ACTIVE")
	private String staTypeActive;

	@Column(name="STA_TYPE_DESC")
	private String staTypeDesc;

	/*//bi-directional many-to-one association to BeaconMaster
	@OneToMany(mappedBy="studioActivityType")
	private List<BeaconMaster> beaconMasters;*/

	//bi-directional many-to-one association to StudiosActivity
	@OneToMany(mappedBy="studioActivityType")
	private List<StudiosActivity> studiosActivities;

	public StudioActivityType() {
	}

	public int getStaTypeId() {
		return this.staTypeId;
	}

	public void setStaTypeId(int staTypeId) {
		this.staTypeId = staTypeId;
	}

	public String getStaTypeActive() {
		return this.staTypeActive;
	}

	public void setStaTypeActive(String staTypeActive) {
		this.staTypeActive = staTypeActive;
	}

	public String getStaTypeDesc() {
		return this.staTypeDesc;
	}

	public void setStaTypeDesc(String staTypeDesc) {
		this.staTypeDesc = staTypeDesc;
	}

	/*public List<BeaconMaster> getBeaconMasters() {
		return this.beaconMasters;
	}

	public void setBeaconMasters(List<BeaconMaster> beaconMasters) {
		this.beaconMasters = beaconMasters;
	}

	public BeaconMaster addBeaconMaster(BeaconMaster beaconMaster) {
		getBeaconMasters().add(beaconMaster);
		beaconMaster.setStudioActivityType(this);

		return beaconMaster;
	}

	public BeaconMaster removeBeaconMaster(BeaconMaster beaconMaster) {
		getBeaconMasters().remove(beaconMaster);
		beaconMaster.setStudioActivityType(null);

		return beaconMaster;
	}*/

	public List<StudiosActivity> getStudiosActivities() {
		return this.studiosActivities;
	}

	public void setStudiosActivities(List<StudiosActivity> studiosActivities) {
		this.studiosActivities = studiosActivities;
	}

	public StudiosActivity addStudiosActivity(StudiosActivity studiosActivity) {
		getStudiosActivities().add(studiosActivity);
		studiosActivity.setStudioActivityType(this);

		return studiosActivity;
	}

	public StudiosActivity removeStudiosActivity(StudiosActivity studiosActivity) {
		getStudiosActivities().remove(studiosActivity);
		studiosActivity.setStudioActivityType(null);

		return studiosActivity;
	}

}