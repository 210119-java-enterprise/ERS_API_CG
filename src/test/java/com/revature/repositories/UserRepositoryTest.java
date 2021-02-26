package com.revature.repositories;


import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.UserService;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    UserRepository repo = new UserRepository();
    UserService uService = new UserService();

    public User setUp(){
        //set up dummy reimbursement
        User user = new User("DEMOBob", "Ross",
                    "Bob", "Ross",
                    "bRoss@happyTrees.paintDEMODEMODEMODEMODEMODEMODEMODEMODEMO", Role.EMPLOYEE.ordinal() + 1);

        System.out.println("\nSETUP");
        List<User> users = uService.getAllUsers();
        for(User u : users){
            System.out.println(u.toString());
        }
        return user;
    }

    public void teardown(User user) {
        System.out.println("\nSNAPSHOT");
        List<User> users = uService.getAllUsers();
        for(User u : users){
            System.out.println(u.toString());
        }

        repo.deleteAUserById(user.getUserId());
        System.out.println("\nTEARDOWN");
        users = uService.getAllUsers();
        for(User u : users){
            System.out.println(u.toString());
        }
    }

    @Test
    public void test_NewUser(){
        //Arrange
        User user = setUp();
        //Act
        boolean worked = repo.addUser(user);
        //Assert
        assertTrue(worked);
        teardown(user);
    }

    @Test
    public void test_NewUser_EmptyUser(){
        //Arrange
        //Act
        boolean worked = repo.addUser(new User());
        //Assert
        assertFalse(worked);
    }

    @Test
    public void test_getAllReimb(){
        //Arrange
        User user = setUp();
        //Act
        int numBefore = repo.getAllUsers().size();
        repo.addUser(user);
        int numAfter = repo.getAllUsers().size();
        //Assert
        assertEquals(1, numAfter - numBefore);
        teardown(user);
    }

    @Test
    public void test_getByEmail(){
        //Arrange
        User user = setUp();
        //Act
        repo.addUser(user);
        User userFound = repo.getAUserByEmail("bRoss@happyTrees.paintDEMODEMODEMODEMODEMODEMODEMODEMODEMO");
        int numAfter = repo.getAllUsers().size();
        //Assert;
        teardown(user);
    }

}
