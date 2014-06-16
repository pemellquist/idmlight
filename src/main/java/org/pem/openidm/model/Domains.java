package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "domains")
public class Domains {
   private List<Domain> domains = new ArrayList<Domain>();

   public void setDomains(List<Domain> domains) {
      this.domains = domains;
   } 

   public List<Domain> getDomains() {
      return domains;
   }

}

