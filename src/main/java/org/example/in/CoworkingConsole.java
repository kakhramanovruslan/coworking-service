package org.example.in;


import org.example.entity.User;
import org.example.entity.Workspace;
import org.example.handler.AdminHandler;
import org.example.handler.MainHandler;
import org.example.handler.UserHandler;
import org.example.handler.WorkspaceHandler;
import org.example.service.CoworkingService;
import org.example.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CoworkingConsole {

    UserService userService;
    CoworkingService coworkingService;
    static String loggedUsername = null; // username активного пользователя
    static boolean logged = false; // отслеживание авторизации
    Scanner scanner = new Scanner(System.in);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CoworkingConsole() {
        this.userService = new UserService();
        this.coworkingService = new CoworkingService();
    }

    public void start(MainHandler mainHandler, UserHandler userHandler, AdminHandler adminHandler, WorkspaceHandler workspaceHandler) {
        while (true) {
            if (!logged) {
                mainHandler.displayMainMenu();
                int choice = userHandler.readChoice();
                switch (choice) {
                    case 1:
                        System.out.print("Введите имя пользователя: ");
                        String username = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String password = scanner.nextLine();
                        userService.register(username, password);
                        break;
                    case 2:
                        System.out.print("Введите имя пользователя: ");
                        String authenticateUsername = scanner.nextLine();
                        System.out.print("Введите пароль: ");
                        String authenticatePassword = scanner.nextLine();
                        if (userService.authenticate(authenticateUsername, authenticatePassword)) {
                            logged = true;
                            loggedUsername = authenticateUsername;
                        }
                        break;
                    case 3:
                        mainHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if ("admin".equals(loggedUsername)) {
                adminHandler.displayAdminMenu();
                int choice = userHandler.readChoice();
                switch (choice) {
                    case 1:
                        workspaceHandler.displayListOfAllWorkspaces(coworkingService.getListOfAllWorkSpaces());
                        break;
                    case 2:
                        workspaceHandler.displayWorkspace(coworkingService, userHandler);
                        break;
                    case 3:
                        workspaceHandler.createWorkspaceMenu(coworkingService, userHandler);
                        break;
                    case 4:
                        workspaceHandler.updateWorkspaceMenu(coworkingService, userHandler);
                        break;
                    case 5:
                        workspaceHandler.deleteWorkspaceMenu(coworkingService, userHandler);
                        break;
                    case 6:
                        mainHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }
            } else if (logged) {
                userHandler.displayUserMenu();
                int choice = userHandler.readChoice();
                switch (choice) {
                    case 1:
                        LocalDateTime currentTime = LocalDateTime.now();
                        List<Workspace> availableWorkspaceNames = coworkingService.getAvailableWorkspaceNames(currentTime);
                        availableWorkspaceNames.stream().forEach(System.out::println);
                        break;
                    case 2:
                        System.out.println("Введите начало и конец интересующего промежутка времени в формате: 2024-06-21 11:30 2024-06-21 12:30");
                        String[] dateTimeString = userHandler.scanner.nextLine().split(" ");
                        LocalDateTime sT = LocalDateTime.parse(dateTimeString[0]+" "+dateTimeString[1], formatter);
                        LocalDateTime eT= LocalDateTime.parse(dateTimeString[2]+" "+dateTimeString[3], formatter);

                        List<Workspace> availableWorkspaceNamesInConcreteDate = coworkingService.getAvailableWorkspaceInConcreteDate(sT, eT);
                        availableWorkspaceNamesInConcreteDate.stream().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("Заполните данные для бронирования в формате приведенном ниже: ");
                        System.out.println("workspaceName startTime endTime");
                        String[] row = userHandler.scanner.nextLine().split(" ");
                        LocalDateTime startTime = LocalDateTime.parse(row[1]+" "+row[2], formatter);
                        LocalDateTime endTime = LocalDateTime.parse(row[3]+" "+row[4], formatter);
                        coworkingService.bookWorkspace(row[0], startTime, endTime, loggedUsername);
                        break;
                    case 4:
                        System.out.println("Введите идентификатор бронирования для его отмены: ");
                        Long id = Long.parseLong(userHandler.scanner.nextLine());
                        if(coworkingService.cancelBook(id)){
                            System.out.println("Бронь была отменена");
                        }else{
                            System.out.println("Бронь с таким идентификатором не существует, попробуйте снова");
                        };
                        break;
                    case 5:
                        System.out.println("Просмотр всех бронирований и их фильтрация по:  дате, пользователю или workspace: ");
                        System.out.println("1. Дате");
                        System.out.println("2. Имени пользователя");
                        System.out.println("3. По имени workspace");
                        System.out.println("Введите в формате: ");
                        System.out.println("1 2024-06-21 11:30");
                        System.out.println("2 rus");
                        System.out.println("3 first");
                        String[] c = userHandler.scanner.nextLine().split(" ");
                        if(c[0].equals("1")){
                            coworkingService.filterBookingsByDate(c[1]+" "+c[2]).stream().forEach(System.out::println);
                        } else if(c[0].equals("2")){
                            coworkingService.filterBookingsByUser(c[1]).stream().forEach(System.out::println);
                        } else if(c[0].equals("3")){
                            coworkingService.filterBookingsByWorkspace(c[1]).stream().forEach(System.out::println);
                        }
                        break;
                    case 6:
                        mainHandler.exitApplication();
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
                }

            }

        }
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
