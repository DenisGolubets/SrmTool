package com.golubets.monitor.environment.model;

import com.golubets.monitor.environment.model.baseobject.Arduino;
import com.golubets.monitor.environment.model.baseobject.BaseObject;
import com.golubets.monitor.environment.model.baseobject.HibernateSessionFactory;
import com.golubets.monitor.environment.model.baseobject.dao.ArduinoDao;
import com.golubets.monitor.environment.model.baseobject.dao.DataDao;
import com.golubets.monitor.environment.model.baseobject.dao.UserDao;
import com.golubets.monitor.environment.model.db.JdbcSqliteConnection;
import com.golubets.monitor.environment.model.mail.MailSettings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by golubets on 24.08.2016.
 */
@Component
public class Interrogation implements AutoCloseable {

    private static final Logger log = Logger.getLogger(Interrogation.class);
    public static Interrogation instance;
    private Map<String, BaseObject> settingsMap;
    ApplicationContext context;
    //private long period = 600000; //10 min
    private long period = 60000; //1 min
    private Timer timer = new Timer();

    @Autowired
    ArduinoDao arduinoDao;

    @Autowired
    UserDao userDao;

    @Autowired
    DataDao dataDao;
    //private DbConnector db;

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

//    public DbConnector getDb() {
//        return db;
//    }

    public ApplicationContext getContext() {
        return context;
    }

    private Interrogation() {
        context = new AnnotationConfigApplicationContext(JdbcSqliteConnection.class, ArduinoDao.class, UserDao.class, DataDao.class);
        //db = (DbConnector) context.getBean("db");
        arduinoDao = (ArduinoDao) context.getBean("arduinoDao");
        userDao = (UserDao) context.getBean("userDao");


        settingsMap = new SettingsLoaderSaver().loadEncryptedSettingsFromJsonFile();
        arduinoList = new ArduinoLoderSaver().loadArduinoFromJsonFile();

        //db = new JdbcSqliteConnection();
        if (arduinoList.size() > 0) {
            for (Arduino a : arduinoList) {

                //db.initialization(a.getId(), a.getName());
                if (arduinoDao.getByID(a.getId()).getId()!=a.getId()){
                    arduinoDao.persist(a);
                }
            }
            for (Arduino a : arduinoList) {
                try {
                    new Thread(new ArduinoListener(a, (DataDao) context.getBean("dataDao"), settingsMap)).start();
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
            new Thread(new ArduinoListener(arduino, dataDao, settingsMap)).start();
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
       // db.renameArduino(oldArduino.getId(), arduino.getName());
        arduinoDao.persist(arduino);
        List<Arduino> list = new ArrayList<>();
        list.add(oldArduino);
        new ArduinoLoderSaver().saveArduinoToJsonFile(list);

    }

    private void checkNewArduino() throws IOException {
        if (arduinoList.size() > arduinoCounter) {
            new Thread(new ArduinoListener(arduinoList.get(arduinoList.size() - 1), dataDao, settingsMap)).start();
            //db.initialization(arduinoList.get(arduinoList.size() - 1).getId(), arduinoList.get(arduinoList.size() - 1).getName());
            arduinoDao.persist(arduinoList.get(arduinoList.size() - 1));
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

    public String sha1(String input) {
        MessageDigest mDigest = null;
        StringBuffer sb = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public List<Arduino> getArduinoList() {
        return arduinoList;
    }

    public Map<String, BaseObject> getSettingsMap() {
        return settingsMap;
    }

    @Override
    public void close() {
//        if (db != null) {
//            db.close();
//        }
        HibernateSessionFactory.getSessionFactory().close();
    }


}



