package com.karol.dao;

import com.karol.entity.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EmployeeDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Employee create(Employee e) {
        entityManager.persist(e);
        return e;
    }

    public Employee findById(int id) {
        return entityManager.find(Employee.class, id);
    }

    public List<Employee> findAll() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    @Transactional
    public void removeAll() {
        entityManager.createQuery("DELETE from Employee").executeUpdate();
    }

}