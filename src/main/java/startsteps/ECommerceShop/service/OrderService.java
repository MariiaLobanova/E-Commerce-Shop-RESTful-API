package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.*;
import startsteps.ECommerceShop.exceptions.ProductNotFoundException;
import startsteps.ECommerceShop.repository.CartProductRepository;
import startsteps.ECommerceShop.repository.OrderRepository;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.responce.CartProductResponse;
import startsteps.ECommerceShop.responce.CartResponse;
import startsteps.ECommerceShop.responce.OrderResponse;

import java.time.LocalDate;
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
    public OrderResponse placeOrder(User user){
        CartResponse cartResponse = cartService.getCart(user);

        if (cartResponse.getCartProducts().isEmpty()){
            return new OrderResponse("Can not place empty order", Collections.emptyList(),0.0);
        }
        List <CartProductResponse> cartProducts = cartResponse.getCartProducts();
        for(CartProductResponse cartProductResponse: cartProducts){
            Long productId = cartProductResponse.getProductId();
            int orderQuantity = cartProductResponse.getQuantity();

            Product product = productService.getProductById(productId)
                    .orElseThrow(()-> new ProductNotFoundException("Product with Id " +productId+ " not found"));
            if(product.getQuantity()<orderQuantity){
                return  new OrderResponse("Insufficient stock for product "+product.getName(), cartProducts, 0.0);
            }
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

        for (CartProductResponse cartProductResponse : cartProducts){
            Long productId = cartProductResponse.getProductId();
            int orderQuantity = cartProductResponse.getQuantity();

            productService.updateProduct(productId,orderQuantity);
        }

        cartService.clearCart(user);
        log.info("cart after cleaaring");

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setMessage("Order placed successfully!");
        orderResponse.setCartProductResponseList(cartResponse.getCartProducts());
        orderResponse.setTotalPrice(cartResponse.getTotalPrice());

        return orderResponse;
    }
}