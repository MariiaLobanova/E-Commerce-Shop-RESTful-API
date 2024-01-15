package startsteps.ECommerceShop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class AuthController {
    private final UserRepository userRepository;

    @GetMapping("/info")
    public ResponseEntity<String> seedata() {
            return ResponseEntity.ok("Here is your resource");
    }

    @GetMapping("/unsecured")
    public ResponseEntity<String> unsecuredData() {
        return ResponseEntity.ok("Unsecured data");
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> adminData() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }



}
