package startsteps.ECommerceShop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cartId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "cartProducts")
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CartProduct> cartProductList;

    @Column(name = "totalPrice")
    private double totalPrice;

}
