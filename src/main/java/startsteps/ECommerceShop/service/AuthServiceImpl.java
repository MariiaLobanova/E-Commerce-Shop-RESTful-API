package startsteps.ECommerceShop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import startsteps.ECommerceShop.entities.Role;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.repository.UserRepository;
import startsteps.ECommerceShop.request.SighInRequest;
import startsteps.ECommerceShop.request.SighUpRequest;
import startsteps.ECommerceShop.responce.JwtAuthResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponse signup(SighUpRequest sighUpRequest) {
        Role role = (sighUpRequest.getRole() != null && sighUpRequest.getRole() == Role.ADMIN) ? Role.ADMIN : Role.USER;

        var user = User.builder()
                .username(sighUpRequest.getUsername())
                .email(sighUpRequest.getEmail())
                .password(passwordEncoder.encode(sighUpRequest.getPassword()))
                .role(role).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthResponse signin(SighInRequest sighInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(sighInRequest.getEmail(), sighInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        var user = userRepository.findByEmail(sighInRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }
}
