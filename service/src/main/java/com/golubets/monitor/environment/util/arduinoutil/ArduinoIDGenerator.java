package com.golubets.monitor.environment.util.arduinoutil;

/**
 * Created by golubets on 23.12.2016.
 */
public class ArduinoIDGenerator {
    public Integer genereteId(){
        return Math.abs(0 + (int) (Math.random() * ((Integer.MAX_VALUE - 0) + 1)));
    }
}
