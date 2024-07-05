package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.AuthRequest;
import org.example.dto.ExceptionResponse;
import org.example.dto.TokenResponse;
import org.example.exceptions.AuthenticationException;
import org.example.service.SecurityService;

import java.io.IOException;

@WebServlet("/auth/login")
public class AuthenticationServlet extends HttpServlet {

    private SecurityService securityService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            AuthRequest request = objectMapper.readValue(req.getInputStream(), AuthRequest.class);
            TokenResponse token = securityService.authenticate(request.username(), request.password());

            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            objectMapper.writeValue(resp.getWriter(), token);
        } catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}