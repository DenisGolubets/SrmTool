package com.dg.smrt.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by golubets on 26.11.2016.
 */
public class MainServlet extends HttpServlet {
    private final String menu = "<tr><td><h1><a href=index>Home</a></h1></td>" +
            "<td><h1><a href=settings>Settings</a></h1></td></tr>";


    private final String homeNav = " <div id=nav><p class=navigationjump><a href=#content>Skip navigation</a></p>" +
            "<h2>Problems</h2><ul><li><a href=./problems.html#unknowns>Unknown</a> (0)</li>" +
            "</ul><h2>Groups</h2><ul><li><a href=./sr1/>Server room 1</a></li></ul></div>";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = null;
        try {
            pw = resp.getWriter();
            resp.setContentType("text/html;charset=utf-8");
            HttpSession session = req.getSession();
            String action = req.getParameter("action");


            if (action != null) {
                if (action.equals("editarduinopage")) {
                    editArduinoPage(req, resp);
                }
                if (action.equals("editarduino")) {
                    editArduino(req, resp);
                }
                if (action.equals("editemail")) {
                    editEmail(req, resp);
                }
            }
            if (req.getRequestURI().endsWith("index")) {
                homePage(req, resp);
            }
            if (req.getRequestURI().endsWith("settings")) {
                settingPage(req, resp);
            }
            if (req.getRequestURI().endsWith("settings/arduino")) {
                settingArduinoPage(req, resp);
            }
            if (req.getRequestURI().endsWith("settings/email")) {
                settingEmailPage(req, resp);
            }
            if (req.getRequestURI().endsWith("arduino?action=editarduinopage")) {
                editArduinoPage(req, resp);
            }


        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private void editEmail(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        List<String> list = new ArrayList<>();
        list.add(req.getParameter("host"));
        list.add(req.getParameter("from"));
        list.add(req.getParameter("to"));
        list.add(req.getParameter("port"));
        list.add(req.getParameter("ssl"));
        list.add(req.getParameter("login"));
        list.add(req.getParameter("pass"));


        settingEmailPage(req, resp);
    }

    private void settingEmailPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("menu", menu);
        req.getRequestDispatcher("settingsEmail.jsp").forward(req, resp);
    }

    private void editArduino(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> list = new ArrayList<>();
        list.add(req.getParameter("name"));
        list.add(req.getParameter("connectionType"));
        list.add(req.getParameter("mac"));
        list.add(req.getParameter("ip"));
        list.add(req.getParameter("subnet"));
        list.add(req.getParameter("gateway"));
        list.add(req.getParameter("dns"));
        list.add(req.getParameter("dhtPort"));
        list.add(req.getParameter("sensorType"));
        list.add(req.getParameter("isAlertT"));
        list.add(req.getParameter("isAlertH"));
        list.add(req.getParameter("topT"));
        list.add(req.getParameter("topH"));

        settingArduinoPage(req, resp);
    }

    public void homePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("menu", menu);
        req.setAttribute("nav", homeNav);
        String context = "    <div id=\"content\">\n" +
                "        <ul class=\"groupview\">\n" +
//                "            <li class=\"last\">\n" +
//                "                <span class=\"domain\"><a href=\"sr1\">Server room 1</a></span>\n" +
                "                <ul>\n" +
                "<div id=curve_chart style=width: 500px; height: 300px></div>"+
//                "                    <li class=\"last\">\n" +
//                "                        <span class=\"host\">Sensor 1</span>\n" +
//                "                        [Temperature: C, Humidity: % ]\n" +
                "                    </li></ul></li></ul>\n" +
                "        <div class=\"contentpusher\"></div>\n" +
                "    </div>";
        req.setAttribute("context", context);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    public void settingPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("menu", menu);
        req.getRequestDispatcher("settings.jsp").forward(req, resp);
    }

    public void settingArduinoPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("menu", menu);

        String context = "<div id=content><h2>Overview</h2>" +
                "<ul class=groupview><li class=last>" +
                " <span class=domain><a href=arduino?action=editarduinopage>Server room 2</a></span>" +
                "<ul><li class=last><span class=host>Sensor 1</span>" +
                "[Temperature: C, Humidity: % ]</li></ul></li></ul>" +
                "<div class=contentpusher></div></div>";
        req.setAttribute("context", context);
        req.getRequestDispatcher("settingsArduino.jsp").forward(req, resp);
    }

    public void editArduinoPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("menu", menu);

        req.getRequestDispatcher("editArduino.jsp").forward(req, resp);
    }


}
