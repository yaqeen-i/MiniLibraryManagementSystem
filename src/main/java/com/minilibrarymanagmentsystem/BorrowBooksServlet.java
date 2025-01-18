package com.minilibrarymanagmentsystem;

import utils.DBUtil;
import utils.TransactionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

@WebServlet(name = "borrowBookServlet", value = "/borrow-book-servlet")
public class BorrowBooksServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");

        // Retrieve userId from session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Check if userId is null (user not logged in)
        if (userId == null) {
            request.setAttribute("error", "You must be logged in to borrow books.");
            request.getRequestDispatcher("index.jsp").forward(request, response); // Redirect to login page
            return;
        }// it takes me back to index page, why? (a note for you when you wake up and complete working)

        try (Connection conn = DBUtil.getConnection()) {

            // Check if the patron already has 3 borrowed books
            String countQuery = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND return_date IS NULL";
            try (PreparedStatement countPs = conn.prepareStatement(countQuery)) {
                countPs.setInt(1, userId);
                ResultSet rs = countPs.executeQuery();
                if (rs.next() && rs.getInt(1) >= 3) {
                    request.setAttribute("error", "You cannot borrow more than 3 books at a time.");
                    request.getRequestDispatcher("patron-dashboard.jsp").forward(request, response);
                    return;
                }
            }

            // Check if the book is available
            String availabilityQuery = "SELECT book_id, available_copies FROM books WHERE isbn = ?";
            int bookId = -1;
            try (PreparedStatement bookPs = conn.prepareStatement(availabilityQuery)) {
                bookPs.setString(1, isbn);
                ResultSet rs = bookPs.executeQuery();
                if (rs.next() && rs.getInt("available_copies") > 0) {
                    bookId = rs.getInt("book_id");
                } else {
                    request.setAttribute("error", "Book is not available.");
                    request.getRequestDispatcher("patron-dashboard.jsp").forward(request, response);
                    return;
                }
            }

            // Create a transaction ID using the existing method in TransactionUtil
            String transactionId = TransactionUtil.generateTransactionID(conn);

            // Insert the borrow transaction
            String borrowQuery = "INSERT INTO transactions (transaction_id, user_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement borrowPs = conn.prepareStatement(borrowQuery)) {
                borrowPs.setString(1, transactionId);
                borrowPs.setInt(2, userId);
                borrowPs.setInt(3, bookId);
                borrowPs.setDate(4, Date.valueOf(LocalDate.now()));
                borrowPs.setDate(5, Date.valueOf(LocalDate.now().plusWeeks(2))); // Due date is 2 weeks from today.
                borrowPs.executeUpdate();
            }

            // Decrement available copies of the book
            String updateBookQuery = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ?";
            try (PreparedStatement updateBookPs = conn.prepareStatement(updateBookQuery)) {
                updateBookPs.setInt(1, bookId);
                updateBookPs.executeUpdate();
            }

            request.setAttribute("success", "Book borrowed successfully! Transaction ID: " + transactionId);
            request.getRequestDispatcher("patron-dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while borrowing the book.");
            request.getRequestDispatcher("patron-dashboard.jsp").forward(request, response);
        }
    }
}
