package com.example.rest_api;

import com.example.rest_api.Service.RestService;
import com.example.rest_api.model.Employee;
import com.example.rest_api.repository.EmployeeRepo;
import com.example.rest_api.response.ApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RestServiceTest {

    @Mock
    private EmployeeRepo employeeRepo;

    @InjectMocks
    private RestService restService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostEmployee_AccountManager_Success() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setDesignation("account manager");
        employee.setManagerId(0);
        employee.setDepartment("Sales");

        when(employeeRepo.existsById(employee.getId())).thenReturn(false);
        when(employeeRepo.findByDepartment(employee.getDepartment())).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = restService.postEmployee(employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiResponse);
        assertEquals("Successfully created", ((ApiResponse) response.getBody()).getMessage());
        verify(employeeRepo, times(1)).save(employee);
    }

    @Test
    public void testPostEmployee_AccountManager_AlreadyExists() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setDesignation("account manager");
        employee.setManagerId(0);
        employee.setDepartment("Sales");

        when(employeeRepo.existsById(employee.getId())).thenReturn(true);

        ResponseEntity<?> response = restService.postEmployee(employee);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiResponse);
        assertEquals("Employee ID already exists", ((ApiResponse) response.getBody()).getMessage());
        verify(employeeRepo, times(0)).save(employee);
    }

    @Test
    public void testPostEmployee_Associate_Success() {
        Employee employee = new Employee();
        employee.setId(2);
        employee.setDesignation("associate");
        employee.setManagerId(1);
        employee.setDepartment("Sales");

        Employee manager = new Employee();
        manager.setId(1);
        manager.setDesignation("account manager");
        manager.setManagerId(0);
        manager.setDepartment("Sales");

        when(employeeRepo.existsById(employee.getId())).thenReturn(false);
        when(employeeRepo.findById(employee.getManagerId())).thenReturn(Optional.of(manager));

        ResponseEntity<?> response = restService.postEmployee(employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiResponse);
        assertEquals("Successfully created", ((ApiResponse) response.getBody()).getMessage());
        verify(employeeRepo, times(1)).save(employee);
    }
    @Test
    public void testPostEmployee_AccountManagerWithNonZeroManagerId() {
    // Given
    Employee employee = new Employee();
    employee.setId(1);
    employee.setDesignation("account manager");
    employee.setManagerId(5); // Non-zero ManagerId
    employee.setDepartment("Sales");

    // Mocking the EmployeeRepo to return false for existsById check
    when(employeeRepo.existsById(employee.getId())).thenReturn(false);

    // When
    ResponseEntity<?> response = restService.postEmployee(employee);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ApiResponse);
    assertEquals("Manager cannot have a ManagerId", ((ApiResponse) response.getBody()).getMessage());
    verify(employeeRepo, times(0)).save(employee); // Ensure save is not called
}
@Test
public void testPostEmployee_ManagerAlreadyExistsInDepartment() {
    Employee employee = new Employee();
    employee.setId(2);
    employee.setDesignation("account manager");
    employee.setManagerId(0);
    employee.setDepartment("Sales");

    Employee existingManager = new Employee();
    existingManager.setId(1);
    existingManager.setDesignation("account manager");
    existingManager.setManagerId(0);
    existingManager.setDepartment("Sales");

    when(employeeRepo.existsById(employee.getId())).thenReturn(false);
    when(employeeRepo.findByDepartment(employee.getDepartment())).thenReturn(Collections.singletonList(existingManager));

    ResponseEntity<?> response = restService.postEmployee(employee);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ApiResponse);
    assertEquals("A manager already exists in the department: Sales", ((ApiResponse) response.getBody()).getMessage());
    verify(employeeRepo, times(0)).save(employee);
}

@Test
public void testPostEmployee_ManagerCannotBeFound() {
    Employee employee = new Employee();
    employee.setId(3);
    employee.setDesignation("associate");
    employee.setManagerId(1);
    employee.setDepartment("Sales");

    when(employeeRepo.existsById(employee.getId())).thenReturn(false);
    when(employeeRepo.findById(employee.getManagerId())).thenReturn(Optional.empty());

    ResponseEntity<?> response = restService.postEmployee(employee);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ApiResponse);
    assertEquals("Manager cannot be found", ((ApiResponse) response.getBody()).getMessage());
    verify(employeeRepo, times(0)).save(employee);
}


@Test
public void testPostEmployee_Associate_ManagerDepartmentDoesNotMatch() {
    // Given
    Employee employee = new Employee();
    employee.setId(2);
    employee.setDesignation("associate");
    employee.setManagerId(1);
    employee.setDepartment("Sales");

    Employee manager = new Employee();
    manager.setId(1);
    manager.setDesignation("account manager");
    manager.setManagerId(0);
    manager.setDepartment("Marketing"); // Different department

    when(employeeRepo.existsById(employee.getId())).thenReturn(false);
    when(employeeRepo.findById(employee.getManagerId())).thenReturn(Optional.of(manager));

    // When
    ResponseEntity<?> response = restService.postEmployee(employee);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ApiResponse);
    assertEquals("Given manager's department does not match", ((ApiResponse) response.getBody()).getMessage());
    verify(employeeRepo, times(0)).save(employee); // Ensure save is not called
}

@Test
public void testPostEmployee_Associate_ManagerIdOrDesignationDoesNotMatch() {
    // Given
    Employee employee = new Employee();
    employee.setId(2);
    employee.setDesignation("associate");
    employee.setManagerId(1);
    employee.setDepartment("Sales");

    Employee manager = new Employee();
    manager.setId(1);
    manager.setDesignation("senior associate"); // Incorrect designation
    manager.setManagerId(0);
    manager.setDepartment("Sales");

    when(employeeRepo.existsById(employee.getId())).thenReturn(false);
    when(employeeRepo.findById(employee.getManagerId())).thenReturn(Optional.of(manager));

    // When
    ResponseEntity<?> response = restService.postEmployee(employee);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ApiResponse);
    assertEquals("Given manager's ID or designation does not match", ((ApiResponse) response.getBody()).getMessage());
    verify(employeeRepo, times(0)).save(employee); // Ensure save is not called
}

@Test
public void testPostEmployee_InvalidDesignationOrData() {
    // Given
    Employee employee = new Employee();
    employee.setId(3);
    employee.setDesignation("invalid_designation"); // Unhandled designation
    employee.setManagerId(1);
    employee.setDepartment("Sales");

    when(employeeRepo.existsById(employee.getId())).thenReturn(false);

    // When
    ResponseEntity<?> response = restService.postEmployee(employee);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ApiResponse);
    assertEquals("Invalid employee designation or data", ((ApiResponse) response.getBody()).getMessage());
    verify(employeeRepo, times(0)).save(employee); // Ensure save is not called
}


    @Test
    public void testGetEmployee_Success() {
        Employee manager = new Employee();
        manager.setId(1);
        manager.setName("Manager 1");
        manager.setDepartment("Sales");

        Employee employee = new Employee();
        employee.setId(2);
        employee.setName("Employee 1");
        employee.setManagerId(1);
        employee.setDepartment("Sales");

        when(employeeRepo.findManagers()).thenReturn(Collections.singletonList(manager));
        when(employeeRepo.findAllByManagerId(manager.getId())).thenReturn(Collections.singletonList(employee));

        Map<String, Object> result = restService.getEmployee(null, null);

        assertEquals("successfully fetched", result.get("message"));
        List<Object> details = (List<Object>) result.get("details");
        assertEquals(1, details.size());

        Map<String, Object> managerDetails = (Map<String, Object>) details.get(0);
        assertEquals("Manager 1", managerDetails.get("account manager"));
        assertEquals("Sales", managerDetails.get("department"));
        assertEquals(1, ((List<?>) managerDetails.get("employeeList")).size());
    }

    @Test
    public void testUpdateEmployee_Success() {
        Employee employee = new Employee();
        employee.setId(2);
        employee.setName("Employee 1");
        employee.setManagerId(1);

        Employee newManager = new Employee();
        newManager.setId(3);
        newManager.setName("New Manager");

        when(employeeRepo.findAllById(employee.getId())).thenReturn(Collections.singletonList(employee));
        when(employeeRepo.findById(newManager.getId())).thenReturn(Optional.of(newManager));
        when(employeeRepo.findById(employee.getManagerId())).thenReturn(Optional.of(employee));

        ResponseEntity<Map<String, String>> response = restService.updateEmployee(employee.getId(), newManager.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee 1's manager has been successfully changed from Employee 1 to New Manager.",
                response.getBody().get("message "));
        verify(employeeRepo, times(1)).save(employee);
    }
    @Test
public void testGetEmployee_FilterByYearsOfExperience() {
    // Arrange
    Integer managerId = 1;
    Integer yearsOfExperience = 5;

    Employee manager = new Employee();
    manager.setId(managerId);
    manager.setName("Manager 1");
    manager.setDepartment("Sales");

    Employee employee1 = new Employee();
    employee1.setId(2);
    employee1.setName("Employee 1");
    employee1.setManagerId(managerId);
    employee1.setYearsOfExperience(6);

    Employee employee2 = new Employee();
    employee2.setId(3);
    employee2.setName("Employee 2");
    employee2.setManagerId(managerId);
    employee2.setYearsOfExperience(4); // Should be filtered out

    when(employeeRepo.findManagers()).thenReturn(Collections.singletonList(manager));
    when(employeeRepo.findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(managerId, yearsOfExperience))
            .thenReturn(Collections.singletonList(employee1));

    // Act
    Map<String, Object> result = restService.getEmployee(managerId, yearsOfExperience);

    // Assert
    assertEquals("successfully fetched", result.get("message"));
    List<Object> details = (List<Object>) result.get("details");
    assertEquals(1, details.size());

    Map<String, Object> managerDetails = (Map<String, Object>) details.get(0);
    assertEquals("Manager 1", managerDetails.get("account manager"));
    assertEquals("Sales", managerDetails.get("department"));

    List<Employee> employeeList = (List<Employee>) managerDetails.get("employeeList");
    assertEquals(1, employeeList.size());
    assertEquals("Employee 1", employeeList.get(0).getName());
    assertEquals(6, employeeList.get(0).getYearsOfExperience());

    verify(employeeRepo, times(1)).findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(managerId, yearsOfExperience);
}

    @Test
    public void testUpdateEmployee_employeeNotFound() {
        Employee employee = new Employee();
        employee.setId(2);
        employee.setName("Employee 1");
        employee.setManagerId(1);

        Employee newManager = new Employee();
        newManager.setId(3);
        newManager.setName("New Manager");

        when(employeeRepo.findAllById(employee.getId())).thenReturn(Arrays.asList());
        when(employeeRepo.findById(newManager.getId())).thenReturn(Optional.of(newManager));
        when(employeeRepo.findById(employee.getManagerId())).thenReturn(Optional.of(employee));

        ResponseEntity<Map<String, String>> response = restService.updateEmployee(employee.getId(), newManager.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee with ID 2 not found.",
                response.getBody().get("message "));
       
    }

    @Test
public void testUpdateEmployee_NoChangesRequired() {
    // Arrange
    Integer employeeId = 1;
    Integer currentManagerId = 2;

    Employee employee = new Employee();
    employee.setId(employeeId);
    employee.setManagerId(currentManagerId);  // Employee is already under this manager

    when(employeeRepo.findAllById(employeeId)).thenReturn(Collections.singletonList(employee));

    // Act
    ResponseEntity<Map<String, String>> response = restService.updateEmployee(employeeId, currentManagerId);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Employee is currently under the given manager.No changes required", response.getBody().get("message"));
    verify(employeeRepo, times(0)).save(employee);  // Ensure that save is not called since no update is needed
}

@Test
public void testUpdateEmployee_NullPointerExceptionHandling() {
    // Arrange
    Integer employeeId = 1;
    Integer newManagerId = 3;

    Employee employee = new Employee();
    employee.setId(employeeId);
    employee.setManagerId(2);  // Employee is currently under a different manager

    when(employeeRepo.findAllById(employeeId)).thenReturn(Collections.singletonList(employee));
    when(employeeRepo.findById(newManagerId)).thenThrow(new NullPointerException("Simulated NullPointerException"));

    // Act
    ResponseEntity<Map<String, String>> response = restService.updateEmployee(employeeId, newManagerId);

    // Assert
    // Since we catch and print the exception, we can't assert directly on the response.
    // We just ensure that the response is returned with HttpStatus.OK despite the exception.
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(employeeRepo, times(0)).save(employee);  // Ensure that save is not called due to the exception
}


    @Test
    public void testDeleteEmployee_Success() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Employee 1");

        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepo.findAllByManagerId(employee.getId())).thenReturn(Collections.emptyList());

        ResponseEntity<Map<String, String>> response = restService.deleteEmployee(employee.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted Employee 1 from employee list of the organization",
                response.getBody().get("message"));
        verify(employeeRepo, times(1)).deleteById(employee.getId());
    }
    @Test
    public void testDeleteEmployee_employeeNotFound() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Employee 1");

        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.empty());
        when(employeeRepo.findAllByManagerId(employee.getId())).thenReturn(Collections.emptyList());

        ResponseEntity<Map<String, String>> response = restService.deleteEmployee(employee.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found",
                response.getBody().get("message"));
       
    }

    @Test
    public void testDeleteEmployee_EmployeeHasSubordinates() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Manager 1");

        Employee subordinate = new Employee();
        subordinate.setId(2);
        subordinate.setManagerId(1);

        when(employeeRepo.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepo.findAllByManagerId(employee.getId())).thenReturn(Collections.singletonList(subordinate));

        ResponseEntity<Map<String, String>> response = restService.deleteEmployee(employee.getId());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot delete employee as the employee has subordinates",
                response.getBody().get("message"));
        verify(employeeRepo, times(0)).deleteById(employee.getId());
    }
}
