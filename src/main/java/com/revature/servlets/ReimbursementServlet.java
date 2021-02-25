package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
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
import java.util.List;

@WebServlet(name = "reimbursements", displayName = "reimbursements", urlPatterns = "/reimbursements/*")
public class ReimbursementServlet extends HttpServlet {

    private final ReimbursementService reimbursementService = new ReimbursementService();
    private static final Logger LOG = LogManager.getLogger(ReimbursementServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Need a reimbursementId
        String reimbId = req.getParameter("reimbId");
        String typeId = req.getParameter("typeId");
        String statusId = req.getParameter("statusId");

        try{
            if (requester != null){
                //Must be a registered user
                if(requester.getUserRole() >= 1 && requester.getUserRole() <= 3){
                    //Either an Admin or Employee user
                    LOG.info("ReimbursementServlet.doGet() invoked by admin/employee requester {}", requester);
                    if(reimbId != null){
                        LOG.info("Retrieving reimbursement " + reimbId + " for user " + requester.getUsername());
                        Integer i = Integer.parseInt(reimbId);
                        Reimbursement reimbursement = reimbursementService.getReimbByReimbId(requester.getUserId(), i);
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursement);
                        writer.write(reimbursementsJSON);
                    }else if(typeId != null){
                        LOG.info("Retrieving reimbursement of type " + typeId + " for user " + requester.getUsername());
                        Integer i = Integer.parseInt(typeId);
                        List<Reimbursement> reimbursements = reimbursementService.getReimbByAuthorAndType(requester.getUserId(), i);
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                        writer.write(reimbursementsJSON);
                    }else if(statusId != null){
                        LOG.info("Retrieving reimbursement of status " + statusId + " for user " + requester.getUsername());
                        Integer i = Integer.parseInt(statusId);
                        List<Reimbursement> reimbursements = reimbursementService.getReimbByAuthorAndStatus(requester.getUserId(), i);
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                        writer.write(reimbursementsJSON);
                    } else{
                        LOG.info("Retrieving all reimbursements for user " + requester.getUsername());
                        List<Reimbursement> reimbursements = reimbursementService.getReimbByUserId(requester.getUserId());
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                        writer.write(reimbursementsJSON);
                    }
                }else{
                    //User is deleted
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    resp.setStatus(403);
                }
            }else{
                //User got past login or using invalidated session
                LOG.warn("Unauthorized request made by unknown requester");
                resp.setStatus(401);
            }

        }catch(Exception e){
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
