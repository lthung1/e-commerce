package vn.shoestore.usecases.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.request.RegisterRequest;
import vn.shoestore.application.response.LoginResponse;
import vn.shoestore.domain.adapter.UserAdapter;
import vn.shoestore.domain.model.User;
import vn.shoestore.infrastructure.configuration.authen.JwtTokenProvider;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.constants.ExceptionMessage;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.exceptions.NotAuthorizedException;
import vn.shoestore.shared.utils.ModelMapperUtils;
import vn.shoestore.usecases.auth.IAuthUseCase;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import static vn.shoestore.shared.constants.ExceptionMessage.INVALID_PASSWORD;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class AuthUseCaseImpl implements IAuthUseCase {

  private final UserAdapter userAdapter;

  private final JwtTokenProvider jwtTokenProvider;

  private final PasswordEncoder passwordEncoder;

  @Value("${vn.shoe_store.secret.jwt_expiration_ms}")
  private int jwtExpirationMs;

  private static final String PASSWORD_REGEX =
      "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

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

  @Override
  public User register(RegisterRequest request) {
    User user = ModelMapperUtils.mapper(request, User.class);

    Pattern pattern = Pattern.compile(PASSWORD_REGEX);
    if(!pattern.matcher(request.getPassword()).matches()) {
      throw new InputNotValidException(INVALID_PASSWORD);
    }

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    return userAdapter.save(user);
  }
}
