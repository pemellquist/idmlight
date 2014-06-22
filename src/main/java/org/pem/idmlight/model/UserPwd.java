package org.pem.idmlight.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userpwd")
public class UserPwd {
   private String username;
   private String userpwd;

   public String getUsername() {
      return username;
   }

   public void setUsername(String name) {
      this.username = name;
   }  

   public String getUserpwd() {
      return userpwd;
   }

   public void setUserpwd(String pwd) {
      this.userpwd = pwd;
   }

}

