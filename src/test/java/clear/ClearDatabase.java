package clear;

import preparedstatement.crud.PreparedStatementUpdate;

public class ClearDatabase {
  public static void clearAll() {
    String sqlCarpark = "TRUNCATE TABLE carpark";
    String sqlCar = "TRUNCATE TABLE ticket_A";
    String sqlCar2 = "TRUNCATE TABLE ticket_B";
    PreparedStatementUpdate.update(sqlCarpark);
    PreparedStatementUpdate.update(sqlCar);
    PreparedStatementUpdate.update(sqlCar2);
  }
}
