package startsteps.ECommerceShop.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name="Authentication System", description = "Endpoints for user registration and login")
public class AuthenticationController {
    private final AuthService authService;
    @PostMapping("/signup")
    @Operation(summary = "User Registration", description = "Allows users to register in the system.")
    public ResponseEntity<JwtAuthResponse> signup(@RequestBody SighUpRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signin")
    @Operation(summary = "User Login", description = "Allows users to log in to the system.")
    public ResponseEntity<JwtAuthResponse> signin(@RequestBody SighInRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}
