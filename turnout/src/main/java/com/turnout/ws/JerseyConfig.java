package com.turnout.ws;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.turnout.ws.controller.BeaconMasterController;
import com.turnout.ws.controller.CommentsController;
import com.turnout.ws.controller.FriendsController;
import com.turnout.ws.controller.Home;
import com.turnout.ws.controller.InfoController;
import com.turnout.ws.controller.LikesController;
import com.turnout.ws.controller.OfferController;
import com.turnout.ws.controller.PartyAuthMechController;
import com.turnout.ws.controller.PartyController;
import com.turnout.ws.controller.PartyNotificationController;
import com.turnout.ws.controller.RewardController;
import com.turnout.ws.controller.RewardsHasVoucherController;
import com.turnout.ws.controller.StudioActivityController;
import com.turnout.ws.controller.StudioController;
import com.turnout.ws.controller.StudioPartyActivityController;
import com.turnout.ws.controller.TurnoutLookupController;
import com.turnout.ws.controller.UiCardsController;
import com.turnout.ws.controller.VoucherController;


/**
 * Resource configuration used to configure a web application. So all classes registered in this class is accessible as web service with jersey API.
 *
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

/**
 * This method will register available classes and packages so an application can make use of them and make it accessible  by end users.
 */
	public JerseyConfig() {
		//register(Endpoint.class);
		packages("com.turnout.ws.exception");	
		register(FriendsController.class);
		register(OfferController.class);
		register(StudioActivityController.class);
		register(PartyAuthMechController.class);	
		register(StudioController.class);	
		register(RewardController.class);
		register(PartyController.class);
		register(TurnoutLookupController.class);
		register(CommentsController.class);
		register(StudioPartyActivityController.class);
		register(RewardsHasVoucherController.class);
		register(VoucherController.class);
		register(LikesController.class);
		register(Home.class);			
		register(CORSResponseFilter.class);
		register(MultiPartFeature.class);
		register(UiCardsController.class);
		register(PartyNotificationController.class);
		register(BeaconMasterController.class);
		register(InfoController.class);
		register(AuthenticationFilter.class);

	}

}
