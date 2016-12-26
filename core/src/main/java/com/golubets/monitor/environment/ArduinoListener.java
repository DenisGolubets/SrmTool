package com.golubets.monitor.environment;

import com.golubets.monitor.environment.arduinoconnection.Connector;
import com.golubets.monitor.environment.arduinoconnection.EthConnector;
import com.golubets.monitor.environment.arduinoconnection.JsscSerialConnector;
import com.golubets.monitor.environment.dao.ArduinoDao;
import com.golubets.monitor.environment.dao.DataDao;
import com.golubets.monitor.environment.dao.MailSettingsDao;
import com.golubets.monitor.environment.mail.EmailSender;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.ConnectionType;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.model.SubjectForMail;
import com.golubets.monitor.environment.util.DaoApplicationContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

/**
 * Created by golubets on 29.11.2016.
 */
public class ArduinoListener {
    private final Logger log = Logger.getLogger(ArduinoListener.class);
    private String separator = System.getProperty("line.separator");
    private Arduino arduino;
    private double avg10MinT;
    private double avg10MinH;
    private int topT;
    private int topH;
    private boolean isAlert = false;
    private long lastEmailSend = 0;
    private EmailSender emailSender = null;
    private Date date;
    private static final long periodicityOfMailing = 20 * 60 * 980;

    private transient Connector connector;

    public ArduinoListener(Arduino arduino, Date date) throws Exception {
        this.arduino = arduino;
        this.topH = arduino.getTopH();
        this.topT = arduino.getTopT();
        this.date = date;
        this.connector = createConnection(arduino.getConnectionType());

        doJob();
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

    private void informIfAlert() throws Exception {
        MailSettingsDao mailSettingsDao = (MailSettingsDao) DaoApplicationContext.getInstance().getContext().getBean("mailSettingsDao");
        List<MailSettings> list = mailSettingsDao.getAll();
        if (list != null && list.size() < 2) {
            emailSender = new EmailSender(list.get(0));
        }
        String subject = String.valueOf(SubjectForMail.ALERT);
        String textBody = arduino + "\n\r";

        if (arduino.isAlertH() && avg10MinH >= topH) {
            textBody += String.format("H: %.2f" + separator, avg10MinH);
        }
        if (arduino.isAlertT() && avg10MinT >= topT) {
            textBody += String.format("T: %.2f" + separator, avg10MinT);
        }
        if (emailSender == null) {
            //save to log
            log.warn(subject + " " + textBody);
        } else if (lastEmailSend == 0 || (System.currentTimeMillis() - lastEmailSend >= periodicityOfMailing)) {
            if (emailSender != null) {
                emailSender.sendMail(subject, textBody);
            } else log.info(subject + "\n\r" + textBody);
            lastEmailSend = System.currentTimeMillis();
        }
    }

    private void writeDateToDB() {
        ArduinoDao arduinoDao = (ArduinoDao) DaoApplicationContext.getInstance().getContext().getBean("arduinoDao");
        DataDao dataDao = (DataDao) DaoApplicationContext.getInstance().getContext().getBean("dataDao");
        arduinoDao.persist(arduino);
        dataDao.persist(arduino, date);
    }

    private void doJob() throws Exception {
        getDate();
        writeDateToDB();
        if (isAlert) {
            informIfAlert();
        }
        connector.close();
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