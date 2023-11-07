package ru.kata.spring.boot_security.demo.repository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserRepository {
    User getUser(long id);
    List<User> listUsers();
    void addUser(User user);
    void editUser(User user);
    void removeUser(long id);
    User getUserByUsername(String username);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
