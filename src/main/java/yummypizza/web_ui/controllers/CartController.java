package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.requests.cart.DeleteCartByIdRequest;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.responses.cart.CreateCartResponse;
import yummypizza.core.responses.cart.DeleteCartByIdResponse;
import yummypizza.core.responses.cart.FindAllCartsResponse;
import yummypizza.core.responses.cart.UpdateCartResponse;
import yummypizza.core.services.cart.CreateCartService;
import yummypizza.core.services.cart.DeleteCartByIdService;
import yummypizza.core.services.cart.FindAllCartsService;
import yummypizza.core.services.cart.UpdateCartService;

@Controller
public class CartController {

    @Autowired
    private CreateCartService createCartService;
    @Autowired
    private FindAllCartsService findAllCartsService;
    @Autowired
    private DeleteCartByIdService deleteCartByIdService;
    @Autowired
    private UpdateCartService updateCartService;

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

    @GetMapping(value = "cartsList")
    public String showCartsListPage(ModelMap modelMap) {
        FindAllCartsResponse response = findAllCartsService.execute();
        modelMap.addAttribute("deleteByIdRequest", new DeleteCartByIdRequest());
        modelMap.addAttribute("carts", response.getAllCarts());
        return "carts/cartsList.html";
    }

    @PostMapping(value = "cartsDeleteById")
    public String processDeleteCartByIdRequest(@ModelAttribute(value = "deleteByIdRequest")
                                                   DeleteCartByIdRequest request, ModelMap modelMap) {
        DeleteCartByIdResponse response = deleteCartByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteByIdRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("isCartDeleted", true);
        }
        return showCartsListPage(modelMap);
    }

    @GetMapping(value = "/cartsUpdate")
    public String showCartsUpdatePage(@RequestParam(value = "id") Long id, ModelMap modelMap) {
        //FindCartByIdResponse response = findCartByIdService.execute(new FindCartByIdRequest(id));
        //Cart cart = response.getFoundCart().get();
        //modelMap.addAttribute("cart", cart);
        return "carts/cartsUpdate.html";
    }

    @PostMapping(value = "/cartsUpdate")
    public String processUpdateCartRequest(@ModelAttribute(value = "updateCartRequest") UpdateCartRequest request, ModelMap modelMap) {
        UpdateCartResponse response = updateCartService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedCart", response.getUpdatedCart());
        }
        return "carts/cartsUpdate.html";
    }

}
