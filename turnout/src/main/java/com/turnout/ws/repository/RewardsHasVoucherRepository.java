package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.RewardsHasVoucher;

/**
 * JPA specific extension of Repository for RewardsHasVoucherRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class RewardsHasVoucherRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 * @see RewardsHasVoucher
 */
@Repository
public interface RewardsHasVoucherRepository extends JpaRepository<RewardsHasVoucher, Integer> {

	/**
	 * Returns a RewardsHasVoucher object with their details.
	 * 
	 * @param vocid An unique id of voucher entity.
	 * @return An instance of RewardsHasVoucher.
	 */
	public RewardsHasVoucher findByVocVocId(int vocid);
}
