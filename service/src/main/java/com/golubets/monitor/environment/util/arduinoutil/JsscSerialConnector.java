package com.golubets.monitor.environment.util.arduinoutil;

import jssc.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by golubets on 29.07.2016.
 */
public class JsscSerialConnector implements Connector, Closeable {
    private final String port;
    private static SerialPort serial;
    private static StringBuilder sb;

    public JsscSerialConnector(String port) {
        this.port = port;
        try {
            connect();
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws SerialPortException, InterruptedException {
        serial = new SerialPort(port);
        serial.openPort();
        serial.setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        // for initialization Arduino, ethShield and DHT sensor needed 4 sec
        TimeUnit.SECONDS.sleep(4);

        sb = new StringBuilder();
    }

    public void close() {
        try {
            serial.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getResponse(String request) throws IOException {
        sb.setLength(0);
        try {
            serial.writeString(request);

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        try {
            long startReading = System.currentTimeMillis();
            while (sb.indexOf("end") < 1) {
                sb.append(serial.readString());
                if (System.currentTimeMillis() - startReading > 2000) {
                    getResponse(request);
                }
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }

        String result = sb.toString().replaceAll("null", "").replaceAll("[\\s]", "");
        return result.substring(0, result.length() - 3);
    }


    @Override
    public void setDate(String request) throws IOException {
        try {
            serial.writeString(request);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
