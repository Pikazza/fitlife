package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import com.turnout.ws.domain.BeaconMaster;
import com.turnout.ws.repository.BeaconMasterRepository;
import com.turnout.ws.repository.CustomBeaconMasterRepository;



/**
 * This is a testing class for BeaconMasterServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking BeaconMasterServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BeaconMasterServiceImplTest {
	
	@Mock
	private BeaconMasterRepository beaconMasterRepository;
	@Mock
	private CustomBeaconMasterRepository customBeaconMasterRepository;
	
	private BeaconMasterServiceImpl beaconMasterServiceImpl;

	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		beaconMasterServiceImpl = new BeaconMasterServiceImpl(beaconMasterRepository, customBeaconMasterRepository);
	}
	
	/**
	 * This test case should test beaconlist method.
	 * It should return list of beacons from database where the parameter may have the value 'ALL' to get all beacons or it can have a string that has to be searched to get the hit result.
	 * 
	 */
	@Test
	public void testShouldBeaconsList() {
		List beaconsAray = new ArrayList();
		Map<String, Object> beaconList,beaconList1;
		beaconList = new HashMap<String,Object>();
		beaconList.put("BCON_STA_TYPE_ID", "2");
		beaconList.put("STD_NAME", "Core Collective");
		beaconList.put("STA_TYPE", "CHALLENGE");
		beaconList.put("BCON_ID", "A00027D0");
		beaconList.put("BCON_SITE", "CORECOLECTIVE");
		beaconList.put("BCON_STATUS", "ACTIVE");
		beaconList.put("BCON_DETECT_TYPE", "OWN");
		beaconList.put("BCON_STD_ID", "3");
		
		beaconsAray.add(beaconList);
		
		beaconList1 = new HashMap<String,Object>();
		
		beaconList1.put("BCON_STA_TYPE_ID", "1");
		beaconList1.put("STD_NAME", "Welltodo");
		beaconList.put("BCON_SITE", "WELLTODO");
		beaconList1.put("STA_TYPE", "EVENTS");
		beaconList1.put("BCON_ID", "A00027D4");
		beaconList1.put("BCON_STATUS", "INACTIVE");
		beaconList1.put("BCON_DETECT_TYPE", "FITERNITY");
		beaconList1.put("BCON_STD_ID", "5");
		
		beaconsAray.add(beaconList1);
		
		when(customBeaconMasterRepository.getAllBeacons("ALL")).thenReturn(beaconsAray);
		JSONArray clst = beaconMasterServiceImpl.beaconsList("ALL");
		System.out.println("Beacon List "+clst);
		assertNotNull(clst);
	}
	
	/**
	 * This test case will test saveBeacon method.
	 * It should get updated or added beacon id from database when all of valid beacon's details has been passed.
	 */
	@Test
	public void testShouldSaveBeacon() {
		BeaconMaster bcon = mock(BeaconMaster.class);
		bcon.setBconId("BC413002");
		bcon.setBconSite("CORECOLECTIVE");
		bcon.setBconStaTypeId(2);
		bcon.setBconStdId(4);
		bcon.setBconDetectType("own");
		bcon.setBconStatus("active");
		
		when(beaconMasterRepository.save(any(BeaconMaster.class))).thenReturn(bcon);
		JSONObject res = beaconMasterServiceImpl.saveBeacon("BC413002","CORECOLECTIVE", 2, 4, "own","active");
		System.out.println("Save Beacon "+res);
		assertNotNull(res);
	}
	/**
	 * This test case should test beaconinfo method.
	 * It should get all the details of beacon based on given beacon id.
	 */
	
	@Test
	public void testShouldBeaconInfo() {
		BeaconMaster bcon = mock(BeaconMaster.class);
		bcon.setBconId("BC413002");
		bcon.setBconSite("CORECOLECTIVE");
		bcon.setBconStaTypeId(2);
		bcon.setBconStdId(4);
		bcon.setBconDetectType("own");
		bcon.setBconStatus("active");
		
		when(beaconMasterRepository.findByBconIdAndBconStdId("BC413002", 2)).thenReturn(bcon);
		JSONObject res = beaconMasterServiceImpl.beaconInfo("BC413002", 2);
		System.out.println("Beacon Info "+res);
		assertNotNull(res);
	}	

}
