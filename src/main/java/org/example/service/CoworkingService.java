package org.example.service;

import org.example.entity.Booking;
import org.example.entity.Workspace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CoworkingService {

    public List<Workspace> listOfAllWorkspaces;
    public List<Booking> listOfAllBookings;

    public CoworkingService() {
        this.listOfAllWorkspaces = new ArrayList<>();
        this.listOfAllBookings = new ArrayList<>();
//        listOfAllWorkspaces.add(new Workspace("first"));
//        listOfAllWorkspaces.add(new Workspace("second"));
//        listOfAllWorkspaces.add(new Workspace("third"));
//        String startTimeString = "2024-06-21 11:30";
//        String endTimeString = "2024-06-21 12:30";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

//        LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);
//        LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);

//        Booking booking = new Booking("first", startTime, endTime, "rus");
//        listOfAllBookings.add(booking);
    }

    public List<Workspace> getListOfAllWorkSpaces() {
        return this.listOfAllWorkspaces;
    }

    public boolean createWorkspace(String name) {
        Workspace newWorkspace = new Workspace(name);
        if(listOfAllWorkspaces.add(newWorkspace)){
            System.out.println("Идентификатор "+name+": "+newWorkspace.getId());
            return true;
        }
        return false;
    }

    public boolean deleteWorkspace(Long id) {
        return listOfAllWorkspaces.removeIf(workspace -> workspace.getId() == id);
    }

    public boolean deleteWorkspace(String name) {
        return listOfAllWorkspaces.removeIf(workspace -> workspace.getName().equals(name));
    }

    public boolean updateWorkspace(Long id, String name) {
        for (Workspace workspace : listOfAllWorkspaces) {
            if (workspace.getId().equals(id)) {
                workspace.setName(name);
                return true;
            }
        }
        return false;
    }

    public Workspace getWorkspace(Long id) {
        Optional<Workspace> workspace = listOfAllWorkspaces.stream().filter(w -> w.getId() == id).findFirst();
        if (workspace.isEmpty()) return null;
        return workspace.get();
    }

    public Workspace getWorkspace(String workspaceName) {
        Optional<Workspace> workspace = listOfAllWorkspaces.stream().filter(w -> w.getName().equals(workspaceName)).findFirst();
        if (workspace.isEmpty()) return null;
        return workspace.get();
    }

    public List<Workspace> getAvailableWorkspaceNames(LocalDateTime currentTime) {
        List<String> bookedWorkspaceNames = listOfAllBookings.stream()
                .filter(booking -> booking.getStartTime().isBefore(currentTime) && booking.getEndTime().isAfter(currentTime))
                .map(Booking::getWorkspaceName)
                .collect(Collectors.toList());

        return listOfAllWorkspaces.stream()
                .filter(workspace -> !bookedWorkspaceNames.contains(workspace.getName()))
                .collect(Collectors.toList());
    }

    public List<Workspace> getAvailableWorkspaceInConcreteDate(LocalDateTime startTime, LocalDateTime endTime) {
        // Получаем список рабочих мест, забронированных в указанное время
        List<String> occupiedWorkspaceIds = listOfAllBookings.stream()
                .filter(booking -> isTimeConflict(booking, startTime, endTime))
                .map(Booking::getWorkspaceName)
                .collect(Collectors.toList());

        return listOfAllWorkspaces.stream()
                .filter(workspace -> !occupiedWorkspaceIds.contains(workspace.getName()))
                .collect(Collectors.toList());
    }

    private boolean isBookingOverlapping(Booking booking, LocalDateTime dateTime) {
        return !dateTime.isBefore(booking.getStartTime()) && !dateTime.isAfter(booking.getEndTime());
    }

    public boolean bookWorkspace(String workspaceName, LocalDateTime startTime, LocalDateTime endTime, String username) {
        // Проверяем, существует ли рабочее место
        boolean workspaceExists = listOfAllWorkspaces.stream()
                .anyMatch(workspace -> workspace.getName().equals(workspaceName));

        if (!workspaceExists) {
            System.out.println("Workspace с именем " + workspaceName + " не существует.");
            return false;
        }

        // Проверяем, доступно ли рабочее место на указанный интервал времени
        boolean isAvailable = listOfAllBookings.stream()
                .filter(booking -> booking.getWorkspaceName().equals(workspaceName))
                .noneMatch(booking -> isTimeConflict(booking, startTime, endTime));

        if (isAvailable) {
            // Создаём новое бронирование
            Booking newBooking = new Booking(workspaceName, startTime, endTime, username);
            listOfAllBookings.add(newBooking);
            System.out.println("Workspace " + workspaceName + " забронирован. Идентификатор бронирования: "+newBooking.getId());
            return true;
        } else {
            System.out.println("Workspace " + workspaceName + " к сожалению занят.");
            return false;
        }
    }


    private boolean isTimeConflict(Booking booking, LocalDateTime startTime, LocalDateTime endTime) {
        return (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime()));
    }

    public boolean cancelBook(Long id) {
        Optional<Booking> bookingOptional = listOfAllBookings.stream()
                .filter(booking -> booking.getId() == id)
                .findFirst();

        if (bookingOptional.isPresent()) {
            listOfAllBookings.remove(bookingOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // Метод для фильтрации бронирований по дате
    public List<Booking> filterBookingsByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return listOfAllBookings.stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(dateTime.toLocalDate()))
                .collect(Collectors.toList());
    }

    // Метод для фильтрации бронирований по пользователю
    public List<Booking> filterBookingsByUser(String username) {
        return listOfAllBookings.stream()
                .filter(booking -> booking.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // Метод для фильтрации бронирований по рабочему месту
    public List<Booking> filterBookingsByWorkspace(String workspaceName) {
        return listOfAllBookings.stream()
                .filter(booking -> booking.getWorkspaceName().equals(workspaceName))
                .collect(Collectors.toList());
    }
}
