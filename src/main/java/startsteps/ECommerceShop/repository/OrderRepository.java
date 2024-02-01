package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startsteps.ECommerceShop.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
