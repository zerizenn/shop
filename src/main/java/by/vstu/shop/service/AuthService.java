package by.vstu.shop.service;

import by.vstu.shop.dto.AuthDTO;
import by.vstu.shop.entity.Role;
import by.vstu.shop.entity.User;
import by.vstu.shop.repository.RoleRepository;
import by.vstu.shop.repository.UserRepository;
import by.vstu.shop.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    public AuthDTO.JwtResponse register(AuthDTO.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        //автовход после регистрации
        return login(new AuthDTO.LoginRequest() {{
            setUsername(request.getUsername());
            setPassword(request.getPassword());
        }});
    }

    public AuthDTO.JwtResponse login(AuthDTO.LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtUtils.generateJwtToken(auth);
        return new AuthDTO.JwtResponse(token, request.getUsername());
    }
}
