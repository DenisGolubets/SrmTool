package com.golubets.monitor.environment.web.servlet;

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
            if (action.equals("index")) {
                homePage(req, resp);
            }
            if (action.equals("settings")) {
                req.getRequestDispatcher("settings.jsp").forward(req, resp);
                settingPage(req, resp);
            }
            if (action.equals("settingsarduino")) {
                settingArduinoPage(req, resp);
            }
            if (action.equals("settingsemailpage")) {
                settingEmailPage(req, resp);
            }
            if (action.equals("editarduinopage")) {
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
        req.getRequestDispatcher("settingsArduino.jsp").forward(req, resp);
    }

    public void editArduinoPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("editArduino.jsp").forward(req, resp);
    }
}
