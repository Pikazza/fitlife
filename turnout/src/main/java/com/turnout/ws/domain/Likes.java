package com.turnout.ws.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
 * The persistent class for the LIKES database table.
 * 
 */
@Entity
@Table(name="LIKES")
@NamedQuery(name="Likes.findAll", query="SELECT l FROM Likes l")
public class Likes implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LIKE_ID")
	private int likeId;	

	@Column(name="LIKE_PTY_ID")
	private int likePtyId;
	
	@Column(name="LIKE_TYPE")
	private String likeType;
	
	@Column(name="LIKE_TYPE_ID")
	private int likeTypeId;
	
	@Column(name="LIKE_YN")
	private String likeYn;
	
	public int getLikeId() {
		return likeId;
	}

	public void setLikeId(int likeId) {
		this.likeId = likeId;
	}

	public int getLikePtyId() {
		return likePtyId;
	}

	public void setLikePtyId(int likePtyId) {
		this.likePtyId = likePtyId;
	}

	public int getLikeTypeId() {
		return likeTypeId;
	}

	public String getLikeType() {
		return likeType;
	}

	public void setLikeType(String likeType) {
		this.likeType = likeType;
	}

	public String getLikeYn() {
		return likeYn;
	}

	public void setLikeYn(String likeYn) {
		this.likeYn = likeYn;
	}

	public void setLikeTypeId(int likeTypeId) {
		this.likeTypeId = likeTypeId;
	}

	
}
