package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }
    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);
    }
    public List<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }
}

