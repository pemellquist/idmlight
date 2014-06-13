package org.pem.tokenidm.handlers;

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
import org.pem.tokenidm.model.Tenant; 
import org.pem.tokenidm.model.User;
import org.pem.tokenidm.model.Role;
import org.pem.tokenidm.model.Roles;
import org.pem.tokenidm.model.Grant;
import org.pem.tokenidm.model.Grants;
import org.pem.tokenidm.model.Tenants;
import org.pem.tokenidm.model.TokenIDMError;
import org.pem.tokenidm.persistence.TenantStore;
import org.pem.tokenidm.persistence.UserStore;
import org.pem.tokenidm.persistence.RoleStore;
import org.pem.tokenidm.persistence.GrantStore; 
import org.pem.tokenidm.persistence.StoreException;
	
@Path("/v1/tenants")
public class TenantHandler {
	
   private static Logger logger = Logger.getLogger(TenantHandler.class);	
   private static TenantStore tenantStore = new TenantStore();
   private static UserStore userStore = new UserStore();
   private static RoleStore roleStore = new RoleStore();
   private static GrantStore grantStore = new GrantStore(); 
   
   @GET
   @Produces("application/json")
   public Response getTenants() {
      logger.info("Get /tenants");
      Tenants tenants=null;
      try {
         tenants = tenantStore.getTenants();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting tenants");
         return Response.status(500).entity(tokenidmerror).build();
      }
      return Response.ok(tenants).build();
   }

   @GET
   @Path("/{id}")
   @Produces("application/json")
   public Response getTenant(@PathParam("id") String id)  {
      logger.info("Get /tenants/" + id);
      Tenant tenant = null;
      long longId=0;
      try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Tenant id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         tenant = tenantStore.getTenant(longId);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting tenant");
         return Response.status(500).entity(tokenidmerror).build();
      }

      if (tenant==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! Tenant id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }
      return Response.ok(tenant).build();
   }

   @POST
   @Consumes("application/json")
   @Produces("application/json")
   public Response createTenant(@Context UriInfo info,Tenant tenant) {
      logger.info("Post /tenants");
      try {
         if (tenant.getEnabled()==null)
            tenant.setEnabled(false);
         if (tenant.getName()==null)
            tenant.setName("");
         if (tenant.getDescription()==null)
            tenant.setDescription("");
         tenant = tenantStore.createTenant(tenant);
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error creating tenant");
         return Response.status(500).entity(tokenidmerror).build();
      } 

      return Response.status(201).entity(tenant).build();
   } 

   @PUT
   @Path("/{id}")
   @Consumes("application/json")
   @Produces("application/json")
   public Response putTenant(@Context UriInfo info,Tenant tenant,@PathParam("id") String id) {
      long longId=0;
      logger.info("Put /tenants/" + id);
       try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Tenant id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         tenant.setTenantid((int)longId);
         tenant = tenantStore.putTenant(tenant);
         if (tenant==null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Not found! Tenant id :" + id);
            return Response.status(404).entity(tokenidmerror).build();
         }

         return Response.status(200).entity(tenant).build();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error putting tenant");
         return Response.status(500).entity(tokenidmerror).build();
      }
   }

   @DELETE
   @Path("/{id}")
   public Response deleteTenant(@Context UriInfo info,@PathParam("id") String id) {
      long longId=0;
      logger.info("Delete /tenants/" + id);
      try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Tenant id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         Tenant tenant = new Tenant();
         tenant.setTenantid((int)longId);
         tenant = tenantStore.deleteTenant(tenant);
         if (tenant==null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Not found! Tenant id :" + id);
            return Response.status(404).entity(tokenidmerror).build();
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error deleting tenant");
         return Response.status(500).entity(tokenidmerror).build();
      }

      return Response.status(204).build();
   }


   @POST
   @Path("/{tid}/users/{uid}/roles")
   @Consumes("application/json")
   @Produces("application/json")
   public Response createGrant( @Context UriInfo info, 
                                @PathParam("tid") String tid,
                                @PathParam("uid") String uid,  
                                Grant grant) {
      logger.info("Post /tenants/"+tid+"/users/"+uid+"/roles");
      Tenant tenant=null;
      User user=null;
      Role role=null;
      long longTid=0;
      long longUid=0;
      long longRid=0;

      if (grant.getDescription()==null)
         grant.setDescription("");

      // validate tenant id
      try {
         longTid= Long.parseLong(tid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Tenant id :" + tid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         tenant = tenantStore.getTenant(longTid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting tenant");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (tenant==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! Tenant id :" + tid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      grant.setTenantid((int)longTid);

      // validate user id
      try {
         longUid= Long.parseLong(uid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid User id :" + uid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         user = userStore.getUser(longUid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (user==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! User id :" + uid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      grant.setUserid((int)longUid);

      // validate role id
      try {
         longRid= grant.getRoleid();
         logger.info("roleid = " + longRid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Role id :" + grant.getRoleid());
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         role = roleStore.getRole(longRid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting role");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (role==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! role :" + grant.getRoleid());
         return Response.status(404).entity(tokenidmerror).build();
      }

      // see if grant already exists for this 
      try {
         Grant existingGrant = grantStore.getGrant(longTid,longUid,longRid);
         if (existingGrant != null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Grant already exists for tid:"+longTid+" uid:"+longUid+" rid:"+longRid);
            return Response.status(403).entity(tokenidmerror).build();
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error creating grant");
         return Response.status(500).entity(tokenidmerror).build();
      }


      // create grant 
      try {
         grant = grantStore.createGrant(grant);
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error creating grant");
         return Response.status(500).entity(tokenidmerror).build();
      }

      return Response.status(201).entity(grant).build();
   }

   @GET
   @Path("/{tid}/users/{uid}/roles")
   @Produces("application/json")
   public Response getRoles( @Context UriInfo info,
                             @PathParam("tid") String tid,
                             @PathParam("uid") String uid) {
      logger.info("GET /tenants/"+tid+"/users/"+uid+"/roles");
      long longTid=0;
      long longUid=0;
      Tenant tenant=null;
      User user=null;
      Roles roles = new Roles();
      List<Role> roleList = new ArrayList<Role>();

      // validate tenant id
      try {
         longTid= Long.parseLong(tid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Tenant id :" + tid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         tenant = tenantStore.getTenant(longTid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting tenant");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (tenant==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! Tenant id :" + tid);
         return Response.status(404).entity(tokenidmerror).build();
      }

      // validate user id
      try {
         longUid=Long.parseLong(uid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid User id :" + uid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         user = userStore.getUser(longUid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (user==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! User id :" + uid);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         Grants grants = grantStore.getGrants(longTid,longUid);
         List<Grant> grantsList = grants.getGrants();
         for (int i=0; i < grantsList.size(); i++) {
            Grant grant = grantsList.get(i);
            Role role = roleStore.getRole(grant.getRoleid());
            roleList.add(role);
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting Roles");
         return Response.status(500).entity(tokenidmerror).build();
      }

      roles.setRoles(roleList);
      return Response.ok(roles).build();
   }

   @DELETE
   @Path("/{tid}/users/{uid}/roles/{rid}")
   public Response deleteGrant( @Context UriInfo info,
                                @PathParam("tid") String tid,
                                @PathParam("uid") String uid,
                                @PathParam("rid") String rid) {
      long longTid=0;
      long longUid=0;
      long longRid=0;
      Tenant tenant=null;
      User user=null;
      Role role=null;

      // validate tenant id
      try {
         longTid= Long.parseLong(tid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Tenant id :" + tid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         tenant = tenantStore.getTenant(longTid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting tenant");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (tenant==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! Tenant id :" + tid);
         return Response.status(404).entity(tokenidmerror).build();
      }

      // validate user id
      try {
         longUid=Long.parseLong(uid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid User id :" + uid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         user = userStore.getUser(longUid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (user==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! User id :" + uid);
         return Response.status(404).entity(tokenidmerror).build();
      }

      // validate role id
      try {
         longRid=Long.parseLong(rid);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Role id :" + rid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         role = roleStore.getRole(longRid);
      }
      catch(StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting Role");
         return Response.status(500).entity(tokenidmerror).build();
      }
      if (role==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Not found! Role id :" + rid);
         return Response.status(404).entity(tokenidmerror).build();
      }
      
      // see if grant already exists 
      try {
         Grant existingGrant = grantStore.getGrant(longTid,longUid,longRid);
         if (existingGrant == null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Grant does not exist for tid:"+longTid+" uid:"+longUid+" rid:"+longRid);
            return Response.status(404).entity(tokenidmerror).build();
         }
         existingGrant = grantStore.deleteGrant(existingGrant);
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error creating grant");
         return Response.status(500).entity(tokenidmerror).build();
      }


      return Response.status(204).build();
   }

}
