package com.dg.smrt.bo;


import com.dg.smrt.model.connection.Connector;
import com.dg.smrt.model.connection.EthConnector;
import com.dg.smrt.model.connection.JsscSerialConnector;

import java.io.*;

/**
 * Created by golubets on 19.07.2016.
 */
public class Arduino implements Serializable, Closeable {
    private static final long serialVersionUID = 0L;


    public Integer getId() {
        return id;
    }

    private int id;
    private String serialPort;


    private String name;
    private String mac;
    private String ip;
    private String subnet;
    private String gateway;
    private String dns;
    private int dhtPort;
    private int dhtType;
    private int topT;
    private int topH;
    private boolean isAlertT;
    private boolean isAlertH;
    private ConnectionType connectionType;
    private transient String connectionString;
    private transient Connector connector;


    public Arduino(ConnectionType connectionType, String connectionString) throws IOException {
        this.connectionType = connectionType;
        this.connectionString = connectionString;
        createConnection(connectionType);
        this.id = Math.abs(0 + (int) (Math.random() * ((Integer.MAX_VALUE - 0) + 1)));
    }

    private void createConnection(ConnectionType connectionType) throws IOException {
        if (this.connectionType == ConnectionType.SERIAL) {
            this.serialPort = connectionString;
            this.connector = new JsscSerialConnector(serialPort);
        } else if (this.connectionType == ConnectionType.ETHERNET) {
            this.connector = new EthConnector(ip, 23);
        }
    }

    public String getDate(String request) throws IOException {
String response = "";
        try {
            response = connector.getResponse(request);
        } catch (IOException e) {
            createConnection(connectionType);
            getDate(request);
        }
        return response;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        try {
            out.defaultWriteObject();
        } catch (IOException e) {
            createConnection(connectionType);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        createConnection(connectionType);

    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setTopT(int topT) {
        this.topT = topT;
    }

    public void setTopH(int topH) {
        this.topH = topH;
    }

    public String getSerialPort() {

        return serialPort;
    }


    public boolean isAlertT() {
        return isAlertT;
    }

    public boolean isAlertH() {
        return isAlertH;
    }

    public int getTopT() {
        return topT;
    }

    public int getTopH() {
        return topH;
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }

    public String getIp() {
        return ip;
    }

    public String getSubnet() {
        return subnet;
    }

    public String getGateway() {
        return gateway;
    }

    public String getDns() {
        return dns;
    }

    public int getDhtPort() {
        return dhtPort;
    }

    public int getDhtType() {
        return dhtType;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arduino)) return false;

        Arduino arduino = (Arduino) o;

        if (id != arduino.id) return false;
        if (!name.equals(arduino.name)) return false;
        return connectionType == arduino.connectionType;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + connectionType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String connectTo = (connectionType == ConnectionType.ETHERNET) ? ip : serialPort;

        return String.format("[Name: %s, id: %d, connected to: %s]", name, id, connectTo);
    }

    @Override
    public void close() throws IOException {
        connector.close();
    }
}
