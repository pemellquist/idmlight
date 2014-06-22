package org.pem.idmlight.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "role")
public class Role {
   private Integer roleid;
   private String name;
   private String description;

   public Integer getRoleid() {
      return roleid;
   }

   public void setRoleid(Integer id) {
      this.roleid = id;
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

}

