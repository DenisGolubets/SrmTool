package com.golubets.monitor.environment;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.exception.PersistException;
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

    private static final Logger LOGGER = Logger.getLogger(Poll.class);
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
        try {
            Date date = new Date();
            ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");
            List<Arduino> list = arduinoDao.getAll();
            for (Arduino a : list) {
                try {
                    new ArduinoDispatcher(a, date);
                } catch (NumberFormatException e) {
                    String textBody = "Check the sensor arduinoutil on Arduino ";
                    LOGGER.error(textBody + a, e);
                } catch (SocketTimeoutException e) {
                    String textBody = "The Arduino is disconnected ";
                    LOGGER.error(textBody + a, e);
                } catch (IOException e) {
                    String textBody = "The Arduino has problems ";
                    LOGGER.error(textBody + a, e);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        } catch (PersistException e) {
            LOGGER.error("", e);
        }
    }
}



