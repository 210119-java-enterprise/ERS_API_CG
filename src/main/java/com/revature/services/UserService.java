package com.revature.services;

import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidInputException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Constitutes the SERVICE LAYER for users. concerned with validating all user
 * input before being sent to the database.
 */
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private UserRepository userRepo = new UserRepository();
    /**
     * Gets all users from the DataBase
     * @return A list of Users
     */
    public List<User> getAllUsers(){
        List<User> users = userRepo.getAllUsers();
        if (users.isEmpty()){
            logger.error("No users in the database", new DatabaseException());
            return null;
        }
        return users;
    }

    /**
     * Authentication method used by the authentication servlet
     * @param username username of the user
     * @param password password of the user
     * @return the object of the requested user
     */
    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")){
            logger.error("Invalid credentials provided", new InvalidCredentialsException());
            return null;
        }
        Optional<User> authUser =userRepo.getAUserByUsernameAndPassword(username,password);
        if (authUser .isPresent())
            return authUser.get();
        else {
            logger.error("Authentication failed", new InvalidCredentialsException());
            return null;
        }

    }

    /**
     * Register a new user in the DB. validates all fields first
     * @param newUser completed user object
     */
    // TODO: encrypt all user passwords before persisting to data source
    public void register(User newUser) {
        if (!isUserValid(newUser)) {
            logger.error("Invalid user field values provided during registration!", new InvalidInputException());
            return;
        }
        Optional<User> existingUser = userRepo.getAUserByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            logger.error("Username is already in use", new InvalidCredentialsException());
            return;
        }
        Optional<User> existingUserEmail = userRepo.getAUserByEmail(newUser.getEmail());
        if (existingUserEmail.isPresent()) {
            logger.error("Email is already in use",new InvalidCredentialsException());
            return;
        }
        newUser.setUserRole(Role.EMPLOYEE.ordinal() + 1);
        userRepo.addUser(newUser);
    }

    /**
     * Update a user in the DB.
     * @param newUser user to update
     */
    public void update(User newUser) {
        if (!isUserValid(newUser)) {
            logger.error("Invalid user field values provided during registration!", new InvalidInputException());
            return;
        }
        if (!userRepo.updateAUser(newUser)){
            logger.error("", new DatabaseException());
        }
    }

    /**
     * Deletes a user by changing their role to 4
     * @param id id of user to delete
     * @return true if role was updated in db
     */
    public boolean deleteUserById(int id) {
        if (id <= 0){
            logger.error("THE PROVIDED ID CANNOT BE LESS THAN OR EQUAL TO ZERO",new InvalidInputException());
            return false;
        }
        return userRepo.deleteAUserById(id);
    }

    /**
     * Method for simple checking of availability of username
     * @param username username to chek
     * @return true if available
     */
    public boolean isUsernameAvailable(String username) {
        User user = userRepo.getAUserByUsername(username).orElse(null);
        return user == null;
    }

    /**
     * Method for simple checking of availability of email
     * @param email
     * @return true if available
     */
    public boolean isEmailAvailable(String email) {
        User user = userRepo.getAUserByEmail(email).orElse(null);
        return user == null;
    }

    /**
     * Validates that the given user and its fields are valid (not null or empty strings). Does
     * not perform validation on id or role fields.
     *
     * @param user
     * @return true or false depending on if the user was valid or not
     */
    public boolean isUserValid(User user) {
        if (user == null) return false;
        if (user.getFirstname() == null || user.getFirstname().trim().equals("")) return false;
        if (user.getLastname() == null || user.getLastname().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        if (user.getPassword() == null || user.getPassword().trim().equals("")) return false;
        return true;
    }
}
