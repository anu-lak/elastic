package com.example.rest_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.rest_api.Service.RestService;
import com.example.rest_api.input.UpdateRequest;
import com.example.rest_api.model.Employee;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private RestService restService;

    //POST
    @PostMapping("/postEmployee")
    public ResponseEntity<?> postEmployee(@Valid @RequestBody Employee employee) {
       // System.out.println(employee);
        return restService.postEmployee(employee);
    }
    
    //GET
    @GetMapping("/getEmployee")
    public Map<String, Object> getEmployee(@RequestParam(required=false) Integer managerId,
                                        @RequestParam(required=false) Integer yearsOfExperience) {
        return restService.getEmployee(managerId, yearsOfExperience);
    }
    //UPDATE
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateEmployee(@RequestBody UpdateRequest updaterequest){
        return restService.updateEmployee(updaterequest.getEmployeeId(), updaterequest.getManagerId());
    }
    //DELETE
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String,String>> deleteEmployee(@RequestParam Integer employeeId){
        return restService.deleteEmployee(employeeId);
    }
}
