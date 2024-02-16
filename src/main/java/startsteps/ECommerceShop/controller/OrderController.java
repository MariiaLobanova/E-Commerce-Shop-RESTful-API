package startsteps.ECommerceShop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import startsteps.ECommerceShop.entities.Order;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.exceptions.OrderNotFoundException;
import startsteps.ECommerceShop.repository.OrderRepository;
import startsteps.ECommerceShop.responce.HistoryResponse;
import startsteps.ECommerceShop.responce.OrderResponse;
import startsteps.ECommerceShop.responce.OrderStatusResponse;
import startsteps.ECommerceShop.responce.OrdersResponse;
import startsteps.ECommerceShop.service.AuthServiceImpl;
import startsteps.ECommerceShop.service.OrderService;
import startsteps.ECommerceShop.service.UserServiceImpl;

import java.util.List;

@RestController
@Tag(name= "Order managment system", description = "Endpoints for managing user orders.")
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
    @Operation(summary = "Place an order", description = "Allows authenticated users to place an order.")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity <OrderResponse> placeOrder(){
        User user =authService.getAuthenticatedUser();
        OrderResponse orderResponse = orderService.placeOrder(user);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/status/{orderId}")
    @Operation(summary = "Change order status", description = "Allows authorized users to change the status of an order.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity <OrderStatusResponse> changeOrderStatus(@PathVariable Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with id "+ orderId+ " not found"));
        OrderStatusResponse orderStatusResponse = orderService.changeOrderStatus(order);
        return ResponseEntity.ok(orderStatusResponse);
    }

    @PutMapping("/cancel/{orderId}")
    @Operation(summary = "Cancel order", description = "Allows authenticated users to cancel their own order.")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity <OrderStatusResponse> cancelOrder(@PathVariable Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with id "+ orderId+ " not found"));
        OrderStatusResponse orderStatusResponse = orderService.cancelOrder(order);
        return ResponseEntity.ok(orderStatusResponse);
    }

    @GetMapping("/getorders")
    @Operation(summary = "Get all orders", description = "Retrieve all orders placed by the authenticated user.")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity <OrdersResponse> getAllOrders (){
        User user = authService.getAuthenticatedUser();
        OrdersResponse orders = orderService.getAllOrders(user);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/gethistory")
    @Operation(summary = "Get order history", description = "Retrieve order history for the authenticated user.")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<HistoryResponse> getHistory(){
        User user = authService.getAuthenticatedUser();
        HistoryResponse response = orderService.getHistory(user);
        return ResponseEntity.ok(response);
    }
}
