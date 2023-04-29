package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.product.*;
import yummypizza.core.services.product.*;

@Controller
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

    @GetMapping(value = "products")
    public String showProductsPage() {
        return "products/products.html";
    }

    @GetMapping(value = "productsCreate")
    public String showProductsCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateProductRequest());
        return "products/productsCreate.html";
    }

    @PostMapping(value = "productsCreate")
    public String processCreateProductRequest(@ModelAttribute(value = "request") CreateProductRequest request, ModelMap modelMap) {
        CreateProductResponse response = createProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("createdProduct", response.getCreatedProduct());
        }
        return "products/productsCreate.html";
    }

    @GetMapping(value = "productsList")
    public String showProductsListPage(ModelMap modelMap) {
        FindAllProductsResponse response = findAllProductsService.execute();
        modelMap.addAttribute("products", response.getAllProducts());
        modelMap.addAttribute("deleteByIdRequest", new DeleteProductByIdRequest());
        return "products/productsList.html";
    }

    @PostMapping(value = "productsDeleteById")
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

    @GetMapping(value = "productsFindById")
    public String showProductsFindByIdPage() {
        return "products/productsFindById.html";
    }

    @GetMapping(value = "products/")
    public String processFindProductByIdRequest(@RequestParam(value = "id") Long id, ModelMap modelMap) {
        FindProductByIdRequest request = new FindProductByIdRequest(id);
        FindProductByIdResponse response = findProductByIdService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
            return "products/productsFindById.html";
        }
        if (response.getFoundProduct().isPresent()) {
            modelMap.addAttribute("foundProduct", response.getFoundProduct().get());
        } else {
            modelMap.addAttribute("productNotFound", true);
        }
        return "products/productsFindById.html";
    }

    @GetMapping(value = "/productsUpdate")
    public String showProductsUpdatePage(@RequestParam(value = "id") Long id, ModelMap modelMap) {
        FindProductByIdResponse response = findProductByIdService.execute(new FindProductByIdRequest(id));
        Product product = response.getFoundProduct().get();
        modelMap.addAttribute("product", product);
        return "products/productsUpdate.html";
    }

    @PostMapping(value = "/productsUpdate")
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

}
