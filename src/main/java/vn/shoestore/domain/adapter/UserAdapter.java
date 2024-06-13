package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.User;

public interface UserAdapter {
  User getUserByUsername(String username);
}
