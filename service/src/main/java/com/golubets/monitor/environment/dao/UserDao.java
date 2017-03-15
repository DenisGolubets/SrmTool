package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.User;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class UserDao {

    private static final Logger log = Logger.getLogger(DataDao.class);

    private static SessionFactory sessionFactory = null;

    public UserDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    private class PersistUser extends User {
        public void setId(Integer id) {
            super.setId(id);
        }
    }

    public synchronized void persist(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User getById(Integer id) {
        Session session = sessionFactory.openSession();
        User user = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("from User where id=:id");
            query.setParameter("id", id);
            user = (User) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    public List<User> getAll() {
        Session session = sessionFactory.openSession();
        List<User> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from User ").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }

    public User getByName(String name) {

        Session session = sessionFactory.openSession();
        User user = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("from User where userName=:name");
            query.setParameter("name", name);
            user = (User) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("error", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }
}
