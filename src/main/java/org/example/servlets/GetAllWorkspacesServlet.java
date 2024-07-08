package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.Workspace;
import org.example.service.WorkspaceService;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for retrieving all workspaces.
 */
@WebServlet("/workspaces")
public class GetAllWorkspacesServlet extends HttpServlet {

    private WorkspaceService workspaceService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        workspaceService = (WorkspaceService) getServletContext().getAttribute("workspaceService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    /**
     * Handles GET requests to retrieve all workspaces.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request handling
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Workspace> workspaces = workspaceService.getListOfAllWorkSpaces();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), workspaces);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}