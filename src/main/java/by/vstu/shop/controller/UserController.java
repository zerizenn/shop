package by.vstu.shop.controller;

import by.vstu.shop.dto.UserDetailsDTO;
import by.vstu.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    /**
     * Вариант 14
     * Возвращает UserDetailsDTO с вычисляемыми полями:
     * количество заказов и общая сумма покупок пользователя.
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Статистика по пользователю: количество заказов и сумма покупок")
    public ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }
}
