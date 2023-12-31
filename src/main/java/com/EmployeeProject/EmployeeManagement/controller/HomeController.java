package com.EmployeeProject.EmployeeManagement.controller;

import com.EmployeeProject.EmployeeManagement.entity.EmployeeEntity;
import com.EmployeeProject.EmployeeManagement.entity.OwnerEntity;
import com.EmployeeProject.EmployeeManagement.service.OwnerService;
import com.EmployeeProject.EmployeeManagement.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Shubh's Employee Management System");
        return "index";
    }

    @GetMapping("/index.html")
    public String home(Model model) {
        model.addAttribute("message", "Shubh's Employee Management System");
        return "index";
    }

    @GetMapping("/display-employees")
    public String displayAllEmployees(Model model) {
    // Get all employees from the database
    List<EmployeeEntity> employees = employeeService.getAllEmployees();

    // Add employees to the model
    model.addAttribute("employees", employees);

    // Return the name of the view to display the employees
    return "employee_data";
}
}

@Controller
@RequestMapping("/api")
class EmployeeApiController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees() {
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeEntity> saveEmployee(@RequestBody EmployeeEntity employee) {
        EmployeeEntity savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @PostMapping("/employee_data")
    public String saveEmployeeInDb(@RequestParam Long id,
                                   @RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam Long ownerId) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setId(id);
        employee.setName(name);
        employee.setEmail(email);

        Optional<OwnerEntity> ownerOptional = ownerService.getOwnerById(ownerId);

        if (ownerOptional.isPresent()) {
            OwnerEntity owner = ownerOptional.get();
            employee.setOwner(owner);
            employeeService.saveEmployee(employee);
        } else {
            return "error_owner_not_found";
        }

        return "redirect:/employee_data";  // redirect to "/employee_data" (not "/employee_data/" + ownerId)
    }
}

