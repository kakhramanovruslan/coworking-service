package org.example.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ExceptionResponse;
import org.example.entity.Booking;
import org.example.exceptions.NotValidArgumentException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WorkspaceNotFoundException;
import org.example.service.BookingService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servlet for retrieving bookings based on various filter parameters.
 */
@WebServlet("/workspaces/bookings")
public class GetAllBookingsServlet extends HttpServlet {
    private BookingService bookingService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    /**
     * Handles GET requests for retrieving bookings based on specified parameters.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request handling
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String startTimeStr = req.getParameter("startTime");
            String endTimeStr = req.getParameter("endTime");
            String username = req.getParameter("username");
            String workspaceName = req.getParameter("name");

            isValidRequest(startTimeStr, endTimeStr, username, workspaceName);

            List<Booking> bookings;
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            if (startTimeStr != null && endTimeStr != null) {
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
                bookings = bookingService.getFilteredBookingsByTimePeriod(startTime, endTime);
            } else if (username != null) {
                bookings = bookingService.getFilteredBookingsByUsername(username);
            } else if (workspaceName != null) {
                bookings = bookingService.getFilteredBookingsByWorkspace(workspaceName);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Parameters of the request are not specified."));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), bookings);

        } catch (UserNotFoundException | WorkspaceNotFoundException | NotValidArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Validates the request parameters to ensure only one filter parameter is provided.
     *
     * @param startTime    the start time parameter
     * @param endTime      the end time parameter
     * @param username     the username parameter
     * @param workspaceName the workspace name parameter
     * @return true if the request is valid, false otherwise
     * @throws NotValidArgumentException if more than one parameter is provided
     */
    private boolean isValidRequest(String startTime, String endTime, String username, String workspaceName) throws NotValidArgumentException {
        int paramCount = 0;
        if (startTime != null && endTime != null) paramCount++;
        if (username != null) paramCount++;
        if (workspaceName != null) paramCount++;

        if (paramCount > 1) throw new NotValidArgumentException("You can only pass one parameter: startTime and endTime, username, or name.");
        return paramCount == 1;
    }
}

