package yummypizza.web_ui.controllers;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.requests.cart_product.FindCartProductsByCartIdRequest;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.responses.cart_product.AddCartProductResponse;
import yummypizza.core.responses.cart_product.FindCartProductsByCartIdResponse;
import yummypizza.core.responses.product.FindProductByIdResponse;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.services.cart_product.AddCartProductService;
import yummypizza.core.services.cart_product.FindCartProductsByCartIdService;
import yummypizza.core.services.product.FindProductByIdService;
import yummypizza.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/products")
public class ProductInformationPageController {

    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private FindCartProductsByCartIdService findCartProductsByCartIdService;
    @Autowired
    private FindProductByIdService findProductByIdService;
    @Autowired
    private AddCartProductService addCartProductService;

    @PermitAll
    @GetMapping(value = "/{id}")
    public String showProductInformationPage(@PathVariable(value = "id", required = false) Long id, ModelMap modelMap) {
        Optional<CustomUserDetails> optionalOfUserDetails = getAuthenticatedUserDetails();
        if (optionalOfUserDetails.isPresent()) {
            CustomUserDetails userDetails = optionalOfUserDetails.get();
            FindCartsByUserIdAndStatusRequest findCartsRequest = new FindCartsByUserIdAndStatusRequest(userDetails.getId(), CartStatus.ACTIVE);
            FindCartsByUserIdAndStatusResponse findCartsResponse = findCartsByUserIdAndStatusService.execute(findCartsRequest);
            modelMap.addAttribute("cart", findCartsResponse.getCarts().get(0));

            FindCartProductsByCartIdRequest findCartProductsRequest =
                    new FindCartProductsByCartIdRequest(findCartsResponse.getCarts().get(0).getId());
            FindCartProductsByCartIdResponse findCartProductsResponse = findCartProductsByCartIdService.execute(findCartProductsRequest);
            List<CartProduct> userCartProducts = findCartProductsResponse.getCartProducts();
            modelMap.addAttribute("userCartProducts", userCartProducts);
        } else modelMap.addAttribute("cart", null);
        modelMap.addAttribute("addProductToCartRequest", new AddCartProductRequest());

        FindProductByIdRequest request = new FindProductByIdRequest(id);
        FindProductByIdResponse response = findProductByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "products/productInformationPage.html";
        }
        if (response.getFoundProduct().isPresent()) {
            modelMap.addAttribute("foundProduct", response.getFoundProduct().get());
        } else {
            modelMap.addAttribute("productNotFound", true);
        }
        return "products/productInformationPage.html";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @PostMapping(value = "/{id}/addProductToCart")
    public String processAddProductToCartRequest(@ModelAttribute(value = "addProductToCartRequest")
                                                 AddCartProductRequest request, ModelMap modelMap) {
        AddCartProductResponse response = addCartProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("addProductToCartRequestErrors", response.getErrors());
        }
        return "redirect:/products/" + request.getProductId();
    }

    private Optional<CustomUserDetails> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
