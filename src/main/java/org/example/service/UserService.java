package org.example.service;

<<<<<<< HEAD
import org.example.dao.impl.UserDaoImpl;
import org.example.entity.User;
import java.util.Optional;


/**
 * Service class for managing users.
 */
public class UserService {

    private static UserService userService = new UserService();
    private final UserDaoImpl userDao = UserDaoImpl.getInstance();

    /**
     * Registers a new user with the provided username and password.
     * @param username Username of the new user
     * @param password Password of the new user
     * @return True if registration is successful, false otherwise.
     */
    public boolean register(String username, String password){
        Optional<User> existingUser = userDao.findByUsername(username);
        if(existingUser.isEmpty()){
            userDao.save(User.builder()
                            .username(username)
                            .password(password)
                            .build());
            return true;
        } else {
=======
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
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
            return false;
        }
    }

    /**
<<<<<<< HEAD
     * Authenticates a user with the provided username and password.
     * @param username Username of the user to authenticate
     * @param password Password of the user to authenticate
     * @return True if authentication is successful, false otherwise.
     */
    public boolean authenticate(String username, String password){
        Optional<User> user = userDao.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password))
            return true;
        else
            return false;
    }

    /**
     * Retrieves a user by their ID.
     * @param id ID of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     */
    public Optional<User> getUser(Long id){
        return userDao.findById(id);
    }

    /**
     * Retrieves a user by their username.
     * @param username Username of the user to retrieve
     * @return Optional containing the user if found, otherwise empty
     */
    public Optional<User> getUser(String username){
        return userDao.findByUsername(username);
    }

    /**
     * Retrieves the singleton instance of UserService.
     * @return Singleton instance of UserService
     */
    public static UserService getInstance() {
        return userService;
=======
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
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
    }
}
