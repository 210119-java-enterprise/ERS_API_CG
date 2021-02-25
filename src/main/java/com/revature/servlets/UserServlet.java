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
        User rqstr = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");
        String userIdParam = req.getParameter("userId");

        try {
            //Must be admin
            if (rqstr != null && rqstr.getUserRole().compareTo(1) == 0) {

                LOG.info("UserServlet.doGet() invoked by requester {}", rqstr);

                if (userIdParam == null) {
                    LOG.info("Retrieving all users");
                    writer.write("All Users \n");
                    List<User> users = userService.getAllUsers();
                    String usersJSON = mapper.writeValueAsString(users);
                    writer.write(usersJSON);
                } else {
                    int soughtId = Integer.parseInt(userIdParam);
                    LOG.info("Retrieving users with id, {}" , soughtId);
                    User user = userService.getUserById(soughtId);
                    String userJSON = mapper.writeValueAsString(user);
                    writer.write(userJSON);
                }

            }else {

                if (rqstr == null) {
                    //User got past login or using invalidated session
                    LOG.warn("Unauthorized request made by unknown requester");
                    resp.setStatus(401);
                } else {
                    //User is not an Admin/authorized user
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", rqstr.getUsername());
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
