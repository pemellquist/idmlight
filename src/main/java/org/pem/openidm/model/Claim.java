package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Claim")
public class Claim {
   private Integer domainid;
   private Integer userid; 
   private String username;
   private Roles roles;

   public Integer getDomainid() {
      return domainid;
   }

   public void setDomainid(Integer id) {
      this.domainid = id;
   }

   public Integer getUserid() {
      return userid;
   }

   public void setUserid(Integer id) {
      this.userid = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String name) {
      this.username = name;
   }  

   public Roles getRoles() {
      return roles;
   }

   public void setRoles(Roles roles) {
      this.roles = roles;
   }

}

