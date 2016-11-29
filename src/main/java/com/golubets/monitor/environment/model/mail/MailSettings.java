package com.golubets.monitor.environment.model.mail;

import com.golubets.monitor.environment.model.baseobject.BaseObject;

import java.io.Serializable;

/**
 * Created by golubets on 16.09.2016.
 */
public class MailSettings implements BaseObject, Serializable {
    private static final long serialVersionUID = 0L;
    private String host;
    private String from;
    private String to;
    private String login;
    private String pass;
    private String port;
    private boolean ssl;

    public MailSettings(String host, String from, String to) {
        this.host = host;
        this.from = from;
        this.to = to;
    }

    public MailSettings(String host, String login, String pass, String port, String from, String to, boolean ssl) {
        this.host = host;
        this.login = login;
        this.pass = pass;
        this.port = port;
        this.from = from;
        this.to = to;
        this.ssl = ssl;
    }
    public MailSettings(String host, String login, String pass, String port, String from, String to) {
        this.host = host;
        this.login = login;
        this.pass = pass;
        this.port = port;
        this.from = from;
        this.to = to;
        this.ssl = false;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }
}
