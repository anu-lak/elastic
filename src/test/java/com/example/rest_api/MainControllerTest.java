package com.example.rest_api;

import com.example.rest_api.Service.RestService;
import com.example.rest_api.controller.MainController;
import com.example.rest_api.input.UpdateRequest;
import com.example.rest_api.model.Employee;
import com.example.rest_api.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MainControllerTest {

    @Mock
    private RestService restService;

    @InjectMocks
    private MainController mainController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPostEmployee_Success() {
        Employee employee = new Employee();
        ApiResponse apiResponse = new ApiResponse("Successfully created");
        when(restService.postEmployee(any(Employee.class))).thenReturn((ResponseEntity) ResponseEntity.ok(new ApiResponse("Successfully created")));

        ResponseEntity<?> response = mainController.postEmployee(employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
void testPostEmployee_Failure() {
    Employee employee = new Employee();
    ApiResponse apiResponse = new ApiResponse("Employee ID already exists");
    
    // Mocking the service to return a bad request when a duplicate employee ID is detected.
    when(restService.postEmployee(any(Employee.class)))
            .thenReturn((ResponseEntity)ResponseEntity.badRequest().body(apiResponse));

    // Call the controller method
    ResponseEntity<?> response = mainController.postEmployee(employee);

    // Asserting that the response status is BAD_REQUEST and the response body matches the mocked response.
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(apiResponse, response.getBody());
}


    @Test
    void testGetEmployee_Success() {
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "successfully fetched");
        when(restService.getEmployee(any(Integer.class), any(Integer.class))).thenReturn(expectedResponse);

        Map<String, Object> response = mainController.getEmployee(1, 5);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testUpdateEmployee_Success() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setEmployeeId(1);
        updateRequest.setManagerId(2);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Employee's manager has been successfully changed");
        when(restService.updateEmployee(any(Integer.class), any(Integer.class))).thenReturn(new ResponseEntity<>(result, HttpStatus.OK));

        ResponseEntity<Map<String, String>> response = mainController.updateEmployee(updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(result, response.getBody());
    }

    @Test
    void testDeleteEmployee_Success() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Successfully deleted John Doe from employee list of the organization");
        when(restService.deleteEmployee(any(Integer.class))).thenReturn(new ResponseEntity<>(result, HttpStatus.OK));

        ResponseEntity<Map<String, String>> response = mainController.deleteEmployee(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(result, response.getBody());
    }
}
