package ageria.u5s7l1.controllers;

import ageria.u5s7l1.dto.EmployeeDTO;
import ageria.u5s7l1.entities.Employee;
import ageria.u5s7l1.exception.BadRequestException;
import ageria.u5s7l1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    // 1. GET
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<Employee> findAll(@RequestParam(defaultValue = "0") int pages,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return this.employeeService.getAllEmployee(pages, size, sortBy);
    }

    // 1.1 GET BY ID
    @GetMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)

    public Employee findById(@PathVariable Long employeeId) {
        return this.employeeService.findById(employeeId);
    }


    //2. POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Validated EmployeeDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = (String) bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException(msg);
        }
        return this.employeeService.saveEmployee(body);
    }

    // 2.1 POST UPLOAD AVATAR
    @PostMapping("/avatar/{employeeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadEmployeeAvatar(@RequestParam("avatar") MultipartFile avatar, @PathVariable Long employeeId) {
        this.employeeService.uploadImage(avatar, employeeId);
    }

    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public Employee updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = (String) bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException(msg);
        }
        return this.employeeService.findByIdAndUpdate(employeeId, body);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteEmploye(@PathVariable Long employeeId) {
        try {
            this.employeeService.findByIdAndDelete(employeeId);
            return "Employee Correctly DELETED";
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("YOU CANNOT DELETE AN EMPLOYEE THAT IS LINKED TO A BOOKING");
        }
    }

}
