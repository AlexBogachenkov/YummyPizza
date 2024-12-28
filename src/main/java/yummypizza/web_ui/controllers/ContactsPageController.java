package yummypizza.web_ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactsPageController {

    @GetMapping(value = "/contacts")
    public String showContactsPage() {
        return "contactsPage.html";
    }

}
