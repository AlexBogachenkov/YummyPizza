package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yummypizza.core.domain.CartStatus;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.order.FindOrdersByUserIdRequest;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.responses.order.FindOrdersByUserIdResponse;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.services.order.FindOrdersByUserIdService;
import yummypizza.security.CustomUserDetails;

import java.util.Optional;

@Controller
@RequestMapping(value = "/my-orders")
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT') or hasRole('EMPLOYEE')")
public class UserOrdersPageController {

    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private FindOrdersByUserIdService findOrdersByUserIdService;

    @GetMapping(value = "")
    public String showUserOrdersPage(ModelMap modelMap) {
        CustomUserDetails userDetails = getAuthenticatedUserDetails().get();
        FindCartsByUserIdAndStatusRequest findCartsRequest = new FindCartsByUserIdAndStatusRequest(userDetails.getId(), CartStatus.ACTIVE);
        FindCartsByUserIdAndStatusResponse findCartsResponse = findCartsByUserIdAndStatusService.execute(findCartsRequest);
        modelMap.addAttribute("cart", findCartsResponse.getCarts().get(0));

        FindOrdersByUserIdRequest findOrdersRequest = new FindOrdersByUserIdRequest(userDetails.getId());
        FindOrdersByUserIdResponse findOrdersResponse = findOrdersByUserIdService.execute(findOrdersRequest);
        modelMap.addAttribute("userOrders", findOrdersResponse.getFoundOrders());

        return "orders/userOrdersPage.html";
    }

    private Optional<CustomUserDetails> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
