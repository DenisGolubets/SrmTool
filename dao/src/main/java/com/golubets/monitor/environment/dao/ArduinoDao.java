package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class ArduinoDao extends Arduino {
    private static SessionFactory sessionFactory = null;

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

        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Arduino where id=:id");
        query.setParameter("id", id);
        return (Arduino) query.uniqueResult();
    }

    public Arduino getByName(String name) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Arduino where name=:name");
        query.setParameter("name", name);
        return (Arduino) query.uniqueResult();
    }

    public List<Arduino> getAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("from Arduino ").list();
    }
}