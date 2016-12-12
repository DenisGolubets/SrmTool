<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 02.12.2016
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css"/>
    <title>Login</title>
</head>
<body>
<div class="container" style="width: 300px;">
    <form action=${loginUrl} method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="hidden" name="action" value="signin"/>
        <input type="text" class="form-control" name="j_username" placeholder="Login" required autofocus>
        <input type="password" class="form-control" name="j_password" placeholder="Password">
        <button class="btn btn-lg btn-primary btn-block" type="submit">sign in</button>
    </form>
</div>
</body>
</html>
