package by.vstu.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}
