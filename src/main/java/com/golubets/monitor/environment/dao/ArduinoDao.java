package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import com.golubets.monitor.environment.model.ArduinoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class ArduinoDao extends Arduino {
    private static SessionFactory sessionFactory = null;

    @Bean(name = "arduinoDao")
    public ArduinoDao getArduinoDao(){
        return new ArduinoDao();
    }
    public ArduinoDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    private class PersistArduino extends Arduino {
        public void setId(Integer id) {
            super.setId(id);
        }
    }

    public synchronized void persist(Arduino arduino) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            ArduinoEntity arduinoEntity = new ArduinoEntity();
            arduinoEntity.setId(arduino.getId());
            arduinoEntity.setName(arduino.getName());
            session.saveOrUpdate(arduinoEntity);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Arduino getByID(Integer id) {
        PersistArduino arduino = null;
        Session session = sessionFactory.openSession();

        List<ArduinoEntity> list = session.createQuery("from ArduinoEntity where id=" + id).list();
        if (list.size() == 1) {
            arduino = new PersistArduino();
            arduino.setId(list.get(0).getId());
            arduino.setName(list.get(0).getName());
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return arduino;
    }
    public List<Arduino> getAll (){
        List<Arduino> list = new ArrayList<>();
        Session session = sessionFactory.openSession();
        List<ArduinoEntity> tmpList = session.createQuery("from ArduinoEntity").list();
        for (ArduinoEntity e:tmpList){
            PersistArduino arduino = new PersistArduino();
            arduino.setId(e.getId());
            arduino.setName(e.getName());
            list.add(arduino);
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return list;
    }
}