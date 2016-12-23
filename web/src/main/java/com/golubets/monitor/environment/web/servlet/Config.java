package com.golubets.monitor.environment.web.servlet;

import com.golubets.monitor.environment.Poll;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by golubets on 01.12.2016.
 */
public class Config implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Poll.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       // Poll.getInstance().close();
    }
}
