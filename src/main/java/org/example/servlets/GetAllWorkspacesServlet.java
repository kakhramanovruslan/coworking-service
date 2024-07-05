package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExceptionResponse;
import org.example.entity.Workspace;
import org.example.service.WorkspaceService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

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