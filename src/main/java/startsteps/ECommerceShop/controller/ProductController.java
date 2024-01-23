package startsteps.ECommerceShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProduct(){
        return productService.getAllProduct();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteProductById(@PathVariable Long productId){
        productService.deleteProduct(productId);
    }

    @GetMapping("/{name}")
    public List<Product> findProductsByName(@PathVariable String name) {
        return productService.findProductsByName(name);
    }
}
