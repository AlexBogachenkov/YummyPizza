package yummypizza.web_ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/login")
public class LogInPageController {

    @GetMapping
    public String showLoginPage(@RequestParam(value = "isAuthenticationFailed", required = false)
                                    boolean isAuthenticationFailed, ModelMap modelMap) {
        if (isAuthenticationFailed)
            modelMap.addAttribute("isAuthenticationFailed", true);
        return "loginPage.html";
    }

}
