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

@WebServlet("/updateUserServlet")
public class UpdateUserServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String role = request.getParameter("role");

        String sql = "UPDATE users SET username = ?,full_name = ?,  email = ?,phone_number = ?, role = ? WHERE user_id = ?";
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, fullName);
                ps.setString(3, email);
                ps.setString(4, phoneNumber);
                ps.setString(5, role);
                ps.setInt(6, user_id);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    response.sendRedirect("admin-dashboard.jsp");  // Redirect back to the admin page
                } else {
                    response.getWriter().println("Error updating user");
                }
            } catch (SQLException e) {
                response.getWriter().println("Error updating user: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    DBUtil.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int user_id = Integer.parseInt(request.getParameter("user_id"));
        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        String sql = "UPDATE users SET username = ?,full_name = ?,  email = ?,phone_number = ? WHERE user_id = ?";
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, fullName);
                ps.setString(3, email);
                ps.setString(4, phoneNumber);
                ps.setInt(5, user_id);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    response.sendRedirect("librarian-dashboard.jsp");  // Redirect back to the librarian page
                } else {
                    response.getWriter().println("Error updating user");
                }
            } catch (SQLException e) {
                response.getWriter().println("Error updating user: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
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
