package com.turnout.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Offers;

/**
 * JPA specific extension of Repository for OffersRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class OffersRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 * 
 */
@Repository
public interface OffersRepository extends JpaRepository<Offers, Integer> {	
	
	/**
	 * It provides list of offers based on given studio id with offer status.
	 * 
	 * @param stdId An unique id of studio.
	 * @param status  a status of an offer which mapped to studio
	 * @return It returns List of Offers.
	 */
	public List<Offers> findByOfrStdIdAndOfrStatus(int stdId, String status);
}