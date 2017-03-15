package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.UserRole;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by golubets on 28.12.2016.
 */
@Component
public class UserRoleDao {

    private static final Logger log = Logger.getLogger(UserRoleDao.class);

    private static SessionFactory sessionFactory = null;

    public UserRoleDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public List<UserRole> getRoleByUserId(Integer id) {
        Session session = sessionFactory.openSession();
        List<UserRole> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from UserRole where userid = " + id).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;

    }

    public List<UserRole> getAll() {
        Session session = sessionFactory.openSession();
        List<UserRole> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from UserRole").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }
}
