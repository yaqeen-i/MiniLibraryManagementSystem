package com.minilibrarymanagmentsystem;

import utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "returnBooksServlet", value = "/return-books-servlet")
public class ReturnBooksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionId = request.getParameter("transactionId");

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            // Retrieve transaction details
            String transactionQuery = "SELECT book_id, due_date FROM transactions WHERE transaction_id = ? AND status = 'Borrowed'";
            int bookId = 0;
            Date dueDate = null;

            try (PreparedStatement ps = conn.prepareStatement(transactionQuery)) {
                ps.setString(1, transactionId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    bookId = rs.getInt("book_id");
                    dueDate = rs.getDate("due_date");
                } else {
                    throw new Exception("Transaction not found or already returned.");
                }
            }

            // Calculate fine
            double fine = 0;
            long daysLate = (System.currentTimeMillis() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
            if (daysLate > 0) {
                fine = daysLate * 1.0; // Example fine: $1 per day
            }

            // Update transaction
            String updateTransactionQuery = "UPDATE transactions SET return_date = CURDATE(), fine_amount = ?, status = 'Returned' WHERE transaction_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateTransactionQuery)) {
                ps.setDouble(1, fine);
                ps.setString(2, transactionId);
                ps.executeUpdate();
            }

            // Update book availability
            String updateBookQuery = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateBookQuery)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            conn.commit();
            request.setAttribute("message", "Book returned successfully! Fine: $" + fine);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
        }

        request.getRequestDispatcher("librarian-dashboard.jsp").forward(request, response);
    }
}
