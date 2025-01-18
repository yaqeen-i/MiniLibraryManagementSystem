<%--
  Created by IntelliJ IDEA.
  User: yaqeenkhazaleh
  Date: 01/12/2024
  Time: 1:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Book</title>
    <link rel="stylesheet" href="Styles/edit-book.css">

</head>
<body>
<div>
<h2>Edit Book</h2>
<form action="updateBookServlet" method="post">
    <input type="hidden" name="book_id" value="<%=  request.getAttribute("book_id") %>">

    <label for="title">Title:</label>
    <input type="text" id="title" name="title" value="<%= request.getAttribute("title") %>" required><br>

    <label for="author">Author: </label>
    <input type="text" id="author" name="author" value="<%= request.getAttribute("author") %>" required><br>

    <label for="isbn">ISBN:</label>
    <input type="text" id="isbn" name="isbn" value="<%= request.getAttribute("isbn") %>" required><br>

    <label for="genre">Genre:</label>
    <input type="text" id="genre" name="genre" value="<%= request.getAttribute("genre") %>">

    <label for="year_published">Year Published:</label>
    <input type="text" id="year_published" name="year_published" value="<%= request.getAttribute("year_published") %>">

    <label for="available_copies">Available Copies:</label>
    <input type="text" id="available_copies" name="available_copies" value="<%= request.getAttribute("available_copies") %>">

    <label for="total_copies">Total Copies:</label>
    <input type="text" id="total_copies" name="total_copies" value="<%= request.getAttribute("total_copies") %>">

    <button type="submit">Update Book</button>
</form>
</div>
</body>
</html>