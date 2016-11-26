<%--
  Created by IntelliJ IDEA.
  User: golubets
  Date: 26.11.2016
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {
            var data = google.visualization.arrayToDataTable([
                ['month', 'sensor1', 'sensor2'],
                ['1',  22,      22.1],
                ['2',  21,      23],
                ['3',  20,       21],
                ['4',  20,      23]
            ]);

            var options = {
                title: '',
                curveType: 'function',
                legend: { position: 'bottom' }
            };

            var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

            chart.draw(data, options);
        }
    </script>

</head>
<head>
    <style>
        <%@include file='/css/style-new.css' %>
    </style>
    <title></title>
</head>
<!-- -*- HTML -*- -->
<body>
<div id="header">
    <h1>ServerRoomMonitorTools</h1>
    <table width="200" border="0">
        <tbody>
        ${menu}
        </tbody>
    </table>
</div>
<div id="main">
    ${nav}
    ${context}
</div>
<div id="footer">
    <p class="tagline">Copyright 2016</p>
</div>
</body>
</html>
