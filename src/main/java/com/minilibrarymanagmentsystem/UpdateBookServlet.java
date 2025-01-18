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

@WebServlet("/updateBookServlet")
public class UpdateBookServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int book_id = Integer.parseInt(request.getParameter("book_id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String isbn = request.getParameter("isbn");
        String genre = request.getParameter("genre");
        String year_published = request.getParameter("year_published");
        String available_copies = request.getParameter("available_copies");
        String total_copies = request.getParameter("total_copies");

        String role = SessionUtils.getUserRoleOrRedirect(request, response);

        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?,genre = ?,year_published = ?,available_copies= ?,total_copies = ? WHERE book_id = ?";
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, title);
                ps.setString(2, author);
                ps.setString(3, isbn);
                ps.setString(4, genre);
                ps.setString(5, year_published);
                ps.setString(6, available_copies);
                ps.setString(7, total_copies);
                ps.setInt(8, book_id);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    SessionUtils.conditionalRouting(request, response, role);
                } else {
                    response.getWriter().println("Error updating Book");
                }
            } catch (SQLException e) {
                response.getWriter().println("Error updating Book: " + e.getMessage());
            }
        }catch (SQLException e) {
            response.getWriter().println("Error updating Book: " + e.getMessage());
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
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
