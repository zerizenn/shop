package by.vstu.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long userId;
    private String username;

    //вычисляемые поля на основе связанных сущностей
    private Long ordersCount;       //количество заказов пользователя
    private BigDecimal totalSpent;  //общая сумма покупок
}
