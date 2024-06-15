package vn.shoestore.usecases.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.response.LoginResponse;
import vn.shoestore.domain.adapter.UserAdapter;
import vn.shoestore.domain.model.User;
import vn.shoestore.domain.service.EncryptDecryptService;
import vn.shoestore.infrastructure.configuration.authen.JwtTokenProvider;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.constants.ExceptionMessage;
import vn.shoestore.shared.exceptions.NotAuthorizedException;
import vn.shoestore.usecases.auth.IAuthUseCase;

import java.time.LocalDateTime;
import java.util.Objects;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class AuthUseCaseImpl implements IAuthUseCase {

  private final UserAdapter userAdapter;

  private final EncryptDecryptService encryptDecryptService;

  private final JwtTokenProvider jwtTokenProvider;

  private final PasswordEncoder passwordEncoder;

  @Value("${vn.shoe_store.secret.jwt_expiration_ms}")
  private int jwtExpirationMs;

  @Override
  public LoginResponse login(LoginRequest request) {
    User user = userAdapter.getUserByUsername(request.getUsername());
    if (Objects.isNull(user)) {
      throw new NotAuthorizedException(ExceptionMessage.LOGIN_FAIL);
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new NotAuthorizedException(ExceptionMessage.LOGIN_FAIL);
    }

    return LoginResponse.builder()
        .accessToken(jwtTokenProvider.generateJwtToken(user))
        .expiresIn(LocalDateTime.now().plusSeconds(jwtExpirationMs / 1000))
        .build();
  }
}
