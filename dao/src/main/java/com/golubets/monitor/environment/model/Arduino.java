package com.golubets.monitor.environment.model;


import com.golubets.monitor.environment.util.ArduinoIDGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by golubets on 19.07.2016.
 */
@Entity
@Table(name = "arduino", schema = "", catalog = "")
public class Arduino implements Serializable {

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

    private double temp;
    private double hum;

    public Arduino() {
    }

    public Arduino(ConnectionType connectionType, String connectionString) throws IOException {
        this.connectionType = connectionType;
        this.id = new ArduinoIDGenerator().genereteId();
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    @NotEmpty
    @Basic
    @Column(name = "name", nullable = false, length = 0)
    public String getName() {
        return name;
    }


    @Basic
    @Column(name = "connectionType", nullable = true, length = 0)
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Basic
    @Column(name = "serialport", nullable = true, length = 0)
    public String getSerialPort() {

        return serialPort;
    }

    @Basic
    @Column(name = "alertt", nullable = true)
    public boolean isAlertT() {
        return isAlertT;
    }

    @Basic
    @Column(name = "alerth", nullable = true)
    public boolean isAlertH() {
        return isAlertH;
    }

    @Basic
    @Column(name = "topt", nullable = true)
    public int getTopT() {
        return topT;
    }

    @Basic
    @Column(name = "toph", nullable = true)
    public int getTopH() {
        return topH;
    }

    @Basic
    @Column(name = "mac", nullable = true, length = 0)
    public String getMac() {
        return mac;
    }

    @Basic
    @Column(name = "ip", nullable = true, length = 0)
    public String getIp() {
        return ip;
    }

    @Basic
    @Column(name = "subnet", nullable = true, precision = -1)
    public String getSubnet() {
        return subnet;
    }

    @Basic
    @Column(name = "gateway", nullable = true, length = 0)
    public String getGateway() {
        return gateway;
    }

    @Basic
    @Column(name = "dns", nullable = true, length = 0)
    public String getDns() {
        return dns;
    }

    @Basic
    @Column(name = "dhtport", nullable = true)
    public int getDhtPort() {
        return dhtPort;
    }

    @Basic
    @Column(name = "dhttype", nullable = true)
    public int getDhtType() {
        return dhtType;
    }

    @Basic
    @Column(name = "lasth", nullable = true)
    public double getHum() {
        return hum;
    }

    @Basic
    @Column(name = "lastt", nullable = true)
    public double getTemp() {
        return temp;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setHum(double hum) {
        this.hum = hum;
    }

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

    public void setTemp(double temp) {
        this.temp = temp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arduino)) return false;

        Arduino arduino = (Arduino) o;

        if (id != arduino.id) return false;
        if (dhtPort != arduino.dhtPort) return false;
        if (dhtType != arduino.dhtType) return false;
        if (topT != arduino.topT) return false;
        if (topH != arduino.topH) return false;
        if (isAlertT != arduino.isAlertT) return false;
        if (isAlertH != arduino.isAlertH) return false;
        if (Double.compare(arduino.temp, temp) != 0) return false;
        if (Double.compare(arduino.hum, hum) != 0) return false;
        if (serialPort != null ? !serialPort.equals(arduino.serialPort) : arduino.serialPort != null) return false;
        if (name != null ? !name.equals(arduino.name) : arduino.name != null) return false;
        if (mac != null ? !mac.equals(arduino.mac) : arduino.mac != null) return false;
        if (ip != null ? !ip.equals(arduino.ip) : arduino.ip != null) return false;
        if (subnet != null ? !subnet.equals(arduino.subnet) : arduino.subnet != null) return false;
        if (gateway != null ? !gateway.equals(arduino.gateway) : arduino.gateway != null) return false;
        if (dns != null ? !dns.equals(arduino.dns) : arduino.dns != null) return false;
        return connectionType == arduino.connectionType;

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = id;
        result = 31 * result + (serialPort != null ? serialPort.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (subnet != null ? subnet.hashCode() : 0);
        result = 31 * result + (gateway != null ? gateway.hashCode() : 0);
        result = 31 * result + (dns != null ? dns.hashCode() : 0);
        result = 31 * result + dhtPort;
        result = 31 * result + dhtType;
        result = 31 * result + topT;
        result = 31 * result + topH;
        result = 31 * result + (isAlertT ? 1 : 0);
        result = 31 * result + (isAlertH ? 1 : 0);
        result = 31 * result + (connectionType != null ? connectionType.hashCode() : 0);
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(hum);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }

    @Override
    public String toString() {
        String connectTo = (connectionType == ConnectionType.ETHERNET) ? ip : serialPort;

        return String.format("[Name: %s, id: %d, connected to: %s]", name, id, connectTo);
    }
}
