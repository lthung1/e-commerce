package vn.shoestore.application.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRefreshTokenRequest {
  @NotNull(message = "Refresh token không được để trống")
  private String token;
}
