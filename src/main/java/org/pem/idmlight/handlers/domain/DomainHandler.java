package org.pem.idmlight.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;
import org.pem.idmlight.model.Domain; 
import org.pem.idmlight.model.User;
import org.pem.idmlight.model.Users;
import org.pem.idmlight.model.Role;
import org.pem.idmlight.model.Roles;
import org.pem.idmlight.model.Grant;
import org.pem.idmlight.model.Grants;
import org.pem.idmlight.model.Domains;
import org.pem.idmlight.model.UserPwd;
import org.pem.idmlight.model.Claim; 
import org.pem.idmlight.model.IDMError;
import org.pem.idmlight.persistence.DomainStore;
import org.pem.idmlight.persistence.UserStore;
import org.pem.idmlight.persistence.RoleStore;
import org.pem.idmlight.persistence.GrantStore; 
import org.pem.idmlight.persistence.StoreException;
	
@Path("/v1/domains")
public class DomainHandler {
	
   private static Logger logger = Logger.getLogger(DomainHandler.class);	
   private static DomainStore domainStore = new DomainStore();
   private static UserStore userStore = new UserStore();
   private static RoleStore roleStore = new RoleStore();
   private static GrantStore grantStore = new GrantStore(); 
   
   @GET
   @Produces("application/json")
   public Response getDomains() {
      logger.info("Get /domains");
      Domains domains=null;
      try {
         domains = domainStore.getDomains();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting domains");
         return Response.status(500).entity(idmerror).build();
      }
      return Response.ok(domains).build();
   }

   @GET
   @Path("/{id}")
   @Produces("application/json")
   public Response getDomain(@PathParam("id") String id)  {
      logger.info("Get /domains/" + id);
      Domain domain = null;
      long longId=0;
      try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + id);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         domain = domainStore.getDomain(longId);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting domain");
         return Response.status(500).entity(idmerror).build();
      }

      if (domain==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! domain id :" + id);
         return Response.status(404).entity(idmerror).build();
      }
      return Response.ok(domain).build();
   }

   @POST
   @Consumes("application/json")
   @Produces("application/json")
   public Response createDomain(@Context UriInfo info,Domain domain) {
      logger.info("Post /domains");
      try {
         if (domain.getEnabled()==null)
            domain.setEnabled(false);
         if (domain.getName()==null)
            domain.setName("");
         if (domain.getDescription()==null)
            domain.setDescription("");
         domain = domainStore.createDomain(domain);
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error creating domain");
         return Response.status(500).entity(idmerror).build();
      } 

      return Response.status(201).entity(domain).build();
   } 

   @PUT
   @Path("/{id}")
   @Consumes("application/json")
   @Produces("application/json")
   public Response putDomain(@Context UriInfo info,Domain domain,@PathParam("id") String id) {
      long longId=0;
      logger.info("Put /domains/" + id);
       try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + id);
         return Response.status(404).entity(idmerror).build();
      }

      try {
         domain.setDomainid((int)longId);
         domain = domainStore.putDomain(domain);
         if (domain==null) {
            IDMError idmerror = new IDMError();
            idmerror.setMessage("Not found! Domain id :" + id);
            return Response.status(404).entity(idmerror).build();
         }

         return Response.status(200).entity(domain).build();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error putting domain");
         return Response.status(500).entity(idmerror).build();
      }
   }

   @DELETE
   @Path("/{id}")
   public Response deleteDomain(@Context UriInfo info,@PathParam("id") String id) {
      long longId=0;
      logger.info("Delete /domains/" + id);
      try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + id);
         return Response.status(404).entity(idmerror).build();
      }

      try {
         Domain domain = new Domain();
         domain.setDomainid((int)longId);
         domain = domainStore.deleteDomain(domain);
         if (domain==null) {
            IDMError idmerror = new IDMError();
            idmerror.setMessage("Not found! Domain id :" + id);
            return Response.status(404).entity(idmerror).build();
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error deleting Domain");
         return Response.status(500).entity(idmerror).build();
      }

      return Response.status(204).build();
   }


   @POST
   @Path("/{did}/users/{uid}/roles")
   @Consumes("application/json")
   @Produces("application/json")
   public Response createGrant( @Context UriInfo info, 
                                @PathParam("did") String did,
                                @PathParam("uid") String uid,  
                                Grant grant) {
      logger.info("Post /domains/"+did+"/users/"+uid+"/roles");
      Domain domain=null;
      User user=null;
      Role role=null;
      long longDid=0;
      long longUid=0;
      long longRid=0;

      if (grant.getDescription()==null)
         grant.setDescription("");

      // validate domain id
      try {
         longDid= Long.parseLong(did);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         domain = domainStore.getDomain(longDid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting domain");
         return Response.status(500).entity(idmerror).build();
      }
      if (domain==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }
      grant.setDomainid((int)longDid);

      // validate user id
      try {
         longUid= Long.parseLong(uid);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid User id :" + uid);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         user = userStore.getUser(longUid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(idmerror).build();
      }
      if (user==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! User id :" + uid);
         return Response.status(404).entity(idmerror).build();
      }
      grant.setUserid((int)longUid);

      // validate role id
      try {
         longRid= grant.getRoleid();
         logger.info("roleid = " + longRid);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Role id :" + grant.getRoleid());
         return Response.status(404).entity(idmerror).build();
      }
      try {
         role = roleStore.getRole(longRid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting role");
         return Response.status(500).entity(idmerror).build();
      }
      if (role==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! role :" + grant.getRoleid());
         return Response.status(404).entity(idmerror).build();
      }

      // see if grant already exists for this 
      try {
         Grant existingGrant = grantStore.getGrant(longDid,longUid,longRid);
         if (existingGrant != null) {
            IDMError idmerror = new IDMError();
            idmerror.setMessage("Grant already exists for did:"+longDid+" uid:"+longUid+" rid:"+longRid);
            return Response.status(403).entity(idmerror).build();
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error creating grant");
         return Response.status(500).entity(idmerror).build();
      }


      // create grant 
      try {
         grant = grantStore.createGrant(grant);
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error creating grant");
         return Response.status(500).entity(idmerror).build();
      }

      return Response.status(201).entity(grant).build();
   }


   @POST
   @Path("/{did}/users/roles")
   @Consumes("application/json")
   @Produces("application/json")
   public Response validateUser( @Context UriInfo info,
                                 @PathParam("did") String did,
                                 UserPwd userpwd) {

      logger.info("GET /domains/"+did+"/users");
      long longDid=0;
      Domain domain=null;
      Claim claim = new Claim();
      List<Role> roleList = new ArrayList<Role>();

      // validate domain id
      try {
         longDid= Long.parseLong(did);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         domain = domainStore.getDomain(longDid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting domain");
         return Response.status(500).entity(idmerror).build();
      }
      if (domain==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }

      // check request body for username and pwd
      String username = userpwd.getUsername();
      if (username==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("username not specfied in request body");
         return Response.status(400).entity(idmerror).build();
      }
      String pwd = userpwd.getUserpwd();
      if (pwd==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("userpwd not specfied in request body");
         return Response.status(400).entity(idmerror).build();
      }

      // find userid for user 
      try {
         Users users = userStore.getUsers(username);
         List<User> userList = users.getUsers();
         if (userList.size()==0) {
            IDMError idmerror = new IDMError();
            idmerror.setMessage("did not find username: "+username);
            return Response.status(404).entity(idmerror).build();
         }
         User user = userList.get(0);
         String userPwd = user.getPassword();
         String reqPwd = userpwd.getUserpwd();
         if (!userPwd.equals(reqPwd)) {
            IDMError idmerror = new IDMError();
            idmerror.setMessage("password does not match for username: "+username);
            return Response.status(401).entity(idmerror).build(); 
         }
         claim.setDomainid((int)longDid);
         claim.setUsername(username);
         claim.setUserid(user.getUserid());
         try {
            Grants grants = grantStore.getGrants(longDid,user.getUserid());
            List<Grant> grantsList = grants.getGrants();
            for (int i=0; i < grantsList.size(); i++) {
               Grant grant = grantsList.get(i);
               Role role = roleStore.getRole(grant.getRoleid());
               roleList.add(role);
            }
         }
         catch (StoreException se) {
            logger.error("StoreException : " + se);
            IDMError idmerror = new IDMError();
            idmerror.setMessage("Internal error getting Roles");
            return Response.status(500).entity(idmerror).build();
         }
         claim.setRoles(roleList); 
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(idmerror).build();
      }

      return Response.ok(claim).build();
   }  

   @GET
   @Path("/{did}/users/{uid}/roles")
   @Produces("application/json")
   public Response getRoles( @Context UriInfo info,
                             @PathParam("did") String did,
                             @PathParam("uid") String uid) {
      logger.info("GET /domains/"+did+"/users/"+uid+"/roles");
      long longDid=0;
      long longUid=0;
      Domain domain=null;
      User user=null;
      Roles roles = new Roles();
      List<Role> roleList = new ArrayList<Role>();

      // validate domain id
      try {
         longDid= Long.parseLong(did);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         domain = domainStore.getDomain(longDid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting domain");
         return Response.status(500).entity(idmerror).build();
      }
      if (domain==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }

      // validate user id
      try {
         longUid=Long.parseLong(uid);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid User id :" + uid);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         user = userStore.getUser(longUid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(idmerror).build();
      }
      if (user==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! User id :" + uid);
         return Response.status(404).entity(idmerror).build();
      }

      try {
         Grants grants = grantStore.getGrants(longDid,longUid);
         List<Grant> grantsList = grants.getGrants();
         for (int i=0; i < grantsList.size(); i++) {
            Grant grant = grantsList.get(i);
            Role role = roleStore.getRole(grant.getRoleid());
            roleList.add(role);
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting Roles");
         return Response.status(500).entity(idmerror).build();
      }

      roles.setRoles(roleList);
      return Response.ok(roles).build();
   }

   @DELETE
   @Path("/{did}/users/{uid}/roles/{rid}")
   public Response deleteGrant( @Context UriInfo info,
                                @PathParam("did") String did,
                                @PathParam("uid") String uid,
                                @PathParam("rid") String rid) {
      long longDid=0;
      long longUid=0;
      long longRid=0;
      Domain domain=null;
      User user=null;
      Role role=null;

      // validate domain id
      try {
         longDid= Long.parseLong(did);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         domain = domainStore.getDomain(longDid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting domain");
         return Response.status(500).entity(idmerror).build();
      }
      if (domain==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! Domain id :" + did);
         return Response.status(404).entity(idmerror).build();
      }

      // validate user id
      try {
         longUid=Long.parseLong(uid);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid User id :" + uid);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         user = userStore.getUser(longUid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(idmerror).build();
      }
      if (user==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! User id :" + uid);
         return Response.status(404).entity(idmerror).build();
      }

      // validate role id
      try {
         longRid=Long.parseLong(rid);
      }
      catch (NumberFormatException nfe) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Invalid Role id :" + rid);
         return Response.status(404).entity(idmerror).build();
      }
      try {
         role = roleStore.getRole(longRid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error getting Role");
         return Response.status(500).entity(idmerror).build();
      }
      if (role==null) {
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Not found! Role id :" + rid);
         return Response.status(404).entity(idmerror).build();
      }
      
      // see if grant already exists 
      try {
         Grant existingGrant = grantStore.getGrant(longDid,longUid,longRid);
         if (existingGrant == null) {
            IDMError idmerror = new IDMError();
            idmerror.setMessage("Grant does not exist for did:"+longDid+" uid:"+longUid+" rid:"+longRid);
            return Response.status(404).entity(idmerror).build();
         }
         existingGrant = grantStore.deleteGrant(existingGrant);
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         IDMError idmerror = new IDMError();
         idmerror.setMessage("Internal error creating grant");
         return Response.status(500).entity(idmerror).build();
      }

      return Response.status(204).build();
   }

}
