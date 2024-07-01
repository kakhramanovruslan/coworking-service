package org.example.dao.impl;

import org.example.config.ContainersEnvironment;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest extends ContainersEnvironment {

    UserDaoImpl userDao;
    private User testUser;

    @BeforeEach
    public void setUp(){
        userDao = UserDaoImpl.getInstance();

        userDao.deleteAll();

        testUser = testUser.builder()
                .username("ruslan")
                .password("123")
                .build();
        userDao.save(testUser);
    }

    @Test
    public void testFindById(){
        Optional<User> foundUser = userDao.findById(testUser.getId());
        assertAll(
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals(testUser.getId(), foundUser.get().getId()),
                () -> assertEquals(testUser.getUsername(), foundUser.get().getUsername()),
                () -> assertEquals(testUser.getPassword(), foundUser.get().getPassword())
        );

        Optional<User> notFoundPlayer = userDao.findById(999L);
        assertFalse(notFoundPlayer.isPresent());
    }

    @Test
    public void testDeleteById(){
        assertTrue(userDao.deleteById(testUser.getId()));
        assertTrue(userDao.findById(testUser.getId()).isEmpty());
    }

    @Test
    public void testDeleteAll(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        userDao.save(testUser2);

        assertTrue(userDao.deleteAll());
        assertEquals(userDao.findAll().size(), 0);
    }

    @Test
    public void testFindAll(){
        User testUser2 = User.builder()
                .username("ruslan2")
                .password("123")
                .build();
        userDao.save(testUser2);

        assertEquals(userDao.findAll().size(), 2);
        assertFalse(userDao.findAll().isEmpty());
    }

}
