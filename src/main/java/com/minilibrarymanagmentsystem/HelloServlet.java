package com.minilibrarymanagmentsystem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;

    private String message;

    public void init() throws ServletException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Initialize the database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "");
            statement = conn.createStatement();
            message = "zyzz!";
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection initialization failed", e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1><strong>" + message + "</strong></h1>");

        try {
            resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                out.println("<p>" + resultSet.getString("email") + "</p>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error retrieving data</p>");
        }

        out.println("</body></html>");
    }

    public void destroy() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
