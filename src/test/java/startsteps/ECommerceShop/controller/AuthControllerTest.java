package startsteps.ECommerceShop.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import startsteps.ECommerceShop.entities.Role;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.service.JwtService;
import startsteps.ECommerceShop.service.JwtServiceImpl;
import startsteps.ECommerceShop.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAdminData() throws Exception {
        List<User> userList = Arrays.asList(
                new User(1L,"testname1","test1email@gmail","password", Role.USER),
                new User(1L,"testname2","test2email@gmail","password", Role.USER));
        given(userRepository.findAll()).willReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("test1email@gmail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("test2email@gmail"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(userRepository, times(1)).findAll();
    }
}

