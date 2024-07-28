package vn.shoestore.domain.adapter.impl;

import static vn.shoestore.shared.constants.ExceptionMessage.BILL_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.BillAdapter;
import vn.shoestore.domain.model.Bill;
import vn.shoestore.domain.model.ProductBill;
import vn.shoestore.infrastructure.repository.entity.BillEntity;
import vn.shoestore.infrastructure.repository.entity.ProductBillEntity;
import vn.shoestore.infrastructure.repository.repository.BillRepository;
import vn.shoestore.infrastructure.repository.repository.ProductBillRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.exceptions.InputNotValidException;
import vn.shoestore.shared.utils.ModelMapperUtils;

@Adapter
@RequiredArgsConstructor
public class BillAdapterImpl implements BillAdapter {
  private final BillRepository billRepository;
  private final ProductBillRepository productBillRepository;

  @Override
  public Bill saveBill(Bill bill) {
    return ModelMapperUtils.mapper(
        billRepository.save(ModelMapperUtils.mapper(bill, BillEntity.class)), Bill.class);
  }

  @Override
  public void saveProductBill(List<ProductBill> productBills) {
    productBillRepository.saveAll(ModelMapperUtils.mapList(productBills, ProductBillEntity.class));
  }

  @Override
  public Bill findBillById(Long billId) {
    Optional<BillEntity> billEntity = billRepository.findById(billId);
    if (billEntity.isEmpty()) {
      throw new InputNotValidException(BILL_NOT_FOUND);
    }
    return ModelMapperUtils.mapper(billEntity.get(), Bill.class);
  }
}
