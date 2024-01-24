package startsteps.ECommerceShop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import startsteps.ECommerceShop.entities.Product;
import startsteps.ECommerceShop.service.JwtService;
import startsteps.ECommerceShop.service.JwtServiceImpl;
import startsteps.ECommerceShop.service.ProductService;
import startsteps.ECommerceShop.service.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser(username = "admin", roles = "ADMIN")
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private JwtServiceImpl jwtServiceImpl;
    @MockBean
    private UserService userService;

    @Test
    void getAllProduct() throws Exception{
        Product product1 = new Product(1L, "testname1", "testdescription1", 1.00, 5);
        Product product2 = new Product(2L, "testname2", "testdescription2", 2.00, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2));

        given(productService.getAllProduct(any(Pageable.class))).willReturn(productPage);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].name").value("testname1"))
                .andExpect(jsonPath("$.content[1].name").value("testname2"))
                .andExpect(jsonPath("$.content[0].description").value("testdescription1"))
                .andExpect(jsonPath("$.content[1].price").value(2.00))
                .andExpect(jsonPath("$.content[0].quantity").value(5));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addProductSuccesfully() throws Exception {
        Product newProduct = new Product(3L, "newproduct", "newDescription", 3.00, 6);

        given(productService.addProduct(any(Product.class))).willReturn(newProduct);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"newproduct\", \"description\": \"newDescription\", \"price\": 3.00, \"quantity\": 6 }")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("newproduct"))
                .andExpect(jsonPath("$.description").value("newDescription"))
                .andExpect(jsonPath("$.price").value(3.00))
                .andExpect(jsonPath("$.quantity").value(6));;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteProductByIdSuccessfully() throws Exception {
        Long productId = 1L;
        mockMvc.perform(delete("/products/delete/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getProductByIdSuccessfully() throws Exception{
        Long productId = 1L;
        Product mockProduct = new Product(1l, "testgetproduct", "testdescription", 2.00,5);

        when(productService.getProductById(productId)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/products/id/{productId}",productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testgetproduct"))
                .andExpect(jsonPath("$.price").value(2.00))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void findProductsByNameSuccessfully() throws Exception{
        String name = "testname";
        List<Product> testProduct = Arrays.asList(
                new Product(1l, "testname", "testdescription", 2.00,5));

        when(productService.findProductsByName(name)).thenReturn(testProduct);

        mockMvc.perform(get("/products/name/{name}",name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].price").value(2.00))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

}