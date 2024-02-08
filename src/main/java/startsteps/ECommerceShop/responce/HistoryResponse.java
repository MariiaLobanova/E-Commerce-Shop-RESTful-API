package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.Order;
import startsteps.ECommerceShop.entities.OrderHistory;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private String message;
    private List<OrderHistory> orderHistories;
}
