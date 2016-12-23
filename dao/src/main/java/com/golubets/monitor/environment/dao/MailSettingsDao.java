package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by golubets on 15.12.2016.
 */
@Repository("mailSettingsDao")
public class MailSettingsDao {
    private static SessionFactory sessionFactory = null;

    public MailSettingsDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public void persist(MailSettings mailSettings) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
//            MailSettings mailSettingsEntity = new MailSettings();
//            mailSettingsEntity.setHost(mailSettings.getHost());
//            mailSettingsEntity.setFrom(mailSettings.getFrom());
//            mailSettingsEntity.setTo(mailSettings.getTo());
//            mailSettingsEntity.setPort(mailSettings.getPort());
//            mailSettings.setLogin(mailSettings.getLogin());
//            mailSettingsEntity.setPass(mailSettings.getPass());
//            mailSettings.setSsl(mailSettings.getSsl());

            session.saveOrUpdate(mailSettings);


            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<MailSettings> getAll() {
        Session session = sessionFactory.openSession();
        List<MailSettings> mailSettings = session.createQuery("from MailSettings ").list();
//        List<MailSettings> mailSettingsEntities = session.createQuery("from MailSettings ").list();
//        for (MailSettings m : mailSettingsEntities) {
//            MailSettings settings = new MailSettings();
//            settings.setHost(m.getHost());
//            settings.setFrom(m.getFrom());
//            settings.setLogin(m.getLogin());
//            settings.setPass(m.getPass());
//            settings.setSsl(m.getSsl());
//            settings.setTo(m.getTo());
//
//            mailSettings.add(settings);
//        }
        return mailSettings;
    }
}
