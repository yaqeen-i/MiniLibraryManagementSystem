package com.minilibrarymanagmentsystem;

import utils.DBUtil;
import utils.UserRole;

import javax.servlet.RequestDispatcher;
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

@WebServlet(name = "userListServlet", value = "/user-list-servlet")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch the role from the session
        HttpSession session = request.getSession(false);
        UserRole role = (session != null) ? (UserRole) session.getAttribute("role") : null;

        if (role == null) {
            response.getWriter().println("Unauthorized access. Please log in.");
            return;
        }

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();

            StringBuilder userTable = new StringBuilder();

            if (role == UserRole.ADMIN) {
                // ADMIN logic: show all users or filter by role
                String roleFilter = request.getParameter("roleFilter");
                String sql = "SELECT * FROM users";
                if (!"ALL".equals(roleFilter)) {
                    sql += " WHERE role = ?";
                }

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    if (!"ALL".equals(roleFilter)) {
                        ps.setString(1, roleFilter);
                    }

                    ResultSet rs = ps.executeQuery();

                    userTable.append("<table border='1'>");
                    userTable.append("<tr><th>User Name</th><th>Full Name</th><th>Email</th><th>Phone Number</th><th>Role</th><th>Created At</th><th>Actions</th></tr>");

                    while (rs.next()) {
                        int userId = rs.getInt("user_id");
                        String userName = rs.getString("username");
                        String fullName = rs.getString("full_name");
                        String email = rs.getString("email");
                        String phoneNumber = rs.getString("phone_number");
                        String userRole = rs.getString("role");
                        String createdAt = rs.getString("created_at");

                        userTable.append("<tr>");
                        userTable.append("<td>").append(userName).append("</td>");
                        userTable.append("<td>").append(fullName).append("</td>");
                        userTable.append("<td>").append(email).append("</td>");
                        userTable.append("<td>").append(phoneNumber).append("</td>");
                        userTable.append("<td>").append(userRole).append("</td>");
                        userTable.append("<td>").append(createdAt).append("</td>");
                        userTable.append("<td>");
                        userTable.append("<a href=\"editUserServlet?id=").append(userId).append("\"> <button>Edit</button> </a> ");
                        userTable.append("<form action=\"DeleteUserServlet\" method=\"post\" style=\"display: inline;\" onsubmit=\"return confirm('Are you sure you want to delete this user?');\">");
                        userTable.append("<input type=\"hidden\" name=\"user_id\" value=\"").append(userId).append("\">");
                        userTable.append("<button type=\"submit\">Delete</button>");
                        userTable.append("</form>");
                        userTable.append("</td>");
                        userTable.append("</tr>");
                    }

                    userTable.append("</table>");
                }

                // Forward to the admin dashboard
                request.setAttribute("userTable", userTable.toString());
                RequestDispatcher dispatcher = request.getRequestDispatcher("admin-dashboard.jsp");
                dispatcher.forward(request, response);

            } else if (role == UserRole.LIBRARIAN) {
                // LIBRARIAN logic: show only patrons
                String sql = "SELECT * FROM users WHERE role = 'PATRON'";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ResultSet rs = ps.executeQuery();

                    userTable.append("<table border='1'>");
                    userTable.append("<tr><th>User Name</th><th>Full Name</th><th>Email</th><th>Phone Number</th><th>Created At</th><th>Actions</th></tr>");

                    while (rs.next()) {
                        int userId = rs.getInt("user_id");
                        String userName = rs.getString("username");
                        String fullName = rs.getString("full_name");
                        String email = rs.getString("email");
                        String phoneNumber = rs.getString("phone_number");
                        String createdAt = rs.getString("created_at");

                        userTable.append("<tr>");
                        userTable.append("<td>").append(userName).append("</td>");
                        userTable.append("<td>").append(fullName).append("</td>");
                        userTable.append("<td>").append(email).append("</td>");
                        userTable.append("<td>").append(phoneNumber).append("</td>");
                        userTable.append("<td>").append(createdAt).append("</td>");
                        userTable.append("<td>");
                        userTable.append("<a href=\"editUserServlet?id=").append(userId).append("\"> <button>Edit</button> </a> ");
                        userTable.append("<form action=\"DeleteUserServlet\" method=\"post\" style=\"display: inline;\" onsubmit=\"return confirm('Are you sure you want to delete this user?');\">");
                        userTable.append("<input type=\"hidden\" name=\"user_id\" value=\"").append(userId).append("\">");
                        userTable.append("<button type=\"submit\">Delete</button>");
                        userTable.append("</form>");
                        userTable.append("</td>");
                        userTable.append("</tr>");
                    }

                    userTable.append("</table>");
                }

                // Forward to the librarian dashboard
                request.setAttribute("userTable", userTable.toString());
                RequestDispatcher dispatcher = request.getRequestDispatcher("librarian-dashboard.jsp");
                dispatcher.forward(request, response);

            } else {
                response.getWriter().println("Unauthorized access. Invalid role.");
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
}
