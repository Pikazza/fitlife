package com.turnout.ws.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value="classpath:propscontrol.properties")
public class PropsControl {
	
	@Value("${alert.emailnotregisteredTitle}")
	private String emailnotregisteredTitle;
	
	@Value("${alert.emailnotregisteredText}")
	private String emailnotregisteredText;
	
	@Value("${alert.passwordmismatchTitle}")
	private String passwordmismatchTitle;
	
	@Value("${alert.passwordmismatchText}")
	private String passwordmismatchText;
	
	@Value("${alert.emailaddressexistsTitle}")
	private String emailaddressexistsTitle;
	
	@Value("${alert.emailaddressexistsText}")
	private String emailaddressexistsText;
	
	@Value("${alert.verifycodemismathchTitle}")
	private String verifycodemismathchTitle;
	
	@Value("${alert.verifycodemismathchText}")
	private String verifycodemismathchText;
	
	@Value("${alert.passwordresetTitle}")
	private String passwordresetTitle;
	
	@Value("${alert.passwordresetText}")
	private String passwordresetText;
	
	@Value("${notify.nonotification}")
	private String nonotification;
	
	@Value("${notify.commentnotificationFirst}")
	private String commentnotificationFirst;
	
	@Value("${notify.commentnotificationAdjacent}")
	private String commentnotificationAdjacent;
	
	@Value("${notify.likenotificationFirst}")
	private String likenotificationFirst;
	
	@Value("${notify.likenotificationAdjacent}")
	private String likenotificationAdjacent;
	
	@Value("${notify.networkinterestactivityFirst}")
	private String networkinterestactivityFirst;
	
	@Value("${notify.networkinterestactivityAdjacent}")
	private String networkinterestactivityAdjacent;
	
	@Value("${notify.checkedIn}")
	private String checkedIn;
	
	@Value("${notify.pointscreditedforClass}")
	private String pointscreditedforClass;

	@Value("${notify.pointscreditedforActivity}")
	private String pointscreditedforActivity;
	
	@Value("${notify.eventRemainder}")
	private String eventRemainder;
	
	@Value("${notify.reedemEligible}")
	private String reedemEligible;
	
	@Value("${label.verifyscreenTitle}")
	private String verifyscreenTitle;
	
	@Value("${label.verifyscreenText}")
	private String verifyscreenText;
	
	@Value("${alert.partynamenotvalidTitle}")
	private String partynamenotvalidTitle;
	
	@Value("${alert.partynamenotvalidText}")
	private String partynamenotvalidText;
	
	@Value("${alert.partyemailnotvalidTitle}")
	private String partyemailnotvalidTitle;
	
	@Value("${alert.partyemailnotvalidText}")
	private String partyemailnotvalidText;
	
	@Value("${alert.partydevicenotfoundTitle}")
	private String partydevicenotfoundTitle;
	
	@Value("${alert.partydevicenotfoundText}")
	private String partydevicenotfoundText;	

	@Value("${alert.partynotfoundTitle}")
	private String partynotfoundTitle;
	
	@Value("${alert.partynotfoundText}")
	private String partynotfoundText;
	
	@Value("${alert.commentnotfoundTitle}")
	private String commentnotfoundTitle;
	
	@Value("${alert.commentnotfoundText}")
	private String commentnotfoundText;	
	
	@Value("${alert.rewardnotfoundTitle}")
	private String rewardnotfoundTitle;
	
	@Value("${alert.rewardnotfoundText}")
	private String rewardnotfoundText;		

	public String getRewardnotfoundTitle() {
		return rewardnotfoundTitle;
	}

	public void setRewardnotfoundTitle(String rewardnotfoundTitle) {
		this.rewardnotfoundTitle = rewardnotfoundTitle;
	}

	public String getRewardnotfoundText() {
		return rewardnotfoundText;
	}

	public void setRewardnotfoundText(String rewardnotfoundText) {
		this.rewardnotfoundText = rewardnotfoundText;
	}

	public String getEmailnotregisteredTitle() {
		return emailnotregisteredTitle;
	}

	public String getCommentnotfoundTitle() {
		return commentnotfoundTitle;
	}

	public void setCommentnotfoundTitle(String commentnotfoundTitle) {
		this.commentnotfoundTitle = commentnotfoundTitle;
	}

	public String getCommentnotfoundText() {
		return commentnotfoundText;
	}

	public void setCommentnotfoundText(String commentnotfoundText) {
		this.commentnotfoundText = commentnotfoundText;
	}

	public void setEmailnotregisteredTitle(String emailnotregisteredTitle) {
		this.emailnotregisteredTitle = emailnotregisteredTitle;
	}

	public String getEmailnotregisteredText() {
		return emailnotregisteredText;
	}

	public void setEmailnotregisteredText(String emailnotregisteredText) {
		this.emailnotregisteredText = emailnotregisteredText;
	}

	public String getPasswordmismatchTitle() {
		return passwordmismatchTitle;
	}

	public void setPasswordmismatchTitle(String passwordmismatchTitle) {
		this.passwordmismatchTitle = passwordmismatchTitle;
	}

	public String getPasswordmismatchText() {
		return passwordmismatchText;
	}

	public void setPasswordmismatchText(String passwordmismatchText) {
		this.passwordmismatchText = passwordmismatchText;
	}

	public String getEmailaddressexistsTitle() {
		return emailaddressexistsTitle;
	}

	public void setEmailaddressexistsTitle(String emailaddressexistsTitle) {
		this.emailaddressexistsTitle = emailaddressexistsTitle;
	}

	public String getEmailaddressexistsText() {
		return emailaddressexistsText;
	}

	public void setEmailaddressexistsText(String emailaddressexistsText) {
		this.emailaddressexistsText = emailaddressexistsText;
	}

	public String getVerifycodemismathchTitle() {
		return verifycodemismathchTitle;
	}

	public void setVerifycodemismathchTitle(String verifycodemismathchTitle) {
		this.verifycodemismathchTitle = verifycodemismathchTitle;
	}

	public String getVerifycodemismathchText() {
		return verifycodemismathchText;
	}

	public void setVerifycodemismathchText(String verifycodemismathchText) {
		this.verifycodemismathchText = verifycodemismathchText;
	}

	public String getPasswordresetTitle() {
		return passwordresetTitle;
	}

	public void setPasswordresetTitle(String passwordresetTitle) {
		this.passwordresetTitle = passwordresetTitle;
	}

	public String getPasswordresetText() {
		return passwordresetText;
	}

	public void setPasswordresetText(String passwordresetText) {
		this.passwordresetText = passwordresetText;
	}

	public String getNonotification() {
		return nonotification;
	}

	public void setNonotification(String nonotification) {
		this.nonotification = nonotification;
	}

	public String getCommentnotificationFirst() {
		return commentnotificationFirst;
	}

	public void setCommentnotificationFirst(String commentnotificationFirst) {
		this.commentnotificationFirst = commentnotificationFirst;
	}

	public String getCommentnotificationAdjacent() {
		return commentnotificationAdjacent;
	}

	public void setCommentnotificationAdjacent(String commentnotificationAdjacent) {
		this.commentnotificationAdjacent = commentnotificationAdjacent;
	}

	public String getLikenotificationFirst() {
		return likenotificationFirst;
	}

	public void setLikenotificationFirst(String likenotificationFirst) {
		this.likenotificationFirst = likenotificationFirst;
	}

	public String getLikenotificationAdjacent() {
		return likenotificationAdjacent;
	}

	public void setLikenotificationAdjacent(String likenotificationAdjacent) {
		this.likenotificationAdjacent = likenotificationAdjacent;
	}

	public String getNetworkinterestactivityFirst() {
		return networkinterestactivityFirst;
	}

	public void setNetworkinterestactivityFirst(String networkinterestactivityFirst) {
		this.networkinterestactivityFirst = networkinterestactivityFirst;
	}

	public String getNetworkinterestactivityAdjacent() {
		return networkinterestactivityAdjacent;
	}

	public void setNetworkinterestactivityAdjacent(String networkinterestactivityAdjacent) {
		this.networkinterestactivityAdjacent = networkinterestactivityAdjacent;
	}

	public String getCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(String checkedIn) {
		this.checkedIn = checkedIn;
	}

	public String getPointscreditedforClass() {
		return pointscreditedforClass;
	}

	public void setPointscreditedforClass(String pointscreditedforClass) {
		this.pointscreditedforClass = pointscreditedforClass;
	}

	public String getPointscreditedforActivity() {
		return pointscreditedforActivity;
	}

	public void setPointscreditedforActivity(String pointscreditedforActivity) {
		this.pointscreditedforActivity = pointscreditedforActivity;
	}

	public String getEventRemainder() {
		return eventRemainder;
	}

	public void setEventRemainder(String eventRemainder) {
		this.eventRemainder = eventRemainder;
	}

	public String getReedemEligible() {
		return reedemEligible;
	}

	public void setReedemEligible(String reedemEligible) {
		this.reedemEligible = reedemEligible;
	}

	public String getVerifyscreenTitle() {
		return verifyscreenTitle;
	}

	public void setVerifyscreenTitle(String verifyscreenTitle) {
		this.verifyscreenTitle = verifyscreenTitle;
	}

	public String getVerifyscreenText() {
		return verifyscreenText;
	}

	public void setVerifyscreenText(String verifyscreenText) {
		this.verifyscreenText = verifyscreenText;
	}

	public String getPartynamenotvalidTitle() {
		return partynamenotvalidTitle;
	}

	public void setPartynamenotvalidTitle(String partynamenotvalidTitle) {
		this.partynamenotvalidTitle = partynamenotvalidTitle;
	}

	public String getPartynamenotvalidText() {
		return partynamenotvalidText;
	}

	public void setPartynamenotvalidText(String partynamenotvalidText) {
		this.partynamenotvalidText = partynamenotvalidText;
	}

	public String getPartyemailnotvalidTitle() {
		return partyemailnotvalidTitle;
	}

	public void setPartyemailnotvalidTitle(String partyemailnotvalidTitle) {
		this.partyemailnotvalidTitle = partyemailnotvalidTitle;
	}

	public String getPartyemailnotvalidText() {
		return partyemailnotvalidText;
	}

	public void setPartyemailnotvalidText(String partyemailnotvalidText) {
		this.partyemailnotvalidText = partyemailnotvalidText;
	}

	public String getPartydevicenotfoundTitle() {
		return partydevicenotfoundTitle;
	}

	public void setPartydevicenotfoundTitle(String partydevicenotfoundTitle) {
		this.partydevicenotfoundTitle = partydevicenotfoundTitle;
	}

	public String getPartydevicenotfoundText() {
		return partydevicenotfoundText;
	}

	public void setPartydevicenotfoundText(String partydevicenotfoundText) {
		this.partydevicenotfoundText = partydevicenotfoundText;
	}
	

	public String getPartynotfoundTitle() {
		return partynotfoundTitle;
	}

	public void setPartynotfoundTitle(String partynotfoundTitle) {
		this.partynotfoundTitle = partynotfoundTitle;
	}

	public String getPartynotfoundText() {
		return partynotfoundText;
	}

	public void setPartynotfoundText(String partynotfoundText) {
		this.partynotfoundText = partynotfoundText;
	}

}
