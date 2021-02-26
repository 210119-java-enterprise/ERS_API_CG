package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.services.ReimbursementService;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;

public class ReimbursementRepositoryTest {
    ReimbursementsRepository repo = new ReimbursementsRepository();
    ReimbursementService rService = new ReimbursementService();

    public Reimbursement setUp(){
        //set up dummy reimbursement
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setAmount(30.00);
        reimbursement.setResolved(null);
        reimbursement.setAuthorId(5);
        reimbursement.setResolverId(5);
        reimbursement.setDescription("DEMODEMODEMODEMODEMODEMODEMODEMODEMODEMO");
        reimbursement.setReimbursementType(ReimbursementType.FOOD);
        reimbursement.setReimbursementStatus(ReimbursementStatus.PENDING);
        reimbursement.setSubmitted(new Timestamp(System.currentTimeMillis()));

        System.out.println("\nSETUP");
        List<Reimbursement> reimbursements = rService.getAllReimb();
        for(Reimbursement r : reimbursements){
            System.out.println(r.toString());
        }
        return reimbursement;
    }

    public void teardown(Reimbursement reimbursement) {
        System.out.println("\nSNAPSHOT");
        List<Reimbursement> reimbursements = rService.getAllReimb();
        for(Reimbursement r : reimbursements){
            System.out.println(r.toString());
        }

        try {
            repo.delete(reimbursement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("\nTEARDOWN");
        reimbursements = rService.getAllReimb();
        for(Reimbursement r : reimbursements){
            System.out.println(r.toString());
        }
    }

    @Test
    public void test_NewReimb(){
        //Arrange
        Reimbursement reimbursement = setUp();
        //Act
        boolean worked = repo.addReimbursement(reimbursement);
        //Assert
        assertTrue(worked);
        teardown(reimbursement);
    }

    @Test
    public void test_NewReimb_emptyReimbursement(){
        //Arrange
        //Act
        boolean worked = repo.addReimbursement(new Reimbursement());
        //Assert
        assertFalse(worked);
    }

    @Test
    public void test_getAllReimb(){
        //Arrange
        Reimbursement reimbursement = setUp();
        //Act
        int numBefore = repo.getAllReimbursements().size();
        repo.addReimbursement(reimbursement);
        int numAfter = repo.getAllReimbursements().size();
        //Assert
        assertEquals(1, numAfter - numBefore);
        teardown(reimbursement);
    }
}
