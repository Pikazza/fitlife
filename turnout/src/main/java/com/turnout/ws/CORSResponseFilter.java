package com.turnout.ws;

import java.io.IOException;


import javax.ws.rs.WebApplicationException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;


/**
 * The CORSResponseFilter will check the users request and set allowable Access control origins, Access control methods and header globally to any outgoing response.
 * So users can access server resources after successful authentication.
 *
 */

@Provider
public class CORSResponseFilter implements ContainerResponseFilter,ContainerRequestFilter {
	
	
	
	/**
	 * Filter method called after a response has been provided for a request (either by a request filter or by a matched resource method.
	 * 
	 * @param requestContext request context.
	 * @param responseContext response context.
	 * @throws IOException if an I/O exception occurs.
	 */
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		
		MultivaluedMap<String, String> reqheader = requestContext.getHeaders();
		System.out.println("Intial Request header:::::::"+reqheader);
		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		System.out.println("Intial Response header:::::::"+headers);	
		
		System.out.println("enter into corsfilter request method"+requestContext.getHeaderString("Access-Control-Request-Methods"));
		System.out.println(" method comming"+requestContext.getMethod());
		System.out.println("requested header"+requestContext.getHeaderString("Access-Control-Request-Headers"));
		
		if("OPTIONS".equals(requestContext.getMethod()))
		{	
			System.out.println("enter into option method");
			if(!headers.containsKey("Access-Control-Allow-Credentials"))
				headers.add("Access-Control-Allow-Credentials", "true");			
			if(!headers.containsKey("Access-Control-Allow-Origin"))
			{
				headers.add("Access-Control-Allow-Origin", "*");
			}
			if(!headers.containsKey("Access-Control-Allow-Methods"))
			{
				headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
			}
			if(!headers.containsKey("Access-Control-Allow-Headers"))
			{
				headers.add("Access-Control-Allow-Headers", "X-Application-Context, X-Requested-With, Content-Type, Authorization, Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
			}
			if(!headers.containsKey("Access-Control-Max-Age"))
			{
				headers.add("Access-Control-Max-Age", "2");		
			}	
			
			//throw new WebApplicationException(javax.ws.rs.core.Response.Status.OK);
			responseContext.setStatus(200);			
		
		}
		else
		{
			System.out.println("enter into requested method");
			headers.putSingle("Content-Type", "application/json;charset=UTF-8");
			if(!headers.containsKey("Access-Control-Allow-Credentials"))
				headers.add("Access-Control-Allow-Credentials", "true");
			if(!headers.containsKey("Access-Control-Allow-Origin"))
			{
				headers.add("Access-Control-Allow-Origin", "*");
			}
			if(!headers.containsKey("Access-Control-Allow-Methods"))
			{
				headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
			}
			if(!headers.containsKey("Access-Control-Allow-Headers"))
			{
				headers.add("Access-Control-Allow-Headers", "X-Application-Context, X-Requested-With, Content-Type, Authorization, Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
			}
			if(!headers.containsKey("Access-Control-Max-Age"))
			{
				headers.add("Access-Control-Max-Age", "2");		
			}	
					
		}
		
		System.out.println("final response headre::::::"+headers);
	
	}

	/**Filter method called after a response has been provided for a request (either by a request filter or by a matched resource method.
	 * 
	 * @param requestContext request context.
	 * @throws IOException It throws when unchecked IO exception happens.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String method = requestContext.getMethod();
	    // myresource/get/56bCA for example
	    //String path = containerRequest.getPath(true);
	    if(method.equals("OPTIONS")) {
	        throw new WebApplicationException(javax.ws.rs.core.Response.Status.OK);
	    }
		
	}

}
