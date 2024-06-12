package vn.shoestore.domain.adapter;

import vn.shoestore.shared.dto.CustomUserDetails;

public interface UserAdapter {
  CustomUserDetails loadUserByUsername(String username);
}
