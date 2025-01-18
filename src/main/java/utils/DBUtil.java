package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/library_system"; // Database URL
    private static final String USER = "root"; // Your DB username
    private static final String PASSWORD = ""; // Your DB password

    // Establish and return a new connection each time it's requested
    public static Connection getConnection() throws SQLException {
        try {
            // Register the MySQL JDBC driver if needed
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database Driver not found.", e);
        }
    }

    // Close the connection
    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
