<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"/>

    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <jsp:include page="/views/headerMenue.jsp"/>

</div>
<div id="main">
    <jsp:include page="/views/leftSettingsMenue.jsp"/>

    <div id=content><h2>Overview</h2>
        <ul class=groupview>
           <span class=domain>
            <c:forEach items="${arduinos}" var="arduino">
                <a href="${pageContext.request.contextPath}/settings/arduino/${arduino.getId()}">${arduino.getName()}</a>
                <ul>
                    <li class=last>
                        <span class=host>Sensor </span>
                        [ Temperature: ${arduino.getTemp()}, Humidity: ${arduino.getHum()} ]</li>
                </ul>
            </c:forEach>
           </span>
        </ul>
        <ui><a href=${pageContext.request.contextPath}/settings/arduino/new>add Arduino</a></ui>
        <div class=contentpusher></div>
    </div>
</div>
<jsp:include page="/views/footer.jsp"/>
</body>
</html>