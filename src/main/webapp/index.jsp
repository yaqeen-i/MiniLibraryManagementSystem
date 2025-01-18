<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Login</title>
    <link href="Styles/login-page.css" rel="stylesheet">
</head>
<body>

<form method="post" action="login-servlet">

    <h3>Login Here</h3>
    <label for="email">Email</label>
    <input type="text" placeholder="Email" id="email" name="email">

    <label for="password">Password</label>
    <input type="password" placeholder="Password" id="password" name="password">

    <button type="submit">Log In</button>

    <div>
        Don't have an account?<a href="register.jsp"> Register Here</a>
    </div>
</form>
</body>
</html>
