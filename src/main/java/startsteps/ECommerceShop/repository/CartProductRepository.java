package startsteps.ECommerceShop.repository;

import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.CartProduct;

public interface CartProductRepository {
    void addCartProduct(CartProduct cartProduct);
    void removeCartProduct(String cartProductId);
    void removeAllCartItems(Cart cart);
}
