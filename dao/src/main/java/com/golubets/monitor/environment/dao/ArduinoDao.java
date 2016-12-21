package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Repository("arduinoDao")
public class ArduinoDao extends Arduino {
    private static SessionFactory sessionFactory = null;

    //   // @Bean(name = "arduinoDao")
//    public ArduinoDao getArduinoDao(){
//        return new ArduinoDao();
//    }
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
            session.saveOrUpdate(arduino);
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
       Arduino arduino = null;
        Session session = sessionFactory.openSession();
        List<Arduino> list = session.createQuery("from Arduino where id=" + id).list();
        if (list.size() == 1) {
            arduino = list.get(0);
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return arduino;
    }

    public Arduino getByName(String name) {
        Arduino arduino = null;
        Session session = sessionFactory.openSession();
        List<Arduino> list = session.createQuery("from Arduino where name=" + name).list();
        if (list.size() == 1) {
            arduino = list.get(0);
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return arduino;
    }

    public List<Arduino> getAll() {
        Session session = sessionFactory.openSession();
        List<Arduino> list = session.createQuery("from Arduino ").list();
        if (session != null && session.isOpen()) {
            session.close();
        }
        return list;
    }
}