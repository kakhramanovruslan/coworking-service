package org.example.handler;

import org.example.entity.Booking;
import org.example.entity.Workspace;
import org.example.in.CoworkingConsole;
import org.example.service.BookingService;
import org.example.service.UserService;
import org.example.service.WorkspaceService;
import org.example.utils.ScannerManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The UserHandler class is a handler for interacting with the user menu of the
 * application console interface. It provides functionality related to the use of the workspaces.
 */
public class UserHandler {

    private final Scanner scanner = ScannerManager.getInstance().scanner;
    private UserService userService = UserService.getInstance();
    private WorkspaceService workspaceService = WorkspaceService.getInstance();
    private BookingService bookingService = BookingService.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Displays the menu for the user in the console.
     * The menu includes a list of available actions for the user.
     */
    public void displayUserMenu() {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║       Выберите действие:                                                ║");
        System.out.println("║  1. Cписок доступных рабочих мест и конференц-залов                     ║");
        System.out.println("║  2. Доступные слоты на определенную дату                                ║");
        System.out.println("║  3. Бронирование рабочего места или конференц-зала                      ║");
        System.out.println("║  4. Отмена бронирования                                                 ║");
        System.out.println("║  5. Просмотр всех бронирований                                          ║");
        System.out.println("║  6. Выйти из аккаунта                                                   ║");
        System.out.println("║  7. Выйти из приложения                                                 ║");
        System.out.println("╚═════════════════════════════════════════════════════════════════════════╝");

    }

    /**
     * Displays the registration menu in the console.
     * The user enters the username and password to register.
     */
    public void displayRegistration(){
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        if(userService.register(username, password))
            System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
        else
            System.out.println("Пользователь с именем " + username + " уже существует. Повторите попытку регистрации еще раз!");
    }

    /**
     * Displays the authentication menu in the console.
     * The user enters the username and password to authenticate.
     */
    public void displayAuthentication(){
        System.out.print("Введите имя пользователя: ");
        String authUsername = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String authPassword = scanner.nextLine();
        if (userService.authenticate(authUsername, authPassword)) {
            System.out.println("Привет, " + authUsername + ". Вы успешно авторизовались.");
            CoworkingConsole.logged = true;
            CoworkingConsole.loggedUsername = authUsername;
        } else
            System.out.println("Вы ввели неправильный логин или пароль.");
    }

    /**
     * Displays the available workspaces menu at now in the console.
     */
    public void displayAvailableWorkspacesAtNow() {
        List<Workspace> workspaces = bookingService.getAvailableWorkspacesAtNow();
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    /**
     * Displays the available workspaces menu for time period in the console.
     * The user enters the time period in format: yyyy-MM-dd HH:mm yyyy-MM-dd HH:mm.
     */
    public void displayAvailableWorkspacesForTimePeriod(){
        System.out.println("Введите период интересующего промежутка времени в формате: 2024-06-21 11:30 2024-06-21 12:30");
        String[] dateTimeString = scanner.nextLine().split(" ");
        LocalDateTime startTime = LocalDateTime.parse(dateTimeString[0]+" "+dateTimeString[1], formatter);
        LocalDateTime endTime = LocalDateTime.parse(dateTimeString[2]+" "+dateTimeString[3], formatter);

        List<Workspace> workspaces = bookingService.getAvailableWorkspacesForTimePeriod(startTime, endTime);
        for (Workspace workspace : workspaces) {
            System.out.println("Id: "+workspace.getId() + ", Name: " + workspace.getName());
        }
    }

    /**
     * Displays menu for booking workspace in the console.
     * The user enters the workspace name and time period in format: workspaceName yyyy-MM-dd HH:mm yyyy-MM-dd HH:mm.
     */
    public void displayBookingWorkspace() {
        try {
            System.out.println("Заполните данные для бронирования в формате приведенном ниже: ");
            System.out.println("someWorkspaceName 2024-06-21 11:30 2024-06-21 12:30");
            String[] row = scanner.nextLine().split(" ");
            String workspaceName = row[0];
            LocalDateTime startTime = LocalDateTime.parse(row[1]+" "+row[2], formatter);
            LocalDateTime endTime = LocalDateTime.parse(row[3]+" "+row[4], formatter);

            Booking booking = bookingService.bookWorkspace(workspaceName, CoworkingConsole.loggedUsername, startTime, endTime);
            if (booking != null)
                System.out.println("Бронирование прошло успешно!");
            else
                System.out.println("К сожалению, workspace на это время уже забронирован.");
        } catch (NoSuchElementException e){
            System.out.println("Workspace с таким именем не найден. ");
        }
    }

    /**
     * Displays menu for deleting workspace in the console.
     * The user enters the booking number from the list of all their bookings.
     */
    public void displayDeletingWorkspace(){
        List<Booking> bookings = bookingService.getFilteredBookingsByUsername(CoworkingConsole.loggedUsername);
        System.out.println(CoworkingConsole.loggedUsername);
        int i = -1;
        for (Booking booking : bookings) {
            i++;
            String workspaceName = workspaceService.getWorkspace(booking.getWorkspaceId()).get().getName();

            System.out.println("№: " + i +", Workspace: " + workspaceName + ", Username: "+ CoworkingConsole.loggedUsername +
                    ", Начало: " + booking.getStartTime() + ", Конец: " + booking.getEndTime());

        }
        System.out.println("Введите порядковый номер брони по вышеперечисленному списку для его отмены: ");
        Integer id = Integer.parseInt(scanner.nextLine());
        Booking booking = bookings.get(id);
        if(bookingService.cancelBook(booking.getId()))
            System.out.println("Бронь успешно была отменена");
        else
            System.out.println("Бронь с таким идентификатором не существует, попробуйте снова");

    }

    /**
     * Displays menu for getting all bookings in the console.
     * The user enters the options of getting list bookings.
     * There are three filtering options:
     * 1) by time period
     * 2) by username
     * 3) by workspace name.
     */
    public void displayAllBookings(){
        System.out.println("Просмотр всех бронирований и их фильтрация по: ");
        System.out.println("1. Промежутку времени");
        System.out.println("2. Имени пользователя");
        System.out.println("3. По имени workspace");
        System.out.println("Введите в формате: ");
        System.out.println("1 2024-06-21 11:30 2024-06-21 13:30");
        System.out.println("2 someUsername");
        System.out.println("3 someWorkspaceName");
        String[] row = scanner.nextLine().split(" ");
        List<Booking> bookings = null;
        if(row[0].equals("1")){
            LocalDateTime startTime = LocalDateTime.parse(row[1]+" "+row[2], formatter);
            LocalDateTime endTime = LocalDateTime.parse(row[3]+" "+row[4], formatter);
            bookings = bookingService.getFilteredBookingsByTimePeriod(startTime, endTime);
        } else if(row[0].equals("2")){
            bookings = bookingService.getFilteredBookingsByUsername(row[1]);
        } else if(row[0].equals("3")){
            bookings =bookingService.getFilteredBookingsByWorkspace(row[1]);
        }
        for (Booking booking : bookings) {
            String username = userService.getUser(booking.getUserId()).get().getUsername();
            String workspaceName = workspaceService.getWorkspace(booking.getWorkspaceId()).get().getName();

            System.out.println("Id: "+booking.getId() + ", Workspace: " + workspaceName + ", Username: "+ username +
                    ", Начало: " + booking.getStartTime() + ", Конец: " + booking.getEndTime());

        }

    }

    public void logout(){
        CoworkingConsole.loggedUsername = null;
        CoworkingConsole.logged = false;
        System.out.println("Выход из аккаунта.");
    }


}
