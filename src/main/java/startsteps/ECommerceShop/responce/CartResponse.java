package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String message;
    private List<CartProductResponse> cartProducts;
    private double totalPrice;
}
