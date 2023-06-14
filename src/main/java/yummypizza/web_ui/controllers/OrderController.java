package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.requests.order.FindOrderByIdRequest;
import yummypizza.core.responses.order.CreateOrderResponse;
import yummypizza.core.responses.order.FindAllOrdersResponse;
import yummypizza.core.responses.order.FindOrderByIdResponse;
import yummypizza.core.services.order.CreateOrderService;
import yummypizza.core.services.order.FindAllOrdersService;
import yummypizza.core.services.order.FindOrderByIdService;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private CreateOrderService createOrderService;
    @Autowired
    private FindAllOrdersService findAllOrdersService;
    @Autowired
    private FindOrderByIdService findOrderByIdService;

    @GetMapping(value = "")
    public String showOrdersPage() {
        return "orders/orders.html";
    }

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
    public String showOrdersListPage(ModelMap modelMap) {
        FindAllOrdersResponse response = findAllOrdersService.execute();
        //modelMap.addAttribute("deleteByIdRequest", new DeleteUserByIdRequest());
        modelMap.addAttribute("orders", response.getAllOrders());
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

}
