package org.example.controller;

import org.example.service.UserService;

public class UserController {

    private static UserController userController = new UserController();
    private UserService userService = UserService.getInstance();

    public void register(String username, String password){
        if(userService.register(username, password))
            System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
        else
            System.out.println("Пользователь с именем " + username + " уже существует. Повторите попытку регистрации еще раз!");
    }

    public void authenticate(String username, String password){
        if(userService.authenticate(username, password))
            System.out.println("Вы успешно вошли в систему.");
        else
            System.out.println("Ошибка авторизации. Пожалуйста, проверьте имя пользователя или пароль.");
    }

    public static UserController getInstance() {
        return userController;
    }
}
