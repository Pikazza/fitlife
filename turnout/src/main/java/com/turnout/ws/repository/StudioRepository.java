package com.turnout.ws.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.turnout.ws.domain.Studio;

/**
 * JPA specific extension of Repository for StudioRepository. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class StudioRepository is a domain type the repository manages and Integer is type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface StudioRepository extends JpaRepository<Studio, Integer> {
	
	/**
	 * It provides list of studios based on sorting order.
	 * 
	 * @param s An object which specify the sorting order.
	 * @return It returns List of studios
	 */
	public List<Studio> findAll(Sort s);
	
	/**
	 * It provides list of studios based on given status with given order.
	 * 
	 * @param status It can have value of active or inactive.
	 * @param s An object which specify the sorting order.
	 * @return It returns List of studios.
	 */
	public List<Studio> findByStdStatus(String status, Sort s);

	/**
	 * It provides result as a list of studios while we search by given string in database.
	 * 
	 * @param searchText A string that has to be searched in table.
	 * @return It returns List of studios.
	 */ 
	@Query("SELECT std FROM Studio std "
            + "WHERE lower(std.stdName) like lower(:searchText) "
            + "OR lower(std.stdPhoneNo) like lower(:searchText)"
            + "OR lower(std.stdMailId) like lower(:searchText)")
	public List<Studio> searchByText(@Param("searchText") String searchText);	
	
	/**
	 * It provides list of studios based on studio type.
	 * 
	 * @param stdType it can be used to specify promotional or normal studio.
	 * @return It returns List of studios.
	 */
	public List<Studio> findByStdType(String stdType);

	
}
