package startsteps.ECommerceShop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class AuthController {

    @GetMapping("/info")
    public ResponseEntity<String> seedata() {
            return ResponseEntity.ok("Here is your resource");
    }

    @GetMapping("/unsecured")
    public ResponseEntity<String> unsecuredData() {
        return ResponseEntity.ok("Unsecured data");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminData() {
        return ResponseEntity.ok("Admin data");
    }



}
