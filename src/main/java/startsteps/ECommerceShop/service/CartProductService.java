package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.repository.CartProductRepository;

@Service
@Transactional
@AllArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository;

    public CartProduct findById(Long cartProductId) throws Exception {
        return cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new Exception("Cart product not found with id: "));
    }

    public void deleteCartProduct(CartProduct cartProduct) {
        cartProductRepository.delete(cartProduct);
    }
}
