package com.golubets.monitor.environment.model.db;

import com.golubets.monitor.environment.model.baseobject.User;

import java.util.Date;

/**
 * Created by golubets on 29.08.2016.
 */
public class JdbcMySQL implements DbConnector {

    @Override
    public void persistArduinoDate(Integer arduinoId, Date date, double t, double h) {

    }

    @Override
    public User getUserByName(String userName) {
        return null;
    }

    @Override
    public void initialization(Integer id, String name) {

    }

    @Override
    public void renameArduino(Integer arduinoId, String name) {

    }

    @Override
    public void close() {

    }
}
