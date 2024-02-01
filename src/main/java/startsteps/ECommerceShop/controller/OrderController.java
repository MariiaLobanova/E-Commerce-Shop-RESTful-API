package startsteps.ECommerceShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.responce.OrderResponse;
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

    @PostMapping("/place")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity <OrderResponse> placeOrder(){
        User user =authService.getAuthenticatedUser();
        OrderResponse orderResponse = orderService.placeOrder(user);

        return ResponseEntity.ok(orderResponse);
    }
}
