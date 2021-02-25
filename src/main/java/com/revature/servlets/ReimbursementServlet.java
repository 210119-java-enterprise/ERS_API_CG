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
                if(requester.getUserRole().compareTo(2) == 0) {
                    //Must be Finance Manager
                    LOG.info("ReimbursementServlet.doGet() invoked by financial manager requester {}", requester);
                    if(reimbId == null){
                        if(typeId != null){
                            //print all reimbursements by type id
                            LOG.info("Retrieving all reimbursements with type id " + typeId);
                            Integer i = Integer.parseInt(typeId);
                            List<Reimbursement> reimbursements = reimbursementService.getReimbByType(i);
                            String usersJSON = mapper.writeValueAsString(reimbursements);
                            writer.write(usersJSON);
                        }else if(statusId != null){
                            //print all reimbursements by status id
                            LOG.info("Retrieving all reimbursements with status id " + statusId);
                            Integer i = Integer.parseInt(statusId);
                            List<Reimbursement> reimbursements = reimbursementService.getReimbByStatus(i);
                            String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                            writer.write(reimbursementsJSON);
                        }else{
                            //print all reimbursements
                            LOG.info("Retrieving all reimbursements");
                            List<Reimbursement> reimbursements = reimbursementService.getAllReimb();
                            String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                            writer.write(reimbursementsJSON);
                        }
                    }else{
                        //print specific reimbursement
                        LOG.info("Retrieving all reimbursements with reimbursement id " + reimbId);
                        Integer i = Integer.parseInt(reimbId);
                        Reimbursement reimbursement = reimbursementService.getReimbByReimbId(i);
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursement);
                        writer.write(reimbursementsJSON);
                    }

                }else if(requester.getUserRole().compareTo(3) == 0 || requester.getUserRole().compareTo(1) == 0){
                    //Either an Admin or Employee user
                    LOG.info("ReimbursementServlet.doGet() invoked by admin/employee requester {}", requester);
                    if(reimbId != null){
                        LOG.info("Retrieving reimbursement " + reimbId + " for user " + requester.getUsername());
                        Integer i = Integer.parseInt(reimbId);
                        Reimbursement reimbursement = reimbursementService.getReimbByReimbId(i);
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursement);
                        writer.write(reimbursementsJSON);
                    }else{
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
