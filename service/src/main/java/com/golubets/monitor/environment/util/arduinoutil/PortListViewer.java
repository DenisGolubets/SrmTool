package com.golubets.monitor.environment.util.arduinoutil;

import jssc.SerialPortList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by golubets on 15.08.2016.
 */
public class PortListViewer {

    public static List<String> getPortNames(){
        List<String> portList = new ArrayList<>();
        Collections.addAll(portList, SerialPortList.getPortNames());
        return portList;
    }
}
