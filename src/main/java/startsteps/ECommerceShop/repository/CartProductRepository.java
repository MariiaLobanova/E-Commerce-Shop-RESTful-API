package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.CartProduct;

import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository <CartProduct, Long> {
    Optional<CartProduct> findById(Long cartProductId);
}
