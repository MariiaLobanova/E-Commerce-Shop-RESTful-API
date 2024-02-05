package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String message;
    private List<CartProductResponse> cartProductResponseList;
    private double totalPrice;
}
