package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.shoestore.domain.adapter.UserAdapter;
import vn.shoestore.domain.model.User;
import vn.shoestore.infrastructure.repository.entity.UserEntity;
import vn.shoestore.infrastructure.repository.repository.UserRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.dto.CustomUserDetails;
import vn.shoestore.shared.utils.ModelMapperUtils;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class UserAdapterImpl implements UserAdapter {
  private final UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String username) {
    Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isEmpty()) {
      throw new UsernameNotFoundException(username);
    }

    return new CustomUserDetails(ModelMapperUtils.mapper(optionalUser.get(), User.class));
  }
}
