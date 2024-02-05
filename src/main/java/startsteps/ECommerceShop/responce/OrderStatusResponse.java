package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.OrderStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponse {
    private String message;
    private List<CartProductResponse> cartProductResponseList;
    private double totalPrice;
    private OrderStatus orderStatus;
}
