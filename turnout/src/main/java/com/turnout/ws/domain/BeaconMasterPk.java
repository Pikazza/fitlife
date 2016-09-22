package com.turnout.ws.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the BEACON_MASTER database table.
 * 
 */
@Embeddable
public class BeaconMasterPk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="BCON_ID", insertable=false, updatable=false)
	private String bconId;

	@Column(name="BCON_STD_ID", insertable=false, updatable=false)
	private int bconStdId;

	
	public String getBconId() {
		return bconId;
	}


	public void setBconId(String bconId) {
		this.bconId = bconId;
	}


	public int getBconStdId() {
		return bconStdId;
	}


	public void setBconStdId(int bconStdId) {
		this.bconStdId = bconStdId;
	}


	public BeaconMasterPk() {
		// TODO Auto-generated constructor stub
	}
	

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeaconMasterPk)) {
			return false;
		}
		BeaconMasterPk castOther = (BeaconMasterPk)other;
		return 
			this.bconId.equals(castOther.bconId)
			&& (this.bconStdId == castOther.bconStdId);
	}	
	
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.bconId.hashCode();
		hash = hash * prime + this.bconStdId;
		
		return hash;
	}
}



