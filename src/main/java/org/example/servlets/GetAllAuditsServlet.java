package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.Authentication;
import org.example.dto.ExceptionResponse;
import org.example.entity.Audit;
import org.example.entity.types.Role;
import org.example.service.AuditService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Servlet for retrieving all audit records.
 */
@WebServlet("/admin/audits")
public class GetAllAuditsServlet extends HttpServlet {

    private AuditService auditService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        auditService = (AuditService) getServletContext().getAttribute("auditService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    /**
     * Handles GET requests to retrieve all audits.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request handling
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            isAdmin(req);
            List<Audit> audits = auditService.getAllAudits();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), audits);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Internal server error"));
        }
    }

    private void isAdmin(HttpServletRequest req) throws AccessDeniedException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You do not have permission to access this page.");
        }
    }
}
