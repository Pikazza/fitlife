package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Friends;
import com.turnout.ws.domain.PartyNotification;


/**
 * JPA specific extension of Repository for PartyNotificationRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class PartyNotificationRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 * @see PartyNotification
 */
@Repository
public interface PartyNotificationRepository extends JpaRepository<PartyNotification, Integer>{

	/**
	 * Returns a PartyNotification object with their details.
	 * 
	 * @param ptyId An unique id of party.
	 * @return An instance of PartyNotification.
	 */
	public PartyNotification findByNotifyPtyId(int ptyId);
}