package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping(value = "/admin")
    public String listUsers(Principal principal, Model model) {
        List<User> users = userService.listUsers();
        model.addAttribute("users", users);
        model.addAttribute("principal", userService.getUserByUsername(principal.getName()));
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "users";
    }

    @GetMapping("/user")
    public String getUserById(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        model.addAttribute("principal", userService.getUserByUsername(principal.getName()));
        return "user";
    }



    @PostMapping("admin/user/new")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("newUserRoles") String[] roles) {
        try {
            Set<Role> roleSet = Arrays.stream(roles)
                    .map(roleService::getRoleByName)
                    .collect(Collectors.toSet());

            user.setRoleSet(roleSet);
            userService.add(user);
        } catch (Exception e) {
            //ignored
        }
        return "redirect:/admin";
    }


    @PatchMapping("/admin/user/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("allRoles[]") String[] roles) {
        try {
            Set<Role> roleSet = Arrays.stream(roles)
                    .map(roleService::getRoleByName)
                    .collect(Collectors.toSet());
            userService.update(user, roleSet);
        } catch (Exception e) {
            //ignored
        }
        return "redirect:/admin";
    }

    @DeleteMapping("admin/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(userService.getUserById(id));
        return "redirect:/admin";
    }
}
