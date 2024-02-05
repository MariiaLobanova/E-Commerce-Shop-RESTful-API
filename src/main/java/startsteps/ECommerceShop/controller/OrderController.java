package startsteps.ECommerceShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import startsteps.ECommerceShop.entities.Order;
import startsteps.ECommerceShop.entities.OrderStatus;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.exceptions.OrderNotFoundException;
import startsteps.ECommerceShop.repository.OrderRepository;
import startsteps.ECommerceShop.responce.OrderResponse;
import startsteps.ECommerceShop.responce.OrderStatusResponse;
import startsteps.ECommerceShop.service.AuthServiceImpl;
import startsteps.ECommerceShop.service.OrderService;
import startsteps.ECommerceShop.service.UserServiceImpl;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/place")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity <OrderResponse> placeOrder(){
        User user =authService.getAuthenticatedUser();
        OrderResponse orderResponse = orderService.placeOrder(user);

        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/status/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <OrderStatusResponse> changeOrderStatus(@PathVariable Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with id "+ orderId+ " not found"));

        OrderStatusResponse orderStatusResponse = orderService.changeOrderStatus(order);

        return ResponseEntity.ok(orderStatusResponse);
    }
    @PutMapping("/cancel/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity <OrderStatusResponse> cancelOrder(@PathVariable Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with id "+ orderId+ " not found"));

        OrderStatusResponse orderStatusResponse = orderService.cancelOrder(order);

        return ResponseEntity.ok(orderStatusResponse);
    }
}
