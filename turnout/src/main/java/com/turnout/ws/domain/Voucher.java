package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the VOUCHER database table.
 * 
 */
@Entity
@Table(name="VOUCHER")
@NamedQuery(name="Voucher.findAll", query="SELECT v FROM Voucher v")
public class Voucher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="VOC_ID")
	private int vocId;

	@Column(name="VOC_CODE")
	private String vocCode;

	@Column(name="VOC_DISC_PERCENTAGE")
	private int vocDiscPercentage;

	@Column(name="VOC_STATUS")
	private String vocStatus;

	//bi-directional many-to-many association to Reward
	@ManyToMany
	@JoinTable(
		name="REWARDS_HAS_VOUCHER"
		, joinColumns={
			@JoinColumn(name="VOC_VOC_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="RWD_RWD_ID")
			}
		)
	private List<Reward> rewards;

	//bi-directional many-to-one association to VoucherParty
	@OneToMany(mappedBy="voucher")
	private List<VoucherParty> voucherParties;

	//bi-directional one-to-one association to RewardsHasVoucher
	@OneToOne(mappedBy="voucher")
	private RewardsHasVoucher rewardsHasVoucher;

	public Voucher() {
	}

	public int getVocId() {
		return this.vocId;
	}

	public void setVocId(int vocId) {
		this.vocId = vocId;
	}

	public String getVocCode() {
		return this.vocCode;
	}

	public void setVocCode(String vocCode) {
		this.vocCode = vocCode;
	}

	public int getVocDiscPercentage() {
		return this.vocDiscPercentage;
	}

	public void setVocDiscPercentage(int vocDiscPercentage) {
		this.vocDiscPercentage = vocDiscPercentage;
	}

	public String getVocStatus() {
		return this.vocStatus;
	}

	public void setVocStatus(String vocStatus) {
		this.vocStatus = vocStatus;
	}

	public List<Reward> getRewards() {
		return this.rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

	public List<VoucherParty> getVoucherParties() {
		return this.voucherParties;
	}

	public void setVoucherParties(List<VoucherParty> voucherParties) {
		this.voucherParties = voucherParties;
	}

	public VoucherParty addVoucherParty(VoucherParty voucherParty) {
		getVoucherParties().add(voucherParty);
		voucherParty.setVoucher(this);

		return voucherParty;
	}

	public VoucherParty removeVoucherParty(VoucherParty voucherParty) {
		getVoucherParties().remove(voucherParty);
		voucherParty.setVoucher(null);

		return voucherParty;
	}

	public RewardsHasVoucher getRewardsHasVoucher() {
		return this.rewardsHasVoucher;
	}

	public void setRewardsHasVoucher(RewardsHasVoucher rewardsHasVoucher) {
		this.rewardsHasVoucher = rewardsHasVoucher;
	}

}