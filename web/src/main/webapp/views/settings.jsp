<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 17:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css" />
    <title>Settings</title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <jsp:include page="/views/headerMenue.jsp" />
</div>
<div id="main">
    <jsp:include page="/views/leftSettingsMenue.jsp" />


    <div id="content">
        <ul class="groupview">
        </ul>
        <div class="contentpusher"></div>
    </div>
</div>
<jsp:include page="/views/footer.jsp"/>
</body>
</html>
