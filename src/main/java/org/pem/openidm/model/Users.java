package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "users")
public class Users {
   private List<User> users = new ArrayList<User>();

   public void setUsers(List<User> users) {
      this.users = users;
   } 

   public List<User> getUsers() {
      return users;
   }

}

