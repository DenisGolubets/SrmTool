<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        <title>Home</title>

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

</head>
<!-- -*- HTML -*- -->
<body>

<div id="header">
    <h1>ServerRoomMonitorTools</h1>
    <table width="200" border="0">
        <tbody>
        <tr>
            <td><h1><a href=${pageContext.request.contextPath}>Home</a></h1></td>
            <td><h1><a href=${pageContext.request.contextPath}../settings?action=settings>Settings</a></h1></td>
        </tr>
        </tbody>
    </table>
</div>
<div id="main">
    <div id=nav>
        <p class=navigationjump><a href=#content>Skip navigation</a></p>
        <h2>Problems</h2>
        <ul>
            <li><a href=./problems.html#unknowns>Server room 1</a> (0)</li>
        </ul>
        <h2>Groups</h2>
        <ul>
            <li><a href=./sr1/>Server room 1</a></li>
        </ul>
    </div>

    <div id="content">
        <ul class=\"groupview\">
            <ul>
                <div id=curve_chart style=width:500px; height: 300px></div>
            </ul>
        </ul>
        <div class="contentpusher"></div>
    </div>
</div>
<div id="footer">
    <p class="tagline">Copyright 2016</p>
</div>
</body>
</html>
