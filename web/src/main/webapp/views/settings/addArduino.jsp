<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 19:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <title>Edit arduino</title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <jsp:include page="/views/headerMenue.jsp" />
</div>
<div id="main">
    <jsp:include page="/views/leftSettingsMenue.jsp" />

    <div id="content">
        <form:form method="post" modelAttribute="arduino">
            <input type="hidden" name="id" value="${arduino.getId()}"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <table>
                <tr>
                    <td>Name</td>
                    <td>
                        <form:input path="name" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>Connection type</td>
                    <td>
                        <form:select path="connectionType" items="${connectionType}" multiple="false"/>
                    </td>
                </tr>
                <tr>
                    <td>MAC</td>
                    <td>
                        <form:input path="mac" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>IP</td>
                    <td>
                        <form:input path="ip" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>Subnet</td>
                    <td>
                        <form:input path="subnet" type="text"/>
                    </td>
                </tr>

                <tr>
                    <td>Gateway</td>
                    <td>
                        <form:input path="gateway" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>DNS</td>
                    <td>
                        <form:input path="dns" type="text"/>
                    </td>
                </tr>

                <tr>
                    <td>Sensor port</td>
                    <td>
                        <form:input path="dhtPort" type="text"/>
                    </td>
                </tr>
                <tr>
                    <td>Sensor type</td>

                    <td>
                        <form:select path="dhtType" items="${dhtType}" multiple="false"/>
                    </td>
                    </td>
                </tr>
                <tr>
                    <td>
                        Alert by Temp<form:checkbox path="alertT"/>
                    </td>
                    <td>
                        <form:input path="topT" type="text"/>
                    </td>
                </tr>
                <tr>
                <tr>
                    <td>
                        Alert by Hum <form:checkbox path="alertH"/>

                    </td>
                    <td>
                        <form:input path="topH" type="text"/>
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