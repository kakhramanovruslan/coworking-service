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
import java.util.Enumeration;
import java.util.Optional;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String workspaceId = req.getParameter("id");
            String workspaceName = req.getParameter("name");

            Optional<Workspace> workspace;

            if(workspaceId != null && workspaceName != null){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Некорректный формат запроса"));
                return;
            } else if (workspaceId != null) {
                try {
                    long id = Long.parseLong(workspaceId);
                    workspace = workspaceService.getWorkspaceById(id);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Некорректный формат ID рабочего пространства"));
                    return;
                }
            } else if (workspaceName != null) {
                workspace = workspaceService.getWorkspaceByName(workspaceName);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Не указано имя или ID рабочего пространства"));
                return;
            }

            if (workspace.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Рабочее пространство не найдено"));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), workspace.get());
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}