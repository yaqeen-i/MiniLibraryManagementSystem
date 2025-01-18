package com.minilibrarymanagmentsystem;

import utils.DBUtil;
import utils.UserRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "loginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();

        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");
                String roleString = rs.getString("role");
                int userId = rs.getInt("user_id"); // Retrieve the user_id from the database


                try {
                    UserRole role = UserRole.valueOf(roleString.toUpperCase());

                    // Simplified password comparison for demonstration
                    if (storedPasswordHash.equals(password)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", email); // Store user email in session
                        session.setAttribute("userId", userId); // Store user_id in session
                        session.setAttribute("role", role); // Store role as enum in session
                        // Redirect based on enum role
                        switch (role) {
                            case ADMIN:
                                response.sendRedirect("admin-dashboard.jsp");
                                break;
                            case LIBRARIAN:
                                response.sendRedirect("librarian-dashboard.jsp");
                                break;
                            case PATRON:
                                response.sendRedirect("patron-dashboard.jsp");
                                break;
                            default:
                                response.getWriter().println("Invalid role assigned to the user.");
                        }
                    } else {
                        response.getWriter().println("Invalid password");
                    }
                } catch (IllegalArgumentException e) {
                    response.getWriter().println("Invalid role in the database: " + roleString);
                }
            } else {
                response.getWriter().println("User not found");
            }
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
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

    }
}
