package com.turnout.ws.domain;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the STUDIO_PARTY_ACTIVITY database table.
 * 
 */
@Entity
@Table(name="STUDIO_PARTY_ACTIVITY")
@NamedQuery(name="StudioPartyActivity.findAll", query="SELECT s FROM StudioPartyActivity s")
public class StudioPartyActivity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PTY_STA_ID")
	private int ptyStaId;
	
	@ManyToOne
	@JoinColumn(name="PTY_ID")
	private Party party;

	@Column(name="STA_ID")
	private int StaId;	

	@ManyToOne		
		@JoinColumn(name="STA_STD_ID", referencedColumnName="STD_ID")		
	private Studio studio;

	@Column(name="PTY_STA_STATUS")
	private String ptaStaStatus;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECKIN_TIME")
	private Date chechinTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECKOUT_TIME")
	private Date checkoutTime;

	@Column(name="GAINED_POINTS")
	private int gainedPoints;

	@Column(name="GAINED_TASK_BAGE")
	private String gainedTaskBadge;
	
	@Column(name="BCON_ID")
	private String bconId;
	
	@Column(name="BCON_RANGE")
	private double bconRange;

	public int getStaId() {
		return StaId;
	}

	public void setStaId(int staId) {
		StaId = staId;
	}

	public int getPtyStaId() {
		return ptyStaId;
	}

	public void setPtyStaId(int ptyStaId) {
		this.ptyStaId = ptyStaId;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public String getPtaStaStatus() {
		return ptaStaStatus;
	}

	public void setPtaStaStatus(String ptaStaStatus) {
		this.ptaStaStatus = ptaStaStatus;
	}	

	public Date getChechinTime() {
		return chechinTime;
	}

	public void setChechinTime(Date chechinTime) {
		this.chechinTime = chechinTime;
	}

	public Date getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(Date checkoutTime) {
		this.checkoutTime = checkoutTime;
	}


	public int getGainedPoints() {
		return gainedPoints;
	}


	public void setGainedPoints(int gainedPoints) {
		this.gainedPoints = gainedPoints;
	}
	
	public StudioPartyActivity() {
	}
	
	public String getGainedTaskBadge() {
		return gainedTaskBadge;
	}
	
	public void setGainedTaskBadge(String gainedTaskBadge) {
		this.gainedTaskBadge = gainedTaskBadge;
	}
	
	public Studio getStudio() {
		return studio;
	}
	
	public void setStudio(Studio studio) {
		this.studio = studio;
	}

	public String getBconId() {
		return bconId;
	}
	public void setBconId(String bconId) {
		this.bconId = bconId;
	}
	public double getBconRange() {
		return bconRange;
	}
	public void setBconRange(double bconRange) {
		this.bconRange = bconRange;
	}

}
