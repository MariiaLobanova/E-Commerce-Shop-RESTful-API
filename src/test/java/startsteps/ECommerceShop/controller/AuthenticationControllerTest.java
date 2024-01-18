package startsteps.ECommerceShop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import startsteps.ECommerceShop.entities.Role;
import startsteps.ECommerceShop.request.SighInRequest;
import startsteps.ECommerceShop.request.SighUpRequest;
import startsteps.ECommerceShop.responce.JwtAuthResponse;
import startsteps.ECommerceShop.service.AuthService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testSignUpUser() throws Exception {
        SighUpRequest sighUpRequest= new SighUpRequest("testname", "testemail@gmail.com","password", Role.USER);
        JwtAuthResponse jwtAuthResponse = JwtAuthResponse.builder().token("mockedToken").build();
        when(authService.signup(sighUpRequest)).thenReturn(jwtAuthResponse);

        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sighUpRequest)))
                .andExpect(status().isOk());

        verify(authService, times(1)).signup(sighUpRequest);

    }

    @Test
    void testSignInUser() throws Exception {
        SighInRequest sighInRequest = new SighInRequest("testemail@gmail.com","password");
        JwtAuthResponse jwtAuthResponse= JwtAuthResponse.builder().token("mokedToken").build();
        when(authService.signin(sighInRequest)).thenReturn(jwtAuthResponse);

        mockMvc.perform(post("/api/v1/auth/signin").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sighInRequest))).andExpect(status().isOk());

        verify(authService,times(1)).signin(sighInRequest);

    }
}