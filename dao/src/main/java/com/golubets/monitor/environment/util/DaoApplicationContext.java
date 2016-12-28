package com.golubets.monitor.environment.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by golubets on 14.12.2016.
 */

public class DaoApplicationContext {
    private static ApplicationContext context;
    private static DaoApplicationContext instance = new DaoApplicationContext();

    public static DaoApplicationContext getInstance() {
        return instance;
    }

    private DaoApplicationContext() {
        context = new AnnotationConfigApplicationContext("com.golubets.monitor.environment.dao");
    }

    public ApplicationContext getContext() {
        return context;
    }
}
