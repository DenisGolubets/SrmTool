package com.dg.smrt.model.db;

import java.util.Date;

/**
 * Created by golubets on 23.07.2016.
 */
public interface DbConnector {

   void initialization(Integer arduinoId, String name);

   void writeData(Integer arduinoId, Date date, double t, double h);

   void close();
}
