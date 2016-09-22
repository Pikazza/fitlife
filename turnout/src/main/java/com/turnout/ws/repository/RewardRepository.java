package com.turnout.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Reward;

/**
 * JPA specific extension of Repository for RewardRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class RewardRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface RewardRepository extends JpaRepository<Reward, Integer> {
	
}
