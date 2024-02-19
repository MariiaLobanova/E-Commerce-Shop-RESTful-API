package startsteps.ECommerceShop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name= "System getting users information")
public class AuthorizationController {
    private final UserRepository userRepository;

    @GetMapping("/admin")
    @Operation(summary = "Get users information", description = "Allows authorized users to retrieve users information.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> adminData() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}
