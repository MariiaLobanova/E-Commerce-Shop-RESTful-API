package startsteps.ECommerceShop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.*;
import startsteps.ECommerceShop.exceptions.CartNotFoundException;
import startsteps.ECommerceShop.exceptions.ProductNotFoundException;
import startsteps.ECommerceShop.repository.CartProductRepository;
import startsteps.ECommerceShop.repository.OrderProductRepository;
import startsteps.ECommerceShop.repository.OrderRepository;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.responce.*;

import java.time.LocalDate;
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

    @Transactional
    public OrderResponse placeOrder(User user) {
        log.info("Placing order for user: {}", user.getUsername());
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
            if (product.getQuantity() < orderQuantity) {
                return new OrderResponse("Insufficient stock for product " + product.getName(), cartProducts, 0.0);
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setName(product.getName());
            orderProduct.setPrice(product.getPrice());
            orderProduct.setQuantity(orderQuantity);
            orderProduct.setOrderStatus(OrderStatus.PAID);
            orderProduct.setOrder(order);
            totalPrice += orderProduct.getPrice() * orderProduct.getQuantity();
            orderProducts.add(orderProduct);
        }
        order.setTotal(totalPrice);
        orderProductRepository.saveAll(orderProducts);

        for (CartProductResponse cartProductResponse : cartProducts) {
            Long productId = cartProductResponse.getProductId();
            int orderQuantity = cartProductResponse.getQuantity();
            productService.updateProduct(productId, orderQuantity);
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
        OrderStatus currentStatus = order.getOrderStatus();

        if (currentStatus == OrderStatus.PAID) {
            order.setOrderStatus(OrderStatus.DISPATCHED);
            orderRepository.save(order);
        } else if (currentStatus == OrderStatus.DISPATCHED) {
            order.setOrderStatus(OrderStatus.IN_TRANSIT);
            orderRepository.save(order);
        }else if (currentStatus == OrderStatus.IN_TRANSIT) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
        } else if (currentStatus == OrderStatus.DELIVERED) {
            order.setOrderStatus(OrderStatus.CLOSED);
            orderRepository.save(order);
        }else {
            throw new IllegalStateException("Invalid state transition for order status");
        }
        return new OrderStatusResponse("Order status changed successfully!",
                Collections.emptyList(),
                order.getTotal(),
                order.getOrderStatus());
    }
    @Transactional
    public OrderStatusResponse cancelOrder(Order order){
        OrderStatus currentStatus = order.getOrderStatus();

        if (currentStatus == OrderStatus.PAID) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new IllegalStateException("Sorry, you can not cancel your order since it is dispatched");
        }
        return new OrderStatusResponse("Your order is cancelled",
                Collections.emptyList(),
                order.getTotal(),
                order.getOrderStatus());
    }

  @Transactional
  public OrdersResponse getAllOrders(User user) {
      List<Order> orders = orderRepository.findAllByUserWithOrderProducts(user);

      if (orders.isEmpty()) {
          return new OrdersResponse("You don't have any placed orders yet", Collections.emptyList());
      }
      return new OrdersResponse("Orders details retrieved successfully", orders);
  }
}
