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
import org.example.dto.AuthRequest;
import org.example.dto.Authentication;
import org.example.dto.BookingRequest;
import org.example.dto.ExceptionResponse;
import org.example.entity.Booking;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WorkspaceAlreadyBookedException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.example.service.BookingService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Set;

/**
 * Servlet handling workspace booking requests.
 */
@WebServlet("/workspaces/book")
public class BookWorkspaceServlet extends HttpServlet {

    private BookingService bookingService;
    private ObjectMapper objectMapper;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    /**
     * Handles POST requests for booking a workspace.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws ServletException if an error occurs during request handling
     * @throws IOException      if an I/O error occurs during request handling
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Authentication authUserInfo = (Authentication) getServletContext().getAttribute("authentication");

            BookingRequest bookingRequest = objectMapper.readValue(req.getReader(), BookingRequest.class);

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<BookingRequest>> violations = validator.validate(bookingRequest);
            for (ConstraintViolation<BookingRequest> violation : violations) {
                throw new NotValidArgumentException(violation.getMessage());
            }

            Booking createdBooking = bookingService.bookWorkspace(bookingRequest, authUserInfo.getUsername());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), createdBooking);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (NotValidArgumentException | WorkspaceNotFoundException | UserNotFoundException | WorkspaceAlreadyBookedException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}


