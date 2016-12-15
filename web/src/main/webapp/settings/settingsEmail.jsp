<%@ page import="com.golubets.monitor.environment.Interrogation" %>
<%@ page import="com.golubets.monitor.environment.model.MailSettings" %>
<%@ page import="com.golubets.monitor.environment.dao.MailSettingsDao" %>
<%@ page import="java.util.List" %>
<%@ page import="com.golubets.monitor.environment.util.DaoApplicationContext" %><%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        MailSettings mail = null;
        // Interrogation interrogation = Interrogation.getInstance();
        MailSettingsDao mailSettingsDao = (MailSettingsDao) DaoApplicationContext.getInstance().getContext().getBean("mailSettingsDao");
        List<MailSettings> list = mailSettingsDao.getAll();
        if (list != null && list.size() > 0) {
            mail = list.get(0);
        }

    %>
    <title>Email Setting</title>
</head>
<head>
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
            </li>
        </ul>
        <ul>
            <li><a href=${pageContext.request.contextPath}/settings/email?action=settingsemailpage>Email</a></li>
        </ul>
    </div>

    <div id="content">
        <form method="post" action='email' name="email">
            <input type="hidden" name="action" value="editemail"/>
            <table>
                <tr>
                    <td>SMTP</td>
                    <td><input ${errHost} type="text" name="host" value="<%=mail.getHost()%>"></td>
                </tr>
                <tr>
                    <td>From</td>
                    <td><input ${errFrom} type="text" name="from" value="<%=mail.getFrom()%>"></td>
                </tr>

                <% for (String s : mail.getTo()) {%>

                <tr>
                    <td>To</td>
                    <td><input ${errTo} type="text" name="to" value="<%=s%>"></td>
                </tr>
                <% } %>


                <tr>
                    <td>port</td>
                    <td><input type="text" name="port" value="<%=mail.getPort()%>"></td>
                </tr>
                <tr>
                    <td>SSL</td>
                    <td><input type="checkbox" name="ssl" <%=(mail.isSsl() ? "checked='checked'" : "")%>/>

                </tr>
                <tr>
                    <td>Login</td>
                    <td><input type="text" name="login" value="<%=mail.getLogin()%>"></td>
                </tr>
                <tr>
                    <td>Pass</td>
                    <td><input type="password" name="pass"></td>
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
<div id="footer">
    <p class="tagline">Copyright 2016</p>
</div>
</body>
</html>