package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "roles")
public class Roles {
   private List<Role> roles = new ArrayList<Role>();

   public void setRoles(List<Role> roles) {
      this.roles = roles;
   } 

   public List<Role> getRoles() {
      return roles;
   }

}

