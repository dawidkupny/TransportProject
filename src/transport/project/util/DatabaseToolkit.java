package transport.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseToolkit {
  private static DatabaseToolkit instance;
  private Connection connection;
  private Statement statement;

  public static synchronized DatabaseToolkit getInstance() {
    if(instance==null) instance = new DatabaseToolkit();
    return instance;
  }

  public void connect() {
   final String DB_URL = "jdbc:mysql://localhost/transport";
   final String DRIVER = "com.mysql.jdbc.Driver";
   final String USERNAME = "root";
   final String PASSWORD = "MyNewPass";
    try {
        Class.forName(DRIVER);
        connection = (Connection)DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
        statement = (Statement)connection.createStatement();

     } catch (ClassNotFoundException ex) {
        System.err.println("Error" +ex.getMessage()); //TO REMOVE
     } catch (SQLException ex) {
        System.err.println("Error: "+ex.getMessage()); //TO REMOVE
     }

  }

  public void disconnect() {
    try { if(connection!=null)
          { statement.close();
            connection.close();}
    } catch (SQLException ex) {
       System.err.println("Error: "+ex.getMessage());
    }
  }

  public ResultSet executeQuery(String query) {
    try {
        connect();
        ResultSet result = statement.executeQuery(query);
      
        return result;
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseToolkit.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public int executeUpdate(String query){
      try {
          connect();
          int result = statement.executeUpdate(query);
      } catch (SQLException ex) {
          Logger.getLogger(DatabaseToolkit.class.getName()).log(Level.SEVERE, null, ex);
      }
      return -1;
  }
}
