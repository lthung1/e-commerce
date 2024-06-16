package vn.shoestore.application.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.shoestore.application.controllers.IAuthController;
import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.request.RegisterRequest;
import vn.shoestore.application.response.BaseResponse;
import vn.shoestore.application.response.LoginResponse;
import vn.shoestore.domain.model.User;
import vn.shoestore.shared.factory.ResponseFactory;
import vn.shoestore.usecases.auth.IAuthUseCase;

@Component
@RequiredArgsConstructor
public class AuthControllerImpl implements IAuthController {
  private final IAuthUseCase iAuthUseCase;

  @Override
  public ResponseEntity<BaseResponse<LoginResponse>> login(LoginRequest request) {
    return ResponseFactory.success(iAuthUseCase.login(request));
  }

  @Override
  public ResponseEntity<BaseResponse<User>> register(RegisterRequest request) {
    return ResponseFactory.success(iAuthUseCase.register(request));
  }
}
