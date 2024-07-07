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
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Не указаны параметры запроса"));
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

    private boolean isValidRequest(String startTime, String endTime, String username, String workspaceName) throws NotValidArgumentException {
        int paramCount = 0;
        if (startTime != null && endTime != null) paramCount++;
        if (username != null) paramCount++;
        if (workspaceName != null) paramCount++;

        if (paramCount > 1) throw new NotValidArgumentException("Можно передавать только один параметр: startTime и endTime, username или name");
        return paramCount == 1;
    }
}

