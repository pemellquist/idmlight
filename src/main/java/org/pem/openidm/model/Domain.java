package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "domain")
public class Domain {
   private Integer domainid;
   private String name;
   private String description;
   private Boolean enabled;

   public Integer getDomainid() {
      return domainid;
   }

   public void setDomainid(Integer id) {
      this.domainid = id;
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

