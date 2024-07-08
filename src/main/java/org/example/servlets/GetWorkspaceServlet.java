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
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.example.service.WorkspaceService;

import java.io.IOException;
import java.util.Optional;

/**
 * Servlet for retrieving a workspace by its ID or name.
 */
@WebServlet("/workspace")
public class GetWorkspaceServlet extends HttpServlet {

    private WorkspaceService workspaceService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        workspaceService = (WorkspaceService) getServletContext().getAttribute("workspaceService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    /**
     * Handles GET requests to retrieve a workspace by its ID or name.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request handling
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String workspaceId = req.getParameter("id");
            String workspaceName = req.getParameter("name");

            isValidRequest(workspaceId, workspaceName);

            Optional<Workspace> workspace;

            if (workspaceId != null) {
                long id = Long.parseLong(workspaceId);
                workspace = workspaceService.getWorkspaceById(id);
            } else if (workspaceName != null) {
                workspace = workspaceService.getWorkspaceByName(workspaceName);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Workspace name or ID is not specified"));
                return;
            }

            if (workspace.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Workspace not found"));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), workspace.get());
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Incorrect format of workspace ID"));
        } catch (WorkspaceNotFoundException | NotValidArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidRequest(String workspaceId, String workspaceName) throws NotValidArgumentException{
        int paramCount = 0;
        if (workspaceId != null) paramCount++;
        if (workspaceName != null) paramCount++;

        if (paramCount > 1) throw new NotValidArgumentException("Only one parameter can be passed: id or name");
        return paramCount == 1;
    }
}
