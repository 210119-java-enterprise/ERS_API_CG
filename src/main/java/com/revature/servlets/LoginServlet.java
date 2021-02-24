package com.revature.servlets;

import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.Encryption;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Login", displayName = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print("<html>");
        printWriter.print("<body>");
        printWriter.print("<h1>Login Form Data</h1>");
        printWriter.print("<p> username :: " +  "</p>");
        printWriter.print("<p> password :: " + "</p>");
        printWriter.print("</body>");
        printWriter.print("</html>");
        printWriter.close();

    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String username = req.getParameter("username");
        String password = Encryption.getEncryption().encrypt(req.getParameter("password"));
        PrintWriter out = resp.getWriter();

        //User user = userService.authenticate(username, password);

        if(password.equals("pass123")){
            RequestDispatcher rs = req.getRequestDispatcher("test");
            rs.forward(req, resp);
        }else{
            out.write("Incorrect username or password");
        }


    }
}
