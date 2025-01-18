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
  <title>Edit User</title>

  <link href="Styles/admin-librarian-shared-edit-user-style.css" rel="stylesheet">
</head>
<body>
<div>
<h2>Edit User</h2>
<form action="updateUserServlet" method="post">
  <input type="hidden" name="user_id" value="<%=  request.getAttribute("user_id") %>">

  <label for="username">Name:</label>
  <input type="text" id="username" name="username" value="<%= request.getAttribute("username") %>" required><br>

  <label for="fullName">Full Name: </label>
  <input type="text" id="fullName" name="fullName" value="<%= request.getAttribute("fullName") %>" required><br>

  <label for="email">Email:</label>
  <input type="email" id="email" name="email" value="<%= request.getAttribute("email") %>" required><br>

  <label for="phoneNumber">Phone No:</label>
  <input type="text" id="phoneNumber" name="phoneNumber" value="<%= request.getAttribute("phoneNumber") %>">

  <label for="role">Role:</label>
  <select name="role" id="role" required>
    <option value="PATRON" <%= request.getAttribute("role").equals("PATRON") ? "selected" : "" %>>Patron</option>
    <option value="LIBRARIAN" <%= request.getAttribute("role").equals("LIBRARIAN") ? "selected" : "" %>>Librarian</option>
  </select><br>

  <button type="submit">Update User</button>
</form>
</div>
</body>
</html>