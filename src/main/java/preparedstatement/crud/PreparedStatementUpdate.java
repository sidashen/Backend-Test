package preparedstatement.crud;

import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PreparedStatementUpdate {
  public static int update(String sql, Object ...args) {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      conn = JDBCUtils.getConnection();
      ps = conn.prepareStatement(sql);
      for (int i = 0; i < args.length; i++) {
        ps.setObject(i + 1, args[i]);
      }
      return ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBCUtils.close(ps, conn);
    }
    return 0;
  }

}
