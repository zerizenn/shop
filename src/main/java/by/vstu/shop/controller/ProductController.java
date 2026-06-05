package by.vstu.shop.controller;

import by.vstu.shop.dto.ProductDTO;
import by.vstu.shop.entity.Product;
import by.vstu.shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Товары")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    //эндпоинт 2: все товары
    @GetMapping
    @Operation(summary = "Получить все товары")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    //эндпоинт 3: товары по категории
    @GetMapping("/by-category/{categoryId}")
    @Operation(summary = "Получить товары по категории")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getByCategory(categoryId));
    }

    //эндпоинт 4: товары не дороже указанной цены
    @GetMapping("/by-max-price")
    @Operation(summary = "Получить товары не дороже указанной цены")
    public ResponseEntity<List<Product>> getByMaxPrice(@RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.getByMaxPrice(maxPrice));
    }

    //эндпоинт 5: только товары в наличии
    @GetMapping("/in-stock")
    @Operation(summary = "Получить только товары в наличии (quantity > 0)")
    public ResponseEntity<List<Product>> getInStock() {
        return ResponseEntity.ok(productService.getInStock());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать товар (только ADMIN)")
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить товар (только ADMIN)")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Мягкое удаление товара (только ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
