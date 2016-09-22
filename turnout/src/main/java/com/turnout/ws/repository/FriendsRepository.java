package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turnout.ws.domain.Friends;
/**
 * JPA specific extension of Repository for CommentsRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class CommentsRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
public interface FriendsRepository extends JpaRepository<Friends, Integer> {

	/**
	 * Returns a friends object with their details.
	 * 
	 * @param frndid1 an id of an party.
	 * @param frndid2 an id of an party
	 * @return An instance of friends.
	 */
	public Friends findByFrndId1AndFrndId2(int frndid1,int frndid2);
	
	/**
	 * Returns a friends object with their details.
	 * 
	 * @param frndid2 an id of an party
	 * @param frndid1 an id of an party
	 * @return An instance of friends.
	 */
	public Friends findByFrndId2AndFrndId1(int frndid2,int frndid1);
}
