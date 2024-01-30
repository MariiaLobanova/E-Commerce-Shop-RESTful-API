package startsteps.ECommerceShop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @ToString.Exclude
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "cartProducts")
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CartProduct> cartProductList = new ArrayList<>();

    @Column(name = "totalPrice")
    private double totalPrice;

}
