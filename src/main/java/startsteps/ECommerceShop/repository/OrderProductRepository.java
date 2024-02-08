package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.OrderProduct;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<OrderProduct> findById(Long cartProductId);
}
