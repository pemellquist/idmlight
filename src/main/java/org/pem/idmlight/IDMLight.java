package org.pem.idmlight;

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

public class IDMLight 
{
   private static Logger logger = Logger.getLogger(IDMLight.class);
   public static IDMConfig idmConfig;
	
   public IDMLight()
   {
      logger.info("starting idmlight .... ");
   }
	
   @SuppressWarnings("deprecation")
   public void run( String[] args)
   {   
      idmConfig = new IDMConfig();
		 
      if (!idmConfig.load(args[0])) {
         logger.error("unable to load tokenidm config file : " + args[0]);
         return;
      }
      idmConfig.log();
		
      try {	    	  
         Server server = new Server();  
         SslSocketConnector sslConnector = new SslSocketConnector();
         sslConnector.setPort(idmConfig.apiPort);
         sslConnector.setKeyPassword(idmConfig.keystorePwd);
         sslConnector.setKeystore(idmConfig.keystore);
         server.addConnector(sslConnector);

         ServletHolder sh = new ServletHolder();
         sh.setName("idmlight");
         sh.setClassName("com.sun.jersey.spi.container.servlet.ServletContainer");
         sh.setInitParameter("com.sun.jersey.config.property.packages", "org.pem.idmlight.handlers");
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
         System.out.println("idmlight <configfile>");
         System.out.println("");
         return;
      } 	
    	
      IDMLight iDMLight  = new IDMLight();
      iDMLight.run(args);       	    	    	    	    	
   }
}
