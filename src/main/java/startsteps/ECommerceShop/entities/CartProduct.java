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

    public CartProduct(Long cartProductId, int quantity, double price, Product product, Cart cart) {
        this.cartProductId = cartProductId;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.cart = cart;
    }
}
