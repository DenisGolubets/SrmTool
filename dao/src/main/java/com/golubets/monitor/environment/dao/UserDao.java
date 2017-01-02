package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.User;
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
            session.saveOrUpdate(user);
            session.flush();
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User getById(Integer id) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from User where id=:id");
        query.setParameter("id", id);
        return (User) query.uniqueResult();
    }

    public List<User> getAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("from User ").list();
    }

    public User getByName(String name) {

        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from User where userName=:name");
        query.setParameter("name", name);
        return (User) query.uniqueResult();
    }
}
