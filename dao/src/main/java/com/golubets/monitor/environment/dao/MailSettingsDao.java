package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.EmailListEntity;
import com.golubets.monitor.environment.model.MailSettings;
import com.golubets.monitor.environment.model.MailSettingsEntity;
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
            MailSettingsEntity mailSettingsEntity = new MailSettingsEntity();
            mailSettingsEntity.setHost(mailSettings.getHost());
            mailSettingsEntity.setFrom(mailSettings.getFrom());
            mailSettingsEntity.setPort(mailSettings.getPort());
            mailSettings.setLogin(mailSettings.getLogin());
            mailSettingsEntity.setPass(mailSettings.getPass());
            mailSettings.setSsl(mailSettings.isSsl());

            session.saveOrUpdate(mailSettingsEntity);

            int id = mailSettingsEntity.getId();
            for (String email : mailSettings.getTo()) {
                EmailListEntity emailListEntity = new EmailListEntity();
                emailListEntity.setMailSettingId(id);
                emailListEntity.setEmail(email);

                session.saveOrUpdate(emailListEntity);
            }
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
        List<MailSettings> mailSettings = new ArrayList<>();
        List<MailSettingsEntity> mailSettingsEntities = session.createQuery("from MailSettingsEntity ").list();
        for (MailSettingsEntity m : mailSettingsEntities) {
            MailSettings settings = new MailSettings();
            settings.setHost(m.getHost());
            settings.setFrom(m.getFrom());
            settings.setLogin(m.getLogin());
            settings.setPass(m.getPass());
            settings.setSsl(m.getSsl());
            settings.setTo(session.createQuery("from EmailListEntity where mailSettingId =" + m.getId()).list());

            mailSettings.add(settings);
        }
        return mailSettings;
    }
}
