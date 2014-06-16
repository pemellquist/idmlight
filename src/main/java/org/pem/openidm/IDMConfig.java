package org.pem.openidm;

/**
 * pemellquist@gmail.com
 */

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class IDMConfig {
   private static Logger logger = Logger.getLogger(IDMConfig.class);
	
   public static String API_PORT       = "api-port";
   public static String KEYSTORE       = "keystore";
   public static String KEYSTOREPWD    = "keystorepwd";
   public static String DB_PATH        = "db-path";
   public static String DB_DRIVER      = "db-driver";
   public static String DB_USER        = "db-user";
   public static String DB_PWD         = "db-pwd";
   public static String DB_VALID_TO    = "db-valid-to";
	
   public int apiPort;
   public String keystore;
   public String keystorePwd;
   public String dbPath;
   public String dbDriver;
   public String dbUser;
   public String dbPwd;
   public int dbValidTimeOut;
	
   public boolean load(String filename) {		
      try {
         XMLConfiguration serviceConfig = new XMLConfiguration(filename);
           
         apiPort = serviceConfig.getInt(API_PORT);
         keystore = serviceConfig.getString(KEYSTORE);
         keystorePwd = serviceConfig.getString(KEYSTOREPWD);
         dbPath = serviceConfig.getString(DB_PATH);
         dbDriver = serviceConfig.getString(DB_DRIVER);
         dbUser = serviceConfig.getString(DB_USER);
         dbPwd = serviceConfig.getString(DB_PWD);
         dbValidTimeOut = serviceConfig.getInt(DB_VALID_TO);
      }  
      catch(ConfigurationException cex) {
         logger.error(cex + "failure to open:" + filename);
         return false;
      }
	    	       
      return true;
   }
	           
   public void log() {
      logger.info("API port                : " + apiPort);
      logger.info("Keystore                : " + keystore); 
      logger.info("DB Path                 : " + dbPath);
      logger.info("DB Driver               : " + dbDriver);
      logger.info("DB Valid Time Out       : " + dbValidTimeOut);
   }

}
