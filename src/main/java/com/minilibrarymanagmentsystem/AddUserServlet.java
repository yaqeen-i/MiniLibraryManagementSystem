package com.minilibrarymanagmentsystem;

import utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "addUserServlet", value = "/addUserServlet")
public class AddUserServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String role = request.getParameter("role");
        String password = request.getParameter("password");

        // Validate input
        if (name == null || fullName == null ||email == null || role == null || password == null || phoneNumber == null || name.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            response.getWriter().println("Error: All fields are required.");
            return;
        }
        Connection conn = null;
        try {

            conn = DBUtil.getConnection();

            // Insert user into the database
            String sql = "INSERT INTO users (username, email, full_name, phone_number, role, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, fullName);
            ps.setString(4, phoneNumber);
            ps.setString(5, role);
            ps.setString(6, password);

            int rowsAffected = ps.executeUpdate();

            ps.close();
            conn.close();

            if (rowsAffected > 0) {
                // Redirect back to admin dashboard after successful addition
                response.sendRedirect("admin-dashboard.jsp");
            } else {
                response.getWriter().println("Error: Unable to add user.");
            }

        } catch (Exception e) {
            response.getWriter().println("Error adding user: " + e.getMessage());
        }finally {
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

    }
}
