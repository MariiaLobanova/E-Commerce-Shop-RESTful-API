package startsteps.ECommerceShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.request.CartRequest;
import startsteps.ECommerceShop.responce.CartResponse;
import startsteps.ECommerceShop.service.AuthServiceImpl;
import startsteps.ECommerceShop.service.CartService;
import startsteps.ECommerceShop.service.UserServiceImpl;

import java.util.Set;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest cartRequest){
        User user =authService.getAuthenticatedUser();
        CartResponse cartResponse = cartService.addProductToCart(cartRequest,user);

        return ResponseEntity.ok(cartResponse);
    }

    @GetMapping("/mycart")
    @PreAuthorize("hasAuthority('USER')")
   public CartResponse getCart(User user){
        User user1 = authService.getAuthenticatedUser();
        CartResponse cartResponse = cartService.getCart(user1);
        return cartResponse;
    }
}
