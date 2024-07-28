package vn.shoestore.application.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.shoestore.application.request.CreateBillRequest;
import vn.shoestore.application.response.BaseResponse;

@RestController
@RequestMapping("/api/v1/bill")
public interface IBillController {
  @PostMapping("create-bill")
  ResponseEntity<BaseResponse> createBill(@RequestBody @Valid CreateBillRequest request);

  @PutMapping("confirm-purchase/{id}")
  ResponseEntity<BaseResponse> confirmPurchase(@PathVariable Long id);
}
