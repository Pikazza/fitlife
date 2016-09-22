package com.turnout.ws.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the UI_CARDS database table.
 * 
 */
@Entity
@Table(name="UI_CARDS")
@NamedQuery(name="UiCards.findAll", query="SELECT u FROM UiCards u order by u.displayOrder")
public class UiCards {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="UIC_ID")
	private String uicId;

	@Column(name="CARD_GROUP_SIZE")
	private int cardGroupSize;

	@Column(name="DISPLAY_ORDER")
	private int displayOrder;

	@Column(name="HANDLER_CLASS")
	private String handlerClass;

	@Column(name="NAME")
	private String name;

	@Column(name="USER_SPECIFIC")
	private String userSpecific;
	
	public UiCards(){		
	}

	public String getUicId() {
		return uicId;
	}

	public void setUicId(String uicId) {
		this.uicId = uicId;
	}

	public int getCardGroupSize() {
		return cardGroupSize;
	}

	public void setCardGroupSize(int cardGroupSize) {
		this.cardGroupSize = cardGroupSize;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserSpecific() {
		return userSpecific;
	}

	public void setUserSpecific(String userSpecific) {
		this.userSpecific = userSpecific;
	}	

}
