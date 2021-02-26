package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.Credentials;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.models.User;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is a servlet intended to handle all logins after which an HttpSession is initiated.
 * <p>Endpoint : /login</p>
 * @author Cole Space
 * @author Gabrielle Luna
 */
@WebServlet(name = "Login", displayName = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    //Attributes ----------------------------------------------------
    private final UserService userService = new UserService();
    private static final Logger LOG = LogManager.getLogger(LoginServlet.class);

    //Http Verbs ----------------------------------------------------
    /**
     * Get will invalidate any pre-existing session, to allow for a new user to login.
     * @param req                   request is empty
     * @param resp                  response reminds user to login
     * @throws ServletException     not thrown
     * @throws IOException          not thrown
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        PrintWriter writer = resp.getWriter();

        //Get current session
        HttpSession session = req.getSession(false);

        //invalidate pre-existing session
        if (session != null) {
            String username = ((User) session.getAttribute("this-user")).getUsername();
            LOG.info("Invalidating session for user, {}", username);
            req.getSession().invalidate();
        }

        writer.write("Please Log In!");
    }

    /**
     * Post will activate user authentication logic.
     * @param req               request body has JSON with login info
     * @param resp              response has information regarding the success or failure of login
     * @throws ServletException not thrown
     * @throws IOException      thrown by mapper logic
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        User authUser = null;

        //Initialize new session for new user
        try {
            //Retrieve and authenticate credentials
            Credentials credentials = mapper.readValue(req.getInputStream(), Credentials.class);
            LOG.info("Attempting to authenticate user, {}, with provided credentials", credentials.getUsername());
            authUser = userService.authenticate(credentials.getUsername(), credentials.getPassword());

            //Print new user to screen
            if (authUser != null) {
                writer.write("Welcome back " + authUser.getFirstname() + " " + authUser.getLastname());
            }else throw new InvalidCredentialsException();

            //Save new user to session
            LOG.info("Establishing a session for user, {}", credentials.getUsername());
            req.getSession().setAttribute("this-user", authUser);

        }catch (SQLException e) {
            //Authentication failure in UserService
            writer.write("SQL Authentication error!");
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }catch (InvalidCredentialsException | IOException e){
            //Bad login
            writer.write("Invalid Login!");
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(401);
        }
    }
}
