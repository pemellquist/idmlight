package org.pem.idmlight.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "Claim")
public class Claim {
   private Integer domainid;
   private Integer userid; 
   private String username;
   private List<Role> roles;

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

   public List<Role> getRoles() {
      return roles;
   }

   public void setRoles(List<Role> roles) {
      this.roles = roles;
   }

}

