package startsteps.ECommerceShop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.responce.ProductResponse;
import startsteps.ECommerceShop.service.ProductService;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name= "Product managment system", description = "Endpoints for managing products")
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a paginated list of all products.")
    public ResponseEntity<ProductResponse> getAllProduct(
            @PageableDefault(page = 0,size = 5)Pageable pageable){
        Page<Product> productPage = productService.getAllProduct(pageable);
        ProductResponse response = new ProductResponse("List of all products: ",productPage.getContent());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new product", description = "Allows authorized users to add a new product.")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody Product product){
        Product addedProduct = productService.addProduct(product);
        ProductResponse response = new ProductResponse("Product added in a List succesfully!", List.of(addedProduct));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a product from List", description = "Allows authorized users to delete a product by its ID.")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable Long productId){
        productService.deleteProduct(productId);
        ProductResponse productResponse= new ProductResponse("Product with ID: "+ productId + " deleted succesfully!");
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/id/{productId}")
    @Operation(summary = "Get product by its ID", description = "Retrieve a product details by its ID.")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId){
        Optional<Product> productOptional = productService.getProductById(productId);
        ProductResponse productResponse = new ProductResponse("Product with ID: "+ productId,List.of(productOptional.get()));
        return ResponseEntity.ok().body(productResponse);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Find products by name", description = "Retrieve products by their name.")
    public ResponseEntity<ProductResponse> findProductsByName(@PathVariable String name) {
        List<Product> productList = productService.findProductsByName(name);
        ProductResponse response = new ProductResponse("Product with name: "+ name,productList);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{productId}")
    @Operation(summary = "Update product", description = "Allows authorized users to update a product's quantity.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <ProductResponse> updateProduct(@PathVariable Long productId,@RequestParam int quantity){
        Product updatedProduct = productService.updateProduct(productId,quantity);
        ProductResponse response = new ProductResponse("Product with ID: "+productId+" updated successfully! Quantity = "+quantity, List.of(updatedProduct));
        return ResponseEntity.ok(response);
    }
}
