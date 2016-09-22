package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the VOUCHER_PARTY database table.
 * 
 */
@Embeddable
public class VoucherPartyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="VOC_ID", insertable=false, updatable=false)
	private int vocId;

	@Column(name="PTY_ID", insertable=false, updatable=false)
	private int ptyId;

	public VoucherPartyPK() {
	}
	public int getVocId() {
		return this.vocId;
	}
	public void setVocId(int vocId) {
		this.vocId = vocId;
	}
	public int getPtyId() {
		return this.ptyId;
	}
	public void setPtyId(int ptyId) {
		this.ptyId = ptyId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VoucherPartyPK)) {
			return false;
		}
		VoucherPartyPK castOther = (VoucherPartyPK)other;
		return 
			(this.vocId == castOther.vocId)
			&& (this.ptyId == castOther.ptyId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.vocId;
		hash = hash * prime + this.ptyId;
		
		return hash;
	}
}