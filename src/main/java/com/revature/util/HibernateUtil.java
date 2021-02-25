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

/**
 * This class holds the configuration for hibernate. It sets up hibernate with the
 * connection information to the database
 * @author Cole Space
 * @author Gabrielle Luna
 */
public class HibernateUtil {

    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    /*
        Static block for initializing the connection to the database through hibernate
     */
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

    /**
     * Making a private no args so the class cannot be instantiated outside of the
     * singleton instance
     */
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
