<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 18:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Settings</title>
</head>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <h1>ServerRoomMonitorTools</h1>
    <table width="200" border="0">
        <tbody>
        <tr>
            <td><h1><a href=${pageContext.request.contextPath}/index>Home</a></h1></td>
            <td><h1><a href=${pageContext.request.contextPath}/settings>Settings</a></h1></td>
        </tr>
        </tbody>
    </table>
</div>
<div id="main">
    <div id=nav>
        <p class=navigationjump><a href=#content>Skip navigation</a></p>
        <ul>
            <li>
                <a href=../settings>General</a>
            </li>
        </ul>
        <ul>
            <li><a href=../settings/arduino>Arduino</a>
            </li>
        </ul>
        <ul>
            <li><a href=../settings/email>Email</a></li>
        </ul>
    </div>
    ${context}
</div>
<div id="footer">
    <p class="tagline">Copyright 2016</p>
</div>
</body>
</html>