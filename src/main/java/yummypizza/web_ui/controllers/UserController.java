package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.requests.DeleteUserByIdRequest;
import yummypizza.core.requests.FindUserByIdRequest;
import yummypizza.core.requests.SaveUserRequest;
import yummypizza.core.responses.DeleteUserByIdResponse;
import yummypizza.core.responses.FindAllUsersResponse;
import yummypizza.core.responses.FindUserByIdResponse;
import yummypizza.core.responses.SaveUserResponse;
import yummypizza.core.services.DeleteUserByIdService;
import yummypizza.core.services.FindAllUsersService;
import yummypizza.core.services.FindUserByIdService;
import yummypizza.core.services.SaveUserService;

@Controller
public class UserController {

    @Autowired
    private SaveUserService saveUserService;
    @Autowired
    private FindUserByIdService findUserByIdService;
    @Autowired
    private FindAllUsersService findAllUsersService;
    @Autowired
    private DeleteUserByIdService deleteUserByIdService;

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

    @GetMapping(value = "usersList")
    public String showUsersListPage(ModelMap modelMap) {
        FindAllUsersResponse response = findAllUsersService.execute();
        modelMap.addAttribute("users", response.getAllUsers());
        return "users/usersList.html";
    }

    @GetMapping(value = "usersDeleteById")
    public String showUsersDeleteByIdPage(ModelMap modelMap) {
        modelMap.addAttribute("request", new DeleteUserByIdRequest());
        return "users/usersDeleteById.html";
    }

    @PostMapping(value = "usersDeleteById")
    public String processDeleteUserByIdRequest(@ModelAttribute(value = "request")
                                                   DeleteUserByIdRequest request, ModelMap modelMap) {
        DeleteUserByIdResponse response = deleteUserByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "users/usersDeleteById.html";
        } else {
            return "users/users.html";
        }
    }

}
