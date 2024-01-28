package startsteps.ECommerceShop.repository;

import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.CartProduct;

@Repository
public interface CartProductRepository {
    void addCartProduct(CartProduct cartProduct);
    void removeCartProduct(String cartProductId);
    void removeAllCartItems(Cart cart);
}
