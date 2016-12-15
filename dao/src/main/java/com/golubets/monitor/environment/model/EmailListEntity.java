package com.golubets.monitor.environment.model;

import javax.persistence.*;

/**
 * Created by golubets on 15.12.2016.
 */
@Entity
@Table(name = "email_list", schema = "", catalog = "")
public class EmailListEntity {
    private int id;
    private int mailSettingId;
    private String email;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "mail_setting_id", nullable = false)
    public int getMailSettingId() {
        return mailSettingId;
    }

    public void setMailSettingId(int mailSettingId) {
        this.mailSettingId = mailSettingId;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 0)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailListEntity that = (EmailListEntity) o;

        if (id != that.id) return false;
        if (mailSettingId != that.mailSettingId) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + mailSettingId;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
