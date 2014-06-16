package org.pem.openidm.persistence;

import org.apache.log4j.Logger;

public class StoreException  extends Exception{
   private static Logger logger = Logger.getLogger(StoreException.class);
   public String message=null;
	
   public StoreException(String msg) {
      logger.error(msg);	
      message = new String(msg);
   }
}
