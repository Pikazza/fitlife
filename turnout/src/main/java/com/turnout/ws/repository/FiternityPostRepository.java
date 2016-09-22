package com.turnout.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turnout.ws.domain.FiternityPost;

/**
 * JPA specific extension of Repository for FiternityPostRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class FiternityPostRepository is a domain type the repository manages and Integer is the type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
public interface FiternityPostRepository extends JpaRepository<FiternityPost, Integer>{

}
