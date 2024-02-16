package startsteps.ECommerceShop.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.Product;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String message;
    private List<Product> productList;

    public ProductResponse(String message) {
        this.message = message;
    }
}
