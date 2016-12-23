<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
    <%--<%--%>
    <%--int id = -1;--%>
    <%--Arduino arduino = null;--%>
    <%--ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");--%>
    <%--try {--%>
    <%--String stringId = request.getParameter("id");--%>
    <%--if (stringId == null || stringId.length() == 0) {--%>
    <%--arduino = (Arduino) request.getSession().getAttribute("arduino");--%>
    <%--} else {--%>
    <%--id = Integer.parseInt(stringId);--%>
    <%--}--%>
    <%--} catch (NumberFormatException e) {--%>
    <%--id = -1;--%>
    <%--}--%>

    <%--if (arduino == null && id != -1) {--%>
    <%--arduino = arduinoDao.getByID(id);--%>
    <%--}--%>

    <%--%>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"/>

    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <h1>ServerRoomMonitorTools</h1>
    <jsp:include page="/views/headerMenue.jsp"/>

</div>
<div id="main">
    <jsp:include page="/views/leftSettingsMenue.jsp"/>


    <div id="content">
        <form method="post">
            <input type="hidden" name="id" value="${arduino.getId()}"/>
            <table>
                <tr>
                    <td>Name</td>
                    <td><input ${errName} type="text" name="name" value="${arduino.getName()}"></td>
                </tr>
                <tr>
                    <td>Connection type</td>
                    <td><select name="connectionType">
                        <option name="${arduino.getConnectionType()}">
                            ${arduino.getConnectionType()}
                        </option>
                    </select></td>
                </tr>
                <tr>
                    <td>MAC</td>
                    <td><input type="text" name="mac" value="${arduino.getMac()}"></td>
                </tr>
                <tr>
                    <td>IP</td>
                    <td><input type="text" name="ip" value="${arduino.getIp()}"></td>
                </tr>
                <tr>
                    <td>Subnet</td>
                    <td><input type="text" name="subnet" value="${arduino.getSubnet()}"></td>
                </tr>

                <tr>
                    <td>Gateway</td>
                    <td><input type="text" name="gateway" value="${arduino.getGateway()}"></td>
                </tr>
                <tr>
                    <td>DNS</td>
                    <td><input type="text" name="dns" value="${arduino.getDns()}"></td>
                </tr>

                <tr>
                    <td>Sensor port</td>
                    <td><input type="text" name="dhtPort" value="${arduino.getDhtPort()}"></td>
                </tr>
                <tr>
                    <td>Sensor type</td>

                    <td><select name="sensorType">
                        <option name="${arduino.getDhtType()}">${arduino.getDhtType()}</option>
                        <option name="22"> 22</option>
                    </select></td>
                    </td>
                </tr>
                <tr>
                    <td><input type="checkbox"
                               name="isAlertT" <c:if test="${arduino.isAlertT()}">checked="checked"</c:if>/><label>Alert
                        by
                        temp</label>
                    </td>
                    <td><input ${errTopT} type="text" name="topT" value="${arduino.getTopT()}"></td>

                </tr>
                <tr>
                <tr>
                    <td><input type="checkbox"
                               name="isAlertH" <c:if test="${arduino.isAlertH()}">checked="checked"</c:if>/><label>Alert by
                        hum</label></td>
                    <td><input ${errTopH} type="text" name="topH" value="${arduino.getTopH()}"></td>
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