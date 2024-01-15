package startsteps.ECommerceShop.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import startsteps.ECommerceShop.entities.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SighUpRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}
