package com.turnout.ws.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.turnout.ws.domain.Info;
import com.turnout.ws.domain.Party;
import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.domain.PartyNotification;
import com.turnout.ws.exception.PartyExistException;
import com.turnout.ws.exception.PartyNotFoundException;
import com.turnout.ws.exception.WrongAccessTokenException;
import com.turnout.ws.exception.WrongVerifyCodeException;
import com.turnout.ws.helper.SmtpMailSender;
import com.turnout.ws.repository.InfoRepository;
import com.turnout.ws.repository.PartyAuthMechRepository;
import com.turnout.ws.repository.PartyNotificationRepository;
import com.turnout.ws.repository.PartyRepository;

/**
 * This is a testing class for PartyAuthMechServiceImpl. So here, we mock that class to write test cases and validate generated output are same as expected for given input.
 * 
 * All methods are mocking PartyAuthMechServiceImpl methods and it does not return any value.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyAuthMechServiceImplTest {
	
	@Mock
	private PartyRepository partyRepository;
	
	@Mock
	private PartyAuthMechRepository partyAuthMechRepository;
	
	@Mock
	private SmtpMailSender smtpMailHelper;
	
	@Mock
	private PartyNotificationRepository partyNotificationRepository;	
	
	@Mock
	private InfoRepository infoRepository;		

	private PartyAuthMechServiceImpl partyAuthMechServiceImpl;
	
	/**
	 * This method to be run before the Test method. So several tests need similar objects created before they can run.
	 * 
	 * @throws MockitoException It throws when runtime errors happens while testing.
	 */
	@Before
	public void setUp() throws MockitoException {
		partyAuthMechServiceImpl = new PartyAuthMechServiceImpl(partyRepository, partyAuthMechRepository,smtpMailHelper, partyNotificationRepository,infoRepository);
	}	
	
	/**
	 * This test case will test resendCode method.
	 * This method will test resending verification code to the user who requested it.
	 * 
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldResendCode() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		PartyAuthMech ptyAuthObj = new PartyAuthMech();
		ptyAuthObj.setPamVerifycodeExpiry(dateFormat.parse("20/06/2016"));
		ptyAuthObj.setPamVerifyCode("26318");
		ptyAuthObj.setPamAuthId("uthirapathi@raisingibrows.com");
		ptyAuthObj.setPtyId(11);
		ptyAuthObj.setAmhId("SL");
		ptyAuthObj.setPamAuthToken("123456789");
	
		when(partyAuthMechRepository.findByPamAuthId("uthirapathi@raisingibrows.com")).thenReturn(ptyAuthObj);
		when(partyAuthMechRepository.saveAndFlush(ptyAuthObj)).thenReturn(ptyAuthObj);

		String res = partyAuthMechServiceImpl.resendCode("Uthirapathi","Devendran","SL","uthirapathi@raisingibrows.com","123456789");

		System.out.println("Resend "+res);
		assertNotNull(res);
	}
	/**
	 * This test case will test signUpUser method.
	 * This method will test a new users can sign up fiternity application. The users information provided by them will be store into database and send verification code to the respective user.
	 * 
	 * @throws MessagingException It throws when the message content is empty.
	 * @throws PartyExistException It throws when entered party is already exist in database
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 */
	@Test
	public void testShouldSignUpUser() throws MessagingException, PartyExistException, ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		
		Party pty = new Party();//mock(Party.class); //new Party();		
		pty.setPtyName("Uthirapathi");
		pty.setPtyLastName("Devanarayanan");
		pty.setPtyDeviceToken("175aeda0eaaa90bceff8fedd41da10c7ca411aaf9a451a8585a60633869d6e2b");
		pty.setPtyDeviceType("iPhone");
		pty.setPtyEmail("silaluthira@gmail.com");
		pty.setPtyGainedPoints(0);
		pty.setPtyDescription("This is sample description");
		pty.setPtyActivityPreference("PUBLIC");
		pty.setPtyShowupPreference("PUBLIC");
		pty.setPtyStatus("ACTIVE");
		pty.setPtyId(11);
		
		
		PartyAuthMech exParty = new PartyAuthMech(); //mock(PartyAuthMech.class); //new PartyAuthMech();
		exParty.setAmhId("SL");
		exParty.setPtyId(pty.getPtyId());
		
		exParty.setPamAuthId("silaluthira@gmail.com");
		exParty.setPamAuthToken("123456789");
		exParty.setPamVerified("N");
		exParty.setPamVerifycodeExpiry(dateFormat.parse("20/06/2016"));
		exParty.setPamVerifyCode("26318");		
		
		PartyNotification pnObj = new PartyNotification();// mock(PartyNotification.class); //new PartyNotification();
		
		pnObj.setNotifyPtyId(pty.getPtyId());
		pnObj.setNotifyOthAcptChlng("Y");
		pnObj.setNotifyOthCmts("Y");
		pnObj.setNotifyOthIntrstEvnt("Y");
		pnObj.setNotifyOthLikes("Y");
		pnObj.setNotifyPersonalEvntRemainder("Y");
		pnObj.setNotifyPersonalPointsCrdt("Y");
		pnObj.setNotifyPersonalReadyReedem("Y");
		
		Info info = mock(Info.class); //new Info();
		info.setDescription("Email already exist");
		info.setTitle("Email Exist");
		
		
	
		when(partyAuthMechRepository.findByPamAuthId("uthira@gmail.com")).thenReturn(exParty);
		when(partyNotificationRepository.saveAndFlush(pnObj)).thenReturn(pnObj);
		when(infoRepository.findByType("email-adrs-exist")).thenReturn(info);
		when(partyAuthMechRepository.saveAndFlush(exParty)).thenReturn(exParty);
		when(partyRepository.saveAndFlush(pty)).thenReturn(pty);
		
		when(smtpMailHelper.gen(1000)).thenReturn(1000);;
		//when(partyAuthMechServiceImpl.verifyCodeGenerator()).thenCallRealMethod();
		

		String res = partyAuthMechServiceImpl.signUpUser("Uthira","Devendran" ,"SL", "silaluthira@gmail.com", "123456789", "175aeda0eaaa90bceff8fedd41da10c7ca411aaf9a451a8585a60633869d6e2b","iPhone");

		System.out.println("Sign up "+res);
		assertNotNull(res);
	}
	/**
	 * This test case will test verifyEmail method.
	 * This method will test that authentication of user by checking the verification code sent to their mail is equal to entered one.
	 * 
	 * @throws WrongVerifyCodeException It throws when temporary verification code does not match with user's entered code.
	 * @throws PartyNotFoundException It throws when entered party is not found.
	 */
	@Test
	public void testShouldVerifyEmail() throws WrongVerifyCodeException, PartyNotFoundException {
		PartyAuthMech ptyAuth = new PartyAuthMech(); //mock(PartyAuthMech.class);
		ptyAuth.setPamVerifyCode("1000");
		ptyAuth.setPamVerified("Y");
		ptyAuth.setPtyId(10);
		ptyAuth.setPamAuthId("silaluthira@gmail.com");
		
		Info infoMsg = new Info(); //mock(Info.class);
		infoMsg.setDescription("Code Mismatch");
		infoMsg.setTitle("Code Incorrect");
		
		Info infoMsg1 = new Info(); //Mockito.mock(Info.class);
		infoMsg1.setDescription("PARTY NOT FOUND");
		infoMsg1.setTitle("PARTY NOT EXISTS");
		
		when(infoRepository.findByType("party-not-found")).thenReturn(infoMsg1);
		when(infoRepository.findByType("code-mismatch")).thenReturn(infoMsg);
		when(partyAuthMechRepository.saveAndFlush(ptyAuth)).thenReturn(ptyAuth);
		when(partyAuthMechRepository.findByPamAuthId("silaluthira@gmail.com")).thenReturn(ptyAuth);
		String res = partyAuthMechServiceImpl.verifyEmail("silaluthira@gmail.com", "1000");
		System.out.println("VERIFY EMAIL "+res);
		assertNotNull(res);
		
	}
	/**
	 * This test case will test signIn method.
	 * The user need to enter their account credential to sign in. They can sign in when already signed up with fiternity application.
	 */
	@Test
	public void testShouldSignIn() {
		PartyAuthMech ptyAuth = new PartyAuthMech(); //mock(PartyAuthMech.class);
		ptyAuth.setPamAuthToken("123456789");
		ptyAuth.setPtyId(10);
		ptyAuth.setPamAuthId("silaluthira@gmail.com");
		ptyAuth.setAmhId("SL");
		
		Party pty = new Party(); //mock(Party.class);
		pty.setPtyDeviceToken("asdasd342342azsdasd");
		pty.setPtyDeviceType("iPhone");
		pty.setPtyStatus("ACTIVE");
		
		when(partyRepository.saveAndFlush(pty)).thenReturn(pty);
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(partyAuthMechRepository.findByPamAuthId("silaluthira@gmail.com")).thenReturn(ptyAuth);
		String res = partyAuthMechServiceImpl.signIn("SL", "silaluthira@gmail.com", "123456789", "asdasd342342azsdasd", "iPhone");
		System.out.println("SIGN IN "+res);
		assertNotNull(res);
	}
	/**
	 * This test case will test forgetPwd method.
	 * This method will test when a user can change their password when the forgot and use them as new login credentials.
	 * 
	 */
	@Test
	public void testShouldForgetPwd() {
		PartyAuthMech ptyAuth = new PartyAuthMech(); //mock(PartyAuthMech.class);
		ptyAuth.setPamAuthToken("123456789");
		ptyAuth.setPtyId(10);
		ptyAuth.setPamAuthId("silaluthira@gmail.com");
		ptyAuth.setAmhId("SL");
		
		Party pty = new Party(); //mock(Party.class);
		pty.setPtyStatus("ACTIVE");
		pty.setPtyName("UTHIRAPATHI");
		
		when(partyRepository.saveAndFlush(pty)).thenReturn(pty);
		when(partyRepository.findOne(10)).thenReturn(pty);
		when(partyAuthMechRepository.findByPamAuthId("silaluthira@gmail.com")).thenReturn(ptyAuth);
		String res = partyAuthMechServiceImpl.forgetPwd("SL", "silaluthira@gmail.com");
		System.out.println("FORGOT PASSWORD "+res);
		assertNotNull(res);		
	}
	/**
	 * This test case will test resetPwd method.
	 * This method will test weather a user can update new password for their account.
	 * 
	 */
	
	@Test
	public void testShouldResetPwd() {
		PartyAuthMech ptyAuth = new PartyAuthMech(); //mock(PartyAuthMech.class);
		ptyAuth.setPamAuthToken("123456789");
		ptyAuth.setPtyId(10);
		ptyAuth.setPamAuthId("silaluthira@gmail.com");
		ptyAuth.setAmhId("SL");
		
		when(partyAuthMechRepository.saveAndFlush(ptyAuth)).thenReturn(ptyAuth);
		when(partyAuthMechRepository.findByPamAuthId("silaluthira@gmail.com")).thenReturn(ptyAuth);
		String res = partyAuthMechServiceImpl.resetPwd("SL", "silaluthira@gmail.com", "123456789", "12345");
		System.out.println("RESET PASSWORD "+res);
		assertNotNull(res);			
		
	}
	/**
	 * This test case will test resetPwd method.
	 * This method will test weather a user can sign up through instagram. Instagram user details will be stored into database and return it's id. 
	 * 
	 * @throws ParseException An exception occurs when you fail to parse a Object.
	 * @throws UnirestException It throws when the http connection fails while getting instagram access token.
	 * @throws WrongAccessTokenException It throws when entered access token of instagram is wrong or not a valid one.
	 */

	@Test
	public void testShouldSignupWithInstagram() throws ParseException, UnirestException, WrongAccessTokenException {
		
		String res = partyAuthMechServiceImpl.signupWithInstagram("IN", "asdf123asda", "sdsfsd343sdfsf45345", "iPhone");
		System.out.println("SIGN UP WITH INSTAGRAM "+res);
		assertNotNull(res);
	}

}


