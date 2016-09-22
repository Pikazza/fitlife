package com.turnout.ws.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.simple.JSONObject;

import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import com.turnout.ws.domain.Voucher;
import com.turnout.ws.repository.VoucherRepository;

/**
 * This is a testing class for VoucherServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking VoucherServiceImpl methods and it does not return any value.
 * 
 * 
 */

@RunWith(MockitoJUnitRunner.class)
public class VoucherServiceImplTest {
	
	@Mock
	private  VoucherRepository voucherRepository;
	
	private VoucherServiceImpl voucherServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException{
		voucherServiceImpl = new VoucherServiceImpl(voucherRepository);
	}
	/**
	 * This test case should test addVoucher method.
	 * It will return last inserted voucher id. 
	 */
	@Test
	public void testShouldAddVoucher()
	{
		Voucher voc = mock(Voucher.class);
		voc.setVocCode("test123");
		voc.setVocDiscPercentage(0);
		voc.setVocStatus("ACTIVE");
		
		when(voucherRepository.save(any(Voucher.class))).thenReturn(voc);
		JSONObject res = voucherServiceImpl.addVoucher("test123", 0, "ACTIVE");
		System.out.println(res);
	}

}
