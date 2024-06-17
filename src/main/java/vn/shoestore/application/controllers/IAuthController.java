package vn.shoestore.application.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.request.RegisterRequest;
import vn.shoestore.application.response.BaseResponse;
import vn.shoestore.application.response.LoginResponse;
import vn.shoestore.domain.model.User;


@RequestMapping("/auth")
@RestController
public interface IAuthController {
  @PostMapping("login")
  ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request);


  @PostMapping("register")
  ResponseEntity<BaseResponse<User>> register(@RequestBody @Valid RegisterRequest request);
}
