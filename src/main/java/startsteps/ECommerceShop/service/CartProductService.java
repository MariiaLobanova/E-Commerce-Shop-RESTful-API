package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.repository.CartProductRepository;

@AllArgsConstructor
@Data
public class CartProductService {

    @Autowired
    CartProductRepository cartProductRepository;
    public void addCartItem(CartProduct cartProduct) {
        cartProductRepository.addCartProduct(cartProduct);

    }
}
