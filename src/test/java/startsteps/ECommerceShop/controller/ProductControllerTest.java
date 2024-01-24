package startsteps.ECommerceShop.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import startsteps.ECommerceShop.ECommerceShopApplication;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.repository.ProductRepository;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.service.JwtService;
import startsteps.ECommerceShop.service.ProductService;
import startsteps.ECommerceShop.service.UserService;
import static org.mockito.ArgumentMatchers.any;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(ProductController.class)
@WithMockUser(username = "admin", roles = "ADMIN")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    void getAllProduct() throws Exception {
        List<Product> productList = Arrays.asList(
                new Product(1L,"testname1","testdescription1",1.00,5),
                new Product(2L,"testname2","testdescription2",2.00,10));
        Page<Product> productPage = new PageImpl<>(productList);
        given(productRepository.findAll(any(Pageable.class))).willReturn(productPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("testname1"))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("testname2"))
                //.andDo(MockMvcResultHandlers.print())
        ;

        //verify(productRepository, times(1)).findAll(any(Pageable.class));;

    }

    @Test
    void addProduct() {
    }

    @Test
    void deleteProductById() {
    }

    @Test
    void getProductById() {
    }

    @Test
    void findProductsByName() {
    }
}