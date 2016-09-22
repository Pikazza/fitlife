package com.turnout.ws.repository;

import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.Comments;
/**
 * JPA specific extension of Repository for CommentsRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class CommentsRepository is a domain type the repository manages and Integer is the type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer>{

}