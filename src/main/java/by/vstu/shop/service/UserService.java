package by.vstu.shop.service;

import by.vstu.shop.dto.UserDetailsDTO;
import by.vstu.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDetailsDTO getUserDetails(Long userId) {
        return userRepository.findUserDetails(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
}
