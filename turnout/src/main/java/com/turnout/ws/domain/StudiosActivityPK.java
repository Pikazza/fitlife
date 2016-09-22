package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the STUDIOS_ACTIVITY database table.
 * 
 */
@Embeddable
public class StudiosActivityPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STA_ID")
	private int staId;

	@Column(name="STD_ID", insertable=false, updatable=false)
	private int stdId;

	public StudiosActivityPK() {
	}
	public int getStaId() {
		return this.staId;
	}
	public void setStaId(int staId) {
		this.staId = staId;		
	}
	public int getStdId() {
		return this.stdId;
	}
	public void setStdId(int stdId) {
		this.stdId = stdId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof StudiosActivityPK)) {
			return false;
		}
		StudiosActivityPK castOther = (StudiosActivityPK)other;
		return 
			(this.staId == castOther.staId)
			&& (this.stdId == castOther.stdId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.staId;
		hash = hash * prime + this.stdId;
		
		return hash;
	}
}