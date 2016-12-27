package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.User;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        public void setId(Integer id){
            super.setId(id);
        }
    }

    public synchronized void persist(User user){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.saveOrUpdate(user);
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User getById (Integer id){
        PersistUser user = null;
        Session session = sessionFactory.openSession();
        List<User> list = session.createQuery("from User").list();
        if (list.size()==1){
            user.setUserName(list.get(0).getUserName());
            user.setPassword(list.get(0).getPassword());
            user.setRole(list.get(0).getRole());
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return user;
    }

    public List<User> getAll() {
        Session session = sessionFactory.openSession();
        List<User> list = new ArrayList<>();
        List<User> entityList = session.createQuery("from User ").list();
        for (User u:entityList){
            PersistUser user = new PersistUser();
            user.setId(u.getId());
            user.setUserName(u.getUserName());
            user.setPassword(u.getPassword());
            user.setRole(u.getRole());
            list.add(user);
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return list;
    }
    public User getByName (String name){
        PersistUser user = new PersistUser();
        Session session = sessionFactory.openSession();
        List<User> list = session.createQuery("from User").list();
        if (list.size()==1){
            user.setUserName(list.get(0).getUserName());
            user.setPassword(list.get(0).getPassword());
            user.setRole(list.get(0).getRole());
        }
        if (session != null && session.isOpen()) {
            session.close();
        }
        return user;
    }

}
