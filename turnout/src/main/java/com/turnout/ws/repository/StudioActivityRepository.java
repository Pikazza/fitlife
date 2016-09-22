package com.turnout.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turnout.ws.domain.StudiosActivity;

/**
 * JPA specific extension of Repository for StudioActivityRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class StudioActivityRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
public interface StudioActivityRepository extends JpaRepository<StudiosActivity, Integer> {
	/**
	 *  Returns a studio activity object with their details based on studio id and its activity id.
	 * 
	 * @param stdId an unique studio id.
	 * @param staId an unique activity id.
	 * @return An instance of Beacon.
	 */
	public StudiosActivity findByStdIdAndStaId(int stdId,int staId);

	/**
	 * Returns a studio activity object with their details based on activity id.
	 * 
	 * @param sta_id an unique activity id.
	 * @return An instance of Beacon.
	 */ 
	public StudiosActivity findByStaId(int sta_id);
	
	

}
