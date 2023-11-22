package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> findAll();

    Role getRoleById(Long id);
}
