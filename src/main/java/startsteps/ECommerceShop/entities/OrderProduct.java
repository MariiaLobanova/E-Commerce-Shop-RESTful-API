package startsteps.ECommerceShop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orderproducts")
public class OrderProduct {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    private String name;
    double price;
    int quantity;
    @JsonIgnore
    private OrderStatus orderStatus;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
