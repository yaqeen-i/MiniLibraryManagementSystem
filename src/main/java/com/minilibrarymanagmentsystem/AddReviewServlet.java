package com.minilibrarymanagmentsystem;

import utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

@WebServlet("/addReview")
public class AddReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("userId");
        if (user_id == null) {
            response.getWriter().println("Please log in to add a review.");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("book_id"));
        String comment = request.getParameter("comment");
        int rating = Integer.parseInt(request.getParameter("rating"));
        Connection conn = null;
        try  {
            conn = DBUtil.getConnection();

            String getUserSql = "SELECT user_id FROM users WHERE user_id = ?";
            PreparedStatement getUserStmt = conn.prepareStatement(getUserSql);
            getUserStmt.setInt(1, user_id);
            ResultSet rs = getUserStmt.executeQuery();

            if ( rs.next()) {
                int userId = rs.getInt("user_id");

                // Insert review into the database
                String sql = "INSERT INTO reviews (book_id, user_id, comment, rating, review_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, bookId);
                ps.setInt(2, userId);
                ps.setString(3, comment);
                ps.setInt(4, rating);
                ps.setDate(5, Date.valueOf(LocalDate.now()));

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    response.sendRedirect("patron-dashboard.jsp");
                }
            } else {
                response.getWriter().println("User not found.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
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
}