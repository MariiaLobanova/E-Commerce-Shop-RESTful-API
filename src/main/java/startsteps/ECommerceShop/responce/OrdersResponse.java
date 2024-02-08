package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.Order;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponse {
    private String message;
    private List<Order> orders;
}
