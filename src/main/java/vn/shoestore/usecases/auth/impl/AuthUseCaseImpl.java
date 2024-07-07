package vn.shoestore.usecases.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.request.RegisterRequest;
import vn.shoestore.application.response.LoginResponse;
import vn.shoestore.domain.adapter.UserAdapter;
import vn.shoestore.domain.adapter.UserRefreshTokenAdapter;
import vn.shoestore.domain.model.User;
import vn.shoestore.domain.model.UserRefreshToken;
import vn.shoestore.infrastructure.configuration.authen.JwtTokenProvider;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.shared.constants.ExceptionMessage;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.exceptions.NotAuthorizedException;
import vn.shoestore.shared.utils.ModelMapperUtils;
import vn.shoestore.shared.utils.ObjectUtils;
import vn.shoestore.usecases.auth.IAuthUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static vn.shoestore.shared.constants.ExceptionMessage.INVALID_PASSWORD;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class AuthUseCaseImpl implements IAuthUseCase {

  private final UserAdapter userAdapter;

  private final JwtTokenProvider jwtTokenProvider;

  private final PasswordEncoder passwordEncoder;

  private final UserRefreshTokenAdapter userRefreshTokenAdapter;

  @Value("${vn.shoe_store.secret.jwt_expiration_ms}")
  private int jwtExpirationMs;

  private static final String PASSWORD_REGEX =
      "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

  @Override
  @Transactional
  public LoginResponse login(LoginRequest request) throws IllegalAccessException {
    User user = userAdapter.getUserByUsername(request.getUsername());
    if (Objects.isNull(user)) {
      throw new NotAuthorizedException(ExceptionMessage.LOGIN_FAIL);
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new NotAuthorizedException(ExceptionMessage.LOGIN_FAIL);
    }

    String token =
        jwtTokenProvider.generateJwtToken(user, ObjectUtils.convertUsingReflection(user));
    String refreshToken =
        jwtTokenProvider.generateRefreshToken(user, ObjectUtils.convertUsingReflection(user));
    processRefreshToken(user, refreshToken);

    return LoginResponse.builder()
        .accessToken(token)
        .refreshToken(refreshToken)
        .expiresIn(LocalDateTime.now().plusSeconds(jwtExpirationMs / 1000))
        .userInfo(user)
        .build();
  }

  @Override
  public User register(RegisterRequest request) {
    User user = ModelMapperUtils.mapper(request, User.class);

    Pattern pattern = Pattern.compile(PASSWORD_REGEX);
    if (!pattern.matcher(request.getPassword()).matches()) {
      throw new InputNotValidException(INVALID_PASSWORD);
    }

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    return userAdapter.save(user);
  }

  private void processRefreshToken(User user, String refreshToken) {
    List<UserRefreshToken> oldToken =
        userRefreshTokenAdapter.findAllByUserIdAndInvoke(user.getId(), false);
    oldToken.forEach(e -> e.setInvoke(true));
    userRefreshTokenAdapter.saveAll(oldToken);

    UserRefreshToken userRefreshToken =
        UserRefreshToken.builder()
            .userId(user.getId())
            .invoke(false)
            .refreshToken(passwordEncoder.encode(refreshToken))
            .build();

    userRefreshTokenAdapter.save(userRefreshToken);
  }
}
