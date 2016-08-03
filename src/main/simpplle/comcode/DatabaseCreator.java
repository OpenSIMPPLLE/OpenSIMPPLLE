/*
 * The University of Montana owns copyright of the designated documentation contained
 * within this file as part of the software product designated by Uniform Resource Identifier
 * UM-OpenSIMPPLLE-1.0. By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all
 * restrictions, requirements, and assertions contained therein. All Other Rights Reserved.
 */

package simpplle.comcode;

//import sun.jdbc.odbc.*;
import java.sql.*;
import org.hsqldb.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.*;
import java.io.File;
import java.util.*;

/**
 * This class has methods to create a database.  It was originally designed to work with Microsoft Access, and much of the commented code is there
 * in case a request is made to go back to that method.  Current versions support Hibernate ORM software libraries.  
 * for more information on Hibernate see link below.
 * 
 * @link http://www.hibernate.org/
 * @author Documentation by Brian Losi
 * <p>Original source code authorship: Kirk A. Moeller
 */

public abstract class DatabaseCreator {
  public static final String USERNAME = "sa";
  public static final String PASSWORD = "";

//  private static Connection conn=null;
//  private static Statement  statement=null;
//  private static boolean    hsqldb=false;

  private static Configuration  cfg;
  private static SessionFactory sessions;


  public static void initHibernate(boolean create, File path) throws SimpplleError {
    initHibernate(path.toString(),create);
  }
  public static void initHibernate(String path, boolean create) throws SimpplleError {


    String url = "jdbc:hsqldb:file:" + path;
    System.setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver");
    System.setProperty("hibernate.connection.url",url);

    System.setProperty("hibernate.connection.hsqldb.default_table_type","cached");
    System.setProperty("hibernate.connection.hsqldb.cache_file_scale","8");
    System.setProperty("hibernate.connection.runtime.gc_interval","50000");

    System.setProperty("hibernate.connection.username",USERNAME);
    System.setProperty("hibernate.connection.password",PASSWORD);
    System.setProperty("hibernate.dialect","org.hibernate.dialect.HSQLDialect");
    System.setProperty("hibernate.jdbc.batch_size","30");
    System.setProperty("hibernate.max_fetch_depth","4");
//    System.setProperty("hibernate.cache.use_second_level_cache","false");

    if (create) {
      System.setProperty("hibernate.hbm2ddl.auto", "create");
    }
    else {
      System.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    try {
      cfg = new Configuration();
      cfg.addClass(simpplle.comcode.VegSimStateData.class);
      cfg.addClass(simpplle.comcode.Lifeform.class);
      cfg.addClass(simpplle.comcode.ProcessType.class);
      cfg.addClass(simpplle.comcode.AreaSummaryDataNew.class);
      cfg.setProperties(System.getProperties());
      sessions = cfg.buildSessionFactory();
    }
    catch (HibernateException ex) {
      throw new SimpplleError(ex.getMessage(),ex);
    }
  }

  public static void closeHibernate() throws SimpplleError {
    if (sessions == null) { return; }
    try {
      org.hibernate.Session session = sessions.openSession();
      session.connection().createStatement().execute("SHUTDOWN");
      session.close();

      sessions.close();
    }
    catch (SQLException ex) {
      throw new SimpplleError(ex.getMessage(),ex);
    }
    catch (HibernateException ex) {
      throw new SimpplleError(ex.getMessage(),ex);
    }
  }

  public static Configuration getConfiguration() { return cfg; }
  public static SessionFactory getSessionFactory() { return sessions; }

//  public static void connectAccess(String path) throws SQLException {
//    // Load the odbc driver.
//   try {
//     Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//   }
//   catch (ClassNotFoundException err) {
//     System.err.println("JdbcOdbcDriver not found");
//   }
//
//   String url = "jdbc:odbc:MS Access Database;DBQ=" + path;
//   conn = DriverManager.getConnection(url);
//   hsqldb = false;
//  }

//  private static void test() throws SQLException {
//    update("INSERT INTO COFFEES " +
//           "VALUES ('Colombian', 101, 7.99, 0, 0)");
//    update("INSERT INTO COFFEES " +
//           "VALUES ('French_Roast', 49, 8.99, 0, 0)");
//    update("INSERT INTO COFFEES " +
//           "VALUES ('Espresso', 150, 9.99, 0, 0)");
//    update("INSERT INTO COFFEES " +
//           "VALUES ('Colombian_Decaf', 101, 8.99, 0, 0)");
//    update("INSERT INTO COFFEES " +
//           "VALUES ('French_Roast_Decaf', 49, 9.99, 0, 0)");
//
//    query("SELECT * FROM COFFEES");
//  }

//  private static void createStatement()  throws SQLException {
//    statement = conn.createStatement();
//  }

//  public static void query(String expression) throws SQLException {
//    if (statement == null) { createStatement(); }
//
//    ResultSet rs = statement.executeQuery(expression);
//  }

//  public static void update(String expression) throws SQLException {
//    if (statement == null) {
//      createStatement();
//    }
//
//    int i = statement.executeUpdate(expression); // run the query
//  }
//  public static void addBatch(String expression) throws SQLException {
//    if (statement == null) {
//      createStatement();
//    }
//
//    statement.addBatch(expression);
//  }
//  public static void executeBatch() throws SQLException {
//    if (statement != null) {
//      statement.executeBatch();
//      statement.clearBatch();
//    }
//  }

//  public static PreparedStatement getPreparedStatment(String stmt) throws SQLException {
//    return conn.prepareStatement(stmt);
//  }

//  public static void dump(ResultSet rs) throws SQLException {
//
//      // the order of the rows in a cursor
//      // are implementation dependent unless you use the SQL ORDER statement
//      ResultSetMetaData meta   = rs.getMetaData();
//      int               colmax = meta.getColumnCount();
//      int               i;
//      Object            o       = null;
//
//      // the result set is a cursor into the data.  You can only
//      // point to one row at a time
//      // assume we are pointing to BEFORE the first row
//      // rs.next() points to next row and returns true
//      // or false if there is no next row, which breaks the loop
//      for (;rs.next();) {
//          for (i = 0; i < colmax; ++i) {
//              o = rs.getObject(i + 1);    // Is SQL the first column is indexed
//                                          // with 1 not 0
//              System.out.print(o.toString() + " ");
//          }
//
//          System.out.println(" ");
//      }
//  }                                       //void dump( ResultSet rs )

//  public static void shutdown() throws SQLException {
//    if (statement != null) {
//      statement.close();
//      statement = null;
//    }
//
//    conn.close();   // if there are no other open connection
//                    // db writes out to files and shuts down
//                    // this happens anyway at garbage collection
//                    // when program ends
//    conn = null;
//  }


}
