package com.example.rest_api.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.rest_api.helpers.Helper;
import com.example.rest_api.model.Employee;
import com.example.rest_api.repository.EmployeeRepo;
import com.example.rest_api.response.ApiResponse;

@Service
public class RestService {
    @Autowired
    private EmployeeRepo employeeRepo;
   
//POST
public ResponseEntity<?> postEmployee(Employee employee) {
    // Check if the employee ID already exists
    if (employeeRepo.existsById(employee.getId())) {
        return ResponseEntity.badRequest().body(new ApiResponse("Employee ID already exists"));
    }

    // Check if the employee is an account manager and whether the given manager id is 0;
    if ("account manager".equals(employee.getDesignation())) {
        if (employee.getManagerId() != 0) {
            return ResponseEntity.badRequest().body(new ApiResponse("Manager cannot have a ManagerId"));
        }

        // Check if the department already has a manager
        List<Employee> deptEmployees = employeeRepo.findByDepartment(employee.getDepartment());
        for (Employee emp : deptEmployees) {
            if (emp.getManagerId() == 0) {
                return ResponseEntity.badRequest().body(new ApiResponse("A manager already exists in the department: " + employee.getDepartment()));
            }
        }

        // Save the new manager
        employee.setCreatedTime(OffsetDateTime.now());
        Helper.calcualteYearsOFExperience(employee);
        employeeRepo.save(employee);
        return ResponseEntity.ok(new ApiResponse("Successfully created"));
    } 

    // If the employee is an associate, check the manager
    if ("associate".equals(employee.getDesignation())) {
        Optional<Employee> optionalManager = employeeRepo.findById(employee.getManagerId());
        if (!optionalManager.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Manager cannot be found"));
        }

        Employee manager = optionalManager.get();
        if (manager.getManagerId() == 0 && "account manager".equals(manager.getDesignation())) {
            if (manager.getDepartment().equals(employee.getDepartment())) {
                // Save the associate
                employee.setCreatedTime(OffsetDateTime.now());
                Helper.calcualteYearsOFExperience(employee);
                employeeRepo.save(employee);
                return ResponseEntity.ok(new ApiResponse("Successfully created"));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse("Given manager's department does not match"));
            }
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse("Given manager's ID or designation does not match"));
        }
    }
    // Default return for unhandled cases
    return ResponseEntity.badRequest().body(new ApiResponse("Invalid employee designation or data"));
}


    //GET
     public Map<String,Object> getEmployee(Integer managerid,Integer yearsOfExperience){
        Map<String,Object> result = new LinkedHashMap<>();
        // result.put("message","successfully fetched");
        List<Object> detailList = new ArrayList<>();
        //creating a managerList to find the manager with the given manager id;
            List<Employee> managerList = employeeRepo.findManagers();
            for(Employee manager: managerList){
                if(managerid == null || managerid.equals(manager.getId())){
                    Map<String,Object> managerEmpMap = new LinkedHashMap<>();
                    managerEmpMap.put("account manager",manager.getName());
                    managerEmpMap.put("department",manager.getDepartment());
                    managerEmpMap.put("id",manager.getId());
                    //create a list of employees under the manager
                    List<Employee> employeeList;
                    if(yearsOfExperience==null){
                        employeeList=employeeRepo.findAllByManagerId(manager.getId());
                    }else{
                        employeeList=employeeRepo.
                        findAllByManagerIdAndYearsOfExperienceGreaterThanEqual(manager.getId(),yearsOfExperience);
                    }
                    managerEmpMap.put("employeeList",employeeList);
                    detailList.add(managerEmpMap);
                }
            }
        // result.put("details", detailList);
        // return result;
        if(detailList.isEmpty()){
            result.put("message","No employees who match the conditions");
            return result;
        }else{
            result.put("message","successfully fetched");
            result.put("details", detailList);
            return result;
        }
       
    }
    //UPDATE
    @Transactional
    public ResponseEntity<Map<String,String>> updateEmployee(Integer employeeId, Integer newManagerid){
        Map<String,String> result=new HashMap<>();
        //check if the employee exist and fetch
        List<Employee> empList = employeeRepo.findAllById(employeeId);
        String oldManagerName;
        String newManagerName;
        //System.out.println(employeeId);
        //System.out.println(newManagerid);
        if(empList.isEmpty()){
            result.put("message ","Employee with ID " + employeeId + " not found.");
                    return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }else{
            for(Employee emp : empList){
                if(emp.getManagerId() == (newManagerid)){
                    result.put("message","Employee is currently under the given manager.No changes required");
                    return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
                }
                else{
                    
                    try{
                        Optional<Employee> m = employeeRepo.findById(newManagerid);
                        System.out.println(m);
                        if(m.isPresent()){
                           // newManagerName = m.getName();
                           Employee newManager = m.get();
                           newManagerName = newManager.getName();

                           int oldManagerId = emp.getManagerId();
                           System.out.println(oldManagerId);
                           Optional<Employee> e = employeeRepo.findById(oldManagerId);
                           System.out.println(e);
                           oldManagerName = "tttttt";
                           if(e.isPresent()){
                               Employee oldmanager = e.get();
                               oldManagerName = oldmanager.getName();
                           }
                           
                           emp.setManagerId(newManagerid);
                          // String newDept = e.getDepartment();
                           //emp.setDepartment(newDept);
                           employeeRepo.save(emp);
                           result.put("message ",""+ emp.getName()+"'s manager has been successfully changed from "+
                           oldManagerName +" to "+ newManagerName+".");
                        }                       
                    }catch(NullPointerException n){
                        System.out.println(n);
                    }
                }
            }
            
        } 
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //DELETE
    public ResponseEntity<Map<String,String>> deleteEmployee(Integer employeeId){
        Map<String,String> result = new HashMap<>();
        Optional<Employee> employeeOptional = employeeRepo.findById(employeeId);
        if(employeeOptional.isEmpty()){
            result.put("message","Employee not found");
            return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);    
        }else{
            Employee employee = employeeOptional.get();
            //Integer managerId = employee.getManagerId();
            List<Employee> empList = employeeRepo.findAllByManagerId(employeeId);
            if(!empList.isEmpty()){
                result.put("message", "Cannot delete employee as the employee has subordinates");
                return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
            }else{
                employeeRepo.deleteById(employeeId);
                result.put("message","Successfully deleted "+employee.getName()+" from employee list of the organization");
                return new ResponseEntity<>(result,HttpStatus.OK);
            }
        }
    }
}
