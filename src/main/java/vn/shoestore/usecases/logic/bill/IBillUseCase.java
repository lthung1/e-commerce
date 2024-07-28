package vn.shoestore.usecases.logic.bill;

import vn.shoestore.application.request.BuyNowRequest;
import vn.shoestore.application.request.CreateBillRequest;

public interface IBillUseCase {
  void createBill(CreateBillRequest request, Boolean isOnlineTransaction);

  void adminConfirmBill(Long billId);

  void buyNow(BuyNowRequest request, Boolean isOnlineTransaction);
}
