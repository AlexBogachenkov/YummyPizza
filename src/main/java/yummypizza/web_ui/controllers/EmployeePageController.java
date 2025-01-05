package yummypizza.web_ui.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/employeePage")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class EmployeePageController {

    @GetMapping
    public String showEmployeePage() {
        return "employeePage.html";
    }

}
