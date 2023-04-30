package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.User;
import yummypizza.core.requests.user.CreateUserRequest;
import yummypizza.core.requests.user.DeleteUserByIdRequest;
import yummypizza.core.requests.user.FindUserByIdRequest;
import yummypizza.core.requests.user.UpdateUserRequest;
import yummypizza.core.responses.user.*;
import yummypizza.core.services.user.*;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private CreateUserService createUserService;
    @Autowired
    private FindUserByIdService findUserByIdService;
    @Autowired
    private FindAllUsersService findAllUsersService;
    @Autowired
    private DeleteUserByIdService deleteUserByIdService;
    @Autowired
    private UpdateUserService updateUserService;

    @GetMapping(value = "")
    public String showUsersPage() {
        return "users/users.html";
    }

    @GetMapping(value = "/create")
    public String showUsersCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateUserRequest());
        return "users/usersCreate.html";
    }

    @PostMapping(value = "/create")
    public String processCreateUserRequest(@ModelAttribute(value = "request")CreateUserRequest request, ModelMap modelMap) {
        CreateUserResponse response = createUserService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdUser", response.getCreatedUser());
        }
        return "users/usersCreate.html";
    }

    @GetMapping(value = "/findById")
    public String showUsersFindByIdPage() {
        return "users/usersFindById.html";
    }

    @GetMapping(value = "/findById/")
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

    @GetMapping(value = "/list")
    public String showUsersListPage(ModelMap modelMap) {
        FindAllUsersResponse response = findAllUsersService.execute();
        modelMap.addAttribute("deleteByIdRequest", new DeleteUserByIdRequest());
        modelMap.addAttribute("users", response.getAllUsers());
        return "users/usersList.html";
    }

    @PostMapping(value = "/deleteById")
    public String processDeleteUserByIdRequest(@ModelAttribute(value = "deleteByIdRequest")
                                               DeleteUserByIdRequest request, ModelMap modelMap) {
        DeleteUserByIdResponse response = deleteUserByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteByIdRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("userDeleted", true);
        }
        return "redirect:/users/list";
    }

    @GetMapping(value = "/{id}/update")
    public String showUsersUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindUserByIdResponse response = findUserByIdService.execute(new FindUserByIdRequest(id));
        User user = response.getFoundUser().get();
        modelMap.addAttribute("user", user);
        return "users/usersUpdate.html";
    }

    @PostMapping(value = "/update")
    public String processUpdateUserRequest(@ModelAttribute(value = "user") User user, ModelMap modelMap) {
        UpdateUserRequest request = new UpdateUserRequest(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getPhone(), user.getRole());
        UpdateUserResponse response = updateUserService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedUser", response.getUpdatedUser());
        }
        return "users/usersUpdate.html";

    }

}

