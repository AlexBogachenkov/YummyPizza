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
import yummypizza.core.domain.OrderStatus;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.requests.cart_product.FindCartProductsByCartIdRequest;
import yummypizza.core.requests.cart_product.UpdateCartProductRequest;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.responses.cart_product.FindCartProductsByCartIdResponse;
import yummypizza.core.responses.order.CreateOrderResponse;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.services.cart_product.FindCartProductsByCartIdService;
import yummypizza.core.services.order.CreateOrderService;
import yummypizza.security.CustomUserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
@RequestMapping(value = "/order/make")
public class MakeOrderPageController {

    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private FindCartProductsByCartIdService findCartProductsByCartIdService;
    @Autowired
    private CreateOrderService createOrderService;


    @GetMapping("")
    public String showMakeOrderPage(ModelMap modelMap) {
        CustomUserDetails userDetails = getAuthenticatedUserDetails().get();
        FindCartsByUserIdAndStatusRequest findCartsRequest = new FindCartsByUserIdAndStatusRequest(userDetails.getId(), CartStatus.ACTIVE);
        FindCartsByUserIdAndStatusResponse findCartsResponse = findCartsByUserIdAndStatusService.execute(findCartsRequest);
        modelMap.addAttribute("cart", findCartsResponse.getCarts().get(0));

        FindCartProductsByCartIdRequest findCartProductsRequest =
                new FindCartProductsByCartIdRequest(findCartsResponse.getCarts().get(0).getId());
        FindCartProductsByCartIdResponse findCartProductsResponse = findCartProductsByCartIdService.execute(findCartProductsRequest);
        List<CartProduct> userCartProducts = findCartProductsResponse.getCartProducts();
        modelMap.addAttribute("userCartProducts", userCartProducts);

        BigDecimal cartAmount = userCartProducts.stream()
                .map(cartProduct -> cartProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setAmount(cartAmount);
        modelMap.addAttribute("createOrderRequest", createOrderRequest);
        return "orders/makeOrderPage.html";
    }

    @PostMapping(value = "")
    public String processCreateOrderRequest(@ModelAttribute(value = "createOrderRequest") CreateOrderRequest request,
                                            ModelMap modelMap) {
        CustomUserDetails userDetails = getAuthenticatedUserDetails().get();
        FindCartsByUserIdAndStatusRequest findCartsRequest = new FindCartsByUserIdAndStatusRequest(userDetails.getId(), CartStatus.ACTIVE);
        FindCartsByUserIdAndStatusResponse findCartsResponse = findCartsByUserIdAndStatusService.execute(findCartsRequest);

        FindCartProductsByCartIdRequest findCartProductsRequest =
                new FindCartProductsByCartIdRequest(findCartsResponse.getCarts().get(0).getId());
        FindCartProductsByCartIdResponse findCartProductsResponse = findCartProductsByCartIdService.execute(findCartProductsRequest);
        List<CartProduct> userCartProducts = findCartProductsResponse.getCartProducts();

        BigDecimal cartAmount = userCartProducts.stream()
                .map(cartProduct -> cartProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setStatus(OrderStatus.RECEIVED);
        request.setAmount(cartAmount);
        request.setDateCreated(LocalDateTime.now());

        CreateOrderResponse response = createOrderService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("createOrderRequestErrors", response.getErrors());
            return showMakeOrderPage(modelMap);
        }
        return "/orders/successfullyPlacedOrderPage.html";
    }

    private Optional<CustomUserDetails> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
