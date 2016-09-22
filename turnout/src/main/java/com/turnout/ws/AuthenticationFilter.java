package com.turnout.ws;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.turnout.ws.domain.PartyAuthMech;
import com.turnout.ws.repository.PartyAuthMechRepository;

/**
 * This class allows a user's request for further process by checking their authentication.
 * If authentication is succeed, it allows the user to access server resource. Otherwise it makes the server resource forbidden.
 * 
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter{
	
	@Autowired
	private PartyAuthMechRepository partyAuthMechRepository;

	@Context
	private ResourceInfo resourceInfo;
	
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
                                                        .entity("You cannot access this resource").build();
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
                                                        .entity("Access blocked for all users !!").build();
      
	/**
	 * This method will check the logged in users authentication and return true if user has an valid account credentials.
	 * Otherwise return false and make the resource forbidden.
	 * 
	 * @param requestContext A mutable object that provides request-specific information for the filter.
	 * @throws IOException It throws when unchecked IO error happens.
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter "javax.ws.rs.container.ContainerRequestContext".
	 */
    
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		Method method = resourceInfo.getResourceMethod();
		
		if(! method.isAnnotationPresent(PermitAll.class))
		{
			
			if(method.isAnnotationPresent(DenyAll.class))
			{
				requestContext.abortWith(ACCESS_FORBIDDEN);
				return;
			}
			
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			
			final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
			
			if(authorization == null || authorization.isEmpty())
			{
				requestContext.abortWith(ACCESS_DENIED);
				return;
			}
			
			final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
			
			 String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));
			 
			 final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			 final String username = tokenizer.nextToken();
	         final String password = tokenizer.nextToken();
	        
	         
	         System.out.println(username);
	         System.out.println(password);
	        
	        
	        if(method.isAnnotationPresent(RolesAllowed.class))
	            {
	        	 System.out.println("Enter into check Roles");
	                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
	                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
	                  System.out.println(rolesSet.toString());
	                //Is user valid?
	                if( ! isUserAllowed(username, password,rolesSet))
	                {
	                	System.out.println("isuser allowed return false");
	                    requestContext.abortWith(ACCESS_DENIED);
	                    return;
	                }
	       }
			 
			 
		}
		
	}


	/**
	 * This method will check the logged in users authentication and return true if user has an account.
	 * 
	 * @param username A string as user name of party account.
	 * @param password A string as password of the party account.
	 * @param rolesSet A String may have the value of ADMIN or USER based on the account type.
	 * @return Return boolean value based on the party's account verification.
	 */
	private boolean isUserAllowed(String username, String password, Set<String> rolesSet) {
		// TODO Auto-generated method stub
		boolean isAllowed = false;
		String userRole ;
		
		if(username.equals("cumulonimbus") && password.equals("roanu"))
		{
			userRole = "ADMIN";	             
			//Step 2. Verify user role
            if(rolesSet.contains(userRole))
            {
                isAllowed = true;
            }
		}	
		else
		{
			PartyAuthMech partyAuthmech = partyAuthMechRepository.findByPamAuthId(username);
			if(username.equals(partyAuthmech.getPamAuthId()) && password.equals(partyAuthmech.getPamAuthToken()) )
				{
				    userRole = "USER";	            
					//Step 2. Verify user role
		            if(rolesSet.contains(userRole))
		            {
		                isAllowed = true;
		            }
				}
        }		
        return isAllowed;
    }
	
}