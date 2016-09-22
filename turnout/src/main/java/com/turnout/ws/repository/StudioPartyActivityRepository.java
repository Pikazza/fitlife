package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.StudioPartyActivity;
import com.turnout.ws.domain.StudiosActivity;

/**
 * JPA specific extension of Repository for StudioPartyActivityRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class StudioPartyActivityRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface StudioPartyActivityRepository extends JpaRepository<StudioPartyActivity, Integer> {

	
}
