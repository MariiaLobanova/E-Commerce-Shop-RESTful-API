package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {
    private Long productId;
    private String name;
    private int quantity;
    private double price;

    public CartProductResponse(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
    }
}
