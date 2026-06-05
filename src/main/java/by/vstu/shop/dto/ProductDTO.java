package by.vstu.shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @DecimalMin("0.00")
    private BigDecimal price;

    @Min(0)
    private Integer quantity;

    private Long categoryId;
    private String categoryName;
}
