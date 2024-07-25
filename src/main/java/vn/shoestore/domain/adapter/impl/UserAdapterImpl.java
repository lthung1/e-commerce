package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.UserAdapter;
import vn.shoestore.domain.model.User;
import vn.shoestore.infrastructure.repository.entity.UserEntity;
import vn.shoestore.infrastructure.repository.repository.UserRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;

import java.util.List;
import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class UserAdapterImpl implements UserAdapter {
  private final UserRepository userRepository;

  @Override
  public User getUserByUsername(String username) {
    Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
    return optionalUser
        .map(userEntity -> ModelMapperUtils.mapper(userEntity, User.class))
        .orElse(null);
  }

  @Override
  public User save(User user) {
    return ModelMapperUtils.mapper(
        userRepository.save(ModelMapperUtils.mapper(user, UserEntity.class)), User.class);
  }

  @Override
  public List<User> getUserByIdIn(List<Long> ids) {
    return ModelMapperUtils.mapList(userRepository.findAllByIdIn(ids), User.class);
  }
}
