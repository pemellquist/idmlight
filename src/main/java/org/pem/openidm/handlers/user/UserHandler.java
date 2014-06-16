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
import org.pem.openidm.model.User; 
import org.pem.openidm.model.Users;
import org.pem.openidm.model.TokenIDMError;
import org.pem.openidm.persistence.UserStore;
import org.pem.openidm.persistence.StoreException;
	
@Path("/v1/users")
public class UserHandler {
	
   private static Logger logger = Logger.getLogger(UserHandler.class);	
   private static UserStore userStore = new UserStore();
   protected final static String DEFAULT_PWD = "changeme";
   
   @GET
   @Produces("application/json")
   public Response getUsers() {
      Users users=null;
      try {
         users = userStore.getUsers();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting users");
         return Response.status(500).entity(tokenidmerror).build();
      }
      return Response.ok(users).build();
   }


   @GET
   @Path("/{id}")
   @Produces("application/json")
   public Response getUser(@PathParam("id") String id)  {
      logger.info("Get /users/" + id);
      User user = null;
      long longId=0;
      try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid User id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }
      try {
         user = userStore.getUser(longId);
      }
      catch(StoreException se) {
         logger.error("Store Exception : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error getting user");
         return Response.status(500).entity(tokenidmerror).build();
      }

      if (user==null) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("User Not found!  id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }
      return Response.ok(user).build();
   }

   @POST
   @Consumes("application/json")
   @Produces("application/json")
   public Response createUser(@Context UriInfo info,User user) {
      logger.info("Post /users");
      try {
         if (user.getEnabled()==null)
            user.setEnabled(false);
         if (user.getName()==null)
            user.setName("");
         if (user.getDescription()==null)
            user.setDescription("");
         if (user.getEmail()==null)
            user.setEmail("");
         if (user.getPassword()==null)
            user.setPassword(DEFAULT_PWD);
         user = userStore.createUser(user);
      }
      catch (StoreException se) {
         logger.error("Store Exception : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error creating user");
         return Response.status(500).entity(tokenidmerror).build();
      } 

      return Response.status(201).entity(user).build();
   } 


   @PUT
   @Path("/{id}")
   @Consumes("application/json")
   @Produces("application/json")
   public Response putUser(@Context UriInfo info,User user,@PathParam("id") String id) {
      long longId=0;
      logger.info("Put /users/" + id);
       try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid User id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         user.setUserid((int)longId);
         user = userStore.putUser(user);
         if (user==null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Not found! User id :" + id);
            return Response.status(404).entity(tokenidmerror).build();
         }

         return Response.status(200).entity(user).build();
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error putting user");
         return Response.status(500).entity(tokenidmerror).build();
      }
   }

   @DELETE
   @Path("/{id}")
   public Response deleteUser(@Context UriInfo info,@PathParam("id") String id) {
      long longId=0;
      logger.info("Delete /users/" + id);
       try {
         longId= Long.parseLong(id);
      }
      catch (NumberFormatException nfe) {
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Invalid User id :" + id);
         return Response.status(404).entity(tokenidmerror).build();
      }

      try {
         User user = new User();
         user.setUserid((int)longId);
         user = userStore.deleteUser(user);
         if (user==null) {
            TokenIDMError tokenidmerror = new TokenIDMError();
            tokenidmerror.setMessage("Not found! User id :" + id);
            return Response.status(404).entity(tokenidmerror).build();
         }
      }
      catch (StoreException se) {
         logger.error("StoreException : " + se);
         TokenIDMError tokenidmerror = new TokenIDMError();
         tokenidmerror.setMessage("Internal error deleting user");
         return Response.status(500).entity(tokenidmerror).build();
      }

      return Response.status(204).build();
   }

 
}
