package com.golubets.monitor.environment.mail;

import com.golubets.monitor.environment.model.BaseObject;
import com.golubets.monitor.environment.model.MailSettings;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by golubets on 19.08.2016.
 * <p>
 * This class can send without authentication, with TLS/STARTTLS and with SSL
 */
public class EmailSender implements BaseObject, Serializable {
    private static final Logger log = Logger.getLogger(EmailSender.class);
    private static final long serialVersionUID = 0L;

    private String host;
    private String from;
    private String to;
    private String login;
    private String pass;
    private String port;
    private boolean ssl;

    private transient Properties properties;
    private transient Session session;

    int errCounter = 0;

    //email without authentication
    public EmailSender(String host, String from, String to) {
        this.host = host;
        this.from = from;
        this.to = to;
        initialize();
    }

    //email with authentication
    public EmailSender(MailSettings mailSettings) {
        this.host = mailSettings.getHost();
        this.login = mailSettings.getLogin();
        this.pass = mailSettings.getPass();
        this.port = mailSettings.getPort();
        this.from = mailSettings.getFrom();
        this.to = mailSettings.getTo();
        this.ssl = mailSettings.getSsl();
        initialize();

    }

    private void initialize() {
        if (properties == null && session == null) {
            if ((login == null || login.length() == 0) || (pass == null || pass.length() == 0) || (port == null || port.length() == 0)) {
                initialize(host);
            } else {
                initialize(host, login, pass, port, ssl);
            }
        }
    }

    private void initialize(String host) {
        properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        session = Session.getDefaultInstance(properties);
    }


    // SSL port google 465
    private void initialize(String host, final String login, final String pass, String port, boolean ssl) {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        if (ssl) {
            properties.put("mail.transport.protocol", "smtp");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.checkserveridentity", "false");
            properties.put("mail.smtp.ssl.trust", "*");
            properties.put("mail.smtp.connectiontimeout", "10000");
        }

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, pass);
                    }
                });
    }

    public void sendMail(String subject, String bodyText) throws Exception {

        try {
            List<String> adressList = Arrays.asList(to.split(";"));
            for (String adress : adressList) {
                MimeMessage message = new MimeMessage(session); // email message
                message.setFrom(new InternetAddress(from)); // setting header fields
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(adress));
                message.setSubject(subject);
                message.setText(bodyText);
                Transport transport;
                if (ssl) {
                    transport = session.getTransport("smtp");
                    transport.send(message);
                } else {
                    Transport.send(message);
                }
                if (errCounter > 0)
                    errCounter = 0;
            }

        } catch (MessagingException e) {
            errCounter++;
            if (subject.equalsIgnoreCase("test")) {
                throw new Exception(e);
            }
            if (errCounter > 5) {
                log.error(e);
            } else {
                try {
                    TimeUnit.MINUTES.sleep(2);
                    sendMail(subject, bodyText);
                } catch (InterruptedException e1) {

                    log.error(e1);
                }
            }
        }
    }
}

