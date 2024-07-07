package org.example.servlets;

import com.fasterxml.jackson.core.JsonParseException;
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
import org.example.dto.AuthRequest;
import org.example.dto.ExceptionResponse;
import org.example.dto.SuccessResponse;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.RegisterException;
import org.example.service.SecurityService;
import org.example.utils.PasswordUtil;

import java.io.IOException;
import java.util.Set;

@WebServlet("/auth/registration")
public class RegistrationServlet extends HttpServlet {

    private SecurityService securityService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        securityService = (SecurityService) getServletContext().getAttribute("securityService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            AuthRequest request = objectMapper.readValue(req.getInputStream(), AuthRequest.class);

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<AuthRequest>> violations = validator.validate(request);
            for (ConstraintViolation<AuthRequest> violation : violations) {
                throw new NotValidArgumentException(violation.getMessage());
            }

            UserDTO registeredUser = securityService.register(request.username(), request.password());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), new SuccessResponse("Player with login " + registeredUser.getUsername() + " successfully created."));
        } catch (NotValidArgumentException | JsonParseException | RegisterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        super.doPost(req, resp);
    }
}