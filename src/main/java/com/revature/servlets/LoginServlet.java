package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.Credentials;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.Encryption;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Login", displayName = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //Returns the current session associated with this request, or if the request does not have a session, creates one.
        HttpSession session = req.getSession(false);

        //invalidate pre-existing session
        if (session != null) {
            String username = ((User) session.getAttribute("this-user")).getUsername();
            //LOG.info("Invalidating session for user, {}", username);
            req.getSession().invalidate();
        }

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html>");
        printWriter.print("<body>");
        printWriter.print("<div style=\"text-align: center\">\n" +
                "        <h1>ERS Login</h1>\n" +
                "        <form action=\"login\" method=\"post\">\n" +
                "            <label for=\"username\">Username:</label>\n" +
                "            <input name=\"username\" size=\"30\" />\n" +
                "            <br><br>\n" +
                "            <label for=\"password\">Password:</label>\n" +
                "            <input type=\"password\" name=\"password\" size=\"30\" />\n" +
                "            <br>${message}\n" +
                "            <br><br>           \n" +
                "            <button type=\"submit\">Login</button>\n" +
                "        </form>\n" +
                "    </div>");
        printWriter.print("</body>");
        printWriter.print("</html>");
        printWriter.close();

    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        try {

            Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);

            //LOG.info("Attempting to authenticate user, {}, with provided credentials", creds.getUsername());
            User authUser = userService.authenticate(creds.getUsername(), creds.getPassword());

            writer.write(mapper.writeValueAsString(authUser));

            //LOG.info("Establishing a session for user, {}", creds.getUsername());
            req.getSession().setAttribute("this-user", authUser);

        }catch (Exception e) {
            e.printStackTrace();
            //LOG.error(e.getMessage());
            resp.setStatus(500);
            //writer.write(errRespFactory.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR).toJSON());
        }
        /*
        String username = req.getParameter("username");
        String password = Encryption.encrypt(req.getParameter("password"));
        PrintWriter out = resp.getWriter();

        //User user = userService.authenticate(username, password);

//        if(password.equals("pass123")){
//            RequestDispatcher rs = req.getRequestDispatcher("test");
//            rs.forward(req, resp);
//        }else{
//            out.write("Incorrect username or password");
//        }

         */


    }
}
