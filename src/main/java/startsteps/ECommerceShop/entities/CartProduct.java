package startsteps.ECommerceShop.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "cartId")
    private Cart cart;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "orderId")
    private Order order;
}
