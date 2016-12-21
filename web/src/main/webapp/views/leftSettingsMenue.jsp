<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 21.12.2016
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id=nav>
    <p class=navigationjump><a href=#content>Skip navigation</a></p>
    <ul>
        <li>
            <a href=${pageContext.request.contextPath}/settings>General</a>
        </li>
    </ul>
    <ul>
        <li><a href=${pageContext.request.contextPath}/settings/arduino>Arduino</a></li>
    </ul>
    <ul>
        <li><a href=${pageContext.request.contextPath}/settings/email>Email</a></li>
    </ul>
</div>
