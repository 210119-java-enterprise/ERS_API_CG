package com.revature.services;

//TODO when writing tests, make sure to add the checks for status and type ids
//TODO to not be outside the range they should be

import com.revature.dtos.RbDTO;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.repositories.ReimbursementsRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for validating reimbursements before sending to or from the Database
 */
public class ReimbursementService {
    private final ReimbursementsRepository reimbRepo = new ReimbursementsRepository();

    /**
     * Gets all Reimbursements from the DataBase
     * @return A list of RbDTO objects
     */
    public List<Reimbursement> getAllReimb(){
        List<Reimbursement> reimbursements = reimbRepo.getAllReimbursements();
        if (reimbursements.isEmpty()){
            throw new RuntimeException();
        }
        return reimbursements;
    }

    /**
     * Gets all reimbursements for a usre given their Id
     * @param userId user id requested
     * @return A list of RbDTO objects
     */
    public List<Reimbursement> getReimbByUserId(Integer userId){
        if (userId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }
        List<Reimbursement> reimb = reimbRepo.getAllReimbSetByAuthorId(userId);
        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     *
     * @param authorId
     * @param reStat
     * @return
     */
    public List<Reimbursement> getReimbByAuthorAndStatus(Integer authorId, Integer reStat){
        if (authorId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }

        List<Reimbursement> reimb = null;

        try {
            reimb = reimbRepo.getAllReimbSetByAuthorIdAndStatus(authorId, reStat);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     *
     * @param authorId
     * @param reType
     * @return
     */
    public List<Reimbursement> getReimbByAuthorAndType(Integer authorId, Integer reType){
        if (authorId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }

        List<Reimbursement> reimb = null;

        try {
            reimb = reimbRepo.getAllReimbSetByAuthorIdAndType(authorId, reType);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     *
     * @param resolverId
     * @param reStat
     * @return
     */
    public List<Reimbursement> getReimbByResolverAndStatus(Integer resolverId, Integer reStat){
        if (resolverId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }

        List<Reimbursement> reimb = null;

        try {
            reimb = reimbRepo.getAllReimbSetByResolverIdAndStatus(resolverId, reStat);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     *
     * @param resolverId
     * @param reType
     * @return
     */
    public List<Reimbursement> getReimbByResolverAndType(Integer resolverId, Integer reType){
        if (resolverId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }

        List<Reimbursement> reimb = null;

        try {
            reimb = reimbRepo.getAllReimbSetByResolverIdAndType(resolverId, reType);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     *
     * @param reimbId
     * @return
     */
    public Reimbursement getReimbByReimbId(Integer reimbId){
        if (reimbId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }
        Reimbursement reimb = null;
        try {
            reimb = reimbRepo.getAReimbByReimbId(reimbId).get();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reimb;
    }

    /**
     * Gets all reimbursements by a specified type
     * @param typeId ordinal number of the type requested, between 1-4
     * @return A list of RbDTO objects
     */
    public List<Reimbursement> getReimbByType(Integer typeId){
        if (typeId <= 0 || typeId >=5){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }
        List<Reimbursement> reimb = reimbRepo.getAllReimbSetByType(typeId);
        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     * Gets all reimbursements by a specified status
     * @param statusId ordinal number of the type requested, between 1-3
     * @return A list of RbDTO objects
     */
    public List<Reimbursement> getReimbByStatus(Integer statusId){
        if (statusId <= 0 || statusId >= 4){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }
        List<Reimbursement> reimb = reimbRepo.getAllReimbSetByStatus(statusId);
        if (reimb.isEmpty()){
            throw new RuntimeException();
        }
        return reimb;
    }

    /**
     *
     * @param resolverId
     * @return
     */
    public List<Reimbursement> getReimbByResolver(Integer resolverId){
        if (resolverId <= 0){
            throw new RuntimeException("THE PROVIDED USER ID CANNOT BE LESS THAN OR EQUAL TO ZERO");
        }

        List<Reimbursement> reimb = null;

        try {
            reimb = reimbRepo.getAllReimbSetByResolverId(resolverId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (reimb.isEmpty()){
            throw new RuntimeException();
        }

        return reimb;
    }

    /**
     * Saves a reimbursement after validation
     * @param reimb the completed reimbursement object
     */
    public void save(Reimbursement reimb){
        if (!isReimbursementValid(reimb)){
            throw new RuntimeException("Invalid user field values provided!");
        }
        if(!reimbRepo.addReimbursement(reimb)){
            throw new RuntimeException("Something went wrong trying to save this reimbursement");
        }
        System.out.println(reimb);
    }

    /**
     * Update a reimbursement
     * @param reimb the completed reimbursement object
     */
    public void updateEMP(Reimbursement reimb) {
        if (!isReimbursementValid(reimb)){
            throw new RuntimeException("Invalid user field values provided!");
        }
        if(!reimbRepo.updateEMP(reimb)){
            throw new RuntimeException("Something went wrong trying to save this reimbursement");
        }
        System.out.println(reimb);
    }

    /**
     * Approve a Reimb.
     * @param resolverId the Id of the fin manager resolving the reimb.
     * @param reimbId id of the Reimb. to approve or disapprove.
     */
    public void approve(Integer resolverId, Integer reimbId) {
        if (reimbId <= 0 || resolverId <=0){
            throw new RuntimeException("Invalid user field values provided!");
        }
        Reimbursement r = null;
        try {
            Optional<Reimbursement> o = reimbRepo.getAReimbByReimbId(reimbId);
            if(!o.isPresent()){
                throw new RuntimeException("No entry in database");
            }
            r = o.get();
            r.setResolverId(resolverId);
            r.setReimbursementStatus(ReimbursementStatus.APPROVED);
            r.setResolved(new Timestamp(System.currentTimeMillis()));
            updateEMP(r);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Deny a reimb.
     * @param resolverId the Id of the fin manager resolving the reimb.
     * @param reimbId id of the Reimb. to approve or disapprove.
     */
    public void deny(Integer resolverId, Integer reimbId) {
        if (reimbId <= 0){
            throw new RuntimeException("Invalid user field values provided!");
        }
        Reimbursement r = null;
        try {
            Optional<Reimbursement> o = reimbRepo.getAReimbByReimbId(reimbId);
            if(!o.isPresent()){
                throw new RuntimeException("No entry in database");
            }
            r = o.get();
            r.setResolverId(resolverId);
            r.setReimbursementStatus(ReimbursementStatus.DENIED);
            r.setResolved(new Timestamp(System.currentTimeMillis()));
            reimbRepo.updateEMP(r);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param reimbId
     * @return
     */
    public boolean delete(Integer reimbId){
        try {
            Optional<Reimbursement> o = reimbRepo.getAReimbByReimbId(reimbId);
            if(!o.isPresent()){
                throw new RuntimeException("Cannot delete what isn't there");
            }
            Reimbursement r = o.get();
            return reimbRepo.delete(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Validates feilds of a reimbursement
     * @param reimb reimb. to be validated
     * @return true or false based on fields
     */
    public boolean isReimbursementValid(Reimbursement reimb){
        if (reimb == null) return false;
        if (reimb.getAmount() == null || reimb.getAmount() <= 0 ) return false;
        if (reimb.getDescription() == null || reimb.getDescription().trim().equals("")) return false;
        if (reimb.getAuthorId() <= 0 ) return false;
        if (reimb.getReimbursementType() == null ) return false;
        return true;
    }


}
