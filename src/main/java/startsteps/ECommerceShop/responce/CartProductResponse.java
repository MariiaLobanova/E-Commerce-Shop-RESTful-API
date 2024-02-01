package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {
    private Long productId;
    private String name;
    private int quantity;
    private double price;

    public CartProductResponse(CartProduct cartproduct) {
        this.productId = cartproduct.getProduct().getProductId();
        this.name = cartproduct.getProduct().getName();
        this.quantity = cartproduct.getQuantity();
        this.price = cartproduct.getPrice();
    }
}
