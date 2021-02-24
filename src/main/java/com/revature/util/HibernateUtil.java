package com.revature.util;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {

    private static final Logger logger = LogManager.getLogger(ConnectionFactory.class);
    private static SessionFactory sessionFactory;

    static{
        logger.info("Creating configuration for hibernate");
        Configuration configuration = new Configuration().addResource("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Reimbursement.class)
                .setProperty("hibernate.connection.driver_class","org.postgresql.Driver")
                .setProperty("hibernate.connection.url",S3BucketReader.getUrl())
                .setProperty("hibernate.connection.username", S3BucketReader.getUsername())
                .setProperty("hibernate.connection.password", S3BucketReader.getPassword());
        logger.info("Creating service registry for hibernate");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()). build();
        logger.info("Building session factory from service registry");
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private HibernateUtil(){
    }

    /**
     * Gets a session that gives access to the database
     * @return a session to the database
     */
    public static Session getSession(){
        return sessionFactory.openSession();
    }


}
