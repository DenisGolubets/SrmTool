package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.UserRole;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by golubets on 28.12.2016.
 */
@Component
public class UserRoleDao {
    private static SessionFactory sessionFactory = null;

    public UserRoleDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public List<UserRole> getRoleByUserId(Integer id){
        Session session = sessionFactory.openSession();
        return session.createQuery("from UserRole where userid = " + id).list();

    }

    public List<UserRole> getAll(){
        Session session = sessionFactory.openSession();
        return session.createQuery("from UserRole ").list();
    }
}
