package ageria.u5s7l1.controllers;

import ageria.u5s7l1.dto.EmployeeDTO;
import ageria.u5s7l1.dto.EmployeeLoginDTO;
import ageria.u5s7l1.dto.EmployeeLoginRespDTO;
import ageria.u5s7l1.dto.NewEmployeeRespDTO;
import ageria.u5s7l1.exception.BadRequestException;
import ageria.u5s7l1.services.AuthService;
import ageria.u5s7l1.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    AuthService authService;


    @PostMapping("/login")
    public EmployeeLoginRespDTO login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        return new EmployeeLoginRespDTO(this.authService.checkCredentialAndGenerateToken(employeeLoginDTO));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeRespDTO save(@RequestBody @Validated EmployeeDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String msg = validationResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining());
            throw new BadRequestException(msg);
        } else {
            return new NewEmployeeRespDTO(this.employeeService.saveEmployee(body).getId());
        }
    }
}
