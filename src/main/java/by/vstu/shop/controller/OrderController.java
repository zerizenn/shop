package by.vstu.shop.controller;

import by.vstu.shop.dto.OrderDTO;
import by.vstu.shop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    //все заказы(только ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все заказы (только ADMIN)")
    public ResponseEntity<List<OrderDTO.Response>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    // Мои заказы(USER и ADMIN)
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Получить мои заказы")
    public ResponseEntity<List<OrderDTO.Response>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    //фильтр по статусу(только ADMIN)
    @GetMapping("/by-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить заказы по статусу (только ADMIN). Статусы: NEW, PAID, SHIPPED, COMPLETED, CANCELLED")
    public ResponseEntity<List<OrderDTO.Response>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }

    //создать заказ(USER и ADMIN)
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Создать заказ")
    public ResponseEntity<OrderDTO.Response> create(@RequestBody OrderDTO.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Мягкое удаление заказа (только ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
