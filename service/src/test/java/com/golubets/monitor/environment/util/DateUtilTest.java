package com.golubets.monitor.environment.util;


import com.golubets.monitor.environment.dao.DataDao;
import com.golubets.monitor.environment.model.Arduino;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by golubets on 02.03.2017.
 */

public class DateUtilTest {
    @Autowired
    private static DataDao dataDao;

    public static void main(String[] args) {

        Arduino arduino = DaoApplicationContext.getInstance().getContext().getBean(Arduino.class);
        arduino.setId(125872268);
        String currentTime = "02.03.2017 00:00:00";
        String prevTime = new DateUtil().getPreviousHour(currentTime);

//        AvgDataEntity entity = dataDao.getAvg(arduino,currentTime,prevTime);

        System.out.println(dataDao.getLastRowByArduino(arduino));

    }

}