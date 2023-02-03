package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yummypizza.core.requests.FindUserByIdRequest;
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.FindUserByIdResponse;
import yummypizza.core.responses.SaveUserResponse;
import yummypizza.core.services.FindUserByIdService;
import yummypizza.core.services.SaveUserService;

@Controller
public class UserController {

    @Autowired
    private SaveUserService saveUserService;
    @Autowired
    private FindUserByIdService findUserByIdService;

    @GetMapping(value = "users")
    public String showUsersPage() {
        return "users/users.html";
    }

    @GetMapping(value = "usersCreate")
    public String showUsersCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new SaveUserRequest());
        return "users/usersCreate.html";
    }

    @PostMapping(value = "usersCreate")
    public String processSaveUserRequest(@ModelAttribute(value = "request")SaveUserRequest request, ModelMap modelMap) {
        SaveUserResponse response = saveUserService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "users/usersCreate.html";
        } else {
            return "users/users.html";
        }
    }

    @GetMapping(value = "usersFindById")
    public String showUsersFindByIdPage() {
        return "users/usersFindById.html";
    }

    @GetMapping(value = "users/")
    public String processFindUserByIdRequest(@RequestParam(value = "id") Long id, ModelMap modelMap) {
        FindUserByIdRequest request = new FindUserByIdRequest(id);
        FindUserByIdResponse response = findUserByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "users/usersFindById.html";
        }
        if (response.getFoundUser().isPresent()) {
            modelMap.addAttribute("foundUser", response.getFoundUser().get());
        } else {
            modelMap.addAttribute("userNotFound", true);
        }
        return "users/usersFindById.html";
    }

}
