package org.example.controller;

import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.example.service.BookingService;
import org.example.service.UserService;
import org.example.service.WorkspaceService;

import java.time.LocalDateTime;
import java.util.List;

public class BookingController {
    private static BookingController bookingController = new BookingController();
    private BookingService bookingService = BookingService.getInstance();
    private WorkspaceService workspaceService = WorkspaceService.getInstance();
    private UserService userService = UserService.getInstance();


    public void getAvailableWorkspacesAtNow(){
        List<Workspace> workspaces = bookingService.getAvailableWorkspacesAtNow();
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    public void getAvailableWorkspacesForTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        List<Workspace> workspaces = bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime);
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    public void bookWorkspace(Long workspaceId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        Booking booking = bookingService.bookWorkspace(workspaceId, userId, startTime, endTime);
        if(booking!=null)
            System.out.println("Бронирование прошло успешно!");
        else
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
    }

    public void cancelBook(Long id) {
        if(bookingService.cancelBook(id))
            System.out.println("Бронь была отменена успешно!");
        else
            System.out.println("Упс...что то пошло не так, повторите попытку снова");
    }

    public void getFilteredBookingsByTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> bookings = bookingService.getFilteredBookingsByTimePeriod(startTime, endTime);
        for (Booking booking: bookings) {
            String username = userService.getUserById(booking.getUserId()).get().getUsername();
            String workspaceName = workspaceService.getWorkspace(booking.getWorkspaceId()).get().getName();
            System.out.println("Id: "+booking.getId() + ", Workspace: " + workspaceName + ", Username: "+ username +
                    ", Начало: " + booking.getStartTime() + ", Конец: " + booking.getEndTime());
        }
    }

    public void getFilteredBookingsByUsername(String username) {
        List<Booking> bookings = bookingService.getFilteredBookingsByUsername(username);
        for (Booking booking: bookings) {
            String workspaceName = workspaceService.getWorkspace(booking.getWorkspaceId()).get().getName();
            System.out.println("Id: "+booking.getId() + ", Workspace: " + workspaceName + ", Username: "+ username +
                    ", Начало: " + booking.getStartTime() + ", Конец: " + booking.getEndTime());
        }
    }

    public void getFilteredBookingsByWorkspace(String workspaceName) {
        List<Booking> bookings = bookingService.getFilteredBookingsByWorkspace(workspaceName);
        for (Booking booking: bookings) {
            String username = userService.getUserById(booking.getUserId()).get().getUsername();
            System.out.println("Id: "+booking.getId() + ", Workspace: " + workspaceName + ", Username: "+ username +
                    ", Начало: " + booking.getStartTime() + ", Конец: " + booking.getEndTime());
        }
    }


    public static BookingController getInstance() {
        return bookingController;
    }
}
