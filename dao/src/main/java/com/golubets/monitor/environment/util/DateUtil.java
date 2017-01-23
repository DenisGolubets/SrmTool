package com.golubets.monitor.environment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by golubets on 20.01.2017.
 */
public class DateUtil {
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public SimpleDateFormat getDATE_FORMAT() {
        return DATE_FORMAT;
    }

    public String getFormatedDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public String getPreviousHour(Date date) {
        Date tmpDate = new Date(date.getTime());
        tmpDate.setMinutes(0);
        tmpDate.setSeconds(0);
        return getFormatedDate(new Date(tmpDate.getTime() - 3600 * 1000));
    }

    public String getPreviousHour(String dateString) {
        Date date = getDateFromString(dateString);
        date.setMinutes(0);
        date.setSeconds(0);
        return getFormatedDate(new Date(date.getTime() - 3600 * 1000));
    }

    public String getCurrentHour(Date date) {
        Date tmpDate = new Date(date.getTime());
        tmpDate.setMinutes(0);
        tmpDate.setSeconds(0);
        return getFormatedDate(tmpDate);
    }

    public String getCurrentHour(String dateString) {
        Date date = getDateFromString(dateString);
        date.setMinutes(0);
        date.setSeconds(0);
        return getFormatedDate(date);
    }

    public Date getDateFromString(String date) {
        Date parseDate = null;
        try {
            parseDate = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parseDate;
    }

}