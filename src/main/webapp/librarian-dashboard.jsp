<%--
  Created by IntelliJ IDEA.
  User: yaqeenkhazaleh
  Date: 30/11/2024
  Time: 5:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Librarian Dashboard</title>
    <link href="Styles/librarian-dashboard.css" rel="stylesheet">
</head>
<body>
<header>
    <h1>Librarian Dashboard</h1>
    <nav>
        <ul>
            <li><a href="#manage-users">Manage Patrons</a></li>
            <li><a href="#manage-books">Manage Books</a></li>
            <li><a href="logout">Logout</a></li>
        </ul>
    </nav>
</header>

<main>
    <section id="manage-users">
        <h2>Manage Patrons</h2>
        <form action="user-list-servlet" method="post">
            <button type="submit">Show Patrons</button>
        </form>
        <%= request.getAttribute("userTable") != null ? request.getAttribute("userTable") : "No Patrons Available." %>

    </section>

    <section id="manage-books">
        <div class="section">
            <h2>Manage Books</h2>
            <form action="book-list-servlet" method="POST">
                <button type="submit">Show Books</button>
            </form>
            <div id="bookList">
                <h2>Books</h2>
                <%= request.getAttribute("bookTable") != null ? request.getAttribute("bookTable") : "No books available." %>
            </div>

            <form action="add-book" method="post">
                <input type="text" name="title" placeholder="Title" required>
                <input type="text" name="author" placeholder="Author" required>
                <input type="text" name="genre" placeholder="Genre" required>
                <input type="text" name="isbn" placeholder="isbn" required>
                <input type="text" name="year_published" placeholder="Year Published" required>
                <input type="text" name="total_copies" placeholder="Total Copies" required>
                <button type="submit" >Add Book</button>
            </form>
        </div>
    </section>

    <!-- Section for Borrowing Records -->

        <h2>Return</h2>
        <form action="return-books-servlet" method="GET">
            <input type="text" name="transactionId" placeholder="transactionId" required>
            <button type="submit">Return book</button>
        </form>



</main>


</body>
</html>
