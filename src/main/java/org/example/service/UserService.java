package org.example.service;

<<<<<<< HEAD
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
=======
import org.example.dao.impl.UserDaoImpl;
>>>>>>> ylab_lab2
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
<<<<<<< HEAD
            System.out.println("Пользователь с именем " + username + " уже существует. Повторите попытку регистрации еще раз!");
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
=======
>>>>>>> ylab_lab2
            return false;
        }
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> ylab_lab2
     * Authenticates a user with the provided username and password.
     * @param username Username of the user to authenticate
     * @param password Password of the user to authenticate
     * @return True if authentication is successful, false otherwise.
<<<<<<< HEAD
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
=======
>>>>>>> ylab_lab2
     */
    public boolean authenticate(String username, String password){
        Optional<User> user = userDao.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password))
            return true;
        else
            return false;
<<<<<<< HEAD
        }
>>>>>>> 43e3611c8f9b95f07c7653312542905fd21780d8
=======
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
>>>>>>> ylab_lab2
    }
}
