package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * A repository class that interacts with hibernate to handle all reimbursement transactions
 *
 * @author Cole Space
 * @author Gabrielle Luna
 */
public class ReimbursementsRepository {
    //Attributes -----------------------------------------------------
    private static final Logger logger = LogManager.getLogger(ReimbursementsRepository.class);

    public ReimbursementsRepository(){
        super();
    }

    //INSERT --------------------------------------------------------
    /**
     * Adds a reimbursement to the database, Does not handle Images!
     * @param reimbursement the reimbursement to be added to the DB
     * @return boolean stating whether transaction worked
     */
    // TODO add support to persist receipt images to data source
    public boolean addReimbursement(Reimbursement reimbursement) {
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            session.save(reimbursement);
            t.commit();
            return true;
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error("Error caught while entering new reimbursement {}",e.getMessage());
        }finally{
            session.close();
        }
        return false;
    }

    //SELECT --------------------------------------------------------
    /**
     * Gets all the reimbursements in the database
     * @return the list of all reimbursements
     */
    public List<Reimbursement> getAllReimbursements() {
        List<Reimbursement> reimbursements = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            reimbursements = session.createQuery("FROM Reimbursement").getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * Gets all reimbursements of a specific status id
     * @param statusId the status id of the reimbursements
     * @return a list of reimbursements
     */
    public List<Reimbursement> getAllReimbSetByStatus(Integer statusId) {
        List<Reimbursement> reimbursements = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE reimbursement_status_id = :statusId")
                    .setParameter("statusId", statusId);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * A method to get Reimbursements by the id of the reimbursement itself
     * @param reimbId The ID of the reimbursement in the database that is requested
     * @return returns an Option Reimbursement object
     * @throws SQLException e
     */
    public Optional<Reimbursement> getAReimbByReimbId(Integer reimbId) throws SQLException {
        Optional<Reimbursement> reimbursement = Optional.empty();
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE id = :reimbId")
                    .setParameter("reimbId", reimbId);
            reimbursement = query.stream().findFirst();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursement;
    }

    /**
     * A method to get all of the records for an author given their id
     * @param authorId the ID of the author of the reimbursement
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByAuthorId(Integer authorId){
        List<Reimbursement> reimbursements = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE author_id = :authorId")
                    .setParameter("authorId", authorId);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * A method to get all of the records for an author given their id and filter by status
     * @param authorId the ID of the author of the reimbursement
     * @param reStat the status that the reimbursement is to be set to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByAuthorIdAndStatus(Integer authorId, Integer reStat) throws SQLException {
        List<Reimbursement> reimbursements = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE author_id = :authorId and reimbursement_status_id = :reStat")
                    .setParameter("authorId", authorId)
                    .setParameter("reStat", reStat);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * A method to get all of the records for an author given their id and filter by type
     * @param authorId ID of the Author User
     * @param reType the Type to update the record to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByAuthorIdAndType(Integer authorId, Integer reType) throws SQLException {
        List<Reimbursement> reimbursements = null;
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE author_id = :authorId and reimbursement_type_id = :reType")
                    .setParameter("authorId", authorId)
                    .setParameter("reType", reType);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * Gets all reimbursements of a certain type
     * @param typeId the type of the reimbursement
     * @return the list of reimbursements
     */
    public List<Reimbursement> getAllReimbSetByType(Integer typeId)  {
        List<Reimbursement> reimbursements = null;
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE reimbursement_type_id = :typeId")
                    .setParameter("typeId", typeId);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * A method to get all of the records for a resolver given their id
     * @param resolverId ID of the Resolver User
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByResolverId(Integer resolverId) throws SQLException {
        List<Reimbursement> reimbursements = null;
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE resolver_id = :resolverId")
                    .setParameter("resolverId", resolverId);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * A method to get all of the records for a resolver given their id and filter by status
     * @param resolverId  ID of the Resolver User
     * @param reStat the status to update the record to
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByResolverIdAndStatus(Integer resolverId, Integer reStat) throws SQLException {
        List<Reimbursement> reimbursements = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE resolver_id = :resolverId and reimbursement_status_id = :reStat")
                    .setParameter("resolverId", resolverId)
                    .setParameter("reStat", reStat);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     * A  method to get all of the records for a resolver given their id and filter by type
     * @param resolverId ID of the Resolver User
     * @param reType type of Reimbursements to select by
     * @return a set of reimbursements mapped by the MapResultSet method
     * @throws SQLException e
     */
    public List<Reimbursement> getAllReimbSetByResolverIdAndType(Integer resolverId, Integer reType) throws SQLException {
        List<Reimbursement> reimbursements = null;

        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            Query<Reimbursement> query = session.createQuery("FROM Reimbursement WHERE resolver_id = :resolverId and reimbursement_type_id = :reType")
                    .setParameter("resolverId", resolverId)
                    .setParameter("reType", reType);
            reimbursements = query.getResultList();
            t.commit();
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return reimbursements;
    }

    //---------------------------------- UPDATE -------------------------------------------- //

    /**
     * Updates the reimbursement in the database
     * @param reimb the reimbursement with the updated data
     * @return true if successful, false if not
     */
    public boolean updateEMP(Reimbursement reimb) {
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            session.update(reimb);
            t.commit();
            return true;
        }catch(HibernateException e){
            if(t != null){
                t.rollback();
            }
            logger.error(e.getMessage());
        }finally{
            session.close();
        }
        return false;
    }
    //---------------------------------- DELETE -------------------------------------------- //

    /**
     * A method to delete a single Reimbursement from the database
     * @param reimbursement the ID of the record to be deleted
     * @return returns true if one and only one record is updated
     * @throws SQLException e
     */
    public boolean delete(Reimbursement reimbursement) throws SQLException {
        Session session = HibernateUtil.getSession();
        Transaction t = null;

        try{
            t = session.beginTransaction();
            session.delete(reimbursement);
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
