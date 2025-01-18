<%--
  Created by IntelliJ IDEA.
  User: yaqeenkhazaleh
  Date: 30/11/2024
  Time: 5:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%@ page import="models.Book" %>
<%@page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patron Dashboard</title>
    <link href="Styles/patron-dashboard.css" rel="stylesheet">
</head>
<body>
<header>
    <h1>Welcome to Your Patron Dashboard</h1>
    <a href="logout"> Logout</a>

<form action="search-books-servlet" method="POST">
    <label for="searchTerm">Search for Books:</label>
    <input type="text" id="searchTerm" name="searchTerm" required>
    <button type="submit">Search</button>
</form>

<!-- Display Search Results -->
<h2>Search Results</h2>
<%
    List<Book> searchResults = (List<Book>) request.getAttribute("searchResults");
    String searchTerm = (String) request.getAttribute("searchTerm");
    if (searchResults != null && !searchResults.isEmpty()) {
%>
<p>Showing results for: <strong><%= searchTerm %></strong></p>
<table border="1">
    <thead>
    <tr>
        <th>Title</th>
        <th>Author</th>
        <th>ISBN</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <% for (Book book : searchResults) { %>
    <tr>
        <td><%= book.getTitle() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getIsbn() %></td>
        <td>
            <form action="borrow-book-servlet" method="POST">
                <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
                <button type="submit">Borrow</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } else if (searchResults != null) { %>
<p>No books found for: <strong><%= searchTerm %></strong></p>
<% } %>

<!-- Error Message -->
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p style="color: red;"><%= error %></p>
<% } %>
    <br><br>
    <form action="borrowingHistory" method="get">
        <button type="submit">View borrow history</button>
    </form>
    <div id="bHistoryTable">

        <%= request.getAttribute("bHistoryTable") != null ? request.getAttribute("bHistoryTable") : "No History available." %>

    </div>

        <h2 id="addReview">Add a Review</h2>
        <form action="addReview" method="POST">
            <label for="book_id">Book ID:</label>
            <input type="number" id="book_id" name="book_id" required>
            <label for="rating">Rating (1-5):</label>
            <input type="number" id="rating" name="rating" min="1" max="5" required>
            <label for="comment">Comment:</label>
            <textarea id="comment" name="comment"></textarea>
            <button type="submit">Submit Review</button>
        </form>
    <
</body>
</html>
