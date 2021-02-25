package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * A class to interact with the database to CRUD reimbursement objects
 */
public class ReimbursementsRepository {
    //base query that combines the name and resolver names from one query
    private String baseQuery = "SELECT er.id, er.amount, er.description, er.reimbursement_status_id, \n" +
            "er.reimbursement_type_id, er.resolved, er.submitted,  er.author_id , er.resolver_id,\n" +
            "author.first_name as author_first_name , author.last_name as author_last_name , \n" +
            "resolver.first_name as resolver_first_name, resolver.last_name as resolver_last_name\n" +
            "FROM project_1.ers_reimbursements er\n" +
            "left join project_1.ers_users author \n" +
            "on er.author_id = author.id\n" +
            "left join project_1.ers_users resolver \n" +
            "on er.resolver_id = resolver.id ";
    private String baseInsert = "INSERT INTO project_1.ers_reimbursements ";
    private String baseUpdate = "UPDATE project_1.ers_reimbursements er ";

    public ReimbursementsRepository(){
        super();
    }

    //---------------------------------- CREATE -------------------------------------------- //
    /**
     * Adds a reimburement to the database, Does not handle Images!
     * @param reimbursement the reimbursement to be added to the DB
     * @throws SQLException e
     * @throws IOException e
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
            e.printStackTrace();
        }finally{
            session.close();
        }
        return false;
    }

    //---------------------------------- READ -------------------------------------------- //

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
            e.printStackTrace();
        }finally{
            session.close();
        }
        return reimbursements;
    }

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            session.close();
        }
        return reimbursements;
    }

    /**
     *
     * @param typeId
     * @return
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            session.close();
        }
        return reimbursements;
    }

    //---------------------------------- UPDATE -------------------------------------------- //
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
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            session.close();
        }
        return true;
    }
}
