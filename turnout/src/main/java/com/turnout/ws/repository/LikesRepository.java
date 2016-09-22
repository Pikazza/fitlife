package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Likes;

/**
 * JPA specific extension of Repository for LikesRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class LikesRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer> {	

	/**
	 * Returns a like object with their details.
	 * 
	 * @param liketypeid An unique id of like.
	 * @param liketype it may be challenge or event
	 * @return It returns List of likes.
	 */
	public Likes findByLikeTypeIdAndLikeType(int liketypeid,String liketype);
		
	
}
