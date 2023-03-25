package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.responses.cart.CreateCartResponse;
import yummypizza.core.services.cart.CreateCartService;

@Controller
public class CartController {

    @Autowired
    private CreateCartService createCartService;

    @GetMapping(value = "carts")
    public String showCartsPage() {
        return "carts/carts.html";
    }
    @GetMapping(value = "cartsCreate")
    public String showCartsCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateCartRequest());
        return "carts/cartsCreate.html";
    }

    @PostMapping(value = "cartsCreate")
    public String processCreateCartRequest(@ModelAttribute(value = "request") CreateCartRequest request, ModelMap modelMap) {
        CreateCartResponse response = createCartService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdCart", response.getCartCreated());
        }
        return "carts/cartsCreate.html";
    }

}
