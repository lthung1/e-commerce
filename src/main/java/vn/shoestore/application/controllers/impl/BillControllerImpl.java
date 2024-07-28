package vn.shoestore.application.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.shoestore.application.controllers.IBillController;
import vn.shoestore.application.request.BuyNowRequest;
import vn.shoestore.application.request.CreateBillRequest;
import vn.shoestore.application.response.BaseResponse;
import vn.shoestore.shared.factory.ResponseFactory;
import vn.shoestore.usecases.logic.bill.IBillUseCase;

@Component
@RequiredArgsConstructor
public class BillControllerImpl implements IBillController {
  private final IBillUseCase iBillUseCase;

  @Override
  public ResponseEntity<BaseResponse> createBill(CreateBillRequest request) {
    iBillUseCase.createBill(request, false);
    return ResponseFactory.success();
  }

  @Override
  public ResponseEntity<BaseResponse> confirmPurchase(Long id) {
    iBillUseCase.adminConfirmBill(id);
    return ResponseFactory.success();
  }

  @Override
  public ResponseEntity<BaseResponse> buyNow(BuyNowRequest request) {
    iBillUseCase.buyNow(request, false);
    return ResponseFactory.success();
  }
}
