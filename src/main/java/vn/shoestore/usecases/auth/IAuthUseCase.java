package vn.shoestore.usecases.auth;

import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.response.LoginResponse;

public interface IAuthUseCase {
  LoginResponse login(LoginRequest request);
}
