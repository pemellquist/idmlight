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
import org.pem.openidm.model.Domains;
import org.pem.openidm.model.Domain;

public class DomainStore {
   private static Logger logger       = Logger.getLogger(DomainStore.class);
   protected Connection  dbConnection = null;
   private static Calendar calendar = Calendar.getInstance();
   
   protected final static String SQL_ID             = "domainid";
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

   protected Domain rsToDomain(ResultSet rs) throws SQLException {
      Domain domain = new Domain();
      try {
         domain.setDomainid(rs.getInt(SQL_ID));
         domain.setName(rs.getString(SQL_NAME));
         domain.setDescription(rs.getString(SQL_DESCR));
         domain.setEnabled(rs.getInt(SQL_ENABLED)==1?true:false);
      }
      catch (SQLException sqle) {
         logger.error( "SQL Exception : " + sqle);
            throw sqle;
      }
      return domain;
   }
   
   public Domains getDomains() throws StoreException {
      Domains domains = new Domains();
      List<Domain> domainList = new ArrayList<Domain>();
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM domains";
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         while (rs.next()) {
            Domain domain = rsToDomain(rs);
            domainList.add(domain);
         }
         rs.close();
         stmt.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
      domains.setDomains(domainList);
      return domains;
   }

   public Domain getDomain(long id) throws StoreException {
      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "SELECT * FROM domains WHERE domainid=" + id;
      try {
         stmt=conn.createStatement();
         ResultSet rs=stmt.executeQuery(query);
         if (rs.next()) {
            Domain domain = rsToDomain(rs);
            rs.close();
            stmt.close();
            dbClose();
            return domain;
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

   public Domain createDomain(Domain domain) throws StoreException {
       int key=0;
       Connection conn = dbConnect();
       try {
          String query = "insert into domains (name,description,enabled) values(?, ?, ?)";
          PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
          statement.setString(1,domain.getName());
          statement.setString(2,domain.getDescription());
          statement.setInt(3,domain.getEnabled()?1:0);
          int affectedRows = statement.executeUpdate();
          if (affectedRows == 0) 
             throw new StoreException("Creating domain failed, no rows affected."); 
          ResultSet generatedKeys = statement.getGeneratedKeys();
          if (generatedKeys.next()) 
             key = generatedKeys.getInt(1);
          else 
             throw new StoreException("Creating domain failed, no generated key obtained.");
          domain.setDomainid(key);
          dbClose();
          return domain;
       }
       catch (SQLException s) {
          dbClose();
          throw new StoreException("SQL Exception : " + s);
       }
   }

   public Domain putDomain(Domain domain) throws StoreException {
      Domain savedDomain = this.getDomain(domain.getDomainid());
      if (savedDomain==null)
         return null;
    
      if (domain.getDescription()!=null)
         savedDomain.setDescription(domain.getDescription());
      if (domain.getName()!=null)
         savedDomain.setName(domain.getName());
      if (domain.getEnabled()!=null)
         savedDomain.setEnabled(domain.getEnabled()); 

      Connection conn = dbConnect();
      try {
         String query = "UPDATE domains SET name = ?, description = ?, enabled = ? WHERE domainid = ?";
         PreparedStatement statement = conn.prepareStatement(query);
         statement.setString(1, savedDomain.getName());
         statement.setString(2, savedDomain.getDescription());
         statement.setInt(3, savedDomain.getEnabled()?1:0);
         statement.setInt(4,savedDomain.getDomainid());
         statement.executeUpdate();
         statement.close();
         dbClose();
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }

      return savedDomain;
   }

   public Domain deleteDomain(Domain domain) throws StoreException {
      Domain savedDomain = this.getDomain(domain.getDomainid());
      if (savedDomain==null)
         return null;

      Connection conn = dbConnect();
      Statement stmt=null;
      String query = "DELETE FROM domains WHERE domainid=" + domain.getDomainid();
      try {
         stmt=conn.createStatement();
         int deleteCount = stmt.executeUpdate(query);
         logger.info("deleted " + deleteCount + " records");
         stmt.close();
         dbClose();
         return savedDomain;
      }
      catch (SQLException s) {
         dbClose();
         throw new StoreException("SQL Exception : " + s);
      }
   }
   
}

