package com.dg.srmt.model;

import com.dg.srmt.bo.Arduino;
import com.dg.srmt.bo.BaseObject;
import com.dg.srmt.bo.SubjectForMail;
import com.dg.srmt.model.db.DbConnector;
import com.dg.srmt.model.db.JdbcSqliteConnection;
import com.dg.srmt.model.mail.EmailSender;
import com.dg.srmt.model.mail.MailSettings;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by golubets on 24.08.2016.
 */
public class MainLogic implements AutoCloseable {

    private static final Logger log = Logger.getLogger(MainLogic.class);
    public static MainLogic instance;
    private Map<String, BaseObject> settingsMap;
    private static final long periodicityOfMailing = 20 * 60 * 980;
    //private long period = 600000; //10 min
    private long period = 60000; //1 min

    Timer timer = new Timer();

    DbConnector db = null;

    String separator = System.getProperty("line.separator");

    private List<Arduino> arduinoList = Collections.synchronizedList(new ArrayList<Arduino>());
    private int arduinoCounter = 0;

    public static MainLogic getInstance() {
        if (instance == null){
            synchronized (MainLogic.class){
                if (instance == null){
                    instance = new MainLogic();
                }
            }
        }
        return instance;
    }

    private MainLogic() {
        settingsMap = new SettingsLoaderSaver().loadSettingsFromFile();
        arduinoList = new ArduinoLoderSaver().loadArduinoFromFile();

        db = new JdbcSqliteConnection();
        if (arduinoList.size()>0){
            for (Arduino a : arduinoList) {

                db.initialization(a.getId(), a.getName());

            }
            for (Arduino a : arduinoList) {
                new Thread(new ArduinoListener(a)).start();
                arduinoCounter++;
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    checkNewArduino();
                    doJob();
                }
            }, period, period);
        }
    }

    public void addArduino(Arduino arduino) {
        arduinoList.add(arduino);
    }

    private void checkNewArduino() {
        if (arduinoList.size() > arduinoCounter) {
            new Thread(new ArduinoListener(arduinoList.get(arduinoList.size() - 1))).start();
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

    private class ArduinoListener implements Runnable {
        private final Logger log = Logger.getLogger(ArduinoListener.class);

        private Arduino arduino;
        private double avg10MinT;
        private double avg10MinH;
        private int topT;
        private int topH;
        private boolean isAlert = false;
        private long lastEmailSend = 0;
        private EmailSender emailSender;


        public ArduinoListener(Arduino arduino) {
            this.arduino = arduino;
            this.topH = arduino.getTopH();
            this.topT = arduino.getTopT();

            if (settingsMap != null) {
                if (settingsMap.containsKey("mail")) {
                    emailSender = new EmailSender((MailSettings) settingsMap.get("mail"));
                }
            }
        }

        private void getDate() throws NullPointerException, IOException {
            String[] arr = arduino.getDate("i").split("\\|");

            for (String s : arr) {
                if (s.startsWith("AVG10minT:")) {
                    avg10MinT = Double.parseDouble(s.replaceAll(",", "").substring(s.indexOf(":") + 1));
                } else if (s.startsWith("AVG10minH:")) {
                    avg10MinH = Double.parseDouble(s.substring(s.indexOf(":") + 1));


                }
            }
            isAlert = (arduino.isAlertH() && avg10MinH >= topH) || (arduino.isAlertT() && avg10MinT >= topT);

        }

        private void informIfAlert() {
            String subject = String.valueOf(SubjectForMail.ALERT);
            String textBody = arduino + "\n\r";

            if (arduino.isAlertH() && avg10MinH >= topH) {
                textBody += String.format("H: %.2f" + separator, avg10MinH);
            }
            if (arduino.isAlertT() && avg10MinT >= topT) {
                textBody += String.format("T: %.2f" + separator, avg10MinT);
            }

            // if have no email, we save alert to log file
            if (emailSender == null) {
                //save to log
                log.warn(subject + " " + textBody);
            } else if (lastEmailSend == 0 || (System.currentTimeMillis() - lastEmailSend >= periodicityOfMailing)) {
                emailSender.sendMail(subject, textBody);
                lastEmailSend = System.currentTimeMillis();
            }
        }

        private void writeDateToDB(Integer arduinoId) {

            db.writeData(arduinoId, new Date(), avg10MinT, avg10MinH);

        }


        @Override
        public void run() {


            synchronized (arduino) {


                while (true) {
                    try {
                        getDate();
                        writeDateToDB(arduino.getId());
                        if (isAlert) {
                            informIfAlert();
                        }
                    } catch (NumberFormatException e) {
                        String textBody = "Check the sensor connection on Arduino ";
                        log.error(textBody + arduino, e);
                        emailSender.sendMail(String.valueOf(SubjectForMail.EXCEPTION), textBody + arduino);
                    } catch (SocketTimeoutException e) {
                        String textBody = "The Arduino is disconnected ";
                        log.error(textBody + arduino, e);
                        emailSender.sendMail(String.valueOf(SubjectForMail.EXCEPTION), textBody + arduino);
                    } catch (IOException e) {
                        String textBody = "The Arduino has problems ";
                        log.error(textBody + arduino, e);
                        emailSender.sendMail(String.valueOf(SubjectForMail.EXCEPTION), textBody + arduino);

                    }
                    try {
                        arduino.wait();
                    } catch (InterruptedException e) {
                        log.error(e);
                    }
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        db.close();
        for (Arduino a : arduinoList) {
            a.close();
        }
        Thread.currentThread().interrupt();
    }
}



