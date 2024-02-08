package startsteps.ECommerceShop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @ToString.Exclude
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
    private List<CartProduct> cartProductList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProductList = new ArrayList<>();

}
