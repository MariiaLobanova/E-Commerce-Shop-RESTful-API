package startsteps.ECommerceShop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @PastOrPresent
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private double total;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id" )
    private User user;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartProduct> cartProductList;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProductList;

    public Order(Long orderId, LocalDate date, OrderStatus orderStatus, double total) {
        this.orderId = orderId;
        this.date = date;
        this.orderStatus = orderStatus;
        this.total = total;
    }
}
