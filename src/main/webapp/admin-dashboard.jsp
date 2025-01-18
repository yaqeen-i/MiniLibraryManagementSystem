<%--
  Created by IntelliJ IDEA.
  User: yaqeenkhazaleh
  Date: 29/11/2024
  Time: 3:58PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="utils.UserRole" %>
<%
    // Get the user role from the session
    UserRole userRole = (UserRole) session.getAttribute("role");

    // If no user is logged in or the role is not admin, redirect to login page
    if (userRole != UserRole.ADMIN) {
        response.sendRedirect("index.jsp");
        return; // Stop further execution of the page
    }

%>
<!-- Rest of your admin-dashboard.jsp content goes here -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Administrator Dashboard</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="Styles/admin-dashboard-styles.css" rel="stylesheet">
</head>
<body>
<header>
    <h1>Administrator Dashboard</h1>
</header>
<div class="container">
    <!-- User Management Section -->
    <div class="section">
        <h2>Manage Users</h2>
        <form action="user-list-servlet" method="POST">
            <label for="roleFilter">Filter by Role:</label>
            <select name="roleFilter" id="roleFilter">
                <option value="ALL">All</option>
                <option value="ADMIN">Admin</option>
                <option value="LIBRARIAN">Librarian</option>
                <option value="PATRON">Patron</option>
            </select>
            <button type="submit">Show Users</button>
        </form>

        <!-- Displaying user list table or other content -->
        <div id="userList">
            <h2>Users</h2>
            <!-- Display the user table if available -->
            <%= request.getAttribute("userTable") != null ? request.getAttribute("userTable") : "No users available." %>
        </div>

            <%

                // Database connection
                String url = "jdbc:mysql://localhost:3306/library_system";
                String user = "root";
                String password = "";
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

            %>


        <form action="addUserServlet" method="post">
            <label>
                <input type="text" name="name" placeholder="Name" required>
            </label>
            <label>
                <input type="email" name="email" placeholder="Email" required>
            </label>
            <label>
                <input type="text" name="fullName" placeholder="Full name" required>
            </label>
            <label>
                <input type="tel" pattern="[0-9]{3}[0-9]{3}[0-9]{4}" name="phoneNumber" placeholder="Phone Number" required>
            </label>
            <label>
                <select name="role" required>
                    <option value="PATRON">Patron</option>
                    <option value="LIBRARIAN">Librarian</option>
                    <option value="ADMIN">Admin</option>
                </select>
            </label>
            <label>
                <input type="password" name="password" placeholder="Password" required>
            </label>
            <button type="submit">Add User</button>
        </form>
    </div>

    <!-- Book Management Section -->
    <div class="section">
        <h2>Manage Books</h2>
        <form action="book-list-servlet" method="POST">
            <button type="submit">Show Books</button>
        </form>
        <div id="bookList">
            <h2>Books</h2>
            <!-- Display the user table if available -->
            <%= request.getAttribute("bookTable") != null ? request.getAttribute("bookTable") : "No books available." %>
        </div>

        <form action="add-book" method="post" style="margin-bottom:100px">
            <input type="text" name="title" placeholder="Title" required>
            <input type="text" name="author" placeholder="Author" required>
            <input type="text" name="genre" placeholder="Genre" required>
            <input type="text" name="isbn" placeholder="isbn" required>
            <input type="text" name="year_published" placeholder="Year Published" required>
            <input type="text" name="total_copies" placeholder="Total Copies" required>
            <button type="submit" >Add Book</button>
        </form>
    </div>
</div>

<div class="logout"><a href="logout">Logout</a></div>
</body>
</html>
