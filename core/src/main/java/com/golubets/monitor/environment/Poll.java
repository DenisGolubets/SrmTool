package com.golubets.monitor.environment;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.util.DaoApplicationContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created by golubets on 24.08.2016.
 */

public class Poll {

    private static final Logger log = Logger.getLogger(Poll.class);
    public static Poll instance;
    //private long period = 600000; //10 min
    private long period = 60000; //1 min
    private Timer timer = new Timer();

    private List<Arduino> arduinoList = Collections.synchronizedList(new ArrayList<Arduino>());
    private int arduinoCounter = 0;

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
        for (Arduino a : arduinoDao.getAll()) {
            try {
                new ArduinoListener(a, date);
            } catch (IOException e) {
                log.error(a.getName() + " has error "+e);
            }
        }
    }

    public Arduino getArduinoById(int id) {
        if (arduinoList.size() > 0) {
            for (Arduino a : arduinoList) {
                if (a.getId() == id) {
                    return a;
                }
            }
        }
        return null;
    }
}



