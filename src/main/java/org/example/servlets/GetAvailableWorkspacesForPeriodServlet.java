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
import org.example.service.BookingService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Servlet for retrieving available workspaces for a specified time period.
 */
@WebServlet("/workspaces/available-for-period")
public class GetAvailableWorkspacesForPeriodServlet extends HttpServlet {

    private BookingService bookingService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    /**
     * Handles GET requests to retrieve available workspaces for the specified time period.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request handling
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String startTimeStr = req.getParameter("endTime");
            String endTimeStr = req.getParameter("startTime");

            if(startTimeStr.isBlank() || endTimeStr.isBlank()) throw new NotValidArgumentException("Invalid request format");

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

            List<Workspace> workspaces = bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime);
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), workspaces);
        } catch (NotValidArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (DateTimeParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Invalid date and time format"));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}