package com.golubets.monitor.environment.model;

import javax.persistence.*;

/**
 * Created by golubets on 19.01.2017.
 */
@Entity
@Table(name = "avg_date", schema = "", catalog = "")
public class AvgDataEntity {
    private int id;
    private int arduinoId;
    private String dateTime;
    private double temp;
    private double hum;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "arduino_id", nullable = false, length = 0)
    public int getArduinoId() {
        return arduinoId;
    }

    public void setArduinoId(int arduinoId) {
        this.arduinoId = arduinoId;
    }

    @Basic
    @Column(name = "date_time", nullable = false, length = 0)
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Basic
    @Column(name = "temp", nullable = false, precision = -1)
    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @Basic
    @Column(name = "hum", nullable = false, precision = -1)
    public double getHum() {
        return hum;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvgDataEntity)) return false;

        AvgDataEntity that = (AvgDataEntity) o;

        if (id != that.id) return false;
        if (arduinoId != that.arduinoId) return false;
        if (Double.compare(that.temp, temp) != 0) return false;
        if (Double.compare(that.hum, hum) != 0) return false;
        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        result = id;
        result = 31 * result + arduinoId;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(hum);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "DataEntity { Arduino = " + arduinoId + ", date: " + dateTime + ", Temp: " + temp + ", Hum: " + hum + " }";
    }
}
