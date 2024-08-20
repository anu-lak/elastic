package com.example.rest_api.helpers;

import java.time.LocalDate;
import java.time.Period;
import com.example.rest_api.model.Employee;

public class Helper{
    // @Autowired
    // private EmployeeRepo employeeRepo;

    public static void calcualteYearsOFExperience(Employee employee){
        if(employee.getDateOfJoining()!=null){
            LocalDate dateOfJoining = employee.getDateOfJoining().toLocalDate();
            LocalDate currentDate = LocalDate.now();
            employee.setYearsOfExperience(Period.between(dateOfJoining, currentDate).getYears());
        }
    }
    
    //id generation
    // public static int generateId(){
    //     List<Employee> allEmployees = employeeRepo.findAll();
    //     int maxId=0;
    //     if(allEmployees != null){
    //         for(Employee emp : allEmployees){
    //             if(emp.getId()>maxId){
    //                 maxId = emp.getId();
    //             }
    //         }
    //     }
    //     return maxId;
    // }
}
