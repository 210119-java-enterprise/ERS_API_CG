package com.revature;

import com.revature.dtos.RbDTO;
import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.services.ReimbursementService;

import java.sql.Timestamp;
import java.util.List;

public class ReimbursementDriver {

    public static void main(String[] args){

        ReimbursementService rs = new ReimbursementService();

        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setAmount(30.00);
        reimbursement.setAuthorId(5);
        reimbursement.setResolverId(5);
        reimbursement.setDescription("Ate food");
        reimbursement.setReimbursementType(ReimbursementType.FOOD);
        reimbursement.setReimbursementStatus(ReimbursementStatus.PENDING);
        reimbursement.setSubmitted(new Timestamp(System.currentTimeMillis()));

        //rs.save(reimbursement);

//        System.out.println("Get all reimbursements");
//
//        List<Reimbursement> reimbursements = rs.getAllReimb();
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by status number");
//
//        reimbursements = rs.getReimbByStatus(1);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by reimbursement id");
//
//        Reimbursement r1 = rs.getReimbByReimbId(4);
//        System.out.println(r1.toString());
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by author id");
//
//        reimbursements = rs.getReimbByUserId(5);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by author id and status id");
//
//        reimbursements = rs.getReimbByAuthorAndStatus(5, 1);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by author id and type id");
//
//        reimbursements = rs.getReimbByAuthorAndType(5, 3);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by type id");
//
//        reimbursements = rs.getReimbByType(3);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by resolver id");
//
//        reimbursements = rs.getReimbByResolver(5);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by resolver id and status id");
//
//        reimbursements = rs.getReimbByResolverAndStatus(5, 1);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        System.out.println("Get all reimbursements by resolver id and type id");
//
//        reimbursements = rs.getReimbByResolverAndStatus(5, 1);
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
//
//        rs.approve(5, 4);
//
//        System.out.println("+--------------------------------------+");
//
//        rs.deny(5, 4);

//        rs.delete(4);

//        System.out.println("Trying to update reimbursement");
//
//        Reimbursement old = rs.getReimbByReimbId(4);
//
//        old.setAmount(50.00);
//        old.setDescription("This is the updated description");
//
//        rs.updateEMP(old);
//        reimbursements = rs.getAllReimb();
//
//        for(Reimbursement r : reimbursements){
//            System.out.println(r.toString());
//        }
//
//        System.out.println("+--------------------------------------+");
    }
}
