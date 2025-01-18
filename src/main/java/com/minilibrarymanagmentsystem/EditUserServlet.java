package com.minilibrarymanagmentsystem;

import utils.UserRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/editUserServlet")
public class EditUserServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database connection failed", e);
        }
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the user's role from the session
        HttpSession session = request.getSession(false);
        UserRole role = (session != null) ? (UserRole) session.getAttribute("role") : null;

        if (role == null) {
            response.getWriter().println("Unauthorized access. Please log in.");
            return;
        }

        int user_id = Integer.parseInt(request.getParameter("id"));
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");

                // Common attributes for both roles
                request.setAttribute("user_id", user_id);
                request.setAttribute("username", username);
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("phoneNumber", phoneNumber);

                // Role-specific logic
                if (role == UserRole.ADMIN) {
                    String adminRole = rs.getString("role");
                    request.setAttribute("role", adminRole);
                    request.getRequestDispatcher("editUser_Admin.jsp").forward(request, response);
                } else if (role == UserRole.LIBRARIAN) {
                    request.getRequestDispatcher("editUser_Librarian.jsp").forward(request, response);
                } else {
                    response.getWriter().println("Unauthorized access for this role.");
                }
            } else {
                response.getWriter().println("User not found");
            }
        } catch (SQLException e) {
            response.getWriter().println("Error fetching user: " + e.getMessage());
        }
    }


    @Override
    public void destroy() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
