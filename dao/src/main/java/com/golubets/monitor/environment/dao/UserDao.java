package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.User;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Repository("userDao")
public class UserDao {
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
        Transaction tx = session.beginTransaction();
        try {
            session.saveOrUpdate(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User getById(Integer id) {
        User user = new User();
        Session session = sessionFactory.openSession();
        List<User> list = session.createQuery("from User").list();
        if (list.size() == 1) {
            return list.get(0);
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return user;
    }

    public List<User> getAll() {
        Session session = sessionFactory.openSession();
        if (session != null && session.isOpen()) {
            session.close();
        }
        return session.createQuery("from User ").list();
    }

    public User getByName(String name) {
        User user = new User();
        Session session = sessionFactory.openSession();
        List<User> list = session.createQuery("from User").list();
        if (list.size() == 1) {
            return list.get(0);
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return user;
    }

}
