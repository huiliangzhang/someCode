package com.zhl.tools.rediscountingbf;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

//@Path("13/v1")
@Path("bf")
public class PidResource {
	
//	@Context ServletContext context;
	BloomFilter tester = new BloomFilter();

	@GET
	@Path("/{var}")
	@Produces("text/html")
	public String get(@PathParam("var") String key, @Context ServletContext servletContext) {

		tester.initialize(servletContext.getInitParameter("RedisServer"));
		tester.initialize(9600000000l, 7, 4);
		
		return tester.request(key);
	}

}