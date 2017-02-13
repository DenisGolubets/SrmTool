package com.golubets.monitor.environment.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by golubets on 15.12.2016.
 */
@Entity
@Table(name = "mail_settings", schema = "", catalog = "")
public class MailSettings {
    private int id;
    private String host;
    private String from;
    private String to;
    private String port;
    private boolean ssl;
    private String mailLogin;
    private String mailPass;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @NotEmpty
    @Basic
    @Column(name = "host", nullable = true, length = 0)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @NotEmpty
    @Basic
    @Column(name = "from_", nullable = true, length = 0)
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @NotEmpty
    @Basic
    @Column(name = "to_")
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Basic
    @Column(name = "port", nullable = true, length = 0)
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Basic
    @Column(name = "ssl", nullable = true)
    public boolean getSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    @Basic
    @Column(name = "login", nullable = true, length = 0)
    public String getMailLogin() {
        return mailLogin;
    }

    public void setMailLogin(String login) {
        this.mailLogin = login;
    }

    @Basic
    @Column(name = "pass", nullable = true, length = 0)
    public String getMailPass() {
        return mailPass;
    }

    public void setMailPass(String pass) {
        this.mailPass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MailSettings)) return false;

        MailSettings that = (MailSettings) o;

        if (id != that.id) return false;
        if (ssl != that.ssl) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (mailLogin != null ? !mailLogin.equals(that.mailLogin) : that.mailLogin != null) return false;
        return mailPass != null ? mailPass.equals(that.mailPass) : that.mailPass == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (ssl ? 1 : 0);
        result = 31 * result + (mailLogin != null ? mailLogin.hashCode() : 0);
        result = 31 * result + (mailPass != null ? mailPass.hashCode() : 0);
        return result;
    }
}
