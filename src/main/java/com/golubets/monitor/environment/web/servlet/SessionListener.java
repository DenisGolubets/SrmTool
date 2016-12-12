package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.model.Interrogation;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by golubets on 12.12.2016.
 */
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        try {
            se.getSession().setAttribute("dataDao",Interrogation.getInstance().getContext().getBean("dataDao"));
            se.getSession().setAttribute("arduinoDao",Interrogation.getInstance().getContext().getBean("arduinoDao"));
            se.getSession().setAttribute("userDao",Interrogation.getInstance().getContext().getBean("userDao"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
