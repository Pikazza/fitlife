package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Voucher;

/**
 * JPA specific extension of Repository for VoucherRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class VoucherRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>{

	/**
	 * Returns a voucher object with their details.
	 *
	 * @param vchId  An unique id of voucher.
	 * @return An instance of voucher.
	 */
	public Voucher findByVocId(int vchId);
}
