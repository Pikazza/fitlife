package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.domain.Reward;
import com.turnout.ws.domain.RewardsHasVoucher;
import com.turnout.ws.domain.Studio;
import com.turnout.ws.domain.Voucher;
import com.turnout.ws.exception.CurrentPasswordNotMatchedException;
import com.turnout.ws.exception.NoSufficientGainedPointsException;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.exception.RewardNotFoundException;
import com.turnout.ws.exception.VoucherNotFoundException;
import com.turnout.ws.helper.InstagramPostReceiver;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.CustomPartyRepository;
import com.turnout.ws.repository.CustomRewardRepository;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;
import com.turnout.ws.repository.RewardRepository;
import com.turnout.ws.repository.RewardsHasVoucherRepository;
import com.turnout.ws.repository.StudioRepository;
import com.turnout.ws.repository.VoucherPartyRepository;
import com.turnout.ws.repository.VoucherRepository;

@RunWith(MockitoJUnitRunner.class)
public class RewardHasVoucherServiceImplTest {
	
	@Mock
	private VoucherRepository voucherRepository;
	
	@Mock
	private RewardsHasVoucherRepository rewardsHasVoucherRepository;
	
	@Mock
	private RewardRepository rewardRepository;
	
	private RewardsHasVoucherServiceImpl rewardsHasVoucherServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		rewardsHasVoucherServiceImpl = new RewardsHasVoucherServiceImpl(rewardsHasVoucherRepository,rewardRepository,voucherRepository);
	}
	
	/**
	 * This test case will test addRewardVoucher method.
	 * It should return new rewardvoucher id of recently added rewardvoucher from database when all of its valid details has been passed as parameter.
	 *
	 * @throws RewardNotFoundException It throws when entered reward is not found in database
	 * @throws VoucherNotFoundException It throws when entered voucher is not found in database.
	 */
	@Test
	public void testShouldaddRewardVoucher() throws RewardNotFoundException, VoucherNotFoundException  {
		Reward rwd = new Reward(); //mock(Reward.class);
		rwd.setRwdId(10);
		
		Voucher voc = new Voucher(); //mock(Voucher.class);
		voc.setVocId(5);
		
		RewardsHasVoucher rv = new RewardsHasVoucher(); //mock(RewardsHasVoucher.class);
		rv.setRwdRwdId(10);
		rv.setVocVocId(5);
		
		when(rewardRepository.findOne(10)).thenReturn(rwd);
		when(voucherRepository.findByVocId(5)).thenReturn(voc);
		when(rewardsHasVoucherRepository.findByVocVocId(5)).thenReturn(rv);
		JSONObject res = rewardsHasVoucherServiceImpl.addRewardVoucher(10, 5);
		System.out.println("Add Reward Has Voucher "+res);
		assertNotNull(res);
		
	}

}