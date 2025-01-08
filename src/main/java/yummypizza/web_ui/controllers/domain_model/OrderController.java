package yummypizza.web_ui.controllers.domain_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.Order;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.requests.order.DeleteOrderByIdRequest;
import yummypizza.core.requests.order.FindOrderByIdRequest;
import yummypizza.core.requests.order.UpdateOrderRequest;
import yummypizza.core.requests.user.DeleteUserByIdRequest;
import yummypizza.core.responses.order.*;
import yummypizza.core.services.order.*;

import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private CreateOrderService createOrderService;
    @Autowired
    private FindAllOrdersService findAllOrdersService;
    @Autowired
    private FindOrderByIdService findOrderByIdService;
    @Autowired
    private DeleteOrderByIdService deleteOrderByIdService;
    @Autowired
    private UpdateOrderService updateOrderService;

    @GetMapping(value = "/create")
    public String showOrdersCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateOrderRequest());
        return "orders/ordersCreate.html";
    }

    @PostMapping(value = "/create")
    public String processCreateOrderRequest(@ModelAttribute(value = "request") CreateOrderRequest request, ModelMap modelMap) {
        CreateOrderResponse response = createOrderService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdOrder", response.getCreatedOrder());
        }
        return "orders/ordersCreate.html";
    }

    @GetMapping(value = "/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public String showOrdersListPage(ModelMap modelMap) {
        FindAllOrdersResponse response = findAllOrdersService.execute();
        modelMap.addAttribute("deleteByIdRequest", new DeleteUserByIdRequest());
        modelMap.addAttribute("orderDtos", response.getAllOrders());
        return "orders/ordersList.html";
    }

    @GetMapping(value = "/find")
    public String showOrdersFindByIdPage() {
        return "orders/ordersFindById.html";
    }

    @GetMapping(value = "/find/")
    public String processFindOrderByIdRequest(@RequestParam(value = "id", required = false) Long id, ModelMap modelMap) {
        FindOrderByIdRequest request = new FindOrderByIdRequest(id);
        FindOrderByIdResponse response = findOrderByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "orders/ordersFindById.html";
        }
        if (response.getFoundOrder().isPresent()) {
            modelMap.addAttribute("foundOrder", response.getFoundOrder().get());
        } else {
            modelMap.addAttribute("orderNotFound", true);
        }
        return "orders/ordersFindById.html";
    }

    @PostMapping(value = "/delete")
    public String processDeleteOrderByIdRequest(@ModelAttribute(value = "deleteByIdRequest")
                                                DeleteOrderByIdRequest request, ModelMap modelMap) {
        DeleteOrderByIdResponse response = deleteOrderByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteByIdRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("isOrderDeleted", true);
        }
        return showOrdersListPage(modelMap);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(value = "/{id}/update")
    public String showOrdersUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindOrderByIdResponse response = findOrderByIdService.execute(new FindOrderByIdRequest(id));
        Optional<Order> optionalOfOrder = response.getFoundOrder();
        if (optionalOfOrder.isEmpty()) {
            modelMap.addAttribute("orderToUpdateNotFound", true);
            return showOrdersListPage(modelMap);
        } else {
            Order order = optionalOfOrder.get();
            modelMap.addAttribute("updateOrderRequest",
                    new UpdateOrderRequest(order.getId(), order.getCart().getId(), order.getStatus(), order.getAmount(),
                            order.getDateCreated(), order.getDateCompleted(), order.isForTakeaway(), order.getCity(), order.getStreet(),
                            order.getBuildingNumber(), order.getApartmentNumber()));
            return "orders/ordersUpdate.html";
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping(value = "/{id}/update")
    public String processUpdateOrderRequest(@ModelAttribute(value = "updateOrderRequest") UpdateOrderRequest request, ModelMap modelMap) {
        UpdateOrderResponse response = updateOrderService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedOrder", response.getUpdatedOrder());
        }
        return "orders/ordersUpdate.html";
    }

}
