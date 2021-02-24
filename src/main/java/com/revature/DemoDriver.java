package com.revature;

import com.revature.dtos.Credentials;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.UserService;

import java.sql.SQLException;
import java.util.List;

import static com.revature.util.Encryption.encrypt;

public class DemoDriver {

    public static UserService userService = new UserService();

    public static void main(String[] args) {
        String username = "testuser3";
        String password = "pass123";

        Credentials credentials = new Credentials(username, password);
        try{
            //Runtime Exception thrown
            //User authUser = userService.authenticate(credentials.getUsername(), credentials.getPassword());

            UserRepository userRepo = new UserRepository();
            //Sends back empty optional
//            userRepo.getAUserByUsernameAndPassword(username,encrypt(password));
//
//            List<User> users= userRepo.getAllUsers();
//            System.out.println("Users: length " + users.size());
//            for (User user : users){
//                System.out.println(user.toString());
//            }

            List<User> userList = userRepo.getAllUsers();
            for(User u : userList){
                System.out.println(u.toString());
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
