package com.karol.dao;

import com.karol.configuration.AppConfig;
import com.karol.configuration.AppConfig.Profiles;
import com.karol.entity.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles(Profiles.MYSQL)
public class EmployeeDaoIT {
    public static final String JOHN = "John";
    public static final String MIKE = "Mike";

    @Inject
    private EmployeeDao employeeDao;
    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void setUp() {
        employeeDao.removeAll();
    }

    @Test
    public void createEmployee() {
        Employee employee = employeeDao.create(Employee.builder().name(JOHN).build());

        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isNotNull();
        assertThat(employeeDao.findById(employee.getId()).getName()).isEqualTo(JOHN);
    }

    @Test
    public void findAll() {
        employeeDao.create(Employee.builder().name(JOHN).build());
        employeeDao.create(Employee.builder().name(MIKE).build());

        assertThat(employeeDao.findAll()).hasSize(2);
    }

    @Test
    public void testWithoutTransactional_doesNotUseFirstLevelCache() throws Exception {
        Employee e = employeeDao.create(Employee.builder().name(JOHN).build());
        //will call database 10 times. Check logs
        //entityManager is a proxy and SharedEntityManagerInvocationHandler will be called to handle method invocation
        //This proxy will first try to get/create entityManger connected to a current transaction, but as we don't have any transaction a new entity
        //manager will be created for each find. That is why first-level cache can't work in this case (it is connected to a single entityManager) and
        //db will be called 10 times.
        for (int i = 0; i < 10; i++) { entityManager.find(Employee.class, e.getId()); }
    }

    @Test
    @Transactional
    public void testWithTransactional_UsesFirstLevelCache() throws Exception {
        Employee e = employeeDao.create(Employee.builder().name(JOHN).build());
        //in this case entityManager will be called just once, because there will be only one entityManager connected to the current transaction
        for (int i = 0; i < 10; i++) { entityManager.find(Employee.class, e.getId()); }
    }
}