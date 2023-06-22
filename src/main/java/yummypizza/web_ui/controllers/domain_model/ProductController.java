package yummypizza.web_ui.controllers.domain_model;

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
import yummypizza.core.domain.Product;
import yummypizza.core.requests.cart.FindCartsByUserIdAndStatusRequest;
import yummypizza.core.requests.cart_product.AddCartProductRequest;
import yummypizza.core.requests.cart_product.FindCartProductsByCartIdRequest;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.cart.FindCartsByUserIdAndStatusResponse;
import yummypizza.core.responses.cart_product.FindCartProductsByCartIdResponse;
import yummypizza.core.responses.product.*;
import yummypizza.core.services.cart.FindCartsByUserIdAndStatusService;
import yummypizza.core.services.cart_product.FindCartProductsByCartIdService;
import yummypizza.core.services.product.*;
import yummypizza.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private CreateProductService createProductService;
    @Autowired
    private FindAllProductsService findAllProductsService;
    @Autowired
    private DeleteProductByIdService deleteProductByIdService;
    @Autowired
    private FindProductByIdService findProductByIdService;
    @Autowired
    private UpdateProductService updateProductService;
    @Autowired
    private FindCartsByUserIdAndStatusService findCartsByUserIdAndStatusService;
    @Autowired
    private FindCartProductsByCartIdService findCartProductsByCartIdService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "")
    public String showProductsPage() {
        return "products/products.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/create")
    public String showProductsCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateProductRequest());
        return "products/productsCreate.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create")
    public String processCreateProductRequest(@ModelAttribute(value = "request") CreateProductRequest request, ModelMap modelMap) {
        CreateProductResponse response = createProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdProduct", response.getCreatedProduct());
        }
        return "products/productsCreate.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/list")
    public String showProductsListPage(ModelMap modelMap) {
        FindAllProductsResponse response = findAllProductsService.execute();
        modelMap.addAttribute("products", response.getAllProducts());
        modelMap.addAttribute("deleteByIdRequest", new DeleteProductByIdRequest());
        return "products/productsList.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/delete")
    public String processDeleteProductByIdRequest(@ModelAttribute(value = "deleteByIdRequest")
                                                      DeleteProductByIdRequest request, ModelMap modelMap) {
        DeleteProductByIdResponse response = deleteProductByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("deleteByIdRequestErrors", response.getErrors());
        } else {
            modelMap.addAttribute("productDeleted", true);
        }
        return showProductsListPage(modelMap);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/find")
    public String showProductsFindByIdPage() {
        return "products/productsFindById.html";
    }

    @PermitAll
    @GetMapping(value = "/{id}")
    public String processFindProductByIdRequest(@PathVariable(value = "id", required = false) Long id, ModelMap modelMap) {
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
            return "products/productPage.html";
        }
        if (response.getFoundProduct().isPresent()) {
            modelMap.addAttribute("foundProduct", response.getFoundProduct().get());
        } else {
            modelMap.addAttribute("productNotFound", true);
        }
        return "products/productPage.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}/update")
    public String showProductsUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindProductByIdResponse response = findProductByIdService.execute(new FindProductByIdRequest(id));
        Optional<Product> foundProduct = response.getFoundProduct();
        if (foundProduct.isEmpty()) {
            modelMap.addAttribute("productToUpdateNotFound", true);
            return showProductsListPage(modelMap);
        } else {
            modelMap.addAttribute("product", foundProduct.get());
            return "products/productsUpdate.html";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{id}/update")
    public String processUpdateProductRequest(@ModelAttribute(value = "product") Product product, ModelMap modelMap) {
        UpdateProductRequest request = new UpdateProductRequest(product.getId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getType());
        UpdateProductResponse response = updateProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedProduct", response.getUpdatedProduct());
        }
        return "products/productsUpdate.html";
    }

    private Optional<CustomUserDetails> getAuthenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

}
