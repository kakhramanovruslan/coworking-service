package org.example.controllers;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.example.dto.Authentication;
import org.example.dto.BookingRequest;
import org.example.entity.Booking;
import org.example.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller for managing workspace bookings.
 */
@RestController
@RequestMapping("/workspaces/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final ServletContext servletContext;

    @PostMapping("/book")
    public ResponseEntity<Booking> bookWorkspace(@RequestBody BookingRequest request) {
        Authentication authentication = (Authentication) servletContext.getAttribute("authentication");
        return ResponseEntity.ok(bookingService.bookWorkspace(request, authentication.getUsername()));
    }

    @GetMapping("/time-period")
    public ResponseEntity<List<Booking>> getFilteredBookingsByTimePeriod(@RequestParam String startTime, @RequestParam String endTime) {
        return ResponseEntity.ok(bookingService.getFilteredBookingsByTimePeriod(startTime, endTime));
    }

    @GetMapping("/username")
    public ResponseEntity<List<Booking>> getFilteredBookingsByUsername(@RequestParam String username) {
        return ResponseEntity.ok(bookingService.getFilteredBookingsByUsername(username));
    }

    @GetMapping("/workspace")
    public ResponseEntity<List<Booking>> getFilteredBookingsByWorkspace(@RequestParam String name) {
        return ResponseEntity.ok(bookingService.getFilteredBookingsByWorkspace(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable String id) throws AccessDeniedException {
        bookingService.cancelBook(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }
}
