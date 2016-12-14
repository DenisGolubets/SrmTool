<%@ page import="com.golubets.monitor.environment.dao.ArduinoDao" %>
<%@ page import="com.golubets.monitor.environment.model.Arduino" %>
<%@ page import="com.golubets.monitor.environment.util.DaoApplicationContext" %>
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
    <%
        ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");
    %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <h1>ServerRoomMonitorTools</h1>
    <table width="200" border="0">
        <tbody>
        <tr>
            <td><h1><a href=${pageContext.request.contextPath}>Home</a></h1></td>
            <td><h1><a href=${pageContext.request.contextPath}/settings?action=settings>Settings</a></h1></td>
        </tr>
        </tbody>
    </table>
</div>
<div id="main">
    <div id=nav>
        <p class=navigationjump><a href=#content>Skip navigation</a></p>
        <ul>
            <li>
                <a href=${pageContext.request.contextPath}/settings?action=settings>General</a>
            </li>
        </ul>
        <ul>
            <li><a href=${pageContext.request.contextPath}/settings/email?action=settingsarduino>Arduino</a></li>
        </ul>
        <ul>
            <li><a href=${pageContext.request.contextPath}/settings/email?action=settingsemailpage>Email</a></li>
        </ul>
    </div>
    <div id=content><h2>Overview</h2>
        <ul class=groupview>
                 <span class=domain>
                    <% for (Arduino a : arduinoDao.getAll()) {%>
                 <a href=${pageContext.request.contextPath}/settings/arduino?action=editarduinopage&id=<%=a.getId()%>><%=a.getName()%></a>
                    <ul>
                    <li class=last><span
                            class=host>Sensor </span>[ Temperature: <%=a.getTemp()%>, Humidity: <%=a.getHum()%>% ]</li>
                    </ul>
                        <% } %>
                </span>
        </ul>
        <li><a href=${pageContext.request.contextPath}/settings/arduino?action=addarduinopage>add Arduino</a></li>
        <div class=contentpusher></div>
    </div>
</div>
<div id="footer">
    <p class="tagline">Copyright 2016</p>
</div>
</body>
</html>