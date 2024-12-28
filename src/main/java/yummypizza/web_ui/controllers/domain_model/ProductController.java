package yummypizza.web_ui.controllers.domain_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yummypizza.core.domain.Product;
import yummypizza.core.requests.product.CreateProductRequest;
import yummypizza.core.requests.product.DeleteProductByIdRequest;
import yummypizza.core.requests.product.FindProductByIdRequest;
import yummypizza.core.requests.product.UpdateProductRequest;
import yummypizza.core.responses.product.*;
import yummypizza.core.services.product.*;

import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping(value = "")
    public String showProductsPage() {
        return "products/products.html";
    }

    @GetMapping(value = "/create")
    public String showProductsCreatePage(ModelMap modelMap) {
        modelMap.addAttribute("request", new CreateProductRequest());
        return "products/productsCreate.html";
    }

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

    @GetMapping(value = "/list")
    public String showProductsListPage(ModelMap modelMap) {
        FindAllProductsResponse response = findAllProductsService.execute();
        modelMap.addAttribute("products", response.getAllProducts());
        modelMap.addAttribute("deleteByIdRequest", new DeleteProductByIdRequest());
        return "products/productsList.html";
    }

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

    @GetMapping(value = "/find")
    public String showProductsFindByIdPage() {
        return "products/productsFindById.html";
    }

    @GetMapping(value = "/find/")
    public String processFindProductByIdRequest(@RequestParam(value = "id", required = false) Long id, ModelMap modelMap) {
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

    @GetMapping(value = "/{id}/update")
    public String showProductsUpdatePage(@PathVariable("id") Long id, ModelMap modelMap) {
        FindProductByIdResponse response = findProductByIdService.execute(new FindProductByIdRequest(id));
        Optional<Product> foundProduct = response.getFoundProduct();
        if (foundProduct.isEmpty()) {
            modelMap.addAttribute("productToUpdateNotFound", true);
            return showProductsListPage(modelMap);
        } else {
            modelMap.addAttribute("request", createUpdateProductRequest(foundProduct.get()));
            return "products/productsUpdate.html";
        }
    }

    @PostMapping(value = "/{id}/update")
    public String processUpdateProductRequest(@ModelAttribute(value = "request") UpdateProductRequest request, ModelMap modelMap) {
        UpdateProductResponse response = updateProductService.execute(request);
        if (response.hasErrors()) {
            modelMap.addAttribute("errors", response.getErrors());
        } else {
            modelMap.addAttribute("updatedProduct", response.getUpdatedProduct());
        }
        return "products/productsUpdate.html";
    }

    private UpdateProductRequest createUpdateProductRequest(Product product) {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setId(product.getId());
        request.setName(product.getName());
        request.setDescription(product.getDescription());
        request.setPrice(product.getPrice());
        request.setType(product.getType());
        request.setImage(null);
        return request;
    }

}
