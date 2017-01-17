<%@ page import="java.util.Date" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <title>Home</title>
    <c:if test="${empty arduino}">
        <c:set var="arduinol" value="${arduinos}"></c:set>
    </c:if>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['month', 'sensor1', 'sensor2'],
                ['1', 22, 22.1],
                ['2', 21, 23],
                ['3', 20, 21],
                ['4', 20, 23]
            ]);
            var options = {
                title: '',
                curveType: 'function',
                legend: {position: 'bottom'}
            };
            var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
            chart.draw(data, options);
        }
    </script>
    <script>
        function getXmlHttp() {
            var xmlhttp;
            try {
                xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (E) {
                    xmlhttp = false;
                }
            }
            if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
                xmlhttp = new XMLHttpRequest();
            }
            return xmlhttp;
        }

        function getarduino(str) {
            var xmlhttp = getXmlHttp()
            var url = "/" + str;
            xmlhttp.open('GET', url, true);
            xmlhttp.send(null);
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    var arr = JSON.parse(xmlhttp.responseText);
                    console.log(arr);
                    var out = "";
                    var temp;
                    var hum;
                    var i;
                    for (i = 0; i < arr.length; i++) {
                        temp = arr[i].temp;
                        hum = arr[i].hum;
                        out += arr[i].name;
                        out += "<br><li class=last> <span class=host>Sensor </span>";
                        out += "[ Temperature: " + arr[i].temp + ", Humidity: " + arr[i].hum + " ]<br></li>";
                    }
                    if (i < 2) {
//                        out += "<div id=curve_chart style=width:500px; height: 300px></div><br>";
                        out += "<p><button class=astext onclick=getarduino(-1)>All</button>";
                    }


                    document.getElementById("test").innerHTML = out;

                    if (i < 2) {
                        google.charts.load('current', {'packages': ['corechart']});
                        var data = google.visualization.arrayToDataTable([
                            ['month', 'temp', 'hum'],
                            ['1', temp, hum],
                            ['2', temp, hum],
                            ['3', temp, hum],
                            ['4', temp, hum]
                        ]);
                        var options = {
                            title: '',
                            curveType: 'function',
                            legend: {position: 'bottom'}
                        };
                        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
                        chart.clear;
                        chart.draw(data, options);
                    }
                }
            };
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
        <p class=navigationjump><a href=#content>Skip navigation</a></p>
        <h2>Groups</h2>
        <ul>
            <c:forEach items="${arduinos}" var="arduino">
                <li>
                    <button class="astext" onclick="getarduino(${arduino.getId()})">${arduino.getName()}</button>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div id="content">
        <ul class=\"groupview\">
            <div id="test">
                <c:forEach items="${arduinol}" var="arduino">
                <li>
                    <button class="astext" onclick="onclick()"
                            value="${arduino.getName()}">${arduino.getName()}</button>
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
