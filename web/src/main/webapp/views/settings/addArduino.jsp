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
        <form method="get" action='arduino' name="arduino">
            <input type="hidden" name="action" value="addarduino"/>
            <table>
                <tr>
                    <td>Name</td>
                    <td><input ${errName} type="text" name="name" value="<%=(request.getParameter("name")==null)?"":request.getParameter("name")%>" ></td>
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
                    <td><input type="text" name="mac" value="<%=(request.getParameter("mac")==null)?"":request.getParameter("mac")%>"></td>
                </tr>
                <tr>
                    <td>IP</td>
                    <td><input ${errIp} type="text" name="ip" value="<%=(request.getParameter("ip")==null)?"":request.getParameter("ip")%>"></td>
                </tr>
                <tr>
                    <td>Subnet</td>
                    <td><input type="text" name="subnet" value="<%=(request.getParameter("subnet")==null)?"":request.getParameter("subnet")%>"></td>
                </tr>

                <tr>
                    <td>Gateway</td>
                    <td><input type="text" name="gateway" value="<%=(request.getParameter("gateway")==null)?"":request.getParameter("gateway")%>"></td>
                </tr>
                <tr>
                    <td>DNS</td>
                    <td><input type="text" name="dns" value="<%=(request.getParameter("dns")==null)?"":request.getParameter("dns")%>"></td>
                </tr>

                <tr>
                    <td>Sensor port</td>
                    <td><input ${errDhtPort} type="text" name="dhtPort" value="<%=(request.getParameter("dhtPort")==null)?"":request.getParameter("dhtPort")%>"></td>
                </tr>
                <tr>
                    <td>Sensor type</td>

                    <td><select name="sensorType">
                        <option name="22"> 22</option>
                        <option name="23"> 23</option>
                        <option name="11"> 11</option>
                    </select></td>
                    </td>
                </tr>
                <tr>
                    <td><input type="checkbox"
                               name="isAlertT" <%=((request.getParameter("isAlertT")!=null) ? "checked='checked'" : "")%> /><label>Alert by
                        temp</label>
                    </td>
                    <td><input ${errTopT} type="text" name="topT" value="<%=(request.getParameter("topT")==null)?"":request.getParameter("topT")%>"></td>

                </tr>
                <tr>
                <tr>
                    <td><input type="checkbox"
                               name="isAlertH" <%=((request.getParameter("isAlertH")!=null) ? "checked='checked'" : "")%> ><label>Alert by
                        hum</label></td>
                    <td><input ${errTopH} type="text" name="topH" value="<%=(request.getParameter("topH")==null)?"":request.getParameter("topH")%>"></td>
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