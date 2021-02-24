package com.revature.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A Class to get a connection from the connection factory
 */
public class ConnectionFactory {

    private Properties props = new Properties();
    private static ConnectionFactory connFactory = new ConnectionFactory();

    private static final Logger logger = LogManager.getLogger(ConnectionFactory.class);
    private static SessionFactory sessionFactory;

    private ConnectionFactory(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props.setProperty("url", "jdbc:postgresql://java-enterprise-210119.cjx4h67gd9rh.us-east-2.rds.amazonaws.com:5432/ERS");
        props.setProperty("username", "postgres");
        props.setProperty("password", "Revature123");

//        logger.info("Creating configuration for hibernate");
//        Configuration configuration = new Configuration().addResource("hibernate.cfg.xml")
//                .setProperty("hibernate.connection.url",S3BucketReader.getUrl())
//                .setProperty("hibernate.connection.username", S3BucketReader.getUsername())
//                .setProperty("hibernate.connection.password", S3BucketReader.getPassword());
//        logger.info("Creating service registry for hibernate");
//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
//                configuration.getProperties()). build();
//        logger.info("Building session factory from service registry");
//        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

    }

    /**
     * Gets the current instance of the session factory that is connected to the DB
     * @return the instance of the session factory
     */
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    /**
     * Gets a session that gives access to the database
     * @return a session to the database
     */
    public static Session getSession(){
        if(sessionFactory.getCurrentSession() == null){
            return sessionFactory.openSession();
        }
        return sessionFactory.getCurrentSession();
    }

    /**
     * Gets the current instance of the Connection to the DB
     * @return returns and instance of thte connection
     */
    public static ConnectionFactory getInstance(){
        return connFactory;
    }

    /**
     * A method to get a connection from the connection factory
     * @return returns a connection to the DB
     */
    public Connection getConnection(){
        Connection conn = null;
        try {
            // Force the JVM to load the PostGreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(props.getProperty("url"),
                    props.getProperty("username"),
                    props.getProperty("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (conn == null) {
            throw new RuntimeException("Failed to establish connection.");
        }
        return conn;
    }

}
