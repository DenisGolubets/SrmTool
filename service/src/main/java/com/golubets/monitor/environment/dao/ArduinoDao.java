package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.exception.PersistException;
import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.DataEntity;
import com.golubets.monitor.environment.util.DaoApplicationContext;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class ArduinoDao extends Arduino {

    private static final Logger LOGGER = Logger.getLogger(Arduino.class);

    private static SessionFactory sessionFactory = null;

    public ArduinoDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public void persist(Arduino arduino) throws PersistException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(arduino);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void update(Arduino arduino) throws PersistException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(arduino);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Arduino getByID(Integer id) throws PersistException {

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
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return arduino;
    }

    public Arduino getByIDWithLastData(Integer id) throws PersistException {

        Session session = sessionFactory.openSession();
        Arduino arduino = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Arduino where id=:id");
            query.setParameter("id", id);
            arduino = (Arduino) query.uniqueResult();
            DataDao dataDao = (DataDao) DaoApplicationContext.getInstance().getContext().getBean("dataDao");
            DataEntity dataEntity = dataDao.getLastRowByArduino(arduino);
            arduino.setTemp(dataEntity.getTemp());
            arduino.setHum(dataEntity.getHum());
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return arduino;
    }

    public Arduino getByName(String name) throws PersistException {
        Session session = sessionFactory.openSession();
        Arduino arduino = null;
        try {
            Query query = session.createQuery("from Arduino where name=:name");
            query.setParameter("name", name);
            arduino = (Arduino) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return arduino;
    }

    public List<Arduino> getAll() throws PersistException {
        Session session = sessionFactory.openSession();
        List<Arduino> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from Arduino").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }

    public List<Arduino> getAllWithLastData() throws PersistException {
        Session session = sessionFactory.openSession();
        List<Arduino> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from Arduino").list();
            session.getTransaction().commit();
            DataDao dataDao = (DataDao) DaoApplicationContext.getInstance().getContext().getBean("dataDao");
            for (Arduino arduino : list) {
                DataEntity dataEntity = dataDao.getLastRowByArduino(arduino);
                if (dataEntity != null) {
                    arduino.setTemp(dataEntity.getTemp());
                    arduino.setHum(dataEntity.getHum());
                } else {
                    arduino.setTemp(0);
                    arduino.setHum(0);
                }
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOGGER.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }
}