package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startsteps.ECommerceShop.entities.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
}
