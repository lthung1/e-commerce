package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.Role;
import vn.shoestore.domain.model.UserRole;

import java.util.List;

public interface RoleAdapter {
  List<Role> findAllByIdIn(List<Long> ids);
  List<UserRole> findAllByUserIdIn(List<Long> userIds);
}
