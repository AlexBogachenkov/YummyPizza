package yummypizza.web_ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.responses.product.CreateProductResponse;
import yummypizza.core.responses.product.DeleteProductByIdResponse;
import yummypizza.core.responses.product.FindAllProductsResponse;
import yummypizza.core.services.product.CreateProductService;
import yummypizza.core.services.product.DeleteProductByIdService;
import yummypizza.core.services.product.FindAllProductsService;

@Controller
public class ProductController {

    @Autowired
    private CreateProductService createProductService;
    @Autowired
    private FindAllProductsService findAllProductsService;
    @Autowired
    private DeleteProductByIdService deleteProductByIdService;

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

}
