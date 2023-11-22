package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository{

    @PersistenceContext
    private EntityManager entityManager;

    public RoleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Role> findAll() {
        String queryString = "SELECT u FROM Role u";
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Override
    public Role getRoleById(Long id){
        return entityManager.find(Role.class, id);
    }
}
