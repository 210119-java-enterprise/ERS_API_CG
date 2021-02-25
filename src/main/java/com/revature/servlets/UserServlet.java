package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebServlet(name = "users", displayName = "users", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private static final Logger LOG = LogManager.getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");
        String userIdParam = req.getParameter("userId");

        //Retrieve all or a specific user
        try {
            //Must be admin
            if (requester != null && requester.getUserRole().compareTo(1) == 0) {

                LOG.info("UserServlet.doGet() invoked by requester {}", requester);

                //No params posted
                if (userIdParam == null) {
                    LOG.info("Retrieving all users");
                    writer.write("All Users \n");
                    List<User> users = userService.getAllUsers();
                    String usersJSON = mapper.writeValueAsString(users);
                    writer.write(usersJSON);
                } else { //Id given
                    int soughtId = Integer.parseInt(userIdParam);
                    LOG.info("Retrieving users with id, {}" , soughtId);
                    User user = userService.getUserById(soughtId);
                    String userJSON = mapper.writeValueAsString(user);
                    writer.write(userJSON);
                }
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
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }

    }

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
                if (userService.isUserValid(newUser)) {
                    //SUCCESS
                    userService.update(newUser);
                    writer.write("New User created : \n");
                    writer.write(mapper.writeValueAsString(newUser));
                    LOG.info("New User created : {}", newUser.getUsername());
                }else{
                    //FAILURE
                    LOG.error("Invalid User created {}", newUser.toString());
                    writer.write("Invalid user created\n");
                }
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
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }
    }


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
                }else{
                    //FAILURE
                    LOG.error("Invalid User update {}", newUser.toString());
                    writer.write("Invalid user update\n");
                }
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
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }
    }

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
                    //SUCCESS
                    writer.write("User Deleted: \n");
                    writer.write(mapper.writeValueAsString(user));
                    LOG.info("User deleted : {}", user.getUsername());
                }else{
                    //FAILURE
                    LOG.error("Failure to delete user: {}", user.toString());
                    writer.write("Failed to delete user\n");
                }
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
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }
    }
}
