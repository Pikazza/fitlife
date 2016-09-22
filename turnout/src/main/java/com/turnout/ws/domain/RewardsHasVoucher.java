package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the REWARDS_HAS_VOUCHER database table.
 * 
 */
@Entity
@Table(name="REWARDS_HAS_VOUCHER")
@NamedQuery(name="RewardsHasVoucher.findAll", query="SELECT r FROM RewardsHasVoucher r")
@IdClass(RewardsHasVoucherPK.class)
public class RewardsHasVoucher implements Serializable {
	private static final long serialVersionUID = 1L;

	/*@EmbeddedId
	private RewardsHasVoucherPK id;*/
	@Id
	@Column(name="RWD_RWD_ID", insertable=false, updatable=false)
	private int rwdRwdId;
	
	@Id
	@Column(name="VOC_VOC_ID", insertable=false, updatable=false)
	private int vocVocId;

	//bi-directional one-to-one association to Reward
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="RWD_RWD_ID",insertable=false, updatable=false)
	private Reward reward;

	//bi-directional one-to-one association to Voucher
	@OneToOne
	@JoinColumn(name="VOC_VOC_ID",insertable=false, updatable=false)
	private Voucher voucher;

	public RewardsHasVoucher() {
	}

	/*public RewardsHasVoucherPK getId() {
		return this.id;
	}

	public void setId(RewardsHasVoucherPK id) {
		this.id = id;
	}*/

	public Reward getReward() {
		return this.reward;
	}

	public int getRwdRwdId() {
		return rwdRwdId;
	}

	public void setRwdRwdId(int rwdRwdId) {
		this.rwdRwdId = rwdRwdId;
	}

	public int getVocVocId() {
		return vocVocId;
	}

	public void setVocVocId(int vocVocId) {
		this.vocVocId = vocVocId;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public Voucher getVoucher() {
		return this.voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

}