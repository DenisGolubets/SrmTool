package com.golubets.monitor.environment.model;

import com.golubets.monitor.environment.model.baseobject.Arduino;
import com.golubets.monitor.environment.model.baseobject.BaseObject;
import com.golubets.monitor.environment.model.db.DbConnector;
import com.golubets.monitor.environment.model.db.JdbcSqliteConnection;
import com.golubets.monitor.environment.model.mail.MailSettings;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created by golubets on 24.08.2016.
 */
public class Interrogation implements AutoCloseable {

    private static final Logger log = Logger.getLogger(Interrogation.class);
    public static Interrogation instance;
    private Map<String, BaseObject> settingsMap;
    //private long period = 600000; //10 min
    private long period = 60000; //1 min
    private Timer timer = new Timer();
    private DbConnector db = null;
    private List<Arduino> arduinoList = Collections.synchronizedList(new ArrayList<Arduino>());
    private int arduinoCounter = 0;

    public static Interrogation getInstance() {
        if (instance == null) {
            synchronized (Interrogation.class) {
                if (instance == null) {
                    instance = new Interrogation();
                }
            }
        }
        return instance;
    }

    private Interrogation() {
        settingsMap = new SettingsLoaderSaver().loadEncryptedSettingsFromJsonFile();
        arduinoList = new ArduinoLoderSaver().loadArduinoFromJsonFile();

        db = new JdbcSqliteConnection();
        if (arduinoList.size() > 0) {
            for (Arduino a : arduinoList) {

                db.initialization(a.getId(), a.getName());
            }
            for (Arduino a : arduinoList) {
                try {
                    new Thread(new ArduinoListener(a, db, settingsMap)).start();
                    arduinoCounter++;
                } catch (IOException e) {
                    log.error(e);
                }
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        checkNewArduino();
                        doJob();
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }, period, period);
        }
    }


    public void addArduino(Arduino arduino) {
        try {
            List<Arduino> list = new ArrayList<>();
            list.add(arduino);
            new ArduinoLoderSaver().saveArduinoToJsonFile(list);
            new Thread(new ArduinoListener(arduino, db, settingsMap)).start();
            arduinoCounter++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        arduinoList.add(arduino);
    }

    public void editMailSetting(MailSettings mailSettings) {
        if (settingsMap == null) {
            settingsMap = new HashMap<>();
        }
        settingsMap.put("mail", mailSettings);
        new SettingsLoaderSaver().saveEncryptedSettingsToJsonFile(settingsMap);
    }

    public void editArduino(Arduino arduino) {
        Arduino oldArduino = getArduinoById(arduino.getId());
        db.renameArduino(oldArduino.getId(), arduino.getName());
        List<Arduino> list = new ArrayList<>();
        list.add(oldArduino);
        new ArduinoLoderSaver().saveArduinoToJsonFile(list);

    }

    private void checkNewArduino() throws IOException {
        if (arduinoList.size() > arduinoCounter) {
            new Thread(new ArduinoListener(arduinoList.get(arduinoList.size() - 1), db, settingsMap)).start();
            db.initialization(arduinoList.get(arduinoList.size() - 1).getId(), arduinoList.get(arduinoList.size() - 1).getName());
        }
    }

    private void doJob() {
        for (Arduino a : arduinoList) {
            synchronized (a) {
                a.notify();
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

    public List<Arduino> getArduinoList() {
        return arduinoList;
    }

    public Map<String, BaseObject> getSettingsMap() {
        return settingsMap;
    }

    @Override
    public void close() {
        if (db != null) {
            db.close();
        }
    }
}



