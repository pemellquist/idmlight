package org.pem.tokenidm;

/**
 * pemellquist@gmail.com
 */

import java.util.EnumSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.DispatcherType;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.sun.jersey.api.core.ResourceConfig;

public class TokenIDM 
{
   private static Logger logger = Logger.getLogger(TokenIDM.class);
   public static TokenIDMConfig tokenIDMConfig;
	
   public TokenIDM()
   {
      logger.info("starting tokenidm .... ");
   }
	
   @SuppressWarnings("deprecation")
   public void run( String[] args)
   {   
      tokenIDMConfig = new TokenIDMConfig();
		 
      if (!tokenIDMConfig.load(args[0])) {
         logger.error("unable to load tokenidm config file : " + args[0]);
         return;
      }
      tokenIDMConfig.log();
		
      try {	    	  
         Server server = new Server();  
         SslSocketConnector sslConnector = new SslSocketConnector();
         sslConnector.setPort(tokenIDMConfig.apiPort);
         sslConnector.setKeyPassword(tokenIDMConfig.keystorePwd);
         sslConnector.setKeystore(tokenIDMConfig.keystore);
         server.addConnector(sslConnector);

         ServletHolder sh = new ServletHolder();
         sh.setName("tokenidm");
         sh.setClassName("com.sun.jersey.spi.container.servlet.ServletContainer");
         sh.setInitParameter("com.sun.jersey.config.property.packages", "org.pem.tokenidm.handlers");
         ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
                        
         context.setContextPath("/");
         context.addServlet(sh, "/*");
         server.setHandler(context);

         server.start();  
         server.join();
      }
      catch (Exception e) {			 
         logger.error(e);
         return;
      }
   }
	
   public static void main( String[] args )
   {   
      System.out.println("main");
      if (args.length<1) {
         System.out.println("not enough args provided!");
         System.out.println("tokenidm <configfile>");
         System.out.println("");
         return;
      } 	
    	
      TokenIDM tokenIDM  = new TokenIDM();
      tokenIDM.run(args);       	    	    	    	    	
   }
}
