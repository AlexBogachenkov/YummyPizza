package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.SaveUserResponse;
import yummypizza.core.services.SaveUserService;

@Controller
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private SaveUserService saveUserService;

    @PostMapping
    public String processSaveUserRequest(@ModelAttribute(value = "request")SaveUserRequest request, ModelMap modelMap) {
        SaveUserResponse response = saveUserService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "adminPage.html";
        } else {
            modelMap.addAttribute("response", response.getSavedUser().getId());
            return "adminPage.html";
        }
    }

}
