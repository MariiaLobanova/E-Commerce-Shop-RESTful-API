package startsteps.ECommerceShop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.*;
import startsteps.ECommerceShop.exceptions.CartNotFoundException;
import startsteps.ECommerceShop.exceptions.ProductNotFoundException;
import startsteps.ECommerceShop.repository.*;
import startsteps.ECommerceShop.responce.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor

public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartProductRepository cartProductRepository;
    private final ProductService productService;
    private final OrderProductRepository orderProductRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    @Transactional
    public OrderResponse placeOrder(User user) {
        log.info("Placing order for user: {}", user.getUsername());
        log.debug("Details: {}", user);
        CartResponse cartResponse = cartService.getCart(user);

        if (cartResponse.getCartProducts().isEmpty()) {
            return new OrderResponse("Can not place empty order", Collections.emptyList(), 0.0);
        }
        List<CartProductResponse> cartProducts = cartResponse.getCartProducts();
        List<OrderProduct> orderProducts = new ArrayList<>();
        double totalPrice = 0.0;

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PAID);
        order.setUser(user);
        order = orderRepository.save(order);

        for (CartProductResponse cartProductResponse : cartProducts) {
            Long productId = cartProductResponse.getProductId();
            int orderQuantity = cartProductResponse.getQuantity();

            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product with Id " + productId + " not found"));

            if (orderQuantity > product.getQuantity()) {
                return new OrderResponse("Insufficient stock for product " + product.getName(), Collections.emptyList(), 0.0);
            }
            int newQuantity = product.getQuantity() - orderQuantity;
            productService.updateProduct(productId, newQuantity);


            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setName(product.getName());
            orderProduct.setPrice(product.getPrice());
            orderProduct.setQuantity(orderQuantity);
            orderProduct.setOrderStatus(OrderStatus.PAID);
            orderProduct.setOrder(order);
            totalPrice += orderProduct.getPrice() * orderProduct.getQuantity();
            orderProducts.add(orderProduct);

            order.setTotal(totalPrice);
            order = orderRepository.save(order);
            orderProductRepository.saveAll(orderProducts);
        }

        cartService.clearCart(user);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setMessage("Order placed successfully!");
        orderResponse.setCartProductResponseList(cartResponse.getCartProducts());
        orderResponse.setTotalPrice(cartResponse.getTotalPrice());

        return orderResponse;
    }
    @Transactional
    public OrderStatusResponse changeOrderStatus(Order order) {
        log.info("Changing order status for orderId: {}", order.getOrderId());
        log.debug("Order details: {}", order);
        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus newStatus;

        if (currentStatus == OrderStatus.PAID) {
            newStatus = OrderStatus.DISPATCHED;
        } else if (currentStatus == OrderStatus.DISPATCHED) {
            newStatus = OrderStatus.IN_TRANSIT;
        }else if (currentStatus == OrderStatus.IN_TRANSIT) {
            newStatus = OrderStatus.DELIVERED;
        } else if (currentStatus == OrderStatus.DELIVERED) {
            newStatus = OrderStatus.CLOSED;
        }else {
            throw new IllegalStateException("Invalid state transition for order status");
        }
        order.setOrderStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        OrderHistory history= new OrderHistory();
        history.setOldStatus(currentStatus);
        history.setNewStatus(newStatus);
        history.setModifyDate(LocalDateTime.now());
        history.setOrder(updatedOrder);
        orderHistoryRepository.save(history);

        return new OrderStatusResponse("Order status changed successfully!",
                Collections.emptyList(),
                order.getTotal(),
                order.getOrderStatus());
    }
    @Transactional
    public OrderStatusResponse cancelOrder(Order order){
        log.info("Cancelling order for orderId: {}", order.getOrderId());
        log.debug("Details of cancelling order: {}", order);
        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus newStatus;

        if (currentStatus == OrderStatus.PAID) {
            newStatus = OrderStatus.CANCELLED;
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("Sorry, you can not cancel your order since it is dispatched");
        }
        OrderHistory history= new OrderHistory();

        history.setOldStatus(currentStatus);
        history.setNewStatus(newStatus);
        history.setModifyDate(LocalDateTime.now());
        history.setOrder(order);
        orderHistoryRepository.save(history);

        return new OrderStatusResponse("Your order is cancelled",
                Collections.emptyList(),
                order.getTotal(),
                order.getOrderStatus());
    }

     @Transactional
     public OrdersResponse getAllOrders(User user) {
        log.info("Retrieving all orders for user: {}", user.getUsername());
         log.debug("Get all order details: {}", user);
         List<Order> orders = orderRepository.findAllByUserWithOrderProducts(user);

         if (orders.isEmpty()) {
             return new OrdersResponse("You don't have any placed orders yet", Collections.emptyList());
         }
         return new OrdersResponse("Orders details retrieved successfully", orders);
     }

     @Transactional(readOnly = true)
    public HistoryResponse getHistory(User user){
         log.info("Retrieving history of orders for user: {}", user.getUsername());
         log.debug("get whole hystory details: {}", user);
        List<OrderHistory> orderHistories = orderHistoryRepository.findByOrderUser(user);
        return new HistoryResponse("Order history retrieved successfully", orderHistories);
     }

}
