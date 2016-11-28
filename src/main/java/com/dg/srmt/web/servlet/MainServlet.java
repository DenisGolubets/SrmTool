package com.dg.srmt.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by golubets on 26.11.2016.
 */
public class MainServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("charset=utf-8");
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String url = req.getRequestURI();

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
        if (url != null && url.length() > 0) {
            if (url.endsWith("index")) {
                homePage(req, resp);
            }
            if (url.endsWith("settings")) {
                settingPage(req, resp);
            }
            if (url.endsWith("settings/arduino")) {
                settingArduinoPage(req, resp);
            }
            if (url.endsWith("settings/email")) {
                settingEmailPage(req, resp);
            }
            if (url.endsWith("arduino?action=editarduinopage")) {
                editArduinoPage(req, resp);
            }
        }
    }

    private void editEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        settingEmailPage(req, resp);
    }

    private void settingEmailPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("settingsEmail.jsp").forward(req, resp);
    }

    private void editArduino(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        settingArduinoPage(req, resp);
    }

    public void homePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    public void settingPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("settings.jsp").forward(req, resp);
    }

    public void settingArduinoPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        req.getRequestDispatcher("editArduino.jsp").forward(req, resp);
    }


}
