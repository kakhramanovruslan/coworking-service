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
import org.example.dto.WorkspaceDTO;
import org.example.entity.Workspace;
import org.example.entity.types.Role;
import org.example.exceptions.WorkspaceAlreadyExist;
import org.example.exceptions.WorkspaceNotFoundException;
import org.example.service.WorkspaceService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@WebServlet("/admin/workspaces")
public class WorkspaceManagementServlet extends HttpServlet {

    private WorkspaceService workspaceService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        workspaceService = (WorkspaceService) getServletContext().getAttribute("workspaceService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
            if (authentication.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("У вас нет разрешения на доступ к этой странице");
            }

            Workspace newWorkspace = objectMapper.readValue(req.getReader(), Workspace.class);

            Workspace createdWorkspace = workspaceService.createWorkspace(newWorkspace);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), createdWorkspace);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (WorkspaceAlreadyExist e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
            if (authentication.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("У вас нет разрешения на доступ к этой странице");
            }

            String workspaceName = req.getParameter("name");
            if (workspaceName == null || workspaceName.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Не указано имя рабочего пространства"));
                return;
            }

            Workspace updatedWorkspace = objectMapper.readValue(req.getReader(), Workspace.class);

            workspaceService.updateWorkspace(workspaceName, updatedWorkspace);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (WorkspaceNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (WorkspaceAlreadyExist e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
            if (authentication.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("У вас нет разрешения на доступ к этой странице");
            }

            String name = req.getParameter("name");
            if (name == null || name.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Не указано имя рабочего пространства"));
                return;
            }

            workspaceService.deleteWorkspace(name);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (WorkspaceNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}

