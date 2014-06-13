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
import org.pem.tokenidm.model.Grant;
import org.pem.tokenidm.model.Grants;

public class GrantStore {
   private static Logger logger       = Logger.getLogger(GrantStore.class);
   protected Connection  dbConnection = null;
   private static Calendar calendar = Calendar.getInstance();
   
   protected final static String SQL_ID             = "grantid";
   protected final static String SQL_DESCR          = "description";
   protected final static String SQL_TENANTID       = "tenantid";
   protected final static String SQL_USERID         = "userid";
   protected final static String SQL_ROLEID         = "roleid";

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

   protected Grant rsToGrant(ResultSet rs) throws SQLException {
      Grant grant = new Grant();
      try {
         grant.setGrantid(rs.getInt(SQL_ID));
         grant.setDescription(rs.getString(SQL_DESCR));
         grant.setTenantid(rs.getInt(SQL_TENANTID));
         grant.setUserid(rs.getInt(SQL_USERID));
         grant.setRoleid(rs.getInt(SQL_ROLEID));
      }
      catch (SQLException sqle) {
         logger.error( "SQL Exception : " + sqle);
            throw sqle;
      }
      return grant;
   }
   
   public Grants getGrants(long tid, long uid) throws StoreException {
      Grants grants = new Grants();
      List<Grant> grantList = new ArrayList<Grant>();
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM grants WHERE tenantid=" + tid + " AND userid="+uid;
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         while (rs.next()) {
            Grant grant = rsToGrant(rs);
            grantList.add(grant);
         }
         rs.close();
         stmt.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
      grants.setGrants(grantList);
      return grants;
   }

   public Grant  getGrant(long id) throws StoreException {
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM grants WHERE grantid=" + id;
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         if (rs.next()) {
            Grant grant = rsToGrant(rs);
            rs.close();
            stmt.close();
            dbClose();
            return grant;
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

   public Grant getGrant(long tid,long uid,long rid) throws StoreException {
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM grants WHERE tenantid=" + tid + " AND userid=" + uid + " AND roleid="+rid;
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         if (rs.next()) {
            Grant grant = rsToGrant(rs);
            rs.close();
            stmt.close();
            dbClose();
            return grant;
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


   public Grant createGrant(Grant grant) throws StoreException {
       int key=0;
       Connection conn = dbConnect();
       try {
          String query = "insert into grants  (description,tenantid,userid,roleid) values(?,?,?,?)";
          PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1,grant.getDescription());
          statement.setInt(2,grant.getTenantid());
          statement.setInt(3,grant.getUserid());
          statement.setInt(4,grant.getRoleid());
          int affectedRows = statement.executeUpdate();
          if (affectedRows == 0) 
             throw new StoreException("Creating grant failed, no rows affected."); 
          ResultSet generatedKeys = statement.getGeneratedKeys();
          if (generatedKeys.next()) 
             key = generatedKeys.getInt(1);
          else 
             throw new StoreException("Creating grant failed, no generated key obtained.");
          grant.setGrantid(key);
          dbClose();
          return grant;
       }
       catch (SQLException s) {
          dbClose();
          throw new StoreException("SQL Exception : " + s);
       }
   }

   public Grant deleteGrant(Grant grant) throws StoreException {
      Grant savedGrant = this.getGrant(grant.getGrantid());
      if (savedGrant==null)
         return null;

      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "DELETE FROM grants WHERE grantid=" + grant.getGrantid();
      try {
         stmt=conn.createStatement();
         int deleteCount = stmt.executeUpdate(query);
         logger.info("deleted " + deleteCount + " records");
         stmt.close();
         dbClose();
         return savedGrant;
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
   }
   
}

