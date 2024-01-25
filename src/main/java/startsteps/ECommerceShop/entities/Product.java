package startsteps.ECommerceShop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    private static final long serialVersionUID = 1L;
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Min(0)
    @Column(name = "price")
    double price;
    @Min(0)
    @Column(name = "quantity")
    int quantity;
    @Column(name = "cartProducts")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CartProduct> cartProductList = new ArrayList<>();
}
