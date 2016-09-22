package com.turnout.ws.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the FRIENDS database table.
 * 
 */
@Embeddable
public class FriendsPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="FRND_ID1", insertable=false, updatable=false)
	private int frndId1;

	@Column(name="FRND_ID2", insertable=false, updatable=false)
	private int frndId2;

	
	public int getFrndId1() {
		return frndId1;
	}


	public void setFrndId1(int frndId1) {
		this.frndId1 = frndId1;
	}


	public int getFrndId2() {
		return frndId2;
	}


	public void setFrndId2(int frndId2) {
		this.frndId2 = frndId2;
	}


	public FriendsPK() {
		// TODO Auto-generated constructor stub
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FriendsPK)) {
			return false;
		}
		FriendsPK castOther = (FriendsPK)other;
		return 
			(this.frndId1 == castOther.frndId1)
			&& (this.frndId2 == castOther.frndId2);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.frndId1;
		hash = hash * prime + this.frndId2;
		
		return hash;
	}

}
