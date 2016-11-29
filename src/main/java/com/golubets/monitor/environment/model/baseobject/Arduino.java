package com.golubets.monitor.environment.model.baseobject;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by golubets on 19.07.2016.
 */
@JsonAutoDetect
public class Arduino implements Serializable {
    private int id;
    private String serialPort;
    private String name;
    private String mac;
    private String ip;
    private String subnet;
    private String gateway;
    private String dns;

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public void setDhtPort(int dhtPort) {
        this.dhtPort = dhtPort;
    }

    public void setDhtType(int dhtType) {
        this.dhtType = dhtType;
    }

    public void setAlertT(boolean alertT) {
        isAlertT = alertT;
    }

    public void setAlertH(boolean alertH) {
        isAlertH = alertH;
    }

    private int dhtPort;
    private int dhtType;
    private int topT;
    private int topH;
    private boolean isAlertT;
    private boolean isAlertH;
    private ConnectionType connectionType;


    public Arduino() {

    }

    public Arduino(ConnectionType connectionType, String connectionString) throws IOException {
        this.connectionType = connectionType;
        this.id = Math.abs(0 + (int) (Math.random() * ((Integer.MAX_VALUE - 0) + 1)));
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
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

    public Integer getId() {
        return id;
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
}
