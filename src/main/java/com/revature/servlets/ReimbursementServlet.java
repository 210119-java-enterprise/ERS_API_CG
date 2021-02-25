package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.services.ReimbursementService;
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

@WebServlet(name = "reimbursements", displayName = "reimbursements", urlPatterns = "/reimbursements/*")
public class ReimbursementServlet extends HttpServlet {

    private final ReimbursementService reimbursementService = new ReimbursementService();
    private static final Logger LOG = LogManager.getLogger(ReimbursementServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = session == null? (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Need a reimbursementId
        //String userIdParam = req.getParameter("userId");

        try{
            //Must be Finance Manager
            if (requester != null && requester.getUserRole().compareTo(2) == 0){
                LOG.info("UserServlet.doGet() invoked by requester {}", requester);



            }else{
                if (requester == null){
                    //User got past login or using invalidated session
                    LOG.warn("Unauthorized request made by unknown requester");
                    resp.setStatus(401);
                }else{
                    //User is not a Finance Manager/authorized user
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    resp.setStatus(403);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            LOG.error(e.getMessage());
            resp.setStatus(500);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPut(req, resp);
    }
}
