package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.dto.Authentication;
import org.example.dto.BookingRequest;
import org.example.dto.ExceptionResponse;
import org.example.dto.WorkspaceRequest;
import org.example.entity.Workspace;
import org.example.entity.types.Role;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.WorkspaceAlreadyExistException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.example.service.WorkspaceService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Set;

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
            isAdmin(req);

            WorkspaceRequest newWorkspace = objectMapper.readValue(req.getReader(), WorkspaceRequest.class);

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<WorkspaceRequest>> violations = validator.validate(newWorkspace);
            for (ConstraintViolation<WorkspaceRequest> violation : violations) {
                throw new NotValidArgumentException(violation.getMessage());
            }

            Workspace createdWorkspace = workspaceService.createWorkspace(newWorkspace);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), createdWorkspace);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (NotValidArgumentException | WorkspaceAlreadyExistException e){
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
            isAdmin(req);

            String workspaceName = req.getParameter("name");
            if (workspaceName == null || workspaceName.isEmpty())
                throw new NotValidArgumentException("Не указано имя рабочего пространства");

            Workspace updatedWorkspace = objectMapper.readValue(req.getReader(), Workspace.class);

            workspaceService.updateWorkspace(workspaceName, updatedWorkspace);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (NotValidArgumentException | WorkspaceAlreadyExistException | WorkspaceNotFoundException e) {
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
            isAdmin(req);

            String name = req.getParameter("name");
            if (name == null || name.isEmpty()) throw new NotValidArgumentException("Не указано имя рабочего пространства");

            workspaceService.deleteWorkspace(name);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (NotValidArgumentException | WorkspaceNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    private void isAdmin(HttpServletRequest req) throws AccessDeniedException {
        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
        if (authentication.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("У вас нет разрешения на доступ к этой странице");
        }
    }
}

