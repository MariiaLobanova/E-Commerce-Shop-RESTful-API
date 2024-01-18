package startsteps.ECommerceShop.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import startsteps.ECommerceShop.entities.Role;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationController authorizationController;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAdminData() throws Exception {
        List<User> userList = Arrays.asList(
                new User(1L,"testname1","test1email@gmail","password", Role.USER),
                new User(1L,"testname2","test2email@gmail","password", Role.USER));
        given(userRepository.findAll()).willReturn(userList);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authorizationController).build();

        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(userList);
        Assertions.assertEquals(2,userList.size());
        Assertions.assertTrue(responseContent.contains("USER"));
        Assertions.assertTrue(responseContent.contains("test2email@gmail"));
    }
}


