package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yummypizza.core.requests.order.CreateOrderRequest;
import yummypizza.core.responses.order.CreateOrderResponse;
import yummypizza.core.services.order.CreateOrderService;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private CreateOrderService createOrderService;

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

}
