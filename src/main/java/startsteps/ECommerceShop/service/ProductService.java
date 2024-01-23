package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getAllProduct(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }
    public void deleteProduct(Long id){productRepository.deleteById(id);}
    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }
    public List<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }
}

