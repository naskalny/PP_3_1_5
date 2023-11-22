package ru.kata.spring.boot_security.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> listUsers() {
        String queryString = "SELECT u FROM User u";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> rolesss = new ArrayList<>();
        for (Role role1 : user.getRoles()) {
            Role role = entityManager.find(Role.class, role1.getRoleId());
            rolesss.add(role);
        }
        user.setRoles(rolesss);
        entityManager.persist(user);
    }

    @Override
    public void editUser(User user) {
        User existingUser = entityManager.find(User.class, user.getId());
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setAge(user.getAge());
            if (!user.getPassword().isEmpty())
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            List<Role> rolesss = new ArrayList<>();
            for (Role role1 : user.getRoles()) {
                Role role = entityManager.find(Role.class, role1.getRoleId());
                rolesss.add(role);
            }
            existingUser.setRoles(rolesss);
        }
    }

    @Override
    @Transactional
    public void removeUser(long id) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser != null && existingUser.getRoles() != null) {
            existingUser.getRoles().clear();
        }
        entityManager.remove(existingUser);
    }

    @Override
    public User getUserByUsername(String username) {
        return (User) entityManager.createQuery("select user from User user where user.username like :username")
                .setParameter("username", username).setMaxResults(1).getSingleResult();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Query query = entityManager.createQuery
                ("select u from User u left join fetch u.roles where u.username=:name", User.class);
        query.setParameter("name", name);
        User user = (User) query.getSingleResult();
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", name));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
