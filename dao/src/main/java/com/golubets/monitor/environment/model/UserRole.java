package com.golubets.monitor.environment.model;

import javax.persistence.*;

/**
 * Created by golubets on 28.12.2016.
 */
@Entity
@Table(name = "user_roles", schema = "", catalog = "")
public class UserRole {
    private int id;
    private int userid;
    private String role;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "userid", nullable = false)
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "role", nullable = false, length = 0)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRoles = (UserRole) o;

        if (id != userRoles.id) return false;
        if (userid != userRoles.userid) return false;
        if (role != null ? !role.equals(userRoles.role) : userRoles.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userid;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
