package vn.shoestore.domain.adapter;

import java.util.List;
import java.util.Optional;

import vn.shoestore.domain.model.Bill;
import vn.shoestore.domain.model.ProductBill;

public interface BillAdapter {
  Bill saveBill(Bill bill);

  void saveProductBill(List<ProductBill> productBills);

  Bill findBillById(Long billId);
}
