package startsteps.ECommerceShop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.Order;
import startsteps.ECommerceShop.entities.OrderStatus;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.repository.CartProductRepository;
import startsteps.ECommerceShop.repository.OrderRepository;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.responce.CartResponse;
import startsteps.ECommerceShop.responce.OrderResponse;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartProductRepository cartProductRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, CartService cartService, CartProductRepository cartProductRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartProductRepository = cartProductRepository;
    }

    @Transactional
    public OrderResponse placeOrder(User user){
        CartResponse cartResponse = cartService.getCart(user);

        if (cartResponse.getCartProducts().isEmpty()){
            return new OrderResponse("Can not place empty order", Collections.emptyList(),0.0);
        }

        Order order = new Order();
        order.setDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PAID);
        order.setUser(user);

        List<CartProduct> productsInCart= user.getCart().getCartProductList();

        List<CartProduct> productsInOrder = productsInCart.stream()
                .map(cartProduct -> cartProductRepository.findById(cartProduct.getCartProductId())
                        .orElseThrow(()->new IllegalStateException("Cart Products not found"))).collect(Collectors.toList());

        order.setOrderCartProducts(productsInOrder);
        order.setTotal(user.getCart().getTotalPrice());

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setMessage("Order placed successfully!");
        orderResponse.setCartProductResponseList(cartResponse.getCartProducts());
        orderResponse.setTotalPrice(cartResponse.getTotalPrice());

        return orderResponse;
    }
}
