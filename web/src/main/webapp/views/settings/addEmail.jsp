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
        <form method="get" action='email' name="email">
            <input type="hidden" name="action" value="addemail"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <table>
                <tr>
                    <td>SMTP</td>
                    <td><input ${errHost} type="text"  name="host" value="<%=(request.getParameter("host")==null)?"":request.getParameter("host")%>" ></td>
                </tr>
                <tr>
                    <td>From</td>
                    <td><input ${errFrom} type="text"  name="from" value="<%=(request.getParameter("from")==null)?"":request.getParameter("from")%>"></td>
                </tr>
                <tr>
                    <td>To</td>
                    <td><input ${errTo} type="text"  name="to" value="<%=(request.getParameter("to")==null)?"":request.getParameter("to")%>" ></td>
                </tr>
                <tr>
                    <td>port</td>
                    <td><input type="text"  name="port" value=""></td>
                </tr>
                <tr>
                    <td>SSL</td>
                    <td><input type="checkbox" name="ssl"  <%=((request.getParameter("ssl")!=null) ? "checked='checked'" : "")%>/>

                </tr>
                <tr>
                    <td>Login</td>
                    <td><input type="text"  name="login" value=""></td>
                </tr>
                <tr>
                    <td>Pass</td>
                    <td><input type="password"  name="pass" ></td>
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