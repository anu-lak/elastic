package com.example.rest_api.repository;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import com.example.rest_api.model.Employee;

public interface ElasticRepo extends ElasticsearchRepository<Employee, Integer> {

    List<Employee> findByDepartment(String department);

    @Query("{\"match\": {\"designation\": \"account manager\"}}")
    List<Employee> findManagers();

    List<Employee> findAllByManagerId(int id);
  
    List<Employee> findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(int id, Integer yearOfExperience);

    List<Employee> findAllByYearsOfExperienceGreaterThanEqual(int yearsOfExperience);

    List<Employee> findAllById(Integer employeeId);

}
