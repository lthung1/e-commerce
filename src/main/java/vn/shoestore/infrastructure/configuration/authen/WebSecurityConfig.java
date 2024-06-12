package vn.shoestore.infrastructure.configuration.authen;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import vn.shoestore.domain.adapter.UserAdapter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserAdapter userAdapter;
}
