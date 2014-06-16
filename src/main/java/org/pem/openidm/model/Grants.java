package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "grants")
public class Grants {
   private List<Grant> grants = new ArrayList<Grant>();

   public void setGrants(List<Grant> grants) {
      this.grants = grants;
   } 

   public List<Grant> getGrants() {
      return grants;
   }

}

