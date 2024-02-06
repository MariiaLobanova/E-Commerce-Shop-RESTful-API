package startsteps.ECommerceShop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.*;
import startsteps.ECommerceShop.exceptions.ProductNotFoundException;
import startsteps.ECommerceShop.repository.CartProductRepository;
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

    @Transactional
    public OrderResponse placeOrder(User user) {
        CartResponse cartResponse = cartService.getCart(user);

        if (cartResponse.getCartProducts().isEmpty()) {
            return new OrderResponse("Can not place empty order", Collections.emptyList(), 0.0);
        }
        List<CartProductResponse> cartProducts = cartResponse.getCartProducts();
        for (CartProductResponse cartProductResponse : cartProducts) {
            Long productId = cartProductResponse.getProductId();
            int orderQuantity = cartProductResponse.getQuantity();

            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product with Id " + productId + " not found"));
            if (product.getQuantity() < orderQuantity) {
                return new OrderResponse("Insufficient stock for product " + product.getName(), cartProducts, 0.0);
            }
        }

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PAID);
        order.setUser(user);

        List<CartProduct> productsInCart = user.getCart().getCartProductList();

        List<CartProduct> productsInOrder = productsInCart.stream()
                .map(cartProduct -> cartProductRepository.findById(cartProduct.getCartProductId())
                        .orElseThrow(() -> new IllegalStateException("Cart not found"))).collect(Collectors.toList());
        order.setOrderCartProducts(productsInOrder);
        order.setTotal(user.getCart().getTotalPrice());

        Long orderId = order.getOrderId();// i need set orderID???

        order.getOrderCartProducts().forEach(cartProduct -> cartProduct.setOrder(null);//here??
        order.getOrderCartProducts().clear();

        orderRepository.save(order);

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
    public OrdersResponse getAllOrders(User user){
        List<Order> orders = orderRepository.findAllByUser(user);

        if (orders.isEmpty()){
            return new OrdersResponse("You don't have any placed orders yet", Collections.emptyList());
        }
        List<Order> fetchedOrders = new ArrayList<>();
        for (Order order : orders) {
            Order fetchedOrder = orderRepository.findById(order.getOrderId()).orElseThrow(null);
            for(CartProduct cp: fetchedOrder.getOrderCartProducts()){
                Product product = cp.getProduct();
                cp.setProduct(product);
            }
                fetchedOrders.add(fetchedOrder);
        }
        return new OrdersResponse("Orders details retrieved successfully",fetchedOrders);
    }
}
