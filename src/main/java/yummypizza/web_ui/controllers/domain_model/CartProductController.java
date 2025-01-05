package yummypizza.web_ui.controllers.domain_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.CartProduct;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.requests.cart_product.DeleteCartProductByIdRequest;
import yummypizza.core.requests.cart_product.FindCartProductByIdRequest;
import yummypizza.core.requests.cart_product.UpdateCartProductRequest;
import yummypizza.core.responses.cart_product.*;
import yummypizza.core.services.cart_product.*;

import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/cart-products")
public class CartProductController {

    @Autowired
    private AddCartProductService addCartProductService;
    @Autowired
    private FindAllCartProductsService findAllCartProductsService;
    @Autowired
    private FindCartProductByIdService findCartProductByIdService;
    @Autowired
    private UpdateCartProductService updateCartProductService;
    @Autowired
    private DeleteCartProductByIdService deleteCartProductByIdService;

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

    @GetMapping(value = "/find")
    public String showCartProductsFindByIdPage() {
        return "cart_products/cartProductsFindById.html";
    }

    @GetMapping(value = "/find/")
    public String processFindCartProductByIdRequest(@RequestParam(value = "id", required = false) Long id, ModelMap modelMap) {
        FindCartProductByIdRequest request = new FindCartProductByIdRequest(id);
        FindCartProductByIdResponse response = findCartProductByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "cart_products/cartProductsFindById.html";
        }
        if (response.getFoundCartProduct().isPresent()) {
            modelMap.addAttribute("foundCartProduct", response.getFoundCartProduct().get());
        } else {
            modelMap.addAttribute("cartProductNotFound", true);
        }
        return "cart_products/cartProductsFindById.html";
    }

    @GetMapping(value = "/{id}/update")
    public String showCartProductsUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindCartProductByIdResponse response = findCartProductByIdService.execute(new FindCartProductByIdRequest(id));
        Optional<CartProduct> optionalOfCartProduct = response.getFoundCartProduct();
        if (optionalOfCartProduct.isEmpty()) {
            modelMap.addAttribute("cartProductToUpdateNotFound", true);
            return showCartProductsListPage(modelMap);
        } else {
            CartProduct cartProduct = optionalOfCartProduct.get();
            modelMap.addAttribute("updateCartProductRequest",
                    new UpdateCartProductRequest(cartProduct.getId(), cartProduct.getCart().getId(),
                            cartProduct.getProduct().getId(), cartProduct.getQuantity()));
            return "cart_products/cartProductsUpdate.html";
        }
    }

    @PostMapping(value = "/{id}/update")
    public String processUpdateCartProductRequest(@ModelAttribute(value = "updateCartProductRequest") UpdateCartProductRequest request, ModelMap modelMap) {
        UpdateCartProductResponse response = updateCartProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedCartProduct", response.getUpdatedCartProduct());
        }
        return "cart_products/cartProductsUpdate.html";
    }

    @PostMapping(value = "/delete")
    public String processDeleteCartProductByIdRequest(@ModelAttribute(value = "deleteByIdRequest")
                                                      DeleteCartProductByIdRequest request, ModelMap modelMap) {
        DeleteCartProductByIdResponse response = deleteCartProductByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteByIdRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("isCartProductDeleted", true);
        }
        return showCartProductsListPage(modelMap);
    }

}
