package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionUtil {
    private static final Object lock = new Object();

    public static String generateTransactionID(Connection conn) throws SQLException {
        synchronized (lock) {
            String query = "SELECT MAX(transaction_id) FROM transactions";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getString(1) != null) {
                    String lastId = rs.getString(1);
                    int numericPart = Integer.parseInt(lastId.substring(2));
                    return "BT" + String.format("%06d", numericPart + 1);
                }
            }
            return "BT000001"; // First ID
        }
    }
}
