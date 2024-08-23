package com.example.rest_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.rest_api.model.Employee;
import com.example.rest_api.repository.ElasticImplementation;
import com.example.rest_api.repository.ElasticRepo;

class ElasticImplementationTest {

    @Mock
    private ElasticRepo elasticRepo;

    @InjectMocks
    private ElasticImplementation elasticImplementation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByDepartment() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(elasticRepo.findByDepartment(anyString())).thenReturn(employees);

        List<Employee> result = elasticImplementation.findByDepartment("IT");

        assertEquals(2, result.size());
        verify(elasticRepo, times(1)).findByDepartment("IT");
    }

    @Test
    void testFindManagers() {
        List<Employee> managers = Arrays.asList(new Employee(), new Employee());
        when(elasticRepo.findManagers()).thenReturn(managers);

        List<Employee> result = elasticImplementation.findManagers();

        assertEquals(2, result.size());
        verify(elasticRepo, times(1)).findManagers();
    }

    @Test
    void testFindAllByManagerId() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(elasticRepo.findAllByManagerId(anyInt())).thenReturn(employees);

        List<Employee> result = elasticImplementation.findAllByManagerId(1);

        assertEquals(2, result.size());
        verify(elasticRepo, times(1)).findAllByManagerId(1);
    }

    @Test
    void testFindAllByManagerIdAndYearsOfExperienceGreaterThanEqual() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(elasticRepo.findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(anyInt(), anyInt()))
            .thenReturn(employees);

        List<Employee> result = elasticImplementation.findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(1, 5);

        assertEquals(2, result.size());
        verify(elasticRepo, times(1)).findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(1, 5);
    }

    @Test
    void testFindAllByYearsOfExperienceGreaterThanEqual() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(elasticRepo.findAllByYearsOfExperienceGreaterThanEqual(anyInt())).thenReturn(employees);

        List<Employee> result = elasticImplementation.findAllByYearsOfExperienceGreaterThanEqual(5);

        assertEquals(2, result.size());
        verify(elasticRepo, times(1)).findAllByYearsOfExperienceGreaterThanEqual(5);
    }

    @Test
    void testFindById() {
        Employee employee = new Employee();
        when(elasticRepo.findById(anyInt())).thenReturn(Optional.of(employee));

        Optional<Employee> result = elasticImplementation.findById(1);

        assertTrue(result.isPresent());
        verify(elasticRepo, times(1)).findById(1);
    }

    @Test
    void testFindAllById() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(elasticRepo.findAllById(anyInt())).thenReturn(employees);

        List<Employee> result = elasticImplementation.findAllById(1);

        assertEquals(2, result.size());
        verify(elasticRepo, times(1)).findAllById(1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(elasticRepo).deleteById(anyInt());

        elasticImplementation.deleteById(1);

        verify(elasticRepo, times(1)).deleteById(1);
    }


    @Test
    void testExistsById() {
        when(elasticRepo.existsById(anyInt())).thenReturn(true);

        boolean exists = elasticImplementation.existsById(1);

        assertTrue(exists);
        verify(elasticRepo, times(1)).existsById(1);
    }
}

