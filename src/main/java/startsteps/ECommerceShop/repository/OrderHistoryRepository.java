package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.OrderHistory;
import startsteps.ECommerceShop.entities.User;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository <OrderHistory, Long> {
    List<OrderHistory> findByOrderUser(User user);

}
