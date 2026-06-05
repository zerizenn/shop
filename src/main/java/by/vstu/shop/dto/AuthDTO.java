package by.vstu.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthDTO {

    @Data
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;
    }

    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Data
    public static class JwtResponse {
        private String token;
        private String type = "Bearer";
        private String username;

        public JwtResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
    }
}
