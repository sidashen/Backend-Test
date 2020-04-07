package clear;

import preparedstatement.crud.PreparedStatementUpdate;

public class ClearDatabase {
  public static void clearAll() {
    String sqlCarpark = "TRUNCATE TABLE carpark";
    String sqlTicket = "TRUNCATE TABLE ticket";
    PreparedStatementUpdate.update(sqlCarpark);
    PreparedStatementUpdate.update(sqlTicket);
  }
}
