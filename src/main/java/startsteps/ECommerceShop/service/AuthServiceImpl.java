package startsteps.ECommerceShop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import startsteps.ECommerceShop.entities.Role;
import startsteps.ECommerceShop.entities.User;
import startsteps.ECommerceShop.exeptions.PasswordNotCorrectException;
import startsteps.ECommerceShop.exeptions.UserAlreadyExistException;
import startsteps.ECommerceShop.exeptions.UserNotFoundException;
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
        Role role = (sighUpRequest.getRole() != null
                && sighUpRequest.getRole() == Role.ADMIN) ? Role.ADMIN : Role.USER;

        userRepository.findByEmail(sighUpRequest.getEmail()).ifPresent(existingUser -> {
            throw new UserAlreadyExistException("User with email " + sighUpRequest.getEmail() + " already exists");
        });

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

        var user = userRepository.findByEmail(sighInRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with given email"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(sighInRequest.getEmail(), sighInRequest.getPassword()));
        } catch (BadCredentialsException exception) {
            throw new PasswordNotCorrectException("Password is not correct. Try one more time!");
        }
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }
    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");}

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        } else {
            throw new IllegalStateException("Unexpected authenticated principal type");
        }
    }
}
