package org.pem.tokenidm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tenant")
public class Tenant {
   private Integer tenantid;
   private String name;
   private String description;
   private Boolean enabled;

   public Integer getTenantid() {
      return tenantid;
   }

   public void setTenantid(Integer id) {
      this.tenantid = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }  

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Boolean getEnabled() {
      return enabled;
   }

   public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
   }

}

