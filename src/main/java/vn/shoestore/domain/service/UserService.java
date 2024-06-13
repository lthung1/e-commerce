package vn.shoestore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.shoestore.domain.adapter.UserAdapter;
import vn.shoestore.domain.model.User;
import vn.shoestore.shared.dto.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserAdapter userAdapter;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userAdapter.getUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new CustomUserDetails(user);
  }
}
