package com.golubets.monitor.environment;

import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.BaseObject;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.serializer.SettingsSerializer;
import com.golubets.monitor.environment.util.DaoApplicationContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by golubets on 24.08.2016.
 */

public class Interrogation {

    private static final Logger log = Logger.getLogger(Interrogation.class);
    public static Interrogation instance;
    //private Map<String, BaseObject> settingsMap;
    //private long period = 600000; //10 min
    private long period = 60000; //1 min
    private Timer timer = new Timer();

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

       // settingsMap = new SettingsSerializer().loadEncryptedSettingsFromJsonFile();
        interview();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                interview();
            }
        }, period, period);
    }

//    public void editMailSetting(MailSettings mailSettings) {
//        if (settingsMap == null) {
//            settingsMap = new HashMap<>();
//        }
//        settingsMap.put("mail", mailSettings);
//        new SettingsSerializer().saveEncryptedSettingsToJsonFile(settingsMap);
//    }

    private void interview() {
       final Date date = new Date();
        ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");
        for (Arduino a : arduinoDao.getAll()) {
            try {
                new ArduinoListener(a, date);
            } catch (IOException e) {
                log.error(e);
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
            byte[] result = mDigest.digest(input.getBytes(Charset.forName("UTF-8")));
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

//    public Map<String, BaseObject> getSettingsMap() {
//        return settingsMap;
//    }
}



