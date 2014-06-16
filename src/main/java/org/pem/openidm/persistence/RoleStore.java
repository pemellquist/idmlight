package org.pem.openidm.persistence;

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
import org.pem.openidm.TokenIDM;
import org.pem.openidm.model.Roles;
import org.pem.openidm.model.Role;

public class RoleStore {
   private static Logger logger       = Logger.getLogger(RoleStore.class);
   protected Connection  dbConnection = null;
   private static Calendar calendar = Calendar.getInstance();
   
   protected final static String SQL_ID             = "roleid";
   protected final static String SQL_NAME           = "name";
   protected final static String SQL_DESCR          = "description";
	
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

   protected  Role rsToRole(ResultSet rs) throws SQLException {
      Role role = new Role();
      try {
         role.setRoleid(rs.getInt(SQL_ID));
         role.setName(rs.getString(SQL_NAME));
         role.setDescription(rs.getString(SQL_DESCR));
      }
      catch (SQLException sqle) {
         logger.error( "SQL Exception : " + sqle);
            throw sqle;
      }
      return role;
   }
   
   public Roles getRoles() throws StoreException {
      Roles roles = new Roles();
      List<Role> roleList = new ArrayList<Role>();
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM roles";
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         while (rs.next()) {
            Role role = rsToRole(rs);
            roleList.add(role);
         }
         rs.close();
         stmt.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
      roles.setRoles(roleList);
      return roles;
   }

   public Role getRole(long id) throws StoreException {
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM roles WHERE roleid=" + id;
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         if (rs.next()) {
            Role role = rsToRole(rs);
            rs.close();
            stmt.close();
            dbClose();
            return role;
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

   public Role createRole(Role role) throws StoreException {
       int key=0;
       Connection conn = dbConnect();
       try {
          String query = "insert into roles (name,description) values(?,?)";
          PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1,role.getName());
          statement.setString(2,role.getDescription());
          int affectedRows = statement.executeUpdate();
          if (affectedRows == 0) 
             throw new StoreException("Creating role failed, no rows affected."); 
          ResultSet generatedKeys = statement.getGeneratedKeys();
          if (generatedKeys.next()) 
             key = generatedKeys.getInt(1);
          else 
             throw new StoreException("Creating role failed, no generated key obtained.");
          role.setRoleid(key);
          dbClose();
          return role;
       }
       catch (SQLException s) {
          dbClose();
          throw new StoreException("SQL Exception : " + s);
       }
   }

   public Role putRole(Role role) throws StoreException {
      
      Role savedRole = this.getRole(role.getRoleid());
      if (savedRole==null)
         return null;
    
      if (role.getDescription()!=null)
         savedRole.setDescription(role.getDescription());
      if (role.getName()!=null)
         savedRole.setName(role.getName());

      Connection conn = dbConnect();
      try {
         String query = "UPDATE roles SET name = ?, description = ? WHERE roleid = ?";
         PreparedStatement statement = conn.prepareStatement(query);
         statement.setString(1, savedRole.getName());
         statement.setString(2, savedRole.getDescription());
         statement.setInt(3,savedRole.getRoleid());
         statement.executeUpdate();
         statement.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }

      return savedRole;
   }

   public Role deleteRole(Role role) throws StoreException {
      Role savedRole = this.getRole(role.getRoleid());
      if (savedRole==null)
         return null;

      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "DELETE FROM roles WHERE roleid=" + role.getRoleid();
      try {
         stmt=conn.createStatement();
         int deleteCount = stmt.executeUpdate(query);
         logger.info("deleted " + deleteCount + " records");
         stmt.close();
         dbClose();
         return savedRole;
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
   }
   
}

