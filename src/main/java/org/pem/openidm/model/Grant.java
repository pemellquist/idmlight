package org.pem.openidm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "grant")
public class Grant {
   private Integer grantid;
   private String description;
   private Integer domainid;
   private Integer userid;
   private Integer roleid;

   public Integer getGrantid() {
      return grantid;
   }

   public void setGrantid(Integer id) {
      this.grantid = id;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

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

   public Integer getRoleid() {
      return roleid;
   }

   public void setRoleid(Integer id) {
      this.roleid = id;
   }

}

