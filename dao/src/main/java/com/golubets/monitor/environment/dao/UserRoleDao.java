package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.UserRole;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by golubets on 28.12.2016.
 */
@Repository("userRoleDao")
public class UserRoleDao {
    private static SessionFactory sessionFactory = null;

    public UserRoleDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public UserRole getRoleByUserId(Integer id){
        Session session = sessionFactory.openSession();
        UserRole userRole = new UserRole();
        List<UserRole> list = session.createQuery("from UserRole where userid = " + id).list();
        if (list.size()==1){
            return list.get(0);
        }
        return userRole;
    }
}
