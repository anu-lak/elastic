package com.example.rest_api.repository;

import java.util.List;
import java.util.Optional;
import com.example.rest_api.model.Employee;


public interface EmployeeRepo{

    List<Employee> findByDepartment(String department);

    public List<Employee> findManagers();

    List<Employee> findAllByManagerId(int id);

    List<Employee> findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(int id, Integer yearOfExperience);

    List<Employee> findAllByYearsOfExperienceGreaterThanEqual(int yearsOfExperience);

    Optional<Employee> findById(int id);

    List<Employee> findAllById(Integer employeeId);

    void deleteById(Integer employeeId);

    void save(Employee emp);

    boolean existsById(int id);
    
}
