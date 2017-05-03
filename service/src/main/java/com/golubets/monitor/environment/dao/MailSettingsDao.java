package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.exception.PersistException;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by golubets on 15.12.2016.
 */
@Component
public class MailSettingsDao {
    private static final Logger log = Logger.getLogger(DataDao.class);

    private static SessionFactory sessionFactory = null;

    public MailSettingsDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public void persist(MailSettings mailSettings) throws PersistException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(mailSettings);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<MailSettings> getAll() throws PersistException {
        Session session = sessionFactory.openSession();
        List<MailSettings> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from MailSettings").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
            throw new PersistException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }
}
