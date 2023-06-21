package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.responses.cart_product.AddCartProductResponse;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.services.cart_product.AddCartProductService;
import yummypizza.core.services.product.FindAllProductsService;
import yummypizza.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/menu")
public class MenuController {

    @Autowired
    private FindAllProductsService findAllProductsService;
    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private AddCartProductService addCartProductService;

    @GetMapping()
    public String showMenuPage(ModelMap modelMap) {
        Optional<CustomUserDetails> optionalOfUserDetails = getAuthenticatedUserDetails();
        if (optionalOfUserDetails.isPresent()) {
            CustomUserDetails userDetails = optionalOfUserDetails.get();
            FindCartsByUserIdAndStatusRequest request = new FindCartsByUserIdAndStatusRequest(userDetails.getId(), CartStatus.ACTIVE);
            FindCartsByUserIdAndStatusResponse response = findCartsByUserIdAndStatusService.execute(request);
            modelMap.addAttribute("cart", response.getCarts().get(0));
        } else modelMap.addAttribute("cart", null);

        List<Product> allProducts = findAllProductsService.execute().getAllProducts();
        modelMap.addAttribute("products", allProducts);
        modelMap.addAttribute("addProductToCartRequest", new AddCartProductRequest());
        return "menuPage.html";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @PostMapping(value = "/addProductToCart")
    public String processAddProductToCartRequest(@ModelAttribute(value = "addProductToCartRequest")
                                                     AddCartProductRequest request, ModelMap modelMap) {
        AddCartProductResponse response = addCartProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("addProductToCartRequestErrors", response.getErrors());
        }
        return showMenuPage(modelMap);
    }

    private Optional<CustomUserDetails> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
