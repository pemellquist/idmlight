package org.pem.openidm.handlers;

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
import org.pem.openidm.model.Role; 
import org.pem.openidm.model.Roles;
import org.pem.openidm.model.TokenIDMError;
import org.pem.openidm.persistence.RoleStore;
import org.pem.openidm.persistence.StoreException;
	
@Path("/v1/roles")
public class RoleHandler {
	
   private static Logger logger = Logger.getLogger(RoleHandler.class);	
   private static RoleStore roleStore = new RoleStore();
   
   @GET
   @Produces("application/json")
   public Response getRoles() {
      logger.info("Get /roles");
      Roles roles=null;
      try {
         roles = roleStore.getRoles();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting roles");
         return Response.status(500).entity(tokenidmerror).build();
      }
      return Response.ok(roles).build();
   }

   @GET
   @Path("/{id}")
   @Produces("application/json")
   public Response getRole(@PathParam("id") String id)  {
      logger.info("Get /roles/" + id);
      Role role = null;
      long longId=0;
      try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Role id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         role = roleStore.getRole(longId);
      }
      catch(StoreException se) {
         logger.error("Store Exception : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting role");
         return Response.status(500).entity(tokenidmerror).build();
      }

      if (role==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Role Not found!  id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }
      return Response.ok(role).build();
   }

   @POST
   @Consumes("application/json")
   @Produces("application/json")
   public Response createRole(@Context UriInfo info,Role role) {
      logger.info("Post /roles");
      try {
         if (role.getName()==null)
            role.setName("");
         if (role.getDescription()==null)
            role.setDescription("");
         role = roleStore.createRole(role);
      }
      catch (StoreException se) {
         logger.error("Store Exception : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error creating role");
         return Response.status(500).entity(tokenidmerror).build();
      } 

      return Response.status(201).entity(role).build();
   } 


   @PUT
   @Path("/{id}")
   @Consumes("application/json")
   @Produces("application/json")
   public Response putRole(@Context UriInfo info,Role role,@PathParam("id") String id) {
      long longId=0;
      logger.info("Put /roles/" + id);
       try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Role id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         role.setRoleid((int)longId);
         role = roleStore.putRole(role);
         if (role==null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Not found! Role id :" + id);
            return Response.status(404).entity(tokenidmerror).build();
         }

         return Response.status(200).entity(role).build();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error putting role");
         return Response.status(500).entity(tokenidmerror).build();
      }
   }

   @DELETE
   @Path("/{id}")
   public Response deleteRole(@Context UriInfo info,@PathParam("id") String id) {
      long longId=0;
      logger.info("Delete /roles/" + id);
       try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid Role id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         Role role = new Role();
         role.setRoleid((int)longId);
         role = roleStore.deleteRole(role);
         if (role==null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Not found! Role id :" + id);
            return Response.status(404).entity(tokenidmerror).build();
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error deleting role");
         return Response.status(500).entity(tokenidmerror).build();
      }

      return Response.status(204).build();
   }

}
