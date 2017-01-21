package com.golubets.monitor.environment;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.util.DaoApplicationContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by golubets on 24.08.2016.
 */
public class Poll {

    private static final Logger log = Logger.getLogger(Poll.class);
    public static Poll instance;
    private long period = 600000; //10 min
    //    private long period = 60000; //1 min
    private Timer timer = new Timer();

    public static Poll getInstance() {
        if (instance == null) {
            synchronized (Poll.class) {
                if (instance == null) {
                    instance = new Poll();
                }
            }
        }
        return instance;
    }

    private Poll() {
        interview();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                interview();
            }
        }, period, period);
    }

    private void interview() {
        final Date date = new Date();
        ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");
        List<Arduino> list = arduinoDao.getAll();
        for (Arduino a : list) {
            try {
                new ArduinoListener(a, date);
            } catch (NumberFormatException e) {
                String textBody = "Check the sensor arduinoconnection on Arduino ";
                log.error(textBody + a, e);
            } catch (SocketTimeoutException e) {
                String textBody = "The Arduino is disconnected ";
                log.error(textBody + a, e);
            } catch (IOException e) {
                String textBody = "The Arduino has problems ";
                log.error(textBody + a, e);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}



