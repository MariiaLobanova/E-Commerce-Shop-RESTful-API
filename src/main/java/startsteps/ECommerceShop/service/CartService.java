package startsteps.ECommerceShop.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.exceptions.CartNotFoundException;
import startsteps.ECommerceShop.exceptions.ProductNotFoundException;
import startsteps.ECommerceShop.exceptions.UnauthorizedAccessException;
import startsteps.ECommerceShop.repository.CartRepository;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.request.CartRequest;
import startsteps.ECommerceShop.responce.CartProductResponse;
import startsteps.ECommerceShop.responce.CartResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @Transactional
    public CartResponse addProductToCart(CartRequest cartRequest, User user) {
        log.info("Adding product to cart for user: {}", user.getUsername());

        int quantity = cartRequest.getQuantity();
        Long productId = cartRequest.getProductId();

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartProductList(new ArrayList<>());
            user.setCart(cart);
        }
        if (cart.getCartProductList() == null) {
            cart.setCartProductList(new ArrayList<>());
        }
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            Optional<CartProduct> cartProductOptional = cart.getCartProductList().stream()
                    .filter(cp -> cp.getProduct().getProductId().equals(productId))
                    .findFirst();

            if (cartProductOptional.isPresent()) {

                CartProduct cartProduct = cartProductOptional.get();
                cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
                cartProduct.setPrice(product.getPrice() * cartProduct.getQuantity());

            } else {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setProduct(product);
                cartProduct.setQuantity(quantity);
                cartProduct.setPrice(product.getPrice() * quantity);
                cart.getCartProductList().add(cartProduct);
                cartProduct.setCart(cart);
            }
            updateCartTotalPrice(cart);
            cartRepository.save(cart);

            CartResponse cartResponse = new CartResponse();
            cartResponse.setMessage("Product added to cart successfully");
            cartResponse.setCartProducts(createCartProduct(cart.getCartProductList()));
            cartResponse.setTotalPrice(cart.getTotalPrice());

            log.info("Cart state after update: {}", user.getCart());

            return cartResponse;

        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found");
        }
    }
        private void updateCartTotalPrice(Cart cart) {
            double totalPrice = cart.getCartProductList().stream()
                    .mapToDouble(CartProduct::getPrice)
                    .sum();
            cart.setTotalPrice(totalPrice);
        }
        private List<CartProductResponse> createCartProduct(List<CartProduct> cartProducts){

        return cartProducts.stream()
                    .map(cartProduct -> new CartProductResponse(cartProduct))
                    .collect(Collectors.toList());
        }

    @Transactional
    public CartResponse getCart(User user){
        Cart cart = user.getCart();

        if (cart == null || cart.getCartProductList().isEmpty()){
            return new CartResponse("You haven't added anything yet, your cart is empty",Collections.emptyList(),0.0);
        }
        log.info("get cart: {}", cart);

        List<CartProductResponse> cartProductResponses = cart.getCartProductList().stream()
                .map(CartProductResponse::new).collect(Collectors.toList());

        return new CartResponse("Cart details retrieved successfully", cartProductResponses, cart.getTotalPrice());
    }

    @Transactional
    public CartResponse removeProduct(Long productId, User user) {
        Cart cart = user.getCart();

        if (cart == null || cart.getCartProductList().isEmpty()) {
            return new CartResponse("Can not remove any products. Your cart is empty", null, 0.00);
        }
        Optional<CartProduct> cartProductOptional = cart.getCartProductList().stream()
                .filter(c -> c.getProduct().getProductId().equals(productId)).findFirst();

        if (cartProductOptional.isPresent()) {
            CartProduct cartProductToRemove = cartProductOptional.get();
            cart.getCartProductList().remove(cartProductToRemove);
            updateCartTotalPrice(cart);
            cartRepository.save(cart);

            List<CartProductResponse> cartProductResponses = cart.getCartProductList().stream()
                    .map(CartProductResponse::new).collect(Collectors.toList());

            return new CartResponse("Product with id" + productId + "removed successfully", cartProductResponses, cart.getTotalPrice());
        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found in your cart.");
        }
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = user.getCart();
        if(cart!=null && !cart.getCartProductList().isEmpty()){
            cart.getCartProductList().clear();
            updateCartTotalPrice(cart);
            cartRepository.save(cart);
        }
    }
}
