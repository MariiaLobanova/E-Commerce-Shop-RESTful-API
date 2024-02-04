package startsteps.ECommerceShop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import startsteps.ECommerceShop.entities.Cart;
import startsteps.ECommerceShop.entities.CartProduct;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.request.CartRequest;
import startsteps.ECommerceShop.responce.CartResponse;
import startsteps.ECommerceShop.service.*;

import static org.hamcrest.Matchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static startsteps.ECommerceShop.entities.Role.USER;

@WebMvcTest(CartController.class)
@WithMockUser(username = "user", roles = "USER")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;
    @MockBean
    private JwtServiceImpl jwtServiceImpl;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @MockBean
    AuthServiceImpl authService;

    @Test
    void addToCart() throws Exception {
        User user = new User(1L, "Anton", "anton@gmail.com", "0000", USER);

        CartRequest cartRequest = new CartRequest(1L,12);
        CartResponse cartResponse = new CartResponse("Product added successfully!", Collections.emptyList(),0.00);

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(user);
        Mockito.verify(cartService).addProductToCart(cartRequest, user);

        given(cartService.addProductToCart(cartRequest,user)).willReturn(cartResponse);

       mockMvc.perform(post("/cart/add").with(csrf()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.message").value("Product added successfully!"))
               .andExpect(jsonPath("$.cartProducts").isArray())
               .andExpect(jsonPath("$.totalPrice").value(0.00));;

    }

    @Test
    void getCart() throws Exception{
        User user1 = new User(1L, "Anton", "anton@gmail.com", "0000", USER);

        Product product1 = new Product(1L, "product1", "description1", 1.00, 5);
        Product product2 = new Product(2L, "product2", "description2", 2.00, 10);
        Product product3 = new Product(3L, "product3", "description3", 3.00, 3);
        Product product4 = new Product(4L, "product4", "description4", 4.00, 6);

        CartProduct cartProduct1 = new CartProduct(1L, 2, 2.00, product1, null);
        CartProduct cartProduct2 = new CartProduct(2L, 3, 3.00, product2, null);
        CartProduct cartProduct3 = new CartProduct(3L, 1, 4.00, product3, null);
        CartProduct cartProduct4 = new CartProduct(4L, 2, 5.00, product4, null);

        Cart cart1 = new Cart();
        cart1.setCartId(1L);
        cart1.setUser(user1);
        cart1.setCartProductList(List.of(cartProduct1, cartProduct2,cartProduct3,cartProduct4));
        Mockito.when(authService.getAuthenticatedUser()).thenReturn(user1);

        given(cartService.getCart(user1)).willReturn(new CartResponse("Cart details retrieved successfully", List.of(), 20.00));

        mockMvc.perform(get("/cart/mycart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Cart details retrieved successfully"))
                .andExpect(jsonPath("$.cartProducts").isArray())
                .andExpect(jsonPath("$.totalPrice").value(20.00));
    }

    @Test
    void removeProduct() throws Exception {
        User user = new User(1L, "Anton", "anton@gmail.com", "0000", USER);
        long productIdToRemove = 8L;

        CartResponse cartResponseAfterRemoval = new CartResponse("Product removed successfully!", Collections.emptyList(), 0.00);

        Mockito.when(authService.getAuthenticatedUser()).thenReturn(user);
        given(cartService.removeProduct(productIdToRemove, user)).willReturn(cartResponseAfterRemoval);

        mockMvc.perform(delete("/cart/remove/{productId}", productIdToRemove).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Product removed successfully!"))
                .andExpect(jsonPath("$.cartProducts").isArray())
                .andExpect(jsonPath("$.totalPrice").value(0.00));
    }
}