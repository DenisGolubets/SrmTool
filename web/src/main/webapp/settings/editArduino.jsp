<%@ page import="com.golubets.monitor.environment.dao.ArduinoDao" %>
<%@ page import="com.golubets.monitor.environment.model.Arduino" %>
<%@ page import="com.golubets.monitor.environment.util.DaoApplicationContext" %>
<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 19:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit arduino</title>
</head>
<head>
    <%
        int id = -1;
        Arduino arduino = null;
        ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");
        try {
            String stringId = request.getParameter("id");
            if (stringId == null || stringId.length() == 0) {
                arduino = (Arduino) request.getSession().getAttribute("arduino");
            } else {
                id = Integer.parseInt(stringId);
            }
        } catch (NumberFormatException e) {
            id = -1;
        }

        if (arduino == null && id != -1) {
            arduino = arduinoDao.getByID(id);
        }

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

    <div id="content">
        <form method="post" action='arduino' name="arduino">
            <input type="hidden" name="action" value="editarduino"/>
            <input type="hidden" name="id" value="<%=arduino.getId()%>"/>
            <table>
                <tr>
                    <td>Name</td>
                    <td><input ${errName} type="text" name="name" value="<%=arduino.getName()%>"></td>
                </tr>
                <tr>
                    <td>Connection type</td>
                    <td><select name="connectionType">
                        <option name="eth">
                            Ethernet
                        </option>
                    </select></td>
                </tr>
                <tr>
                    <td>MAC</td>
                    <td><input type="text" name="mac" value="<%=arduino.getMac()%>"></td>
                </tr>
                <tr>
                    <td>IP</td>
                    <td><input type="text" name="ip" value="<%=arduino.getIp()%>"></td>
                </tr>
                <tr>
                    <td>Subnet</td>
                    <td><input type="text" name="subnet" value="<%=arduino.getSubnet()%>"></td>
                </tr>

                <tr>
                    <td>Gateway</td>
                    <td><input type="text" name="gateway" value="<%=arduino.getGateway()%>"></td>
                </tr>
                <tr>
                    <td>DNS</td>
                    <td><input type="text" name="dns" value="<%=arduino.getDns()%>"></td>
                </tr>

                <tr>
                    <td>Sensor port</td>
                    <td><input type="text" name="dhtPort" value="<%=arduino.getDhtPort()%>"></td>
                </tr>
                <tr>
                    <td>Sensor type</td>

                    <td><select name="sensorType">
                        <option name="<%=arduino.getDhtType()%>"><%=arduino.getDhtType()%>
                        </option>
                        <option name="11"> 11</option>
                    </select></td>
                    </td>
                </tr>
                <tr>
                    <td><input type="checkbox"
                               name="isAlertT" <%=(arduino.isAlertT() ? "checked='checked'" : "")%>/><label>Alert by
                        temp</label>
                    </td>
                    <td><input ${errTopT} type="text" name="topT" value="<%=arduino.getTopT()%>"></td>

                </tr>
                <tr>
                <tr>
                    <td><input type="checkbox"
                               name="isAlertH" <%=(arduino.isAlertH()? "checked='checked'" : "")%> ><label>Alert by
                        hum</label></td>
                    <td><input ${errTopH} type="text" name="topH" value="<%=arduino.getTopH()%>"></td>
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