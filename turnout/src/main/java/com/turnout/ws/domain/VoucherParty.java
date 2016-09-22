package com.turnout.ws.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the VOUCHER_PARTY database table.
 * 
 */
@Entity
@Table(name="VOUCHER_PARTY")
@NamedQuery(name="VoucherParty.findAll", query="SELECT v FROM VoucherParty v")
@IdClass(VoucherPartyPK.class)
public class VoucherParty implements Serializable {	

	private static final long serialVersionUID = 1L;

	/*@EmbeddedId
	private VoucherPartyPK id;*/
	@Id
	@Column(name="VOC_ID", insertable=false, updatable=false)
	private int vocId;

	@Id
	@Column(name="PTY_ID", insertable=false, updatable=false)
	private int ptyId;

	@Column(name="VOC_REEDEMED_POINTS")
	private int vocReedemedPoints;

	@Column(name="VOC_REEDEMED_STATUS")
	private String vocReedemedStatus;

	@Column(name="VOC_STATUS")
	private String vocStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="VOC_STATUS_DATE")
	private Date vocStatusDate;

	//bi-directional many-to-one association to Party
	@ManyToOne
	@JoinColumn(name="PTY_ID",insertable=false, updatable=false)
	private Party party;

	//bi-directional many-to-one association to Voucher
	@ManyToOne
	@JoinColumn(name="VOC_ID",insertable=false, updatable=false)
	private Voucher voucher;

	public VoucherParty() {
	}

	/*public VoucherPartyPK getId() {
		return this.id;
	}

	public void setId(VoucherPartyPK id) {
		this.id = id;
	}*/

	public int getVocReedemedPoints() {
		return this.vocReedemedPoints;
	}

	public void setVocReedemedPoints(int vocReedemedPoints) {
		this.vocReedemedPoints = vocReedemedPoints;
	}

	public String getVocReedemedStatus() {
		return this.vocReedemedStatus;
	}

	public void setVocReedemedStatus(String vocReedemedStatus) {
		this.vocReedemedStatus = vocReedemedStatus;
	}

	public String getVocStatus() {
		return this.vocStatus;
	}

	public void setVocStatus(String vocStatus) {
		this.vocStatus = vocStatus;
	}

	public Date getVocStatusDate() {
		return this.vocStatusDate;
	}

	public void setVocStatusDate(Date vocStatusDate) {
		this.vocStatusDate = vocStatusDate;
	}

	public Party getParty() {
		return this.party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Voucher getVoucher() {
		return this.voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}
	
	public int getVocId() {
		return vocId;
	}

	public void setVocId(int vocId) {
		this.vocId = vocId;
	}

	public int getPtyId() {
		return ptyId;
	}

	public void setPtyId(int ptyId) {
		this.ptyId = ptyId;
	}

}