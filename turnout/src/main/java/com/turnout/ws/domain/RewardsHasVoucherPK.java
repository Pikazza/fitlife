package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the REWARDS_HAS_VOUCHER database table.
 * 
 */
@Embeddable
public class RewardsHasVoucherPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
	
	@Column(name="RWD_RWD_ID", insertable=false, updatable=false)
	private int rwdRwdId;

	@Column(name="VOC_VOC_ID", insertable=false, updatable=false)
	private int vocVocId;

	public RewardsHasVoucherPK() {
	}
	public int getRwdRwdId() {
		return this.rwdRwdId;
	}
	public void setRwdRwdId(int rwdRwdId) {
		this.rwdRwdId = rwdRwdId;
	}
	public int getVocVocId() {
		return this.vocVocId;
	}
	public void setVocVocId(int vocVocId) {
		this.vocVocId = vocVocId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RewardsHasVoucherPK)) {
			return false;
		}
		RewardsHasVoucherPK castOther = (RewardsHasVoucherPK)other;
		return 
			(this.rwdRwdId == castOther.rwdRwdId)
			&& (this.vocVocId == castOther.vocVocId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.rwdRwdId;
		hash = hash * prime + this.vocVocId;
		
		return hash;
	}
}