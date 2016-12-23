<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Email Setting</title>
</head>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />

    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <h1>ServerRoomMonitorTools</h1>
    <jsp:include page="/views/headerMenue.jsp" />
</div>
<div id="main">
    <jsp:include page="/views/leftSettingsMenue.jsp" />

    <div id="content">
        <form method="post" action='email' name="email">
            <input type="hidden" name="id" value="${mailSettings.getId()}"/>
            <table>
                <tr>
                    <td>SMTP</td>
                    <td><input ${errHost} type="text"  name="host" value="${mailSettings.getHost()}"></td>
                </tr>
                <tr>
                    <td>From</td>
                    <td><input ${errFrom} type="text"  name="from" value="${mailSettings.getFrom()}"></td>
                </tr>
                    <tr>
                        <td>To</td>
                        <td><input ${errTo} type="text"  name="to" value="${mailSettings.getTo()}" ></td>
                    </tr>
                <tr>
                    <td>SSL</td>

                    <td><input type="checkbox" name="ssl" <c:if test="${mailSettings.getSsl()}">checked="checked"</c:if> />

                </tr>
                <tr>
                    <td>port</td>
                    <td><input type="text"  name="port" value="${mailSettings.getPort()}"></td>
                </tr>
                <tr>
                    <td>Login</td>
                    <td><input type="text"  name="login" value="${mailSettings.getLogin()}"></td>
                </tr>
                <tr>
                    <td>Pass</td>
                    <td><input type="password"  name="pass" value="${mailSettings.getPass()}" ></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form>
        <ul class="groupview">
        </ul>
        <div class="contentpusher"></div>
    </div>
</div>
<jsp:include page="/views/footer.jsp"/>
</body>
</html>