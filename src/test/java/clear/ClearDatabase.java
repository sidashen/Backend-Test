package clear;

import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearDatabase {
  public static void clearAll() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = JDBCUtils.getConnection();
      String sqlCarpark = "TRUNCATE TABLE carpark;";
      String sqlCar = "TRUNCATE TABLE car;";
      preparedStatement = connection.prepareStatement(sqlCarpark);
      preparedStatement.executeLargeUpdate();
      preparedStatement = connection.prepareStatement(sqlCar);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.close(preparedStatement, connection);
    }
  }
}
