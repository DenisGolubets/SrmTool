package com.golubets.monitor.environment.model.db;

import java.util.Date;

/**
 * Created by golubets on 23.07.2016.
 */
public interface DbConnector {

   void initialization(Integer arduinoId, String name);
   void renameArduino(Integer arduinoId, String name);

   void persist(Integer arduinoId, Date date, double t, double h);

   void close();
}
