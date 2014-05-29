package org.pem.tokenidm.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "tenants")
public class Tenants {
   private List<Tenant> tenants = new ArrayList<Tenant>();

   public void setTenants(List<Tenant> tenants) {
      this.tenants = tenants;
   } 

   public List<Tenant> getTenants() {
      return tenants;
   }

}

