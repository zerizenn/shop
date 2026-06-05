package by.vstu.shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    @Data
    public static class CreateRequest {
        private List<ItemRequest> items;

        @Data
        public static class ItemRequest {
            private Long productId;
            private Integer quantity;
        }
    }

    @Data
    public static class Response {
        private Long id;
        private String username;
        private LocalDateTime createdAt;
        private String status;
        private BigDecimal totalAmount;
        private List<ItemResponse> items;

        @Data
        public static class ItemResponse {
            private Long productId;
            private String productName;
            private Integer quantity;
            private BigDecimal priceAtTime;
        }
    }
}
