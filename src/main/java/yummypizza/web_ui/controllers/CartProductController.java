package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.responses.cart_product.AddCartProductResponse;
import yummypizza.core.responses.cart_product.FindAllCartProductsResponse;
import yummypizza.core.services.cart_product.AddCartProductService;
import yummypizza.core.services.cart_product.FindAllCartProductsService;

@Controller
@RequestMapping(value = "/cart-products")
public class CartProductController {

    @Autowired
    private AddCartProductService addCartProductService;
    @Autowired
    private FindAllCartProductsService findAllCartProductsService;

    @GetMapping(value = "")
    public String showCartProductsPage() {
        return "cart_products/cartProducts.html";
    }

    @GetMapping(value = "/add")
    public String showAddCartProductPage(ModelMap modelMap) {
        modelMap.addAttribute("request", new AddCartProductRequest());
        return "cart_products/cartProductsAdd.html";
    }

    @PostMapping(value = "/add")
    public String processAddCartProductRequest(@ModelAttribute(value = "request") AddCartProductRequest request, ModelMap modelMap) {
        AddCartProductResponse response = addCartProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("addedCartProduct", response.getCartProductAdded());
        }
        return "cart_products/cartProductsAdd.html";
    }

    @GetMapping(value = "/list")
    public String showCartProductsListPage(ModelMap modelMap) {
        FindAllCartProductsResponse response = findAllCartProductsService.execute();
//        modelMap.addAttribute("deleteByIdRequest", new DeleteCartProductByIdRequest());
        modelMap.addAttribute("cartProducts", response.getAllCartProducts());
        return "cart_products/cartProductsList.html";
    }

}
