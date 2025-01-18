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

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {

    }

         protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = request.getParameter("user_id");
        Connection conn = null;
        String sql = "DELETE FROM users WHERE user_id = ?";

             try {
                 conn = DBUtil.getConnection();
                 try (PreparedStatement ps = conn.prepareStatement(sql)) {
                     ps.setInt(1, Integer.parseInt(user_id));
                     int rowsAffected = ps.executeUpdate();

                     if (rowsAffected > 0) {
                         // Redirect back to the user list after successful deletion
                         response.sendRedirect("admin-dashboard.jsp");
                     } else {
                         response.getWriter().println("Error: User not found or could not be deleted.");
                     }
                 } catch (Exception e) {
                     response.getWriter().println("Error deleting user: " + e.getMessage());
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
