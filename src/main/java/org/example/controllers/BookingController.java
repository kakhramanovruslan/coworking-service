package org.example.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.example.dto.Authentication;
import org.example.dto.BookingRequest;
import org.example.entity.Booking;
import org.example.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/workspaces/bookings")
@RequiredArgsConstructor
@Api(value = "Booking controller")
public class BookingController {

    private final BookingService bookingService;

    private final ServletContext servletContext;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Booking workspace", response = Booking.class)
    @PostMapping("/book")
    public ResponseEntity<Booking> bookWorkspace(@RequestBody BookingRequest request) {
        Authentication authentication = (Authentication) servletContext.getAttribute("authentication");
        return ResponseEntity.ok(bookingService.bookWorkspace(request, authentication.getUsername()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list bookings for time period", response = Booking.class)
    @GetMapping("/time-period")
    public ResponseEntity<List<Booking>> getFilteredBookingsByTimePeriod(@RequestParam String startTime, @RequestParam String endTime) {
        return ResponseEntity.ok(bookingService.getFilteredBookingsByTimePeriod(startTime, endTime));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list bookings by username", response = Booking.class)
    @GetMapping("/username")
    public ResponseEntity<List<Booking>> getFilteredBookingsByUsername(@RequestParam String username) {
        return ResponseEntity.ok(bookingService.getFilteredBookingsByUsername(username));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list bookings by workspace name", response = Booking.class)
    @GetMapping("/workspace")
    public ResponseEntity<List<Booking>> getFilteredBookingsByWorkspace(@RequestParam String name) {
        return ResponseEntity.ok(bookingService.getFilteredBookingsByWorkspace(name));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Cancel booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable String id) throws AccessDeniedException {
        bookingService.cancelBook(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }
}
