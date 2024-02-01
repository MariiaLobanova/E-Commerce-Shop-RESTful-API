package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
