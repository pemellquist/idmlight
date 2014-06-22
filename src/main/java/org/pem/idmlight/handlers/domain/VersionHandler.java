package org.pem.idmlight.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.pem.idmlight.model.Version;
	
@Path("/")
public class VersionHandler {
	
   private static Logger logger = Logger.getLogger(VersionHandler.class);	
   
   protected static String CURRENT_VERSION      = "v1.0";
   protected static String LAST_UPDATED         = "2014-04-18T18:30:02.25Z";
   protected static String CURRENT_STATUS       = "CURRENT"; 
   
   @GET
   @Produces("application/json")
   public Version getVersion(@Context HttpServletRequest request) {
      logger.info("Get /");
      Version version = new Version();
      version.setId(CURRENT_VERSION);
      version.setUpdated(LAST_UPDATED);
      version.setStatus(CURRENT_STATUS);	   
      return version; 
   }
   
}
