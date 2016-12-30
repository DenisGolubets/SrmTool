package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.DataEntity;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class DataDao {
    private static SessionFactory sessionFactory = null;
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public DataDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public void persist(Arduino arduino, Date date) {
        Session session = sessionFactory.openSession();
        try {
            DataEntity entity = new DataEntity();
            entity.setArduinoId(arduino.getId());
            entity.setDateTime(DATE_FORMAT.format(date));
            entity.setTemp(arduino.getTemp());
            entity.setHum(arduino.getHum());
            session.save(entity);
            session.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<DataEntity> getAllByArduino(Arduino arduino) {
        Session session = sessionFactory.openSession();
        return session.createQuery("from DataEntity where arduinoId=" + arduino.getId()).list();
    }
}
