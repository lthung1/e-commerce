package vn.shoestore.application.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {
  @NotNull(message = "username cannot be null")
  private String username;

  @NotNull(message = "password cannot be null")
  private String password;

  @NotNull(message = "email cannot be null")
  private String email;

  @NotNull(message = "first_name cannot be null")
  private String firstName;

  @NotNull(message = "last_name cannot be null")
  private String lastName;
}
