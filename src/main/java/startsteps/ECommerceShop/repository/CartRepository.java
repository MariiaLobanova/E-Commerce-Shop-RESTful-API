package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.Product;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
