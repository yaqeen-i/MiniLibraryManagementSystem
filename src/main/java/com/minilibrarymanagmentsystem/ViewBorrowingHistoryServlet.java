package com.minilibrarymanagmentsystem;

import utils.DBUtil;

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

@WebServlet("/borrowingHistory")
public class ViewBorrowingHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.getWriter().println("Please log in to view your borrowing history.");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            // Corrected SQL query
            String sql = "SELECT  b.title, t.transaction_id, t.borrow_date, t.due_date, t.return_date, t.fine_amount " +
                    "FROM transactions t " +
                    "JOIN books b ON t.book_id = b.book_id " +
                    "WHERE t.user_id = (SELECT user_id FROM users WHERE user_id= ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // Check if there are any results
            boolean hasRows = false;
            StringBuilder bHistoryTable = new StringBuilder();
            bHistoryTable.append("<table border='1'>");
            bHistoryTable.append("<tr><th>title</th><th>Borrow Date</th><th>Due Date</th><th>Return Date</th><th>Fine Amount</th><th>Transaction ID</th></tr>");

            while (rs.next()) {
            hasRows = true; // Mark that rows exist
            String title = rs.getString("title");
            String borrowDate= (rs.getDate("borrow_date")).toString();
            String due_date = (rs.getDate("due_date")).toString();
            String return_date = rs.getDate("return_date") != null ? rs.getDate("return_date").toString() : "Not Returned";
            Double fine_amount = rs.getDouble("fine_amount");
            String tID = rs.getString("transaction_id");

            bHistoryTable.append("<tr>");
            bHistoryTable.append("<td>").append(title).append("</td>");
            bHistoryTable.append("<td>").append(borrowDate).append("</td>");
            bHistoryTable.append("<td>").append(due_date).append("</td>");
            bHistoryTable.append("<td>").append(return_date).append("</td>");
            bHistoryTable.append("<td>").append(fine_amount).append("</td>");
            bHistoryTable.append("<td>").append(tID).append("</td>");
            bHistoryTable.append("</tr>");
            }
            bHistoryTable.append("</table>");
            if (!hasRows) {
                bHistoryTable.append("<tr><td colspan='5'>No borrowing history found.</td></tr>");
            }
            request.setAttribute("bHistoryTable", bHistoryTable.toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("patron-dashboard.jsp");
            dispatcher.forward(request, response);

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}