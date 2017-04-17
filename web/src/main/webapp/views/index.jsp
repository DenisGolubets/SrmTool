<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.png" type="image/png">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <title>Home</title>
    <c:if test="${empty arduino}">
        <c:set var="arduinol" value="${arduinos}"></c:set>
    </c:if>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});
    </script>
    <script>
        $(document).ready(function () {
            var avgDate;
            $('.astext').click(function () {
                var buttonid = this.id;
                $.ajax({
                    url: '/' + buttonid,
                    type: 'GET',
                    contentType: 'application/x-www-form-urlencoded',
                    dataType: "json",
                    success: function (arr) {
                        console.log(arr);
                        var out = "";
                        var i;
                        var buttonid = arr[0].id;
                        for (i = 0; i < arr.length; i++) {
                            temp = arr[i].temp;
                            hum = arr[i].hum;
                            out += "<li>" + arr[i].name + "</li>";
                            out += "<ul><li class=last> <span class=host>Sensor </span>";
                            out += "[ Temperature: " + arr[i].temp + ", Humidity: " + arr[i].hum + " ]<br></li></ul>";
                        }
                        if (i < 2) {
                            out += "<button class=astext id='-1' >All</button>";
                        }
                        document.getElementById("mbody").innerHTML = out;
                        drawChartAvdData(buttonid);
                    }
                });
            });
        });
        function drawChartAvdData(buttonid) {
            $.ajax({
                url: '/m' + buttonid,
                type: 'GET',
                contentType: 'application/x-www-form-urlencoded',
                dataType: "json",
                success: function (arr) {
                    var data = [[]];
                    var i;
                    for (i = 0; i < arr.length; i++) {
                        data[i] = [];
                        data[i][0] = arr[i].dateTime;
                        if (arr[i].temp > 0) {
                            data[i][1] = arr[i].temp;
                        } else data[i][1] = null;

                        if (arr[i].hum > 0) {
                            data[i][2] = arr[i].hum;
                        } else data[i][2] = null;
                    }
                    data.unshift(['date', 'temp,C', 'hum,%']);
                    var data = google.visualization.arrayToDataTable(data);
                    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
                    chart.clear;
                    var options = {
                        title: '', curveType: 'function',
                        legend: {position: 'bottom'}
                    };
                    chart.draw(data, options);
                }
            });
        }
    </script>

</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <jsp:include page="/views/headerMenue.jsp"/>
</div>
<div id="main">
    <div id=nav>
        <h2>Groups</h2>
        <ul>
            <c:forEach items="${arduinos}" var="arduino">
                <li>
                    <button class="astext" id="${arduino.getId()}">${arduino.getName()}</button>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div id="content">
        <ul class=\"groupview\">
            <div id="mbody">
                <c:forEach items="${arduinol}" var="arduino">
                <li>
                        ${arduino.getName()}
                </li>
                <ul>
                    <li class=last>
                        <span class=host>Sensor </span>
                        [ Temperature: ${arduino.getTemp()}, Humidity: ${arduino.getHum()} ]
                    </li>
                </ul>
                <div>
                    </c:forEach>
        </ul>
        <div id=curve_chart style="height: 300px">
            <br>
            <div class="contentpusher"></div>
        </div>
    </div>
</div>
<jsp:include page="/views/footer.jsp"/>
</body>
</html>
