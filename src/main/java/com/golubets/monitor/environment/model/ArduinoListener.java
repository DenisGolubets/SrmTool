package com.golubets.monitor.environment.model;

import com.golubets.monitor.environment.model.baseobject.Arduino;
import com.golubets.monitor.environment.model.baseobject.BaseObject;
import com.golubets.monitor.environment.model.baseobject.ConnectionType;
import com.golubets.monitor.environment.model.baseobject.SubjectForMail;
import com.golubets.monitor.environment.model.connection.Connector;
import com.golubets.monitor.environment.model.connection.EthConnector;
import com.golubets.monitor.environment.model.connection.JsscSerialConnector;
import com.golubets.monitor.environment.model.db.DbConnector;
import com.golubets.monitor.environment.model.mail.EmailSender;
import com.golubets.monitor.environment.model.mail.MailSettings;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Map;

/**
 * Created by golubets on 29.11.2016.
 */
public class ArduinoListener implements Runnable {
    private final Logger log = Logger.getLogger(ArduinoListener.class);

    private DbConnector db;
    private String separator = System.getProperty("line.separator");
    private Arduino arduino;
    private double avg10MinT;
    private double avg10MinH;
    private int topT;
    private int topH;
    private boolean isAlert = false;
    private long lastEmailSend = 0;
    private EmailSender emailSender;
    private static final long periodicityOfMailing = 20 * 60 * 980;

    private transient Connector connector;


    public ArduinoListener(Arduino arduino, DbConnector db, Map<String, BaseObject> settingsMap) throws IOException {
        this.arduino = arduino;
        this.topH = arduino.getTopH();
        this.topT = arduino.getTopT();
        this.db = db;
        this.connector = createConnection(arduino.getConnectionType());
        if (settingsMap != null) {
            if (settingsMap.containsKey("mail")) {
                emailSender = new EmailSender((MailSettings) settingsMap.get("mail"));
            }
        }
    }


    private void getDate() throws NullPointerException, IOException {
        String[] arr = getDate("i").split("\\|");

        for (String s : arr) {
            if (s.startsWith("AVG10minT:")) {
                avg10MinT = Double.parseDouble(s.replaceAll(",", "").substring(s.indexOf(":") + 1));
                arduino.setTemp(avg10MinT);
            } else if (s.startsWith("AVG10minH:")) {
                avg10MinH = Double.parseDouble(s.substring(s.indexOf(":") + 1));
                arduino.setHum(avg10MinH);
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
        db.persist(arduinoId, new Date(), avg10MinT, avg10MinH);
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
                    arduino.wait();
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

                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }
    }


    public String getDate(String request) throws IOException {
        try {
            return connector.getResponse(request);
        } catch (IOException e) {
            createConnection(arduino.getConnectionType());
            getDate(request);
        }
        return null;
    }

    public Connector createConnection(ConnectionType connectionType) throws IOException {
        Connector connector = null;
        if (connectionType == ConnectionType.SERIAL) {
                        connector = new JsscSerialConnector(arduino.getSerialPort());
        } else if (connectionType == ConnectionType.ETHERNET) {
            connector = new EthConnector(arduino.getIp(), 23);
        }
        return connector;
    }

}