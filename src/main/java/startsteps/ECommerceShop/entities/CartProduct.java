package startsteps.ECommerceShop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cartProducts")
public class CartProduct {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cartProductId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartProductId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cartId")
    private Cart cart;

}
