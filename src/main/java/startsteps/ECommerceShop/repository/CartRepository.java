package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.User;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findCartByCartId(Long cartId);
}
