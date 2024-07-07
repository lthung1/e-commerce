package vn.shoestore.application.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
  @NotNull(message = "Mật khẩu cũ không được rỗng")
  private String oldPassword;

  @NotNull(message = "Mật khẩu mới không được rỗng")
  @Pattern(
      regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
      message = "Mật khẩu không đúng định dạng")
  private String newPassword;
}
