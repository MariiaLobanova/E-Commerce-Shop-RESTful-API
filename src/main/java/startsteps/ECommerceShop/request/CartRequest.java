package startsteps.ECommerceShop.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private int quantity;
}
