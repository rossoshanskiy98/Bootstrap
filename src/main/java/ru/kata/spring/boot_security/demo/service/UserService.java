package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {

    void add(User user);
    void delete(User user);
    void update(User user, Set<Role> roleSet);
    List<User> listUsers();
    User getUserById(long id);
    User getUserByUsername(String username);
}
