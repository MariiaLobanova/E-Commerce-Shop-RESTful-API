package startsteps.ECommerceShop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startsteps.ECommerceShop.request.SighInRequest;
import startsteps.ECommerceShop.request.SighUpRequest;
import startsteps.ECommerceShop.responce.JwtAuthResponse;
import startsteps.ECommerceShop.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name="Registration/ login system")
public class AuthenticationController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> signup(@RequestBody SighUpRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signin(@RequestBody SighInRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}
