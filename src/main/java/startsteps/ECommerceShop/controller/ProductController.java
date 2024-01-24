package startsteps.ECommerceShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.service.ProductService;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    //@Autowired
    //private ProductService productService;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public Page<Product> getAllProduct(
            @PageableDefault(page = 0,size = 5)Pageable pageable){
        return productService.getAllProduct(pageable);
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

    @GetMapping("/id/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Optional<Product> getProductById(@PathVariable Long productId){ return productService.getProductById(productId);}

    @GetMapping("/name/{name}")
    public List<Product> findProductsByName(@PathVariable String name) {
        return productService.findProductsByName(name);
    }
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product updateProduct(@PathVariable Long productId,@RequestParam int quantity){
        return productService.updateProduct(productId, quantity);
    }

}
