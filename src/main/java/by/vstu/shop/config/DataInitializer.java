package by.vstu.shop.config;

import by.vstu.shop.entity.Role;
import by.vstu.shop.entity.User;
import by.vstu.shop.repository.RoleRepository;
import by.vstu.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        //создаём роли
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(Role.ERole.ROLE_USER));
            roleRepository.save(new Role(Role.ERole.ROLE_ADMIN));
            log.info("Roles created");
        }

        //создаём администратора
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN).orElseThrow();
            Role userRole  = roleRepository.findByName(Role.ERole.ROLE_USER).orElseThrow();

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);
            log.info("Admin created — login: admin / password: admin123");
        }
    }
}
