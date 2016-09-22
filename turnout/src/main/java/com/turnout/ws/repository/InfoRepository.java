package com.turnout.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turnout.ws.domain.Info;

/**
 * JPA specific extension of Repository for InfoRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class InfoRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
public interface InfoRepository extends JpaRepository<Info, Integer> {
	
	/**
	 * Returns a info object with their details.
	 * 
	 * @param type type of notification.
	 * @return An instance of info.
	 */
	public Info findByType(String type);
	
	/**
	 * It provides list of info with ascending order.
	 * 
	 * @return A list contains all info entity.
	 */
	public List<Info> findAllByOrderByIdAsc();

}