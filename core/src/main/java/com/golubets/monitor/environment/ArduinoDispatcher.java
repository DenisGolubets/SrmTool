package com.golubets.monitor.environment;

import com.golubets.monitor.environment.exception.PersistException;
import com.golubets.monitor.environment.util.arduinoutil.Connector;
import com.golubets.monitor.environment.util.arduinoutil.EthConnector;
import com.golubets.monitor.environment.util.arduinoutil.JsscSerialConnector;
import com.golubets.monitor.environment.dao.DataDao;
import com.golubets.monitor.environment.dao.MailSettingsDao;
import com.golubets.monitor.environment.mail.EmailSender;
import com.golubets.monitor.environment.model.*;
import com.golubets.monitor.environment.util.DaoApplicationContext;
import com.golubets.monitor.environment.util.DateUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

/**
 * Created by golubets on 29.11.2016.
 */
public class ArduinoDispatcher {
    private final Logger log = Logger.getLogger(ArduinoDispatcher.class);
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

    private Connector connector;

    public ArduinoDispatcher(Arduino arduino, Date date) throws Exception {
        this.arduino = arduino;
        this.topH = arduino.getTopH();
        this.topT = arduino.getTopT();
        this.date = date;
        this.connector = createConnection(arduino.getConnectionType());


        doJob();
    }

    public ArduinoDispatcher(Arduino arduino) throws IOException {
        this.arduino = arduino;
        this.connector = createConnection(arduino.getConnectionType());

    }

    private void getDate() throws NullPointerException, IOException {
        String[] arr = getCurrentDate("i").split("\\|");
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
        if (emailSender == null) {
            initializationEmailSender();
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

    private void initializationEmailSender() throws Exception {
        MailSettingsDao mailSettingsDao = (MailSettingsDao) DaoApplicationContext.getInstance().getContext().getBean("mailSettingsDao");
        List<MailSettings> list = mailSettingsDao.getAll();
        if (list != null && list.size() < 2) {
            emailSender = new EmailSender(list.get(0));
        }
    }

    private void writeDateToDB() {
        try {
            DataDao dataDao = (DataDao) DaoApplicationContext.getInstance().getContext().getBean("dataDao");
            DateUtil dateUtil = new DateUtil();
            DataEntity prevData = dataDao.getLastRowByArduino(arduino);
            dataDao.persist(arduino, date);
            if (prevData != null) {
                String prevDataTime = dateUtil.getCurrentHour(prevData.getDateTime());
                String curDateTime = dateUtil.getCurrentHour(date);
                if (!(prevDataTime.equals(curDateTime))) {
                    AvgDataEntity avgDataEntity = dataDao.getAvg(arduino, prevDataTime, curDateTime);
                    dataDao.persistAvg(avgDataEntity);
                }
            }
        } catch (PersistException e) {
            log.error("", e);
        }

    }

    private void doJob() throws Exception {
        try {
            getDate();
            writeDateToDB();
            if (isAlert) {
                informIfAlert();
            }
        } catch (Exception e) {
            log.error("error ", e);
        } finally {
            connector.close();
        }
    }

    public String getCurrentDate(String request) throws IOException {
        try {
            return connector.getResponse(request);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    public Connector createConnection(ConnectionType connectionType) throws IOException {
        Connector connector = null;
        try {
            if (connectionType == ConnectionType.SERIAL) {
                connector = new JsscSerialConnector(arduino.getSerialPort());
            } else if (connectionType == ConnectionType.ETHERNET) {
                connector = new EthConnector(arduino.getIp(), 23);
            }
        } catch (SocketTimeoutException e) {
            String mailBody = arduino.getName() + " connected to: " + arduino.getIp() + " not responding";
            try {
                if (emailSender == null) {
                    initializationEmailSender();
                }
                emailSender.sendMail("Arduino exception", mailBody);
            } catch (Exception e1) {
                log.error("", e1);
            }
        }
        return connector;
    }

    public void writeToArduino() throws IOException, InterruptedException {
        connector.setDate("smac" + arduino.getMac().replaceAll("-", ".").substring(8));
        Thread.sleep(2000);
        connector.setDate("si" + arduino.getIp());
        Thread.sleep(2000);
        connector.setDate("ss" + arduino.getSubnet());
        Thread.sleep(2000);
        connector.setDate("sg" + arduino.getGateway());
        Thread.sleep(2000);
        connector.setDate("sd" + arduino.getDns());
        Thread.sleep(2000);
        connector.setDate("sT" + arduino.getTopT());
        Thread.sleep(2000);
        connector.setDate("sH" + arduino.getTopH());
        Thread.sleep(2000);
        connector.setDate("sDt" + arduino.getDhtType());
        Thread.sleep(2000);
        connector.setDate("sDp" + arduino.getDhtPort());
        Thread.sleep(2000);
        connector.setDate("sw");


    }
}