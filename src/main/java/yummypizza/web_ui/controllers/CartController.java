package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.Cart;
import yummypizza.core.requests.cart.CreateCartRequest;
import yummypizza.core.requests.cart.DeleteCartByIdRequest;
import yummypizza.core.requests.cart.FindCartByIdRequest;
import yummypizza.core.requests.cart.UpdateCartRequest;
import yummypizza.core.responses.cart.*;
import yummypizza.core.services.cart.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/carts")
public class CartController {

    @Autowired
    private CreateCartService createCartService;
    @Autowired
    private FindAllCartsService findAllCartsService;
    @Autowired
    private DeleteCartByIdService deleteCartByIdService;
    @Autowired
    private UpdateCartService updateCartService;
    @Autowired
    private FindCartByIdService findCartByIdService;

    @GetMapping(value = "")
    public String showCartsPage() {
        return "carts/carts.html";
    }

    @GetMapping(value = "/create")
    public String showCartsCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateCartRequest());
        return "carts/cartsCreate.html";
    }

    @PostMapping(value = "/create")
    public String processCreateCartRequest(@ModelAttribute(value = "request") CreateCartRequest request, ModelMap modelMap) {
        CreateCartResponse response = createCartService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdCart", response.getCartCreated());
        }
        return "carts/cartsCreate.html";
    }

    @GetMapping(value = "/list")
    public String showCartsListPage(ModelMap modelMap) {
        FindAllCartsResponse response = findAllCartsService.execute();
        modelMap.addAttribute("deleteByIdRequest", new DeleteCartByIdRequest());
        modelMap.addAttribute("carts", response.getAllCarts());
        return "carts/cartsList.html";
    }

    @PostMapping(value = "/delete")
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

    @GetMapping(value = "/find")
    public String showCartsFindByIdPage() {
        return "carts/cartsFindById.html";
    }

    @GetMapping(value = "/find/")
    public String processFindCartByIdRequest(@RequestParam(value = "id", required = false) Long id, ModelMap modelMap) {
        FindCartByIdRequest request = new FindCartByIdRequest(id);
        FindCartByIdResponse response = findCartByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "carts/cartsFindById.html";
        }
        if (response.getFoundCart().isPresent()) {
            modelMap.addAttribute("foundCart", response.getFoundCart().get());
        } else {
            modelMap.addAttribute("cartNotFound", true);
        }
        return "carts/cartsFindById.html";
    }

    @GetMapping(value = "/{id}/update")
    public String showCartsUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindCartByIdResponse response = findCartByIdService.execute(new FindCartByIdRequest(id));
        Optional<Cart> optionalOfCart = response.getFoundCart();
        if (optionalOfCart.isEmpty()) {
            modelMap.addAttribute("cartToUpdateNotFound", true);
            return showCartsListPage(modelMap);
        } else {
            Cart cart = optionalOfCart.get();
            modelMap.addAttribute("updateCartRequest",
                    new UpdateCartRequest(cart.getId(), cart.getUser().getId(), cart.getStatus()));
            return "carts/cartsUpdate.html";
        }
    }

    @PostMapping(value = "/{id}/update")
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
