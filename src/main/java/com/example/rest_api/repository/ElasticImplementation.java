package com.example.rest_api.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import com.example.rest_api.model.Employee;



@Repository
@Profile("elasticsearch")
public class ElasticImplementation implements EmployeeRepo {
    
    @Autowired
    ElasticRepo elasticRepo;

    @Override
    public List<Employee> findByDepartment(String department) {
        return elasticRepo.findByDepartment(department);
    }

    @Override
    public List<Employee> findManagers() {
        return elasticRepo.findManagers();
    }

    @Override
    public List<Employee> findAllByManagerId(int id) {
        return elasticRepo.findAllByManagerId(id);
    }

    @Override
    public List<Employee> findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(int id, Integer yearOfExperience) {
        return elasticRepo.findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(id, yearOfExperience);
    }

    @Override
    public List<Employee> findAllByYearsOfExperienceGreaterThanEqual(int yearsOfExperience) {
        return elasticRepo.findAllByYearsOfExperienceGreaterThanEqual(yearsOfExperience);
    }

    @Override
    public Optional<Employee> findById(int id) {
        return elasticRepo.findById(id);
    }

    @Override
    public List<Employee> findAllById(Integer employeeId) {
        return elasticRepo.findAllById(employeeId); // Adjust for proper handling if needed
    }

    @Override
    public void deleteById(Integer employeeId) {
        elasticRepo.deleteById(employeeId);
    }

    @Override
    public void save(Employee emp) {
        elasticRepo.save(emp);
    }

    @Override
    public boolean existsById(int id) {
        return elasticRepo.existsById(id);
    }
}
