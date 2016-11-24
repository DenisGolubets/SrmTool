package com.dg.smrt.model.db;

import java.util.Date;

/**
 * Created by golubets on 29.08.2016.
 */
public class JdbcMySQL implements DbConnector {

    @Override
    public void writeData(Integer arduinoId, Date date, double t, double h) {

    }

    @Override
    public void initialization(Integer id, String name) {

    }

    @Override
    public void close() {

    }
}
