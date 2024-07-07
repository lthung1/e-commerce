package vn.shoestore.shared.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import vn.shoestore.shared.dto.CustomUserDetails;

public class AuthUtils {
  public static CustomUserDetails getAuthUserDetails() {
    return (CustomUserDetails)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
