package com.revature;

import com.revature.dtos.Credentials;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.services.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.revature.util.Encryption.encrypt;

public class DemoDriver {

    public static UserService userService = new UserService();

    public static void main(String[] args) {
        String username = "testuser3";
        String password = "pass123";

        Credentials credentials = new Credentials(username, password);
        try{
            UserRepository userRepo = new UserRepository();

            List<User> userList = userRepo.getAllUsers();
            for(User u : userList){
                System.out.println(u.toString());
            }


//            User newUser = new User("Gab", "Moon",
//                    "Gabby", "Luna", "lunagab@sonoma.edu", 3);

//            System.out.println("Inserting new user: " + newUser);
//            userRepo.addUser(newUser);
//
//            userList = userRepo.getAllUsers();
//            for(User u : userList){
//                System.out.println(u.toString());
//            }

//            System.out.println("\nGet Matching email : ");
//            Optional<User> emailUser = userRepo.getAUserByEmail("lunagab@sonoma.edu");
//            if (emailUser.isPresent())
//                System.out.println(emailUser);
//
//            System.out.println("\nGet Matching userName : ");
//            Optional<User> usernameUser = userRepo.getAUserByUsername("Gab");
//            if (usernameUser.isPresent())
//                System.out.println(usernameUser);
//
//            System.out.println("\nGet Matching username & password : ");
//            Optional<User> matchingLoginUser = userRepo.getAUserByUsernameAndPassword("Gab", "Moon");
//            if(matchingLoginUser.isPresent())
//                System.out.println(matchingLoginUser);

//            System.out.println("\nupdate username to User01: ");
//            Optional<User> user01 = userRepo.getAUserByEmail("lunagab@sonoma.edu");
//            user01.ifPresent(user -> user.setUsername("User01"));
//            user01.ifPresent(userRepo::updateAUser);
//            System.out.println(user01.get());

//            userRepo.deleteAUserById(9);
//
//            userList = userRepo.getAllUsers();
//            for(User u : userList){
//                System.out.println(u.toString());
//            }
            User newUser = new User("Gab", "Moon",
                    "Gabby", "Luna", "lunagab@sonoma.edu", 3);

            System.out.println("Inserting new user: " + newUser);
            userRepo.addUser(newUser);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
