package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FirstRestController {
    private final UserService userService;
    private final RoleService roleService;

    public FirstRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hellow";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.listUsers();
    }

    @GetMapping("/users/{id}")
    public User getAllUsers(@PathVariable("id") long id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public ResponseEntity<String> create(@RequestBody @Valid User user,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            return ResponseEntity.badRequest().body(errorMsg.toString().substring(0, errorMsg.toString().length()-1));
        }
        userService.addUser(user);
        return ResponseEntity.ok("User created successfully");
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<String> update(@PathVariable("id") long id,
                                             @RequestBody @Valid User user,
                                             BindingResult bindingResult) {
        System.out.println("Received PATCH request for user ID: " + id);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            return ResponseEntity.badRequest().body(errorMsg.toString().substring(0, errorMsg.toString().length()-1));
        }
        userService.editUser(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/roles/{id}")
    public Role getAllRoles(@PathVariable("id") Long id) {

        return roleService.getRoleById(id);
    }

    @GetMapping("/users/current")
    public User getCurrentUser(Principal principal) {
        return userService.getUserByUsername(principal.getName());
    }
}
