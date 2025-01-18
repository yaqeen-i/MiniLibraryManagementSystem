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

@WebServlet(name = "addBookServlet", value = "/add-book")
public class AddBookServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String genre = request.getParameter("genre");
        String year_published = request.getParameter("year_published");
        int total_copies = Integer.parseInt(request.getParameter("total_copies"));
        int available_copies = total_copies;

//        HttpSession session = request.getSession(false);
//        UserRole userRole = (session != null) ? (UserRole) session.getAttribute("role") : null;
//
//        if (userRole == null) {
//            response.sendRedirect("index.jsp");
//            return;
//        }
//        // Get the role as a string
//        String role = userRole.name();
        String role = SessionUtils.getUserRoleOrRedirect(request, response);

        String sql = "INSERT INTO books (title, author, isbn, genre, year_published, available_copies, total_copies) VALUES (?, ?, ?, ?, ?, ?,?)";
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, title);
                ps.setString(2, author);
                ps.setString(3, isbn);
                ps.setString(4, genre);
                ps.setString(5, year_published);
                ps.setInt(6, available_copies);
                ps.setInt(7, total_copies);

                int result = ps.executeUpdate();

                if (result > 0) {
                    if ("LIBRARIAN".equals(role)) {
                        response.sendRedirect("librarian-dashboard.jsp?message=Book Added Successfully");
                    } else if ("ADMIN".equals(role)) {
                        response.sendRedirect("admin-dashboard.jsp?message=Book Added Successfully");
                    } else {
                    response.getWriter().println("Unauthorized access.");
                }
                } else {
                    response.getWriter().println("Error adding book.");
                }
            }
            catch (SQLException e) {
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
