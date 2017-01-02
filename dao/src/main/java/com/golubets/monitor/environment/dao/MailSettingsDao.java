package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
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

    public void persist(MailSettings mailSettings) {
        Session session = sessionFactory.openSession();
        try {
            session.saveOrUpdate(mailSettings);
            session.flush();
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<MailSettings> getAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("from MailSettings ").list();
    }
}
