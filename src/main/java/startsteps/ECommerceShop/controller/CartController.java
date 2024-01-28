package startsteps.ECommerceShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.request.CartRequest;
import startsteps.ECommerceShop.responce.CartResponse;
import startsteps.ECommerceShop.service.AuthServiceImpl;
import startsteps.ECommerceShop.service.CartService;
import startsteps.ECommerceShop.service.UserService;
import startsteps.ECommerceShop.service.UserServiceImpl;

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
}
