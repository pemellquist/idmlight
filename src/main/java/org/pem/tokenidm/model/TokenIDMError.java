package org.pem.tokenidm.model;

import org.apache.log4j.Logger;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tokenidmerror")
public class TokenIDMError{
   private static Logger logger = Logger.getLogger(TokenIDMError.class);

   private String message;
   private String details;

   public String getMessage() {
      return message;
   }

   public void setMessage(String msg) {
      this.message = msg;
   }

   public String getDetails() {
      return details;
   }

   public void setDetails(String details) {
      this.details = details;
   }
	
}
