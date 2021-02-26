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
 * This class is a servlet intended to handle all updates that non financial
 * managers are able to do to the reimbursements. Reimbursements can be added
 * or updated given certain criteria
 * <p>Endpoint : /reimbursements</p>
 * @author Cole Space
 * @author Gabrielle Luna
 */
@WebServlet(name = "reimbursements", displayName = "reimbursements", urlPatterns = "/reimbursements/*")
public class ReimbursementServlet extends HttpServlet {

    private final ReimbursementService reimbursementService = new ReimbursementService();
    private static final Logger LOG = LogManager.getLogger(ReimbursementServlet.class);

    /**
     * The Get HTTP verb, this will list all or a select amount of reimbursements for
     * any user that is connected to the web server
     * @param req the user request
     * @param resp the response back to the user
     * @throws ServletException e
     * @throws IOException e
     */
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
                    LOG.info("ReimbursementServlet.doGet() invoked by admin/employee/manager requester {}", requester);
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
                    writer.write("You lack the proper authorization to reach this endpoint. This insubordination has been reported to the authorities");
                    resp.setStatus(403);
                }
            }else{
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
     * The Post HTTP verb, this will create new reimbursements for whichever
     * user is connected to the endpoint on the webserver
     * @param req the user request
     * @param resp the response back to the user
     * @throws ServletException e
     * @throws IOException e
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        try{
            if (requester != null){
                //Must be a registered user
                if(requester.getUserRole() >= 1 && requester.getUserRole() <= 3){
                    //Allowing for any user to create a reimbursement for another user
                    LOG.info("ReimbursementServlet.doPost() invoked by admin/employee/manager requester {}", requester);
                    Reimbursement newReimbursement = mapper.readValue(req.getInputStream(), Reimbursement.class);
                    //No need to worry about inputting time
                    if(reimbursementService.save(newReimbursement)){
                        writer.write("New Reimbursement created");
                        LOG.info("New Reimbursement created");
                    }else{
                        writer.write("Reimbursement could not be saved, please check your input");
                        LOG.warn("Reimbursement could not be saved");
                    }
                }else{
                    //User is deleted
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    writer.write("You lack the proper authorization to reach this endpoint. This insubordination has been reported to the authorities");
                    resp.setStatus(403);
                }
            }else{
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
     * The Put HTTP verb, this will update a specific reimbursement entry for
     * whichever user is connected to the endpoint on the web server
     * @param req the user request
     * @param resp the response back to the user
     * @throws ServletException e
     * @throws IOException e
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doPut(req, resp);
        PrintWriter writer = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = req.getSession(false);
        User requester = (session == null) ? null : (User) req.getSession(false).getAttribute("this-user");
        resp.setContentType("application/json");

        try{
            if (requester != null){
                //Must be a registered user
                if(requester.getUserRole() >= 1 && requester.getUserRole() <= 3){
                    //Allowing for any user to create a reimbursement for another user
                    LOG.info("ReimbursementServlet.doPost() invoked by admin/employee/manager requester {}", requester);
                    Reimbursement newReimbursement = mapper.readValue(req.getInputStream(), Reimbursement.class);
                    //No need to worry about inputting time
                    if(reimbursementService.updateEMP(requester.getUserId(), newReimbursement)){
                        writer.write("Reimbursement updated");
                        LOG.info("New Reimbursement created");
                    }else{
                        writer.write("Reimbursement could not be updated");
                        LOG.warn("Reimbursement could not be saved");
                        resp.setStatus(400);
                    }
                }else{
                    //User is deleted
                    LOG.warn("Request made by requester, {}, who lacks proper authorities", requester.getUsername());
                    writer.write("You lack the proper authorization to reach this endpoint. This insubordination has been reported to the authorities");
                    resp.setStatus(403);
                }
            }else{
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
