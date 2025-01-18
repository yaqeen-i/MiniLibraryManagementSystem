package com.minilibrarymanagmentsystem;

import utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "registerServlet", value = "/register-servlet")
public class RegisterServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // No need to establish a connection here as DBUtil will handle it
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phoneNumber");
        String role = request.getParameter("role");

        Connection conn = null;
        try {
            // Get the connection using DBUtil
            conn = DBUtil.getConnection();

            // Check if email or phone number already exists
            String checkQuery = "SELECT * FROM users WHERE email = ? OR phone_number = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {
                psCheck.setString(1, email);
                psCheck.setString(2, phone);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    // If a matching email or phone number is found, show an error message and forward to register.jsp
                    request.setAttribute("message", "Email or phone number already exists. Please use a different one.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    return; // Exit the method if a duplicate is found
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("message", "Error: " + e.getMessage());
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return; // Exit if there's an error checking the database
            }

            // Prepare the SQL query for insertion
            String sql = "INSERT INTO users (username, password, full_name, email, phone_number, role) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, fullName);
                ps.setString(4, email);
                ps.setString(5, phone);
                ps.setString(6, role.toUpperCase());

                int rowsInserted = ps.executeUpdate();
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();

                // Check if insertion is successful and respond accordingly
                if (rowsInserted > 0) {
                    out.println("<script>alert('Registration successful!'); window.location.href='index.jsp';</script>");
                } else {
                    out.println("<script>alert('Registration failed! Please try again.'); window.location.href='register.jsp';</script>");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('Error: " + e.getMessage() + "'); window.location.href='register.jsp';</script>");
        } finally {
            // Close the connection
            try {
                if (conn != null) {
                    DBUtil.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        // No need to close connection here since it will be handled by DBUtil
    }
}
