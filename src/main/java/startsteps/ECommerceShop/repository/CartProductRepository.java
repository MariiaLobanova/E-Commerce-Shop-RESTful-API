package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.CartProduct;

import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository <CartProduct, Long> {
    Optional<CartProduct> findById(Long cartProductId);

    @Modifying
    @Query("DELETE FROM CartProduct c WHERE c.cartProductId = ?1")
    void deleteOneCartProduct(long cartProductId);

    @Modifying
    @Query("DELETE from CartProduct c where c.cart.cartId =?1")
    void deleteCartProductByCartId (long cartId);
}
