package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.PartyAuthMech;


/**
 * JPA specific extension of Repository for PartyAuthMechRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class PartyAuthMechRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface PartyAuthMechRepository extends JpaRepository<PartyAuthMech, Integer> {

	/**
	 * Returns a PartyAuthMech object with their details.
	 * 
	 * @param partyAuthid a value of username of the party. 
	 * @return
	 */
	public PartyAuthMech findByPamAuthId(String partyAuthid);

	/**
	 * Returns a PartyAuthMech object with their details.
	 * 
	 * @param ptyid An unique id of party.
	 * @return An instance of PartyAuthMech.
	 */
    public PartyAuthMech findByPtyId(int ptyid);
	
	
	
	

}
