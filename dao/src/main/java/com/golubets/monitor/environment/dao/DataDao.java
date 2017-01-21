package com.golubets.monitor.environment.dao;

import com.golubets.monitor.environment.model.Arduino;
import com.golubets.monitor.environment.model.AvgDataEntity;
import com.golubets.monitor.environment.model.DataEntity;
import com.golubets.monitor.environment.util.DateUtil;
import com.golubets.monitor.environment.util.HibernateSessionFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by golubets on 9.12.2016.
 */
@Component
public class DataDao {

    private static final Logger log = Logger.getLogger(DataDao.class);

    private static SessionFactory sessionFactory = null;

    public DataDao() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    public void persist(Arduino arduino, Date date) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            DataEntity entity = new DataEntity();
            entity.setArduinoId(arduino.getId());
            entity.setDateTime(new DateUtil().getFormatedDate(date));
            entity.setTemp(arduino.getTemp());
            entity.setHum(arduino.getHum());
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            session.close();
        }
    }

    public void persistAvg(AvgDataEntity entity) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            session.close();
        }
    }

    public List<DataEntity> getAllByArduino(Arduino arduino) {
        Session session = sessionFactory.openSession();

        List<DataEntity> list = null;
        try {
            session.beginTransaction();
            list = session.createQuery("from DataEntity where arduinoId=" + arduino.getId()).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }

    public DataEntity getLastRowByArduino(Arduino arduino) {
        Session session = sessionFactory.openSession();
        DataEntity dataEntity = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("from DataEntity where id=(" +
                    "select max(id) from DataEntity where arduinoId=:id)");
            query.setParameter("id", arduino.getId());
            dataEntity = (DataEntity) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return dataEntity;
    }

    public AvgDataEntity getAvg(Arduino arduino, String dateFrom, String dateTo) {
        Session session = sessionFactory.openSession();
        AvgDataEntity avgDataEntity = null;
        List<DataEntity> list = null;
        try {
            session.beginTransaction();

            Criteria criteria = session.createCriteria(DataEntity.class);
            criteria.add(Restrictions.eq("arduinoId", arduino.getId()));
            criteria.add(Restrictions.between("dateTime", dateFrom, dateTo));
            list = criteria.list();
            double avgTemp = 0;
            double avgHum = 0;
            for (DataEntity entity : list) {
                avgTemp += entity.getTemp();
                avgHum += entity.getHum();
            }
            avgTemp = Math.round(avgTemp / list.size());
            avgHum = Math.round(avgHum / list.size());
            avgDataEntity = new AvgDataEntity();
            avgDataEntity.setArduinoId(arduino.getId());
            avgDataEntity.setDateTime(dateFrom);
            avgDataEntity.setTemp(avgTemp);
            avgDataEntity.setHum(avgHum);

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        //return dataEntity;
        return avgDataEntity;
    }
}
