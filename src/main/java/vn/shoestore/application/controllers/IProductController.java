package vn.shoestore.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shoestore.application.request.ProductRequest;
import vn.shoestore.application.response.BaseResponse;

@RestController
@RequestMapping("/api/v1/product/")
public interface IProductController {

  @PostMapping("/save")
  ResponseEntity<BaseResponse> saveOrUpdateProduct(@RequestBody ProductRequest request);
}
