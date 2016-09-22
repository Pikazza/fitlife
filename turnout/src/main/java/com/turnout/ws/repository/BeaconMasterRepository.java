package com.turnout.ws.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turnout.ws.domain.BeaconMaster;

/**
 * JPA specific extension of Repository for BeaconMaster. So it can encapsulating storage, retrieval, and search behavior which emulates a collection of objects.
 * 
 * In this class BeaconMaster is a domain type the repository manages and String is the type of the id of domain the repository manages.
 * 
 * Domain repositories extending this interface can automatically expose CRUD methods.
 * 
 * @see JpaRepository
 */
@Repository
public interface BeaconMasterRepository extends JpaRepository<BeaconMaster, String> {
	/**
	 * It provides list of beacons based on given beacon id with descending order.
	 * 
	 * @param bconid An id of beacon a studio owned for recording check in and check out.
	 * @return It returns List of beacons.
	 */
	public List<BeaconMaster> findByBconIdOrderByBconDetectTypeDesc(String bconid);

	/**
	 * It provides list of beacons based on given beacon id with descending order.
	 * 
	 * @param bconId An id of beacon a studio owned for recording check in and check out.
	 * @param sort An object which specify the sorting order.
	 * @return It returns List of beacons.
	 */
	public List<BeaconMaster> findByBconId(String bconId, Sort sort);
	
	/**
	 * Returns a beacon object with their details.
	 * 
	 * @param bconId An id of beacon a studio owned for recording check in and check out.
	 * @param stdId  An unique id of studio.
	 * @return An instance of Beacon.
	 */
	public BeaconMaster findByBconIdAndBconStdId(String bconId, int stdId );
	
	/**
	 * Returns a beacon object.
	 * 
	 * @param bconId n id of beacon a studio owned for recording check in and check out.
	 * @param sort An object which specify the sorting order.
	 * @return It instance beacon.
	 */
	public BeaconMaster findByBconId(String bconId);	

}
