package org.pem.tokenidm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tenant")
public class Tenant {
   private Integer id;
   private String name;
   private String description;
   private Boolean enabled;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
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

