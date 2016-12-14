package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.Interrogation;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by golubets on 01.12.2016.
 */
public class Config implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Interrogation.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       // Interrogation.getInstance().close();
    }
}
