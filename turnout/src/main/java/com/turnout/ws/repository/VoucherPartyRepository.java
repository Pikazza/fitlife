package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.VoucherParty;

/**
 * JPA specific extension of Repository for VoucherPartyRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class VoucherPartyRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface VoucherPartyRepository extends JpaRepository<VoucherParty, Integer>{
	
	

}
