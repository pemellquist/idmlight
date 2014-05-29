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
import org.pem.tokenidm.model.Tenants;
import org.pem.tokenidm.model.TokenIDMError;
import org.pem.tokenidm.persistence.TenantStore;
import org.pem.tokenidm.persistence.StoreException;
	
@Path("/V1/tenants")
public class TenantHandler {
	
   private static Logger logger = Logger.getLogger(TenantHandler.class);	
   private static TenantStore tenantStore = new TenantStore();
   
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
         tenant.setId((int)longId);
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
         tenant.setId((int)longId);
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
 
}
