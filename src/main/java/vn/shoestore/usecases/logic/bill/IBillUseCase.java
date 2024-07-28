package vn.shoestore.usecases.logic.bill;

import vn.shoestore.application.request.CreateBillRequest;

public interface IBillUseCase {
  void createBill(CreateBillRequest request, Boolean isOnlineTransaction);

  void adminConfirmBill(Long billId);
}
