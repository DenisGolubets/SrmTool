package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.Interrogation;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.ConnectionType;
import com.golubets.monitor.environment.model.User;
import com.golubets.monitor.environment.dao.UserDao;
import com.golubets.monitor.environment.model.MailSettings;

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
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        String role = (String) session.getAttribute("role");

        if (role == null) {
            if (action != null && action.equals("signin")) {
                if (validateUser(req, resp)) {
                    req.getRequestDispatcher("settings.jsp").include(req, resp);
                } else {
                    req.getRequestDispatcher("login.jsp").forward(req, resp);
                }
            } else {
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } else {
            if (role.equals("root")) {
                if (action != null) {
                    if (action.equals("editarduinopage")) {
                        req.getRequestDispatcher("editArduino.jsp").forward(req, resp);
                    }
                    if (action.equals("editarduino")) {
                        editArduinopage(req, resp);
                    }
                    if (action.equals("index")) {
                        req.getRequestDispatcher("index.jsp").forward(req, resp);
                    }
                    if (action.equals("settings")) {
                        req.getRequestDispatcher("settings.jsp").forward(req, resp);
                    }
                    if (action.equals("settingsarduino")) {
                        req.getRequestDispatcher("settingsArduino.jsp").forward(req, resp);
                    }
                    if (action.equals("addemail")) {
                        configureEmail(req, resp);
                    }

                    if (action.equals("editemail")) {
                        configureEmail(req, resp);
                    }

                    if (action.equals("settingsemailpage")) {
                        if (Interrogation.getInstance().getSettingsMap() != null && Interrogation.getInstance().getSettingsMap().containsKey("mail")) {
                            req.getRequestDispatcher("settingsEmail.jsp").forward(req, resp);
                        } else {
                            req.getRequestDispatcher("addEmail.jsp").forward(req, resp);
                        }
                    }
                    if (action.equals("addarduinopage")) {
                        req.getRequestDispatcher("addArduino.jsp").forward(req, resp);
                    }
                    if (action.equals("addarduino")) {
                        addArduino(req, resp);
                    }
                    if (action.equals("editarduino")) {
                        editArduino(req, resp);
                    }
                    if (action.equals("addarduino")) {
                        editArduino(req, resp);
                    }
                } else {
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                }
            }
        }
    }

    private boolean validateUser(HttpServletRequest req, HttpServletResponse resp) {

        User user = new User();
        user.setUserName(req.getParameter("j_username"));
        user.setPassword(Interrogation.getInstance().sha1(req.getParameter("j_password")));
//        User userDb = Interrogation.getInstance().getDb().getUserByName(user.getUserName());
        UserDao userDao = (UserDao) req.getSession().getAttribute("userDao");
        User userDb = userDao.getByName(user.getUserName()) ;

        if (userDb != null && user.getUserName().equals(userDb.getUserName()) && user.getPassword().equals(userDb.getPassword())) {
            req.getSession().setAttribute("role", userDb.getRole());
            return true;
        } else {
            return false;
        }
    }


    private void configureEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MailSettings mailSettings = prepareEmail(req);
        int mailErrCounter = checkEmail(mailSettings, req, resp);
        if (mailErrCounter == 0) {
            Interrogation.getInstance().editMailSetting(mailSettings);
            editEmail(req, resp);
        } else {
            req.getRequestDispatcher("addEmail.jsp").forward(req, resp);
        }
    }

    private int checkEmail(MailSettings mailSettings, HttpServletRequest req, HttpServletResponse resp) {
        int mailErrCounter = 0;
        if (mailSettings.getHost().length() == 0) {

            req.setAttribute("errHost", "style=\"background-color: #FF7A7C \"");
            mailErrCounter++;
        }
        if (mailSettings.getFrom().length() == 0) {
            req.setAttribute("errFrom", "style=\"background-color: #FF7A7C \"");
            mailErrCounter++;
        }
        if (mailSettings.getTo().length() == 0) {
            req.setAttribute("errTo", "style=\"background-color: #FF7A7C \"");
            mailErrCounter++;
        }
        return mailErrCounter;
    }

    private MailSettings prepareEmail(HttpServletRequest req) {

        MailSettings mailSettings = new MailSettings();
        mailSettings.setHost(req.getParameter("host"));
        mailSettings.setFrom(req.getParameter("from"));
        mailSettings.setTo(req.getParameter("to"));
        mailSettings.setLogin(req.getParameter("login"));
        mailSettings.setPass(req.getParameter("pass"));
        mailSettings.setPort(req.getParameter("port"));
        mailSettings.setSsl((req.getParameter("host") != null) ? true : false);
        return mailSettings;
    }

    private void editArduino(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Arduino arduino = prepareArduino(req);

        int errorCounter = chekArduino(arduino, req, resp);
        if (errorCounter == 0) {
            Interrogation.getInstance().editArduino(arduino);
            req.getRequestDispatcher("settingsArduino.jsp").forward(req, resp);
        } else {
            req.getSession().setAttribute("arduino", arduino);
            req.getRequestDispatcher("editArduino.jsp").forward(req, resp);
        }
    }

    private void addArduino(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Arduino arduino = prepareArduino(req);
        int errArduinoCounter = chekArduino(arduino, req, resp);
        if (errArduinoCounter == 0) {
            Interrogation interrogation = Interrogation.getInstance();
            interrogation.addArduino(arduino);
            req.getRequestDispatcher("settingsArduino.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("addArduino.jsp").forward(req, resp);
        }
    }

    private int chekArduino(Arduino arduino, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int errorCounter = 0;
        if (arduino.getName().length() == 0) {
            req.setAttribute("errName", "style=\"background-color: #FF7A7C \"");
            errorCounter++;
        }
        if (arduino.getIp().length() == 0) {
            req.setAttribute("errIp", "style=\"background-color: #FF7A7C \"");
            errorCounter++;
        }
        if (arduino.getDhtPort() == -1) {
            req.setAttribute("errDhtPort", "style=\"background-color: #FF7A7C \"");
            errorCounter++;
        }

        return errorCounter;
    }

    private Arduino prepareArduino(HttpServletRequest req) throws IOException {
        Arduino arduino = null;
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String stringConnectionType = req.getParameter("connectionType");
        ConnectionType connectionType = null;

        if (stringConnectionType.equalsIgnoreCase(ConnectionType.ETHERNET.name())) {
            connectionType = ConnectionType.ETHERNET;
        } else if (stringConnectionType.equalsIgnoreCase(ConnectionType.SERIAL.name())) {
            connectionType = ConnectionType.SERIAL;
        }

        String serialPort = req.getParameter("serialPort");
        String mac = req.getParameter("mac");
        String ip = req.getParameter("ip");
        String subnet = req.getParameter("subnet");
        String gateway = req.getParameter("gateway");
        String dns = req.getParameter("dns");

        String stringDhtPort = req.getParameter("dhtPort");
        int dhtPort;
        try {
            dhtPort = Integer.parseInt(stringDhtPort);
        } catch (NumberFormatException e) {
            dhtPort = -1;
        }
        String stringDhtType = req.getParameter("dhtType");
        int dhtType;
        try {
            dhtType = Integer.parseInt(stringDhtType);
        } catch (NumberFormatException e) {
            dhtType = -1;
        }

        String StringTopT = req.getParameter("topT");
        int topT;
        String StringTopH = req.getParameter("topH");
        int topH;

        String StringIsAlertT = req.getParameter("isAlertT");
        boolean isAlertT = (StringIsAlertT != null && StringIsAlertT.equals("on")) ? true : false;
        String StringIsAlertH = req.getParameter("isAlertH");
        boolean isAlertH = (StringIsAlertH != null && StringIsAlertH.equals("on")) ? true : false;
        try {
            topT = Integer.parseInt(StringTopT);

        } catch (NumberFormatException e) {
            topT = -1;
        }
        try {
            topH = Integer.parseInt(StringTopH);

        } catch (NumberFormatException e) {
            topH = -1;
        }

        if (id == null || id.length() == 0) {
            arduino = new Arduino(connectionType, ip);
        } else {
            arduino = Interrogation.getInstance().getArduinoById(Integer.parseInt(id));
        }
        arduino.setName(name);
        arduino.setConnectionType(connectionType);
        arduino.setIp(ip);
        arduino.setMac(mac);
        arduino.setSubnet(subnet);
        arduino.setGateway(gateway);
        arduino.setDns(dns);
        arduino.setDhtPort(dhtPort);
        arduino.setDhtType(dhtType);
        arduino.setSerialPort(serialPort);
        arduino.setDhtPort(dhtPort);
        arduino.setAlertT(isAlertT);
        arduino.setAlertH(isAlertH);
        arduino.setTopT(topT);
        arduino.setTopH(topH);


        return arduino;
    }

    private void editEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("settingsEmail.jsp").forward(req, resp);
    }


    private void editArduinopage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
