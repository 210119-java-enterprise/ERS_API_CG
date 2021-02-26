package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.DatabaseException;
import com.revature.exceptions.InvalidInputException;
import com.revature.exceptions.RegistrationException;
import com.revature.models.User;
import com.revature.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * This class is a servlet intended to handle an admins user management needs, including:
 * <ul>
 *     <li>viewing users</li>
 *     <li>adding new users</li>
 *     <li>updating users</li>
 *     <li>deleting users</li>
 * </ul>
 * Endpoint : /users/*
 * @author Cole Space
 * @author Gabrielle Luna
 */
@WebServlet(name = "users", displayName = "users", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {
    //attributes ----------------------------------------------------
    private final UserService userService = new UserService();
    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    //Http Verbs ----------------------------------------------------
    /**
     * Get request will allow requester to view users. If given parameters like the users id
     * the list will be narrowed down to that user
     * @param req                   request may contain parameter with userId
     * @param resp                  response sends users
     * @throws ServletException     not thrown
     * @throws IOException          thrown by mapper logic
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Retrieve userId param if provided
        String userIdParam = req.getParameter("userId");

        //Retrieve all or a specific user
        try {
            //Must be admin
            if (requester != null && requester.getUserRole().compareTo(1) == 0) {

                LOG.info("UserServlet.doGet() invoked by requester {}", requester);

                //No params posted
                if (userIdParam == null) {
                    LOG.info("Retrieving all users");
                    writer.write("All Users: \n");
                    List<User> users = userService.getAllUsers();

                    if (!users.isEmpty()){
                        String usersJSON = mapper.writeValueAsString(users);
                        writer.write(usersJSON);
                    }else throw new DatabaseException(); //should be unreachable

                } else { //Id given
                    int soughtId = Integer.parseInt(userIdParam);
                    LOG.info("Retrieving users with id, {}" , soughtId);
                    writer.write("Requested User: \n");

                    User user = userService.getUserById(soughtId);
                    if (user != null){
                        String userJSON = mapper.writeValueAsString(user);
                        writer.write(userJSON);
                    }else throw new InvalidInputException();

                }
            }else {
                if (requester == null) {
                    //User skipped login or using invalidated session
                    LOG.warn("Unauthorized request made by unknown requester");
                    resp.setStatus(401);
                } else {
                    //User is not an Admin/authorized user
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    resp.setStatus(403);
                }

            }
        }catch(InvalidInputException e){
            //User matching provided id could not be found
            e.printStackTrace();
            LOG.error("Invalid user Id provided");
            writer.write("No user matching the userId could be found");
            resp.setStatus(404);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }

    }

    /**
     * Post will add a new user to the database
     * @param req               request body holds the description of a user
     * @param resp              response holds a confirmation
     * @throws ServletException not thrown
     * @throws IOException      thrown by object mapper
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Add new User
        try {
            //Must be admin
            if (requester != null && requester.getUserRole().compareTo(1) == 0) {

                LOG.info("UserServlet.doPost() invoked by requester {}", requester);

                User newUser = mapper.readValue(req.getInputStream(), User.class);

                if (userService.register(newUser)) {
                    writer.write("New User created : \n");
                    writer.write(mapper.writeValueAsString(newUser));
                    LOG.info("New User created : {}", newUser.getUsername());
                    resp.setStatus(201);
                }else throw new RegistrationException();

            }else {
                if (requester == null) {
                    //User got past login or using invalidated session
                    LOG.warn("Unauthorized request made by unknown requester");
                    resp.setStatus(401);
                } else {
                    //User is not an Admin/authorized user
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    resp.setStatus(403);
                }
            }
        }catch(RegistrationException | IOException e){
            //Invalid new user
            LOG.error("Invalid User created");
            writer.write("Invalid user created\n");
            resp.setStatus(400);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }
    }

    /**
     * Put updates an existing user
     * @param req               request holds a complete user with updated fields
     * @param resp              response holds a confirmation with the uspdated users info
     * @throws ServletException not thrown
     * @throws IOException      thrown by object mapper
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //UpdateUser
        try {
            //Must be admin
            if (requester != null && requester.getUserRole().compareTo(1) == 0) {

                LOG.info("UserServlet.doPut() invoked by requester {}", requester);

                User newUser = mapper.readValue(req.getInputStream(), User.class);
                if (userService.update(newUser)) {
                    //SUCCESS
                    writer.write("User Updated: \n");
                    writer.write(mapper.writeValueAsString(newUser));
                    LOG.info("User updated : {}", newUser.getUsername());
                }else throw new InvalidInputException();

            }else {
                if (requester == null) {
                    //User got past login or using invalidated session
                    LOG.warn("Unauthorized request made by unknown requester");
                    resp.setStatus(401);
                } else {
                    //User is not an Admin/authorized user
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    resp.setStatus(403);
                }
            }
        }catch(InvalidInputException e){
            //Error in UserService due to invalid input by user
            LOG.error("Invalid User update attempted");
            writer.write("Invalid user update\n");
            resp.setStatus(400);
        }catch (Exception e) {
            writer.write(e.getMessage());
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }
    }

    /**
     * Delete will delete the specified user from the database
     * @param req               request hold details about a user to be deleted
     * @param resp              response holds a confirmation of the deleted user
     * @throws ServletException not thrown
     * @throws IOException      thrown by object mapper
     */
    //Disallow a self delete
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Delete User
        try {
            //Must be admin
            if (requester != null && requester.getUserRole().compareTo(1) == 0) {

                LOG.info("UserServlet.doDelete() invoked by requester {}", requester);

                User user = mapper.readValue(req.getInputStream(), User.class);
                if (userService.deleteUserById(user.getUserId())) {
                    writer.write("User Deleted: \n");
                    writer.write(mapper.writeValueAsString(user));
                    LOG.info("User deleted : {}", user.getUsername());
                }else throw new InvalidInputException();

            }else {
                if (requester == null) {
                    //User got past login or using invalidated session
                    LOG.warn("Unauthorized request made by unknown requester");
                    resp.setStatus(401);
                } else {
                    //User is not an Admin/authorized user
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    resp.setStatus(403);
                }

            }
        }catch(InvalidInputException e){
            LOG.error("Failure to delete user");
            writer.write("Failed to delete user\n");
            resp.setStatus(400);
        }
        catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }
    }
}
