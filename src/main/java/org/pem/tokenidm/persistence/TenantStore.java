package org.pem.tokenidm.persistence;

/**
 * @author pemellquist@gmail.com
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.sql.*;
import org.apache.log4j.Logger;
import org.pem.tokenidm.TokenIDM;
import org.pem.tokenidm.model.Tenants;
import org.pem.tokenidm.model.Tenant;

public class TenantStore {
   private static Logger logger       = Logger.getLogger(TenantStore.class);
   protected Connection  dbConnection = null;
   private static Calendar calendar = Calendar.getInstance();
   
   protected final static String SQL_ID             = "id";
   protected final static String SQL_NAME           = "name";
   protected final static String SQL_DESCR          = "description";
   protected final static String SQL_ENABLED        = "enabled";
	
   protected Connection dbConnect() throws StoreException {
      if ( dbConnection==null ) {
         try {           
	    Class.forName (TokenIDM.tokenIDMConfig.dbDriver).newInstance ();
	    dbConnection = DriverManager.getConnection (TokenIDM.tokenIDMConfig.dbPath); 
            return dbConnection;
         }
         catch (Exception e) {
            throw new StoreException("Cannot connect to database server "+ e);
         }       
      }
      else {
         try {
            if ( dbConnection.isClosed()) {
               try {          
		  Class.forName (TokenIDM.tokenIDMConfig.dbDriver).newInstance ();
		  dbConnection = DriverManager.getConnection (TokenIDM.tokenIDMConfig.dbPath);
		  return dbConnection;
               }
               catch (Exception e) {
                  throw new StoreException("Cannot connect to database server "+ e);
               }      
            }
            else
               return dbConnection;
         }
	 catch (SQLException sqe) {
            throw new StoreException("Cannot connect to database server "+ sqe);
         }
      }
   }
      
   protected void dbClose() {
      if (dbConnection != null)
      {
         try {
            dbConnection.close ();
          }
          catch (Exception e) { 
            logger.error("Cannot close Database Connection " + e);
          }
       }
   }
	
   protected void finalize ()  {
      dbClose();
   }

   protected Tenant rsToTenant(ResultSet rs) throws SQLException {
      Tenant tenant = new Tenant();
      try {
         tenant.setId(rs.getInt(SQL_ID));
         tenant.setName(rs.getString(SQL_NAME));
         tenant.setDescription(rs.getString(SQL_DESCR));
         tenant.setEnabled(rs.getInt(SQL_ENABLED)==1?true:false);
      }
      catch (SQLException sqle) {
         logger.error( "SQL Exception : " + sqle);
            throw sqle;
      }
      return tenant;
   }
   
   public Tenants getTenants() throws StoreException {
      Tenants tenants = new Tenants();
      List<Tenant> tenantList = new ArrayList<Tenant>();
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM tenants";
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         while (rs.next()) {
            Tenant tenant = rsToTenant(rs);
            tenantList.add(tenant);
         }
         rs.close();
         stmt.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
      tenants.setTenants(tenantList);
      return tenants;
   }

   public Tenant getTenant(long id) throws StoreException {
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM tenants WHERE id=" + id;
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         if (rs.next()) {
            Tenant tenant = rsToTenant(rs);
            rs.close();
            stmt.close();
            dbClose();
            return tenant;
         }
         else {
            rs.close();
            stmt.close();
            dbClose();
            return null; 
         } 
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
   }

   public Tenant createTenant(Tenant tenant) throws StoreException {
       int key=0;
       Connection conn = dbConnect();
       try {
          String query = "insert into tenants (name,description,enabled) values(?, ?, ?)";
          PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1,tenant.getName());
          statement.setString(2,tenant.getDescription());
          statement.setInt(3,tenant.getEnabled()?1:0);
          int affectedRows = statement.executeUpdate();
          if (affectedRows == 0) 
             throw new StoreException("Creating tenant failed, no rows affected."); 
          ResultSet generatedKeys = statement.getGeneratedKeys();
          if (generatedKeys.next()) 
             key = generatedKeys.getInt(1);
          else 
             throw new StoreException("Creating tenant failed, no generated key obtained.");
          tenant.setId(key);
          dbClose();
          return tenant;
       }
       catch (SQLException s) {
          dbClose();
          throw new StoreException("SQL Exception : " + s);
       }
   }

   public Tenant putTenant(Tenant tenant) throws StoreException {
      
      Tenant savedTenant = this.getTenant(tenant.getId());
      if (savedTenant==null)
         return null;
    
      if (tenant.getDescription()!=null)
         savedTenant.setDescription(tenant.getDescription());
      if (tenant.getName()!=null)
         savedTenant.setName(tenant.getName());
      if (tenant.getEnabled()!=null)
         savedTenant.setEnabled(tenant.getEnabled()); 

      Connection conn = dbConnect();
      try {
         String query = "UPDATE tenants SET name = ?, description = ?, enabled = ? WHERE id = ?";
         PreparedStatement statement = conn.prepareStatement(query);
         statement.setString(1, savedTenant.getName());
         statement.setString(2, savedTenant.getDescription());
         statement.setInt(3, savedTenant.getEnabled()?1:0);
         statement.setInt(4,savedTenant.getId());
         statement.executeUpdate();
         statement.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }

      return savedTenant;
   }

   public Tenant deleteTenant(Tenant tenant) throws StoreException {
      Tenant savedTenant = this.getTenant(tenant.getId());
      if (savedTenant==null)
         return null;

      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "DELETE FROM tenants WHERE id=" + tenant.getId();
      try {
         stmt=conn.createStatement();
         int deleteCount = stmt.executeUpdate(query);
         logger.info("deleted " + deleteCount + " records");
         stmt.close();
         dbClose();
         return savedTenant;
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
   }
   
}

