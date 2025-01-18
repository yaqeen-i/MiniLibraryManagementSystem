package com.minilibrarymanagmentsystem;


import utils.DBUtil;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/deleteBookServlet")
public class DeleteBookServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("book_id");
        String role = SessionUtils.getUserRoleOrRedirect(request, response);
        Connection conn = null;

        String sql = "DELETE FROM books WHERE book_id = ?";

        try {
            conn = DBUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(bookId));
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    if ("LIBRARIAN".equals(role)) {
                        response.sendRedirect("librarian-dashboard.jsp?message=Book Added Successfully");
                    } else if ("ADMIN".equals(role)) {
                        response.sendRedirect("admin-dashboard.jsp?message=Book Added Successfully");
                    }
                } else {
                    response.getWriter().println("Error: Book not found or could not be deleted.");
                }
            } catch (SQLException e) {
                response.getWriter().println("Error: " + e.getMessage());
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
