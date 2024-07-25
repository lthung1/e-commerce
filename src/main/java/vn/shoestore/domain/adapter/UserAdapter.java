package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.User;

import java.util.List;

public interface UserAdapter {
  User getUserByUsername(String username);

  User save(User user);

  List<User> getUserByIdIn(List<Long> ids);
}
