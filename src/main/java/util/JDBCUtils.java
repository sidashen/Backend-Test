package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class JDBCUtils {
  private static String URL;
  private static String USER;
  private static String PASSWORD;
  private static String DRIVER;

  static {
    try {
      Properties pro = new Properties();
      ClassLoader classLoader = JDBCUtils.class.getClassLoader();
      String path = Objects.requireNonNull(classLoader.getResource("jdbc.properties")).getPath();
      pro.load(new FileInputStream(path));
      URL = pro.getProperty("url");
      USER = pro.getProperty("user");
      PASSWORD = pro.getProperty("password");
      DRIVER = pro.getProperty("driver");
      Class.forName(DRIVER);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() throws SQLException {

    return DriverManager.getConnection(URL, USER, PASSWORD);
  }

  public static void close(Statement stmt, Connection conn) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static void close(ResultSet rs, Statement stmt, Connection conn) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    close(stmt, conn);
  }
}
