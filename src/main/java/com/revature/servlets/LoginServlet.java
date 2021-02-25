package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.Credentials;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet(name = "Login", displayName = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();
    private static final Logger LOG = LogManager.getLogger(LoginServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //Returns the current session associated with this request, or if the request does not have a session, creates one.
        HttpSession session = req.getSession(false);

        //invalidate pre-existing session
        if (session != null) {
            String username = ((User) session.getAttribute("this-user")).getUsername();
            LOG.info("Invalidating session for user, {}", username);
            req.getSession().invalidate();
        }

        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("{\n" +
                "    \"username\": \" \",\n" +
                "    \"password\": \" \"\n" +
                "}");
        printWriter.close();

    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        User authUser = null;

        try {
            Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);

            LOG.info("Attempting to authenticate user, {}, with provided credentials", creds.getUsername());
            authUser = userService.authenticate(creds.getUsername(), creds.getPassword());

            writer.write(mapper.writeValueAsString(authUser));

            LOG.info("Establishing a session for user, {}", creds.getUsername());
            req.getSession().setAttribute("this-user", authUser);



        }catch (Exception e) {
            resp.getWriter().write(e.toString());
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
            //writer.write(errRespFactory.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR).toJSON());
        }
    }
}
