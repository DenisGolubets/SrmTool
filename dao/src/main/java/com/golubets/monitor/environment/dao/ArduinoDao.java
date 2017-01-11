package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class ArduinoDao extends Arduino {

    private static final Logger log = Logger.getLogger(Arduino.class);

    private static SessionFactory sessionFactory = null;

    public ArduinoDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    private class PersistArduino extends Arduino {
        public void setId(Integer id) {
            super.setId(id);
        }
    }

    public void persist(Arduino arduino) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(arduino);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void update(Arduino arduino) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(arduino);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Arduino getByID(Integer id) {

        Session session = sessionFactory.openSession();
        Arduino arduino = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Arduino where id=:id");
            query.setParameter("id", id);
            arduino = (Arduino) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return arduino;
    }

    public Arduino getByName(String name) {
        Session session = sessionFactory.openSession();
        Arduino arduino = null;
        try {
            Query query = session.createQuery("from Arduino where name=:name");
            query.setParameter("name", name);
            arduino = (Arduino) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return arduino;
    }

    public List<Arduino> getAll() {
        Session session = sessionFactory.openSession();
        List<Arduino> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from Arduino").list();
            session.getTransaction().commit();
            return list;
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }
}