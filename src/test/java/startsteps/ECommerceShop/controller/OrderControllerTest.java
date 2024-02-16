package startsteps.ECommerceShop.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import startsteps.ECommerceShop.entities.*;
import startsteps.ECommerceShop.repository.OrderHistoryRepository;
import startsteps.ECommerceShop.repository.OrderRepository;
import startsteps.ECommerceShop.responce.HistoryResponse;
import startsteps.ECommerceShop.responce.OrderResponse;
import startsteps.ECommerceShop.responce.OrderStatusResponse;
import startsteps.ECommerceShop.responce.OrdersResponse;
import startsteps.ECommerceShop.service.AuthServiceImpl;
import startsteps.ECommerceShop.service.JwtServiceImpl;
import startsteps.ECommerceShop.service.OrderService;
import startsteps.ECommerceShop.service.UserServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static startsteps.ECommerceShop.entities.Role.USER;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean
    private JwtServiceImpl jwtServiceImpl;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @MockBean
    AuthServiceImpl authService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderHistoryRepository orderHistoryRepository;



    @Test
    @WithMockUser(username = "user", roles = "USER")
    void placeOrderSuccesfully() throws Exception {
        User user = new User(1L, "Anton", "anton@gmail.com", "0000", USER);
        OrderResponse orderResponse = new OrderResponse("Order placed successfully!", Collections.emptyList(), 100.00);

        when(authService.getAuthenticatedUser()).thenReturn(user);

        given(orderService.placeOrder(user)).willReturn(orderResponse);

        mockMvc.perform(post("/order/place").with(csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order placed successfully!"))
                .andExpect(jsonPath("$.cartProductResponseList").isArray())
                .andExpect(jsonPath("$.totalPrice").value(100.00));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void changeOrderStatusSuccessfully() throws Exception {
        Long orderId = 1L;
        Order order = new Order(orderId, LocalDate.of(2024,2,12), OrderStatus.PAID, 100.00);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderStatusResponse orderStatusResponse = new OrderStatusResponse("Order status changed successfully!", Collections.emptyList(), 100, OrderStatus.DISPATCHED);
        when(orderService.changeOrderStatus(order)).thenReturn(orderStatusResponse);

        mockMvc.perform(put("/order/status/{orderId}",orderId)
                        .with(csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order status changed successfully!"))
                .andExpect(jsonPath("$.orderStatus").value("DISPATCHED"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void cancelOrderSuccesfully() throws Exception {
        Long orderId = 1L;
        Order order = new Order(orderId, LocalDate.of(2024,2,12), OrderStatus.PAID, 100.00);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderStatusResponse orderStatusResponse = new OrderStatusResponse("Your order is cancelled", Collections.emptyList(), 100, OrderStatus.CANCELLED);
        when(orderService.cancelOrder(order)).thenReturn(orderStatusResponse);

        mockMvc.perform(put("/order/cancel/{orderId}",orderId)
                        .with(csrf())).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your order is cancelled"))
                .andExpect(jsonPath("$.orderStatus").value("CANCELLED"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getAllOrdersSuccessfully() throws Exception {
        User user = new User(1L, "Liza", "liza@example.com", "0000", Role.USER);
        List<Order> orders = Collections.singletonList(
                new Order(1L, LocalDate.of(2024, 2, 12), OrderStatus.PAID, 100.00));

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(orderService.getAllOrders(user)).thenReturn(
                new OrdersResponse("Orders details retrieved successfully", orders));

        mockMvc.perform(get("/order/getorders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Orders details retrieved successfully"))
                .andExpect(jsonPath("$.orders", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.orders[0].orderId").value(1))
                .andExpect(jsonPath("$.orders[0].orderStatus").value("PAID"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getHistorySuccessfully() throws Exception {
        User user = new User(1L, "Liza", "liza@example.com", "0000", Role.USER);
        List<OrderHistory> ordersHistory = Collections.singletonList(
                new OrderHistory(1L,OrderStatus.PAID,OrderStatus.DISPATCHED, LocalDate.of(2024, 2, 12).atStartOfDay(), new Order()));

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(orderService.getHistory(user)).thenReturn(new HistoryResponse("Order history retrieved successfully",ordersHistory));

        mockMvc.perform(get("/order/gethistory").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Order history retrieved successfully"))
                .andExpect(jsonPath("$.orderHistories[0].orderHistoryId").value(1))
                .andExpect(jsonPath("$.orderHistories[0].oldStatus").value("PAID"))
                .andExpect(jsonPath("$.orderHistories[0].newStatus").value("DISPATCHED"))
                .andExpect(jsonPath("$.orderHistories[0].modifyDate").value("2024-02-12T00:00:00"));
    }
}