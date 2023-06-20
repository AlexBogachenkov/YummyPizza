package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import yummypizza.core.domain.UserRole;
import yummypizza.core.requests.user.CreateUserRequest;
import yummypizza.core.responses.user.CreateUserResponse;
import yummypizza.core.services.user.CreateUserService;

@Controller
public class SignUpPageController {

    @Autowired
    private CreateUserService createUserService;

    @GetMapping("/signup")
    public String showSignUpPage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateUserRequest());
        return "signupPage.html";
    }

    @PostMapping(value = "/signup")
    public String processSignUpUserRequest(@ModelAttribute(value = "request") CreateUserRequest request, ModelMap modelMap) {
        request.setRole(UserRole.CLIENT);
        CreateUserResponse response = createUserService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "signupPage.html";
        } else {
            modelMap.addAttribute("createdUser", response.getCreatedUser());
            return "successfulSignUpPage.html";
        }
    }

}
