package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.exceptions.ProductNotFoundException;
import startsteps.ECommerceShop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getAllProduct(Pageable pageable){
        log.info("Getting list of all products by paging: {}", pageable);
        return productRepository.findAll(pageable);
    }

    public Product addProduct(Product product){
        log.info("Adding product:{} to list of products", product);
        return productRepository.save(product);
    }
    public void deleteProduct(Long id){
        log.info("Deleting products by its id: {}", id);
        productRepository.deleteById(id);}
    public Optional<Product> getProductById(Long id){
        log.info("Getting product by id: {}", id);
        return productRepository.findById(id);
    }
    public List<Product> findProductsByName(String name) {
        log.info("Finding product by name: {}", name);
        return productRepository.findByName(name);
    }
    public Product updateProduct (Long id, int quantity){
        log.info("Updating quatities:{} of products by id: {}",quantity, id);
        return productRepository.findById(id).map(product -> {product.setQuantity(quantity);
            return productRepository.save(product);
        }).orElseThrow(()->new ProductNotFoundException("Product with ID " + id + " not found"));
    }
}

