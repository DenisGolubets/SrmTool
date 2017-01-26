<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />

    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <jsp:include page="/views/headerMenue.jsp" />
</div>
<div id="main">
    <jsp:include page="/views/leftSettingsMenue.jsp" />

    <div id="content">
        <form:form method="post" modelAttribute="mailSettings">
            <input type="hidden" name="id" value="${mailSettings.getId()}"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <table>
                <tr>
                    <td>SMTP</td>
                    <td>
                        <form:input path="host" type="text"/>

                    </td>
                </tr>
                <tr>
                    <td>From</td>
                    <td>
                        <form:input path="from" type="text"/>

                    </td>
                </tr>
                    <tr>
                        <td>To</td>
                        <td>
                            <form:input path="to" type="text"/>

                        </td>
                    </tr>
                <tr>
                    <td>SSL</td>

                    <td>
                            <form:checkbox path="ssl"/>
                </tr>
                <tr>
                    <td>port</td>
                    <td>
                        <form:input path="port" type="text"/>

                    </td>
                </tr>
                <tr>
                    <td>User Name</td>
                    <td>
                        <form:input path="mailLogin" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td>
                        <form:input path="mailPass" type="password" />
                    </td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form:form>
        <ul class="groupview">
        </ul>
        <div class="contentpusher"></div>
    </div>
</div>
<jsp:include page="/views/footer.jsp"/>
</body>
</html>