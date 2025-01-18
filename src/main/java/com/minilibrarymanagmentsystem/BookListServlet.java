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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "bookListServlet", value = "/book-list-servlet")
public class BookListServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String role = SessionUtils.getUserRoleOrRedirect(request, response);
        Connection conn= null;
        String sql = "SELECT * FROM books";

        try {
            conn = DBUtil.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();
                StringBuilder bookTable = new StringBuilder();
                bookTable.append("<table border='1'>");
                bookTable.append("<tr><th>ID</th><th>Title</th><th>Author</th> <th>ISBN</th><th>Genre</th><th>Year Published</th>" +
                        "<th>Availability</th><th>Actions</th></tr>");

                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String isbn = rs.getString("isbn");
                    String genre = rs.getString("genre");
                    int year_published = rs.getInt("year_published");
                    int available_copies = rs.getInt("available_copies");
                    String availableC = null;
                    if (available_copies >= 1) {
                        availableC = "Available, " + available_copies + " Copies Available";
                    } else if (available_copies == 0) {
                        availableC = "Not Available, All copies are borrowed or reserved";
                    }

                    bookTable.append("<tr>");
                    bookTable.append("<td>").append(bookId).append("</td>");
                    bookTable.append("<td>").append(title).append("</td>");
                    bookTable.append("<td>").append(author).append("</td>");
                    bookTable.append("<td>").append(isbn).append("</td>");
                    bookTable.append("<td>").append(genre).append("</td>");
                    bookTable.append("<td>").append(year_published).append("</td>");
                    bookTable.append("<td>").append(availableC).append("</td>");
                    bookTable.append("<td>");
                    bookTable.append("<a href=\"editBookServlet?id=").append(bookId).append("\"> <button>Edit</button> </a> ");
                    bookTable.append("<form action=\"deleteBookServlet\" method=\"post\" style=\"display: inline;\" onsubmit=\"return confirm('Are you sure you want to delete this book?');\">");
                    bookTable.append("<input type=\"hidden\" name=\"book_id\" value=\"").append(bookId).append("\">");
                    bookTable.append("<button type=\"submit\">Delete</button>");
                    bookTable.append("</form>");
                    bookTable.append("</td>");
                    bookTable.append("</tr>");
                }
                bookTable.append("</table>");
                request.setAttribute("bookTable", bookTable.toString());

                SessionUtils.conditionalRouting(request, response, role);


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
