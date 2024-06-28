package org.example.service;

import org.example.dao.impl.UserDaoImpl;
import org.example.entity.User;

import java.util.Optional;

public class UserService {

    private static UserService userService = new UserService();

    private final UserDaoImpl userDao = UserDaoImpl.getInstance();

    public boolean register(String username, String password){
        Optional<User> existingUser = userDao.findByUsername(username);
        if(existingUser.isEmpty()){
            userDao.save(User.builder()
                            .username(username)
                            .password(password)
                            .build());
            return true;
        } else {
            return false;
        }
    }

    public boolean authenticate(String username, String password){
        Optional<User> user = userDao.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password))
            return true;
        else
            return false;
    }

    public Optional<User> getUserById(Long id){
        return userDao.findById(id);
    }

    public static UserService getInstance() {
        return userService;
    }
}
