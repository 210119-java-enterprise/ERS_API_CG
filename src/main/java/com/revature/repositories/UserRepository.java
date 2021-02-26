package com.revature.repositories;

import com.revature.models.User;
import com.revature.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.*;

public class UserRepository {

    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    //INSERT --------------------------------------------------------
    /**
     * Adds a new user to the database.
     * @param newUser   new user without an id
     * @return          boolean
     */
    public boolean addUser(User newUser)  {
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try {
            t = session.beginTransaction();
            session.save(newUser);
            t.commit();
            return true;
        } catch (HibernateException e) {
            if (t != null)
                t.rollback();

            logger.error("Error caught while entering new user {}", e.getMessage());
        }finally{
            session.close();
        }
        return false;
    }

    //SELECT --------------------------------------------------------
    /**
     * Retrieves a list of all Users
     * @return  a list of users found in the database
     */
    public List<User> getAllUsers() {
        List<User> users = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            users = session.createQuery("FROM User").getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return users;
    }

    /**
     * A method to get a single User by email
     * @param email the email address to search the DB for
     * @return returns an Optional user
     * @throws SQLException e
     */
   public Optional<User> getAUserByEmail(String email) {
        Optional user = Optional.empty();
        Session session = HibernateUtil.getSession();
       Transaction t = session.beginTransaction();

       Query query = session.createQuery("FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        user = query.stream().findFirst();

       if(t != null){
           t.rollback();
       }
       session.close();

       return user;
    }

    public Optional<User> getAUserByUsername(String userName) {
        Optional<User> user = Optional.empty();

        Session session = HibernateUtil.getSession();
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("FROM User u WHERE u.username = :username");
        query.setParameter("username", userName);
        user = query.stream().findFirst();

        if(t != null){
            t.rollback();
        }
        session.close();

        return user;
    }

    public Optional<User> getAUserById(int id) {
        Optional<User> user = Optional.empty();

        Session session = HibernateUtil.getSession();
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("FROM User u WHERE u.id = :id");
        query.setParameter("id", id);
        user = query.stream().findFirst();

        if(t != null){
            t.rollback();
        }
        session.close();

        return user;
    }

    /**
     * A method to get a single user by a given username and password
     * @param userName the users username
     * @param password the users password
     * @return returns an optional user
     * @throws SQLException e
     */
    public Optional<User> getAUserByUsernameAndPassword(String userName, String password) throws SQLException {
        Optional user = Optional.empty();

        Session session = HibernateUtil.getSession();
        Transaction t = session.beginTransaction();

        Query query = session.createQuery("FROM User u WHERE u.username = :username AND u.password = :password");
        query.setParameter("username", userName);
        query.setParameter("password", password);
        user = query.stream().findFirst();

        if(t != null){
            t.rollback();
        }
        session.close();

        return user;
    }

    //---------------------------------- UPDATE -------------------------------------------- //

    public boolean updateAUser(User newUser) {

        Session session = HibernateUtil.getSession();
        Transaction t = session.beginTransaction();

        try{
            session.update(newUser);
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return true;
    }

    //---------------------------------- DELETE -------------------------------------------- //

    /**
     * A method to delete a single User from the database
     * @param userId the ID of the record to be deleted
     * @return returns true if one and only one record is updated
     * @throws SQLException
     */
    public boolean deleteAUserById(Integer userId) {
        Session session = HibernateUtil.getSession();
        Transaction t = session.beginTransaction();

        try{
            String hql = "delete from User where userId= :userId";
            session.createQuery(hql).setParameter("userId", userId).executeUpdate();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return true;
    }
}
