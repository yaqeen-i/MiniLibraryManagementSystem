<%--
  Created by IntelliJ IDEA.
  User: yaqeenkhazaleh
  Date: 29/11/2024
  Time: 12:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>

    <title>Register Page</title>

    <link href="Styles/register-page.css" rel="stylesheet">
</head>
<body>

<form method="post" action="register-servlet">
    <h3>Register Here</h3>

    <label for="username">Username</label>
    <input type="text" placeholder="Choose a user name" id="username" name="username" required>

    <label for="email">Email</label>
    <input type="email" placeholder="Email" id="email" name="email" required>

    <label for="password">Password</label>
    <input type="password" placeholder="Password" id="password" name="password" required>

    <label for="fullname">Full Name</label>
    <input type="text" placeholder="Your Full name" id="fullname" name="fullName" required>

    <label for="phoneNumber">Phone Number</label>
    <input type="tel" pattern="[0-9]{3}[0-9]{3}[0-9]{4}" placeholder="Your Phone Number" id="phoneNumber" name="phoneNumber" required>

    <label>Choose your role</label>
    <div class="radio-group">

        <label>
            <input type="radio" name="role" value="admin" required> Admin
        </label>
        <label>
            <input type="radio" name="role" value="librarian"> Librarian
        </label>
        <label>
            <input type="radio" name="role" value="patron"> Patron
        </label>
    </div>

    <button>Register</button>

    <div id="backpage">
        <p><a href="index.jsp"> Back to Login Page</a></p>
    </div>
</form>
<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<script type="text/javascript">
    alert('<%= message %>');  // Display the message in an alert box
</script>
<% } %>
</body>
</html>

