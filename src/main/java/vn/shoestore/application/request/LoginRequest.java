package vn.shoestore.application.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
  @NotNull(message = "username cannot be null")
  private String username;

  @NotNull(message = "password cannot be null")
  private String password;
}
