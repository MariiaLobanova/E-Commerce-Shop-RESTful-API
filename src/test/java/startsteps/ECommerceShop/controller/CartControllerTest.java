package startsteps.ECommerceShop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import startsteps.ECommerceShop.service.CartService;
import startsteps.ECommerceShop.service.JwtServiceImpl;
import startsteps.ECommerceShop.service.ProductService;
import startsteps.ECommerceShop.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

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
    private UserService userService;

    @Test
    void addToCart() {
    }

    @Test
    void getCart() {
    }

    @Test
    void removeProduct() {
    }
}