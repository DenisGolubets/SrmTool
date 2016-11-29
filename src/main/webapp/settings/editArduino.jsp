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
            <li><a href= ${pageContext.request.contextPath}/settings/email?action=settingsemailpage>Email</a></li>    </ul>
</div>

        <div id="content">
            <form method="post" action='arduino' name="arduino">
                <input type="hidden" name="action" value="editarduino"/>
                <table>
                    <tr>
                        <td>Name</td>
                        <td><input ${errName} type="text"  name="name" value=${name}></td>
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
                        <td><input  type="text"  name="mac" value="${mac}"></td>
                    </tr>
                    <tr>
                        <td>IP</td>
                        <td><input  type="text"  name="ip" value="${ip}"></td>
                    </tr>
                    <tr>
                        <td>Subnet</td>
                        <td><input  type="text"  name="subnet" value="${subnet}"></td>
                    </tr>

                    <tr>
                        <td>Gateway</td>
                        <td><input  type="text"  name="gateway" value="${gateway}"></td>
                    </tr>
                    <tr>
                        <td>DNS</td>
                        <td><input  type="text"  name="dns" value="${dns}"></td>
                    </tr>

                    <tr>
                        <td>Sensor port</td>
                        <td><input  type="text"  name="dhtPort" value="${dhtPort}"></td>
                    </tr>
                    <tr>
                        <td>Sensor type</td>

                        <td><select name="sensorType">
                            <option name="22"> 22 </option>
                            <option name="11"> 11 </option>
                        </select></td>
                        </td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="isAlertT" checked/><label>Alert by temp</label></td>
                        <td><input type="checkbox" name="isAlertH" checked/><label>Alert by hum</label></td>
                    </tr>
                    <tr>
                        <td>Max T</td>
                        <td><input ${errSecondName} type="text"  name="topT" value="${topT}"></td>
                    </tr>
                    <tr>
                        <td>Max H</td>
                        <td><input ${errSecondName} type="text"  name="topH" value="${topT}"></td>
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