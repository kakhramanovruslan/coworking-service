package org.example.service;

import org.example.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private Map<String, User> users;

    public UserService(){
        this.users = new HashMap<>();
        users.put("admin", new User("admin", "admin"));
    }

    /**
     * Регистрация нового пользователя с указанным именем и паролем.
     *
     * @return true, если регистрация успешно выполнена, иначе false.
     */

    public boolean register(String username, String password){
        if (!users.containsKey(username)) {
            User newUser = new User(username, password);
            users.put(username, newUser);
            System.out.println("Привет, " + username + ". Вы успешно зарегистрировались.");
            return true;
        } else {
            System.out.println("Пользователь с именем " + username + " уже существует. Повторите попытку регистрации еще раз!");
            return false;
        }
    }

    /**
     * Метод аутентифицирует пользователя по имени и паролю.
     *
     * @return true, если аутентификация успешно выполнена, иначе false.
     */

    public boolean authenticate(String username, String password){
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Вы успешно вошли в систему.");
            return true;
        } else {
            System.out.println("Ошибка авторизации. Пожалуйста, проверьте имя пользователя или пароль.");
            return false;
        }
    }
}
