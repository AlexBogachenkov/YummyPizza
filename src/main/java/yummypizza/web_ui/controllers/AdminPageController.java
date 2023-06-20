package yummypizza.web_ui.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/adminPage")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPageController {

    @GetMapping
    public String showAdminPage() {
        return "adminPage.html";
    }

}
