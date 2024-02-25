package startsteps.ECommerceShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import startsteps.ECommerceShop.entities.Order;
import startsteps.ECommerceShop.entities.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProductList WHERE o.user.id = :userId")
    List<Order> findAllByUserWithOrderProducts(@Param("userId") Long userId);
}
