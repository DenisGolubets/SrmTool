package com.dg.srmt.web.servlet;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by golubets on 27.11.2016.
 */
public class ErrorHandler extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = null;
        try {
            response.setContentType("text/html");
            Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
            if (servletName == null) {
                servletName = "Unknown";
            }
            String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
            if (requestUri == null) {
                requestUri = "Unknown";
            }
            pw = response.getWriter();
            String title = "Error/Exception Information";
            String docType = "<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">";
            pw.println(docType + "<html>" + "<head><title>" + title + "</title></head>" + "<body bgcolor=\"#D9D9D9\">");

            if (throwable == null && statusCode == null) {
                pw.println("<h2>Error information is missing</h2>");
                pw.println("Please return to the <a href=\"" + response.encodeURL("http://localhost:8080/srmt/") + "\">Home Page</a>.");
            } else if (statusCode != null) {
                pw.println("The status code : " + statusCode);
            } else {
                pw.println("<h2>Error information</h2>");
                pw.println("Servlet Name : " + servletName + "</br></br>");
                pw.println("Exception Type : " + throwable.getClass().getName() + "</br></br>");
                pw.println("The request URI: " + requestUri + "<br><br>"); pw.println("The exception message: " + throwable.getMessage());
            }
            pw.println("</body>");
            pw.println("</html>");
        }finally {
            if (pw!=null){
                pw.close();
            }
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}