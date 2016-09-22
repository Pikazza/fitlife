package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the REWARDS database table.
 * 
 */
@Entity
@Table(name="REWARDS")
@NamedQuery(name="Reward.findAll", query="SELECT r FROM Reward r")
public class Reward implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="RWD_ID")
	private int rwdId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RWD_CREATED_DATE")
	private Date rwdCreatedDate;

	@Column(name="RWD_DESCRIPTION")
	private String rwdDescription;

	@Temporal(TemporalType.DATE)
	@Column(name="RWD_EXPIRY_DATE")
	private Date rwdExpiryDate;

	@Column(name="RWD_IMG_URL")
	private String rwdImgUrl;

	@Temporal(TemporalType.DATE)
	@Column(name="RWD_MODIFIED_DATE")
	private Date rwdModifiedDate;

	@Column(name="RWD_NAME")
	private String rwdName;

	@Column(name="RWD_POINTS")
	private int rwdPoints;

	//bi-directional many-to-one association to Studio
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RWD_STD_ID")
	private Studio studio;

	//bi-directional many-to-many association to Voucher
	@ManyToMany(mappedBy="rewards",fetch=FetchType.LAZY)
	private List<Voucher> vouchers;

	//bi-directional one-to-one association to RewardsHasVoucher
	/*@OneToOne
	@JoinColumn(name="RWD_ID", referencedColumnName="RWD_RWD_ID",insertable=false,updatable=false)
	private RewardsHasVoucher rewardsHasVoucher;*/
	
	@Column(name="RWD_STATUS")
	private String rwdStatus;

	public String getRwdStatus() {
		return rwdStatus;
	}

	public void setRwdStatus(String rwdStatus) {
		this.rwdStatus = rwdStatus;
	}

	public Reward() {
	}

	public int getRwdId() {
		return this.rwdId;
	}

	public void setRwdId(int rwdId) {
		this.rwdId = rwdId;
	}

	public Date getRwdCreatedDate() {
		return this.rwdCreatedDate;
	}

	public void setRwdCreatedDate(Date rwdCreatedDate) {
		this.rwdCreatedDate = rwdCreatedDate;
	}

	public String getRwdDescription() {
		return this.rwdDescription;
	}

	public void setRwdDescription(String rwdDescription) {
		this.rwdDescription = rwdDescription;
	}

	public Date getRwdExpiryDate() {
		return this.rwdExpiryDate;
	}

	public void setRwdExpiryDate(Date rwdExpiryDate) {
		this.rwdExpiryDate = rwdExpiryDate;
	}

	public String getRwdImgUrl() {
		return this.rwdImgUrl;
	}

	public void setRwdImgUrl(String rwdImgUrl) {
		this.rwdImgUrl = rwdImgUrl;
	}

	public Date getRwdModifiedDate() {
		return this.rwdModifiedDate;
	}

	public void setRwdModifiedDate(Date rwdModifiedDate) {
		this.rwdModifiedDate = rwdModifiedDate;
	}

	public String getRwdName() {
		return this.rwdName;
	}

	public void setRwdName(String rwdName) {
		this.rwdName = rwdName;
	}

	public int getRwdPoints() {
		return this.rwdPoints;
	}

	public void setRwdPoints(int rwdPoints) {
		this.rwdPoints = rwdPoints;
	}

	public Studio getStudio() {
		return this.studio;
	}

	public void setStudio(Studio studio) {
		this.studio = studio;
	}

	public List<Voucher> getVouchers() {
		return this.vouchers;
	}

	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}

	/*public RewardsHasVoucher getRewardsHasVoucher() {
		return this.rewardsHasVoucher;
	}

	public void setRewardsHasVoucher(RewardsHasVoucher rewardsHasVoucher) {
		this.rewardsHasVoucher = rewardsHasVoucher;
	}*/

}