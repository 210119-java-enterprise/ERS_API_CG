package com.revature.servlets;

import com.revature.services.ReimbursementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "manage", displayName = "manage", urlPatterns = "/manage/*")
public class ManageServlet extends HttpServlet {
    private final ReimbursementService reimbursementService = new ReimbursementService();
    private static final Logger LOG = LogManager.getLogger(ReimbursementServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //View all reimbursements, allow filter by type, or view by id
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Update Reimbursement Status

    }
}
