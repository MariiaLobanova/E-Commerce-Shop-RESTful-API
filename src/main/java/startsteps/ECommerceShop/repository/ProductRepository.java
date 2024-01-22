package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startsteps.ECommerceShop.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
