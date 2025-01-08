package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.requests.cart_product.FindCartProductsByCartIdRequest;
import yummypizza.core.requests.cart_product.UpdateCartProductRequest;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.responses.cart_product.DeleteCartProductByIdResponse;
import yummypizza.core.responses.cart_product.FindCartProductsByCartIdResponse;
import yummypizza.core.responses.cart_product.UpdateCartProductResponse;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.services.cart_product.DeleteCartProductByIdService;
import yummypizza.core.services.cart_product.FindCartProductsByCartIdService;
import yummypizza.core.services.cart_product.UpdateCartProductService;
import yummypizza.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT') or hasRole('EMPLOYEE')")
@RequestMapping(value = "/cart")
public class UserCartPageController {

    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private FindCartProductsByCartIdService findCartProductsByCartIdService;
    @Autowired
    private DeleteCartProductByIdService deleteCartProductByIdService;
    @Autowired
    private UpdateCartProductService updateCartProductService;


    @GetMapping("")
    public String showUserCartPage(ModelMap modelMap) {
        CustomUserDetails userDetails = getAuthenticatedUserDetails().get();
        FindCartsByUserIdAndStatusRequest findCartsRequest = new FindCartsByUserIdAndStatusRequest(userDetails.getId(), CartStatus.ACTIVE);
        FindCartsByUserIdAndStatusResponse findCartsResponse = findCartsByUserIdAndStatusService.execute(findCartsRequest);
        modelMap.addAttribute("cart", findCartsResponse.getCarts().get(0));

        FindCartProductsByCartIdRequest findCartProductsRequest =
                new FindCartProductsByCartIdRequest(findCartsResponse.getCarts().get(0).getId());
        FindCartProductsByCartIdResponse findCartProductsResponse = findCartProductsByCartIdService.execute(findCartProductsRequest);
        List<CartProduct> userCartProducts = findCartProductsResponse.getCartProducts();
        modelMap.addAttribute("userCartProducts", userCartProducts);
        modelMap.addAttribute("deleteCartProductRequest", new DeleteCartProductByIdRequest());
        modelMap.addAttribute("updateCartProductRequest", new UpdateCartProductRequest());
        return "carts/userCartPage.html";
    }

    @PostMapping(value = "/deleteCartProduct")
    public String processDeleteCartProductRequest(@ModelAttribute(value = "deleteByIdRequest")
                                                      DeleteCartProductByIdRequest request, ModelMap modelMap) {
        DeleteCartProductByIdResponse response = deleteCartProductByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteCartProductRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("isCartProductDeleted", true);
        }
        return showUserCartPage(modelMap);
    }

    @PostMapping(value = "/updateCartProductQuantity")
    public String processUpdateCartProductQuantityRequest(@ModelAttribute(value = "updateCartProductRequest") UpdateCartProductRequest request, ModelMap modelMap) {
        UpdateCartProductResponse response = updateCartProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("updatedCartProductRequestErrors", response.getErrors());
        }
        return showUserCartPage(modelMap);
    }


    private Optional<CustomUserDetails> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
