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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/editBookServlet")
public class EditBookServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int book_id = Integer.parseInt(request.getParameter("id"));
        String sql = "SELECT * FROM books WHERE book_id = ?";

        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, book_id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String isbn = rs.getString("isbn");
                    String genre = rs.getString("genre");
                    String year_published = rs.getString("year_published");
                    String available_copies = rs.getString("available_copies");
                    String total_copies = rs.getString("total_copies");

                    // Set the book data in request scope for the JSP
                    request.setAttribute("book_id", book_id);
                    request.setAttribute("title",title );
                    request.setAttribute("author", author);
                    request.setAttribute("isbn", isbn);
                    request.setAttribute("genre", genre);
                    request.setAttribute("year_published", year_published);
                    request.setAttribute("available_copies", available_copies);
                    request.setAttribute("total_copies", total_copies);

                    // Forward to the edit form JSP
                    request.getRequestDispatcher("/editBook.jsp").forward(request, response);
                } else {
                    response.getWriter().println("Book not found");
                }
            } catch (SQLException e) {
                response.getWriter().println("Error fetching Book: " + e.getMessage());
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
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
