package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String getAllUsers(@ModelAttribute("user") User user, Model model, Principal principal) {
        User authenticatedUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("authenticatedUser", authenticatedUser);
        List<Role> roles = (List<Role>) roleService.getAllRoles();
        model.addAttribute("AllRoles", roles);
        return "users";
    }

    @GetMapping(value = "getUser")
    public String getUser(@RequestParam(name = "id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "details";
    }

    @GetMapping(value = "/create")
    public String addUser(@ModelAttribute("user") User user) {
        return "create";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin";
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/edit")
    public String edit(@RequestParam(name = "id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "edit";
        userService.editUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        userService.removeUser(id);
        return "redirect:/admin";
    }
}
