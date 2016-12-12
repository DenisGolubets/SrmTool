package com.golubets.monitor.environment.model.baseobject.domain;

import com.golubets.monitor.environment.model.baseobject.Arduino;

import javax.persistence.*;

/**
 * Created by golubets on 09.12.2016.
 */
@Entity
@Table(name = "arduino", schema = "", catalog = "")
public class ArduinoEntity {
    private int id;
    private String name;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 0)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArduinoEntity that = (ArduinoEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Arduino{ id = "+id + ", name = " + name + " }";
    }
}
