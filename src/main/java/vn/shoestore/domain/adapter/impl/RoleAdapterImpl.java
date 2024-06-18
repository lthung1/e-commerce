package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.RoleAdapter;
import vn.shoestore.domain.model.Role;
import vn.shoestore.domain.model.UserRole;
import vn.shoestore.infrastructure.repository.repository.RoleRepository;
import vn.shoestore.infrastructure.repository.repository.UserRoleRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class RoleAdapterImpl implements RoleAdapter {
  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;

  @Override
  public List<Role> findAllByIdIn(List<Long> ids) {
    return ModelMapperUtils.mapList(roleRepository.findAllByIdIn(ids), Role.class);
  }

  @Override
  public List<UserRole> findAllByUserIdIn(List<Long> userIds) {
    return ModelMapperUtils.mapList(userRoleRepository.findAllByUserIdIn(userIds), UserRole.class);
  }
}
