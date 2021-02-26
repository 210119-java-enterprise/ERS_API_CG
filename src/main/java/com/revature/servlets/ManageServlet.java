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

/**
 * This class is a servlet intended to handle all financial manager
 * updates to the reimbursements within the database. Only users
 * that are finincial managers are able to hit this endpoint
 * <p>Endpoint : /manage</p>
 * @author Cole Space
 * @author Gabrielle Luna
 */
@WebServlet(name = "manage", displayName = "manage", urlPatterns = "/manage/*")
public class ManageServlet extends HttpServlet {
    private final ReimbursementService reimbursementService = new ReimbursementService();
    private static final Logger LOG = LogManager.getLogger(ReimbursementServlet.class);

    /**
     * The Get HTTP verb, this will list all or a select amount of reimbursements for
     * all users across the web application
     * @param req the user request
     * @param resp the response back to the user
     * @throws ServletException e
     * @throws IOException e
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //View all reimbursements, allow filter by type, or view by id
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Need a reimbursementId
        String reimbId = req.getParameter("reimbId");
        String typeId = req.getParameter("typeId");
        String statusId = req.getParameter("statusId");

        try {
            if (requester != null) {
                if (requester.getUserRole().compareTo(2) == 0) {
                    //Must be Finance Manager
                    LOG.info("ReimbursementServlet.doGet() invoked by financial manager requester {}", requester);
                    if (reimbId == null) {
                        if (typeId != null) {
                            //print all reimbursements by type id
                            LOG.info("Retrieving all reimbursements with type id " + typeId);
                            Integer i = Integer.parseInt(typeId);
                            List<Reimbursement> reimbursements = reimbursementService.getReimbByType(i);
                            String usersJSON = mapper.writeValueAsString(reimbursements);
                            writer.write(usersJSON);
                        } else if (statusId != null) {
                            //print all reimbursements by status id
                            LOG.info("Retrieving all reimbursements with status id " + statusId);
                            Integer i = Integer.parseInt(statusId);
                            List<Reimbursement> reimbursements = reimbursementService.getReimbByStatus(i);
                            String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                            writer.write(reimbursementsJSON);
                        } else {
                            //print all reimbursements
                            LOG.info("Retrieving all reimbursements");
                            List<Reimbursement> reimbursements = reimbursementService.getAllReimb();
                            String reimbursementsJSON = mapper.writeValueAsString(reimbursements);
                            writer.write(reimbursementsJSON);
                        }
                    } else {
                        //print specific reimbursement
                        LOG.info("Retrieving all reimbursements with reimbursement id " + reimbId);
                        Integer i = Integer.parseInt(reimbId);
                        Reimbursement reimbursement = reimbursementService.getReimbByReimbId(i);
                        String reimbursementsJSON = mapper.writeValueAsString(reimbursement);
                        writer.write(reimbursementsJSON);
                    }

                }else{
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    writer.write("You lack the proper authorization to reach this endpoint. This insubordination has been reported to the authorities");
                    resp.setStatus(403);
                }
            } else {
                //User got past login or using invalidated session
                LOG.warn("Unauthorized request made by unknown requester");
                writer.write("You are an unknown user. Who are you?");
                resp.setStatus(401);
            }
        }catch(Exception e){
            LOG.error(e.getMessage());
            writer.write("Internal server error, sorry about that! Give us one moment to fix this, we strive to provide excellent service");
            resp.setStatus(500);
        }

    }


    /**
     * The Put HTTP verb, this will allow for a financial manager to update
     * the status of a specific reimbursement
     * @param req the user request
     * @param resp the response back to the user
     * @throws ServletException e
     * @throws IOException e
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Update Reimbursement Status
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        //Need a reimbursementId
        String reimbId = req.getParameter("reimbId");
        String statusId = req.getParameter("statusId");

        try {
            if (requester != null) {
                if (requester.getUserRole().compareTo(2) == 0) {
                    //Must be Finance Manager
                    LOG.info("ReimbursementServlet.doGet() invoked by financial manager requester {}", requester);
                    if (reimbId != null) {
                        if (statusId != null) {
                            //print all reimbursements by status id
                            LOG.info("Updating reimbursement " + reimbId + " with status id " + statusId);
                            Integer r = Integer.parseInt(reimbId);

                            if(statusId.equals("2")){
                                //approve
                                reimbursementService.approve(requester.getUserId(), r);
                                writer.write("Successfully approved");
                            }else if(statusId.equals("3")){
                                //deny
                                reimbursementService.deny(requester.getUserId(), r);
                                writer.write("Successfully denied");
                            }else{
                                LOG.warn("Request made by requester, {}, incorrect reimbursement status id", requester.getUsername());
                                writer.write("The reimbursement status id provided doesn't match any on record, try again");
                                resp.setStatus(400);
                            }
                        } else {
                            LOG.warn("Request made by requester, {}, did not specify reimbursement status id", requester.getUsername());
                            writer.write("You need to provide a reimbursement status id if you want to approve or deny a reimbursement");
                            resp.setStatus(400);
                        }
                    } else {
                        LOG.warn("Request made by requester, {}, did not specify reimbursement id", requester.getUsername());
                        writer.write("Please provide a reimbursement id, since you cannot update what isn't specified");
                        resp.setStatus(400);
                    }

                }else{
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    writer.write("You lack the proper authorization to reach this endpoint. This insubordination has been reported to the authorities");
                    resp.setStatus(403);
                }
            } else {
                //User got past login or using invalidated session
                LOG.warn("Unauthorized request made by unknown requester");
                writer.write("You are an unknown user. Who are you?");
                resp.setStatus(401);
            }
        }catch(Exception e){
            LOG.error(e.getMessage());
            writer.write("Internal server error, sorry about that! Give us one moment to fix this, we strive to provide excellent service");
            resp.setStatus(500);
        }
    }
}
