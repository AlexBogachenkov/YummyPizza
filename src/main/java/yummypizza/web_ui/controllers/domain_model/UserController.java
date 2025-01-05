package yummypizza.web_ui.controllers.domain_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.User;
import yummypizza.core.requests.user.*;
import yummypizza.core.responses.user.*;
import yummypizza.core.services.user.*;

import java.util.Optional;

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
    @Autowired
    private UpdateUserProfileInformationService updateUserProfileInformationService;

    @GetMapping(value = "/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUsersCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateUserRequest());
        return "users/usersCreate.html";
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String processCreateUserRequest(@ModelAttribute(value = "request") CreateUserRequest request, ModelMap modelMap) {
        CreateUserResponse response = createUserService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdUser", response.getCreatedUser());
        }
        return "users/usersCreate.html";
    }

    @GetMapping(value = "/find")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUsersFindByIdPage() {
        return "users/usersFindById.html";
    }

    @GetMapping(value = "/find/")
    @PreAuthorize("hasRole('ADMIN')")
    public String processFindUserByIdRequest(@RequestParam(value = "id", required = false) Long id, ModelMap modelMap) {
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
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public String showUsersListPage(ModelMap modelMap) {
        FindAllUsersResponse response = findAllUsersService.execute();
        modelMap.addAttribute("deleteByIdRequest", new DeleteUserByIdRequest());
        modelMap.addAttribute("users", response.getAllUsers());
        return "users/usersList.html";
    }

    @PostMapping(value = "/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String processDeleteUserByIdRequest(@ModelAttribute(value = "deleteByIdRequest")
                                               DeleteUserByIdRequest request, ModelMap modelMap) {
        DeleteUserByIdResponse response = deleteUserByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteByIdRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("userDeleted", true);
        }
        return showUsersListPage(modelMap);
    }

    @GetMapping(value = "/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUsersUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindUserByIdResponse response = findUserByIdService.execute(new FindUserByIdRequest(id));
        Optional<User> foundUser = response.getFoundUser();
        if (foundUser.isEmpty()) {
            modelMap.addAttribute("userToUpdateNotFound", true);
            return showUsersListPage(modelMap);
        } else {
            foundUser.get().setPassword(null);
            modelMap.addAttribute("user", foundUser.get());
            return "users/usersUpdate.html";
        }
    }

    @PostMapping(value = "/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping(value = "/{id}")
    @PreAuthorize("#id == authentication.getPrincipal().getId()")
    public String showUserProfilePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindUserByIdResponse response = findUserByIdService.execute(new FindUserByIdRequest(id));
        Optional<User> foundUser = response.getFoundUser();
        if (foundUser.isEmpty()) {
            modelMap.addAttribute("userToUpdateNotFound", true);
        } else {
            modelMap.addAttribute("request", createUpdateUserProfileInformationRequest(foundUser.get()));
        }
        return "users/userProfilePage.html";
    }

    @PostMapping(value = "/{id}")
    @PreAuthorize("#id == authentication.getPrincipal().getId()")
    public String processUpdateUserProfileInformationRequest(@PathVariable("id") Long id, @ModelAttribute(value = "request") UpdateUserProfileInformationRequest request, ModelMap modelMap) {
        UpdateUserProfileInformationResponse response = updateUserProfileInformationService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedUser", response.getUpdatedUser());
        }
        return "users/userProfilePage.html";
    }

    private UpdateUserProfileInformationRequest createUpdateUserProfileInformationRequest(User user) {
        UpdateUserProfileInformationRequest request = new UpdateUserProfileInformationRequest();
        request.setId(user.getId());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setEmail(user.getEmail());
        request.setPhone(user.getPhone());
        request.setRole(user.getRole());
        return request;
    }

}

