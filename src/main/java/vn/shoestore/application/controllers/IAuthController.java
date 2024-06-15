package vn.shoestore.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shoestore.application.request.LoginRequest;
import vn.shoestore.application.response.BaseResponse;
import vn.shoestore.application.response.LoginResponse;

import javax.validation.Valid;

@RequestMapping("/auth")
@RestController
public interface IAuthController {
  @PostMapping("login")
  ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request);
}
